package com.fishweeb.practical

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.SurfaceView

class VibrateManager {
    private var _vibrator: Vibrator? = null

    fun Init(_view: SurfaceView) {
        _vibrator = _view.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun startVibrate() {
        if (Build.VERSION.SDK_INT >= 26) {
            _vibrator!!.vibrate(VibrationEffect.createOneShot(150, 10))
            Log.v(TAG, "VIBRATED")
        } else {
            val pattern = longArrayOf(0, 50, 0)
            _vibrator!!.vibrate(pattern, -1)
            Log.v(TAG, "VIBRATED")
        }
    }

    fun stopVibrate() {
        _vibrator!!.cancel()
    }

    companion object {
        val Instance: VibrateManager = VibrateManager()
        protected val TAG: String? = null
    }
}
