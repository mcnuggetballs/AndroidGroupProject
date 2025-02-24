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
    var width: Float = 0f
    var height: Float = 0f
    protected var radius: Float = 0f
    protected var pressed: Boolean = false
    protected var clicked: Boolean = false

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
        // Define anything you need to use here

        // sheetRow = 1;
        // sheetCol = 3;
        // spritesheet = new Sprite(BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_ray),sheetRow,sheetCol,12);

        //bmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.bullet);

        //width = spritesheet.GetWidth();
        //height = spritesheet.GetHeight();

        // = bmp.getWidth();
        //height = bmp.getHeight();

        width = 30f
        height = 30f

        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels

        velocity.x = 0f
        velocity.y = 0f

        radius = if (width > height) width * 0.5f
        else height * 0.5f
    }

    open fun OnClickFunction() {
    }

    open fun OffClickFunction() {
    }

    override fun Update(_dt: Float) {
        //Movement
        Pos.PlusEqual(velocity, _dt)

        if (spritesheet != null) spritesheet!!.Update(_dt)

        clicked = false
        if (TouchManager.Companion.Instance.IsDown()) {
            if (Collision.SphereToSphere(
                    TouchManager.Companion.Instance.GetPosX().toFloat(),
                    TouchManager.Companion.Instance.GetPosY().toFloat(),
                    0f,
                    Pos.x,
                    Pos.y,
                    radius
                )
            ) {
                if (pressed == false) {
                    clicked = true
                    OnClickFunction()
                    pressed = true
                }
            }
        } else {
            if (pressed == true) {
                OffClickFunction()
                pressed = false
            }
        }
    }

    override fun Render(_canvas: Canvas) {
        // Render anything
        if (spritesheet != null) spritesheet!!.Render(_canvas, Pos.x.toInt(), Pos.y.toInt())
        else if (bmp != null) _canvas.drawBitmap(
            bmp!!,
            Pos.x - (width * 0.5f),
            Pos.y - (height * 0.5f),
            null
        )
        else _canvas.drawRect(
            Pos.x - width * 0.5f,
            Pos.y - height * 0.5f,
            Pos.x + width * 0.5f,
            Pos.y + height * 0.5f,
            PaintColor.Companion.Instance.GetPaint(Color.BLUE)!!
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
        //Init any variables here
        protected val TAG: String? = null
        fun Create(xPos: Float, yPos: Float): ButtonEntity {
            val result = ButtonEntity()
            result.Pos.x = xPos
            result.Pos.y = yPos
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}

