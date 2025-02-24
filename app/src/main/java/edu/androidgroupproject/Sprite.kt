package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect

class Sprite(_bmp: Bitmap?, _row: Int, _col: Int, _fps: Int) {
    private val row = _row
    private val col = _col
    private val bmp: Bitmap? = _bmp

    private val width = bmp?.width?.div(_col) ?: 1
    private val height = bmp?.height?.div(_row) ?: 1

    private var currentFrame = 0
    private var startFrame = 0
    private var endFrame = _col * _row

    private val timePerFrame = 1.0f / _fps.toFloat()
    private var timeAcc = 0.0f

    fun Update(_dt: Float) {
        timeAcc += _dt
        if (timeAcc > timePerFrame) {
            ++currentFrame
            if (currentFrame >= endFrame) currentFrame = startFrame
            timeAcc = 0.0f
        }
    }

    fun Render(_canvas: Canvas, _x: Int, _y: Int) {
        bmp?.let { bitmap ->
            val frameX = currentFrame % col
            val frameY = currentFrame / col
            val srcX = frameX * width
            val srcY = frameY * height

            val adjustedX = (_x - 0.5f * width).toInt()
            val adjustedY = (_y - 0.5f * height).toInt()

            val src = Rect(srcX, srcY, srcX + width, srcY + height)
            val dst = Rect(adjustedX, adjustedY, adjustedX + width, adjustedY + height)

            _canvas.drawBitmap(bitmap, src, dst, null)
        }
    }

    fun SetAnimationFrames(_start: Int, _end: Int) {
        timeAcc = 0.0f
        currentFrame = _start
        startFrame = _start
        endFrame = _end
    }

    fun ContinueAnimationFrames(_start: Int, _end: Int) {
        val currentRow = currentFrame / col
        val currentCol = currentFrame % col

        // Ensure new frame is within _start and _end
        currentFrame = (_start + (currentCol % (endFrame - startFrame))).coerceIn(_start, _end)
        startFrame = _start
        endFrame = _end
    }

    fun GetCurrentFrameColumn(): Int = currentFrame % col

    fun GetHeight(): Int = height

    fun GetWidth(): Int = width
}
