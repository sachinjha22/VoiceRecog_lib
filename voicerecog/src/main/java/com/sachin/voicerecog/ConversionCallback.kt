package com.sachin.voicerecog

import android.os.Bundle
import android.speech.tts.TextToSpeech

interface ConversionCallback {

    fun onSuccess(result: String)

    fun onCompletion()

    fun onErrorOccurred(errorMessage: String)

    fun onSpeaking(textToSpeech: TextToSpeech)

    fun onReadyForSpeech(params: Bundle) = Unit

    fun onBeginningOfSpeech() = Unit

    fun onRmsChanged(value: Float) = Unit

    fun onBufferReceived(buffer: ByteArray) = Unit

    fun onEndOfSpeech() = Unit

    fun onPartialResults(partialResults: Bundle) = Unit

    fun onEvent(eventType: Int, params: Bundle) = Unit
}