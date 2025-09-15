package com.scannerlibrary.objects

import android.app.Activity
import android.content.Intent
import androidx.annotation.RawRes
import com.chit1998.scannerlibrary.R
import com.scannerlibrary.ScannerActivity

object Scanner {
    const val REQUEST_CODE = 2001
    const val EXTRA_RESULT = "scanner_result"

    fun start(
        activity: Activity,
        cameraTime: Long = 60000,
        @RawRes soundId: Int? = null,
        visibilityFlash: Boolean = false,
        cameraFacing: Int = CameraFacing.BACK
    ) {
        val intent = Intent(activity, ScannerActivity::class.java).apply {
            putExtra("cameraTime", cameraTime)
            putExtra("soundId", soundId ?: R.raw.beep)
            putExtra("visibilityFlash", visibilityFlash)
            putExtra("cameraFacing", cameraFacing)
        }
        activity.startActivityForResult(intent, REQUEST_CODE)
    }

    object CameraFacing {
        const val FRONT = 0
        const val BACK = 1
    }
}