package com.scannerlibrary

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.chit1998.scannerlibrary.R
import com.chit1998.scannerlibrary.databinding.ActivityScannerBinding
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.scannerlibrary.objects.Scanner
import java.util.Timer
import java.util.TimerTask

class ScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScannerBinding
    private lateinit var scannerOptions: ScannerOptions
    private lateinit var camera: Camera
    private lateinit var timer: Timer
    private var rawValue: String? = ""
    private var flash = false

    private val scanner by lazy { BarcodeScanning.getClient() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 Check permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            setupScanner()
        }

    }

    private fun setupScanner() {
        // 🔹 Get config from Intent
        val cameraTime = intent.getLongExtra("cameraTime", 60000)
        val soundId = intent.getIntExtra("soundId", R.raw.beep)
        val visibilityFlash = intent.getBooleanExtra("visibilityFlash", false)
        val cameraFacing = intent.getIntExtra("cameraFacing", Scanner.CameraFacing.BACK)

        scannerOptions = ScannerOptions.Builder()
            .setCameraTime(cameraTime)
            .setSoundId(soundId)
            .setVisibilityFlash(visibilityFlash)
            .build()

        startCamera(cameraFacing, soundId)

        // // 🔹 Auto-close after cameraTime
        // timer = Timer()
        // timer.schedule(object : TimerTask() {
        //     override fun run() {
        //         finish()
        //     }
        // }, cameraTime)
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                Handler(Looper.getMainLooper()).post {
//                    val activity = context as? Activity ?: return@post
//                    // Clear entire task and close app flow
//                    activity.finishAffinity()
                    closeApp(this@ScannerActivity)
                }
            }
        }, cameraTime)

    }

    @OptIn(ExperimentalGetImage::class)
    private fun startCamera(cameraFace: Int, @RawRes sound: Int) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.surfaceProvider = binding.preview.surfaceProvider
            }

            val imageAnalyzer = ImageAnalysis.Builder().build().also {
                it.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val inputImage = InputImage.fromMediaImage(
                            mediaImage,
                            imageProxy.imageInfo.rotationDegrees
                        )

                        scanner.process(inputImage)
                            .addOnSuccessListener { barcodes ->
                                for (barcode in barcodes) {
                                    if (rawValue.isNullOrEmpty()) {
                                        rawValue = barcode.rawValue
                                        scannerOptions.cameraBeep(true, this, sound)

                                        // 🔹 Send result back
                                        val resultIntent = Intent()
                                        resultIntent.putExtra(Scanner.EXTRA_RESULT, rawValue)
                                        setResult(Scanner.REQUEST_CODE, resultIntent)
                                        finish()
                                    }
                                }
                            }
                            .addOnCompleteListener { imageProxy.close() }
                    } else {
                        imageProxy.close()
                    }
                }
            }

            val cameraSelector =
                if (cameraFace == Scanner.CameraFacing.FRONT)
                    CameraSelector.DEFAULT_FRONT_CAMERA
                else
                    CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )

                // update builder with camera
                scannerOptions = ScannerOptions.Builder()
                    .setCamera(camera)
                    .setSoundId(scannerOptions.soundId)
                    .setCameraTime(scannerOptions.cameraTime)
                    .setVisibilityFlash(scannerOptions.visibilityFlash)
                    .build()

                // flash button
                binding.imgFlash.setOnClickListener {
                    if (!flash) {
                        flash = true
                        scannerOptions.cameraTorch(true)
                        binding.imgFlash.setImageResource(R.drawable.flash_off_rounded)
                    } else {
                        flash = false
                        scannerOptions.cameraTorch(false)
                        binding.imgFlash.setImageResource(R.drawable.flash_on_rounded)
                    }
                }

            } catch (e: Exception) {
                Log.d(TAG, "startCamera: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onPause() {
        super.onPause()
        if (rawValue.isNullOrEmpty()) {
            timer.cancel()
            finish()
            return
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupScanner()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                setResult(RESULT_CANCELED)
                finish()
            }
        }
    }

    fun closeApp(activity: Activity) {
        Handler(Looper.getMainLooper()).post {
            activity.finishAffinity()
        }
    }

    companion object {
        const val TAG = "ScannerActivity"
        const val REQUEST_CAMERA_PERMISSION = 1001
    }
}
