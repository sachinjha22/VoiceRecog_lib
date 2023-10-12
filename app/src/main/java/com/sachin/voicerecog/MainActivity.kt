package com.sachin.voicerecog

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity(), VoiceCallBack {
    private var voiceRecognition: VoiceRecognition? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpUi()
    }

    private fun setUpUi() {
        voiceRecognition = VoiceRecognition().getInstance(this, this)
        val btnStart = findViewById<Button>(R.id.btn_start)

        try {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO),
                100
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        btnStart.setOnClickListener {
            voiceRecognition?.speechToText()
        }
    }

    override fun onSuccessSTT(result: String) {
        super.onSuccessSTT(result)
        val text = findViewById<TextView>(R.id.recognizedText)
        text.text = result
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        when (requestCode) {
            100 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("VVV", "1")
                voiceRecognition?.textToSpeech("Hello How Can I Help you")
            } else {
                Toast.makeText(
                    this,
                    "Permission Denied!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}