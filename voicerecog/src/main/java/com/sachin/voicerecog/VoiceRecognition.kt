package com.sachin.voicerecog

import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log

class VoiceRecognition {

    private var activity: Activity? = null
    private var voiceCallBack: VoiceCallBack? = null
    private var tts: TextToSpeech? = null

    fun getInstance(activity: Activity, voiceCallBack: VoiceCallBack): VoiceRecognition {
        this.activity = activity
        this.voiceCallBack = voiceCallBack
        return this
    }

    fun textToSpeech(speak: String) {
        if (speak.isNotEmpty()) {
            Log.d("VVV", "2")
            TranslatorFactory
                .instance
                .with(TranslatorFactory.TRANSLATORS.TEXT_TO_SPEECH,
                    object : ConversionCallback {
                        override fun onSuccess(result: String) {
                            voiceCallBack?.onSuccessTTS(result)
                        }

                        override fun onCompletion() {
                            voiceCallBack?.onCompletionTTS()
                        }

                        override fun onErrorOccurred(errorMessage: String) {
                            voiceCallBack?.onErrorOccurredTTS(errorMessage)
                        }

                        override fun onSpeaking(textToSpeech: TextToSpeech) {
                            tts = textToSpeech
                            voiceCallBack?.onSpeakingTTS(textToSpeech)
                        }

                    }
                ).initialize(speak, activity!!)

        }
    }

    fun speechToText() {
        Log.d("VVV", "speack")
        TranslatorFactory
            .instance
            .with(TranslatorFactory.TRANSLATORS.SPEECH_TO_TEXT,
                object : ConversionCallback {
                    override fun onSuccess(result: String) {
                        voiceCallBack?.onSuccessSTT(result)
                    }

                    override fun onCompletion() {
                        voiceCallBack?.onCompletionSTT()
                    }

                    override fun onErrorOccurred(errorMessage: String) {
                        voiceCallBack?.onErrorOccurredSTT(errorMessage)
                    }

                    override fun onSpeaking(textToSpeech: TextToSpeech) {
                        tts = textToSpeech
                        voiceCallBack?.onSpeakingSTT(textToSpeech)
                    }

                    override fun onReadyForSpeech(params: Bundle) {
                        voiceCallBack?.onReadyForSpeech(params)
                    }

                    override fun onBeginningOfSpeech() {
                        voiceCallBack?.onBeginningOfSpeech()
                    }

                    override fun onRmsChanged(value: Float) {
                        voiceCallBack?.onRmsChanged(value)
                    }

                    override fun onBufferReceived(buffer: ByteArray) {
                        voiceCallBack?.onBufferReceived(buffer)
                    }

                    override fun onEndOfSpeech() {
                        voiceCallBack?.onEndOfSpeech()
                    }

                    override fun onPartialResults(partialResults: Bundle) {
                        voiceCallBack?.onPartialResults(partialResults)
                    }

                    override fun onEvent(eventType: Int, params: Bundle) {
                        voiceCallBack?.onEvent(eventType, params)
                    }

                }
            ).initialize("Speak Now !!", activity!!)
    }

    fun isSpeaking(): Boolean {
        if (tts != null) {
            return !tts!!.isSpeaking
        }
        return false
    }

    fun onPause() {
        if (tts != null) {
            if (tts!!.isSpeaking) {
                tts!!.stop()
            }
        }
    }

    fun onDestroy() {
        if (tts != null) {
            if (tts!!.isSpeaking) {
                tts!!.stop()
                tts!!.shutdown()
            }
        }
    }

}