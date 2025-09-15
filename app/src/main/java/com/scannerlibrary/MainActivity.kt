package com.scannerlibrary

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.scannerlibrary.databinding.ActivityMainBinding
import com.scannerlibrary.objects.Scanner

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start scanner
        Scanner.start(
            this,
            cameraTime = 30000,          // auto-close after 30 sec
                                          // custom beep
            visibilityFlash = true,       // enable flash toggle
//            cameraFacing = Scanner.CameraFacing.BACK
        )
    }


    // Handle result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Scanner.REQUEST_CODE && data != null) {
            val scannedText = data.getStringExtra(Scanner.EXTRA_RESULT)
            binding.textView.text = scannedText
            Toast.makeText(this, "Scanned: $scannedText", Toast.LENGTH_SHORT).show()
        }
    }
}