package edu.androidgroupproject

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
        return values().size
    }
}

class ParticleObject(var type: PARTICLETYPE) {
    private var bmp: Bitmap? = null
    var active: Boolean = false
    var position: Vector2 = Vector2()
    var velocity: Vector2 = Vector2()
    var target: Vector2 = Vector2()
    var width: Float = 0f
    var height: Float = 0f
    var timer: Float = 0f
    var paint: Paint = Paint() // Ensure paint is always initialized

    init {
        // If it's a blood particle, set its paint color to RED
        if (type == PARTICLETYPE.P_BLOOD) {
            paint.color = android.graphics.Color.RED
            paint.alpha = 255  // Full opacity initially
        }
    }

    fun SetBMP(_bmp: Bitmap?) {
        _bmp?.let {
            bmp = it
            width = it.width.toFloat()
            height = it.height.toFloat()
        }
    }

    fun GetBMP(): Bitmap? {
        return bmp
    }
}
