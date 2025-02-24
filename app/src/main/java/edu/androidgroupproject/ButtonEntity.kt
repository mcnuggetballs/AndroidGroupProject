package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.SurfaceView

open class ButtonEntity : EntityBase {
    protected var bmp: Bitmap? = null
    protected var spritesheet: Sprite? = null
    var isDone: Boolean = false
    var Pos: Vector2 = Vector2()
    protected var ScreenWidth: Int = 0
    protected var ScreenHeight: Int = 0
    protected var sheetRow: Int = 0
    protected var sheetCol: Int = 0
    protected var velocity: Vector2 = Vector2()
    var width: Float = 30f
    var height: Float = 30f
    protected var radius: Float = 0f
    protected var pressed: Boolean = false
    protected var clicked: Boolean = false

    override fun IsDone(): Boolean = isDone

    override fun SetIsDone(_isDone: Boolean) {
        isDone = _isDone
    }

    override fun SetRenderLayer(_newLayer: Int) {
        // No implementation needed
    }

    override fun GetRenderLayer(): Int = LayerConstants.BUTTON_LAYER

    override fun Init(_view: SurfaceView) {
        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels

        velocity.x = 0f
        velocity.y = 0f

        radius = if (width > height) width * 0.5f else height * 0.5f
    }

    open fun OnClickFunction() {
        // Override in subclasses if needed
    }

    open fun OffClickFunction() {
        // Override in subclasses if needed
    }

    override fun Update(_dt: Float) {
        // Movement
        Pos.PlusEqual(velocity, _dt)

        spritesheet?.Update(_dt)

        clicked = false
        if (TouchManager.Instance.IsDown()) {
            if (Collision.SphereToSphere(
                    TouchManager.Instance.GetPosX().toFloat(),
                    TouchManager.Instance.GetPosY().toFloat(),
                    0f,
                    Pos.x,
                    Pos.y,
                    radius
                )
            ) {
                if (!pressed) {
                    clicked = true
                    OnClickFunction()
                    pressed = true
                }
            }
        } else {
            if (pressed) {
                OffClickFunction()
                pressed = false
            }
        }
    }

    override fun Render(_canvas: Canvas) {
        spritesheet?.Render(_canvas, Pos.x.toInt(), Pos.y.toInt())
            ?: bmp?.let { _canvas.drawBitmap(it, Pos.x - (width * 0.5f), Pos.y - (height * 0.5f), null) }
            ?: _canvas.drawRect(
                Pos.x - width * 0.5f,
                Pos.y - height * 0.5f,
                Pos.x + width * 0.5f,
                Pos.y + height * 0.5f,
                PaintColor.Instance.GetPaint(Color.BLUE) ?: return
            )
    }

    fun SetPosY(yPos: Float) {
        Pos.y = yPos
    }

    fun SetPosX(xPos: Float) {
        Pos.x = xPos
    }

    fun SetVelX(xVel: Float) {
        velocity.x = xVel
    }

    fun SetVelY(yVel: Float) {
        velocity.y = yVel
    }

    companion object {
        fun Create(xPos: Float, yPos: Float): ButtonEntity {
            return ButtonEntity().apply {
                Pos.x = xPos
                Pos.y = yPos
                EntityManager.Instance.AddEntity(this)
            }
        }
    }
}
