package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect

class Sprite
    (_bmp: Bitmap?, _row: Int, _col: Int, _fps: Int) {
    private var row = 0
    private var col = 0
    private var width = 0
    private var height = 0

    private var bmp: Bitmap? = null

    private var currentFrame = 0
    private var startFrame = 0
    private var endFrame = 0

    private var timePerFrame = 0.0f
    private var timeAcc = 0.0f

    init {
        bmp = _bmp
        row = _row
        col = _col

        width = bmp!!.width / _col
        height = bmp.height / _row

        timePerFrame = 1.0f / _fps.toFloat()

        endFrame = _col * _row
    }

    fun Update(_dt: Float) {
        timeAcc += _dt
        if (timeAcc > timePerFrame) {
            ++currentFrame
            if (currentFrame >= endFrame) currentFrame = startFrame
            timeAcc = 0.0f
        }
    }

    fun Render(_canvas: Canvas, _x: Int, _y: Int) {
        var _x = _x
        var _y = _y
        val frameX = currentFrame % col
        val frameY = currentFrame / col
        val srcX = frameX * width
        val srcY = frameY * height

        _x = (_x - 0.5f * width).toInt()
        _y = (_y - 0.5f * height).toInt()

        val src = Rect(srcX, srcY, srcX + width, srcY + height)

        val dst = Rect(_x, _y, _x + width, _y + height)

        _canvas.drawBitmap(bmp!!, src, dst, null)
    }

    fun SetAnimationFrames(_start: Int, _end: Int) {
        timeAcc = 0.0f
        currentFrame = _start
        startFrame = _start
        endFrame = _end
    }

    fun ContinueAnimationFrames(_start: Int, _end: Int) {
        val currentRow = (_end / col) - 1
        currentFrame = (currentFrame % col) + (currentRow * col)
        startFrame = _start
        endFrame = _end
    }

    fun GetCurrentFrameColumn(): Int {
        val currentRow = (currentFrame / col)
        val currentCol = currentFrame - currentRow * col
        return currentCol
    }

    fun GetHeight(): Int {
        return height
    }

    fun GetWidth(): Int {
        return width
    }
}
