package com.scannerlibrary

import android.content.Context
import android.media.SoundPool
import androidx.annotation.RawRes
import androidx.camera.core.Camera
import com.scannerlibrary.listeners.CameraInfosListener

open class ScannerOptions private constructor(
    val camera: Camera?,
    val soundId: Int,
    val cameraTime: Long,
    val visibilityFlash: Boolean
) : CameraInfosListener {

    override fun cameraTorch(boolean: Boolean) {
        if (visibilityFlash) {
            if (camera?.cameraInfo?.hasFlashUnit() == true) {
                camera.cameraControl.enableTorch(boolean)
            }
        }
    }

    override fun cameraBeep(boolean: Boolean, context: Context, @RawRes sound: Int) {
        val soundPool = SoundPool.Builder().setMaxStreams(1).build()
        soundPool.load(context, sound, 1)
        soundPool.setOnLoadCompleteListener { sp, id, _ ->
            sp.play(id, 1f, 1f, 0, 0, 1f)
        }
    }

    // Builder
    class Builder {
        private var camera: Camera? = null
        var soundId: Int = 0
        var cameraTime: Long = 60000
        var visibilityFlash: Boolean = false

        fun setCamera(camera: Camera?) = apply { this.camera = camera }
        fun setSoundId(soundId: Int) = apply { this.soundId = soundId }
        fun setCameraTime(cameraTime: Long) = apply { this.cameraTime = cameraTime }
        fun setVisibilityFlash(visibilityFlash: Boolean) = apply { this.visibilityFlash = visibilityFlash }

        fun build(): ScannerOptions {
            return ScannerOptions(camera, soundId, cameraTime, visibilityFlash)
        }
    }
}