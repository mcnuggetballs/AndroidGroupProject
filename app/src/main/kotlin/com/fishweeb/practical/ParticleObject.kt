package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.Paint

enum class PARTICLETYPE {
    P_MONEY,
    P_BLOOD,
    P_POWERUP_STRAIGHT,
    P_POWERUP_SPREAD,
    P_BUBBLE,
    P_FISH,
    P_NUM;

    fun GetSize(): Int {
        return size
    }

    companion object {
        private val size = entries.size
    }
}

class ParticleObject
    (var type: PARTICLETYPE) {
    private var bmp: Bitmap? = null
    var active: Boolean = false
    var position: Vector2 = Vector2()
    var velocity: Vector2 = Vector2()
    var target: Vector2 = Vector2()
    var width: Float = 0f
    var height: Float = 0f
    var timer: Float = 0f
    var paint: Paint? = null

    fun SetBMP(_bmp: Bitmap) {
        bmp = _bmp
        width = _bmp.width.toFloat()
        height = _bmp.height.toFloat()
    }

    fun GetBMP(): Bitmap? {
        return bmp
    }
}
