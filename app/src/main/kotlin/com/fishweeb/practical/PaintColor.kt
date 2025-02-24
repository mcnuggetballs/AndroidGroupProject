package com.fishweeb.practical

import android.graphics.Color
import android.graphics.Paint

class PaintColor
private constructor() {
    private val Red = Paint()
    private val Blue: Paint
    private val Black: Paint
    private val Yellow: Paint

    init {
        Red.color = Color.RED

        Blue = Paint()
        Blue.color = Color.BLUE

        Black = Paint()
        Black.color = Color.BLACK

        Yellow = Paint()
        Yellow.color = Color.YELLOW
    }

    fun GetPaint(_color: Int): Paint? {
        return when (_color) {
            Color.RED -> Red
            Color.BLUE -> Blue
            Color.BLACK -> Black
            Color.YELLOW -> Yellow
            else -> null
        }
    }

    companion object {
        var Instance: PaintColor = PaintColor()
    }
}
