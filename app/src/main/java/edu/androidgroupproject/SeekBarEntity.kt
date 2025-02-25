package edu.androidgroupproject

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceView

open class SeekBarEntity : EntityBase {
    protected var bmp: Bitmap? = null
    protected var sliderBmp: Bitmap? = null
    var isDone: Boolean = false
    var Pos: Vector2 = Vector2()
    var SliderPos: Vector2 = Vector2()
    protected var sliderWidth: Float = 0f
    protected var sliderHeight: Float = 0f
    protected var width: Float = 0f
    protected var height: Float = 0f
    protected var xStart: Float = 0f
    protected var dragging: Boolean = false
    protected var textPaint: Paint = Paint()
    protected var textBounds: Rect = Rect()
    override fun IsDone(): Boolean {
        return isDone
    }

    override fun SetIsDone(_isDone: Boolean) {
        isDone = _isDone
    }

    override fun SetRenderLayer(_newLayer: Int) {
        return
    }

    override fun GetRenderLayer(): Int {
        return LayerConstants.BUTTON_LAYER
    }

    override fun Init(_view: SurfaceView) {
    }

    override fun Update(_dt: Float) {
        if (TouchManager.Instance.HasTouch()) {
            if (Collision.AABBToAABB(
                    Pos.x,
                    Pos.y,
                    width,
                    height,
                    TouchManager.Instance.GetPosX().toFloat(),
                    TouchManager.Instance.GetPosY().toFloat(),
                    1f,
                    1f
                )
            ) {
                dragging = true
            }
        } else {
            dragging = false
        }

        if (dragging == true) {
            val xOffset: Float = TouchManager.Instance.GetPosX() - xStart
            if (xOffset > 0 && xOffset < width) SliderPos.x =
                TouchManager.Instance.GetPosX().toFloat()
            else if (xOffset <= 0) {
                SliderPos.x = Pos.x - width * 0.5f
            } else if (xOffset >= width) {
                SliderPos.x = Pos.x + width * 0.5f
            }
        }
    }

    override fun Render(_canvas: Canvas) {
        if (bmp == null) {
            _canvas.drawRect(
                Pos.x - width * 0.5f,
                Pos.y - height * 0.5f,
                Pos.x + width * 0.5f,
                Pos.y + height * 0.5f,
                PaintColor.Companion.Instance.GetPaint(Color.BLACK)!!
            )
        }

        if (sliderBmp == null) {
            _canvas.drawRect(
                SliderPos.x - sliderWidth * 0.5f,
                SliderPos.y - sliderHeight * 0.5f,
                SliderPos.x + sliderWidth * 0.5f,
                SliderPos.y + sliderHeight * 0.5f,
                PaintColor.Companion.Instance.GetPaint(Color.BLUE)!!
            )
        }

        val xOffset = SliderPos.x - xStart
        val value = ((xOffset / width) * 100).toInt()
        _canvas.drawText(
            "" + value,
            Pos.x + width * 0.6f,
            Pos.y + textBounds.height() * 0.5f,
            textPaint
        )
    }

    fun SetPosY(yPos: Float) {
        Pos.y = yPos
    }

    fun SetPosX(xPos: Float) {
        Pos.x = xPos
    }

    companion object {
        protected val TAG: String? = null
        fun Create(_xPos: Float, _yPos: Float, barWidth: Float, barHeight: Float): SeekBarEntity {
            val result = SeekBarEntity()
            EntityManager.Companion.Instance.AddEntity(result)
            result.Pos.x = _xPos
            result.Pos.y = _yPos
            result.SliderPos.x = _xPos
            result.SliderPos.y = _yPos
            result.width = barWidth
            result.height = barHeight
            result.sliderWidth = barWidth * 0.1f
            result.sliderHeight = barHeight
            result.xStart = result.Pos.x - barWidth * 0.5f
            result.textPaint.color = Color.BLACK
            result.textPaint.isFakeBoldText = true
            result.textPaint.textSize = barHeight
            result.textPaint.getTextBounds("50", 0, 2, result.textBounds)
            return result
        }
    }
}
