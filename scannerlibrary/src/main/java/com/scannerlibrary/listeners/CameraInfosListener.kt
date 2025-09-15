package com.scannerlibrary.listeners

import android.content.Context
import androidx.annotation.RawRes

interface CameraInfosListener {
    fun cameraTorch(boolean: Boolean)
    fun cameraBeep(boolean: Boolean, context: Context, @RawRes sound: Int)
}