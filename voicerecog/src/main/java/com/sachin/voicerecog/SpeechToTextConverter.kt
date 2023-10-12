package com.sachin.voicerecog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.Locale

class SpeechToTextConverter(private val conversionCallback: ConversionCallback) :
    TranslatorFactory.IConverter {

    override fun initialize(message: String, activity: Activity): SpeechToTextConverter {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            message
        )
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        intent.putExtra(
            RecognizerIntent.EXTRA_CALLING_PACKAGE,
            activity.packageName
        )

        //Add listeners
        val listener = CustomRecognitionListener()
        val sr = SpeechRecognizer.createSpeechRecognizer(activity)
        sr.setRecognitionListener(listener)
        sr.startListening(intent)
        conversionCallback.onCompletion()
        return this
    }

    internal inner class CustomRecognitionListener : RecognitionListener {

        override fun onReadyForSpeech(params: Bundle) {
            conversionCallback.onReadyForSpeech(params)
        }

        override fun onBeginningOfSpeech() {
            conversionCallback.onBeginningOfSpeech()
        }

        override fun onRmsChanged(value: Float) {
            conversionCallback.onRmsChanged(value)
        }

        override fun onBufferReceived(buffer: ByteArray) {
            conversionCallback.onBufferReceived(buffer)
        }

        override fun onEndOfSpeech() {
            conversionCallback.onEndOfSpeech()
        }

        override fun onError(error: Int) {
            conversionCallback.onErrorOccurred(getErrorText(error))
        }

        override fun onResults(results: Bundle) {
            var translateResults = String()
            val data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            for (result in data!!) {
                translateResults += result + "\n"
            }
            conversionCallback.onSuccess(translateResults)
        }

        override fun onPartialResults(partialResults: Bundle) {
            conversionCallback.onPartialResults(partialResults)
        }

        override fun onEvent(eventType: Int, params: Bundle) {
            conversionCallback.onEvent(eventType,params)
        }
    }

    override fun getErrorText(errorCode: Int): String {
        val message = when (errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "no match click icon to speak again"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
            SpeechRecognizer.ERROR_SERVER -> "error from server"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Didn't understand, please try again."
        }
        return message
    }


}