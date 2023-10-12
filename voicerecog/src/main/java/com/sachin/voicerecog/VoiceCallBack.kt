package com.sachin.voicerecog

import android.os.Bundle
import android.speech.tts.TextToSpeech

interface VoiceCallBack {

    fun onSuccessTTS(result: String) = Unit

    fun onCompletionTTS() = Unit

    fun onErrorOccurredTTS(errorMessage: String) = Unit

    fun onSpeakingTTS(textToSpeech: TextToSpeech) = Unit

    fun onSuccessSTT(result: String) = Unit

    fun onCompletionSTT() = Unit

    fun onErrorOccurredSTT(errorMessage: String) = Unit

    fun onSpeakingSTT(textToSpeech: TextToSpeech) = Unit

    fun onReadyForSpeech(params: Bundle) = Unit

    fun onBeginningOfSpeech() = Unit

    fun onRmsChanged(value: Float) = Unit

    fun onBufferReceived(buffer: ByteArray) = Unit

    fun onEndOfSpeech() = Unit

    fun onPartialResults(partialResults: Bundle) = Unit

    fun onEvent(eventType: Int, params: Bundle) = Unit
}