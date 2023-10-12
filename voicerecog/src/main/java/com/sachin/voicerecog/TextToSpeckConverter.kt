package com.sachin.voicerecog

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.*

class TextToSpeckConverter(private val conversionCallaBack: ConversionCallback) :
    TranslatorFactory.IConverter {
    private var textToSpeech: TextToSpeech? = null

    @SuppressLint("ObsoleteSdkInt")
    override fun initialize(message: String, activity: Activity): TranslatorFactory.IConverter {
        textToSpeech = TextToSpeech(activity) { status ->
            if (status != TextToSpeech.ERROR) {
                // textToSpeech!!.language = Locale.getDefault()
                textToSpeech!!.setPitch(1f)
                textToSpeech!!.setSpeechRate(0.8f)
                textToSpeech!!.language = Locale("hi_IN")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts(message, false)
                } else {
                    tts(message, true)
                }
            } else {
                conversionCallaBack.onErrorOccurred("Failed to initialize TTS engine")
            }
        }
        return this
    }

    private fun finish() {
        if (textToSpeech != null) {
            textToSpeech!!.stop()
        }
    }

    @Suppress("DEPRECATION")
    private fun tts(text: String, isUnder: Boolean) {
        if(isUnder) {
            val map = HashMap<String, String>()
            map[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "MessageId"
            textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, map)
        }else{
            val utteranceId = this.hashCode().toString() + ""
            textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        }

        textToSpeech!!.setOnUtteranceProgressListener(object : UtteranceProgressListener() {

            override fun onStart(utteranceId: String) {
                conversionCallaBack.onSpeaking(textToSpeech!!)
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                super.onError(utteranceId, errorCode)
                conversionCallaBack.onErrorOccurred("Some Error Occurred "+ getErrorText(errorCode))

            }

            override fun onError(utteranceId: String) {
                conversionCallaBack.onErrorOccurred("Some Error Occurred $utteranceId")
            }

            override fun onDone(utteranceId: String) {
                conversionCallaBack.onCompletion()
                finish()
                conversionCallaBack.onSpeaking(textToSpeech!!)
            }
        })

    }

    override fun getErrorText(errorCode: Int): String {
        val message = when (errorCode) {
            TextToSpeech.ERROR -> "Generic error"
            TextToSpeech.ERROR_INVALID_REQUEST -> "Client side error, invalid request"
            TextToSpeech.ERROR_NOT_INSTALLED_YET -> "Insufficient download of the voice data"
            TextToSpeech.ERROR_NETWORK -> "Network error"
            TextToSpeech.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            TextToSpeech.ERROR_OUTPUT -> "Failure in to the output (audio device or a file)"
            TextToSpeech.ERROR_SYNTHESIS -> "Failure of a TTS engine to synthesize the given input."
            TextToSpeech.ERROR_SERVICE -> "error from server"
            else -> "Didn't understand, please try again."
        }
        return message
    }

}