package com.sachin.voicerecog

import android.app.Activity

class TranslatorFactory private constructor() {

    enum class TRANSLATORS {
        TEXT_TO_SPEECH,
        SPEECH_TO_TEXT
    }

    interface IConverter {
        fun initialize(message: String, activity: Activity): IConverter

        fun getErrorText(errorCode: Int): String
    }


    fun with(TRANSLATORS: TRANSLATORS, conversionCallback: ConversionCallback): IConverter {
        return when (TRANSLATORS) {
            TranslatorFactory.TRANSLATORS.TEXT_TO_SPEECH ->
                TextToSpeckConverter(conversionCallback)

            TranslatorFactory.TRANSLATORS.SPEECH_TO_TEXT ->
                SpeechToTextConverter(conversionCallback)
        }
    }

    companion object {
        val instance = TranslatorFactory()
    }
}