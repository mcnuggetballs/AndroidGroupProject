package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.view.SurfaceView

class ParticleButtonEntity : EntityBase {
    protected var bmp: Bitmap? = null
    protected var itembmp: Bitmap? = null
    protected var spritesheet: Sprite? = null
    var isDone: Boolean = false
    var Pos: Vector2 = Vector2()
    protected var sheetRow: Int = 0
    protected var sheetCol: Int = 0
    protected var velocity: Vector2 = Vector2(0f, 0f)
    var width: Float = 0f
    protected var height: Float = 0f
    protected var radius: Float = 0f
    protected var text: String? = null
    protected var paint: Paint = Paint()
    protected var textBounds: Rect = Rect()
    protected var textwidth: Float = 0f
    protected var textheight: Float = 0f
    protected var myfont: Typeface? = null
    protected var pressed: Boolean = false
    var clicked: Boolean = false
    protected var border: Boolean = false
    protected var header: Boolean = false
    protected var defaultColor: Int = Color.WHITE
    protected var onclickColor: Int = Color.YELLOW
    var Cost: Int = 0
    var bought: Boolean = false

    fun SetBMP(_bmp: Bitmap?) {
        bmp = Bitmap.createScaledBitmap(_bmp!!, width.toInt(), height.toInt(), true)
    }

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
        return LayerConstants.UI_LAYER
    }

    fun SetText(_text: String?) {
        text = _text
    }

    fun GetText(): String? {
        return text
    }

    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here
        myfont = Typeface.createFromAsset(_view.context.assets, "fonts/akashi.ttf")
        paint.setTypeface(myfont)

        text = if (!bought) Cost.toString()
        else "OWN"
        paint.getTextBounds(text, 0, text!!.length, textBounds)
        textwidth = textBounds.width().toFloat()
        textheight = textBounds.height().toFloat()

        radius = if (width > height) width * 0.5f
        else height * 0.5f
    }

    fun OnClickFunction() {
        paint.color = onclickColor
    }

    fun OffClickFunction() {
        paint.color = defaultColor
    }

    override fun Update(_dt: Float) {
        text = if (!bought) Cost.toString()
        else "OWN"
        paint.getTextBounds(text, 0, text!!.length, textBounds)
        textwidth = textBounds.width().toFloat()
        textheight = textBounds.height().toFloat()

        radius = if (width > height) width * 0.5f
        else height * 0.5f

        //Movement
        Pos.PlusEqual(velocity, _dt)

        if (spritesheet != null) spritesheet!!.Update(_dt)

        clicked = false
        if (TouchManager.Companion.Instance.IsDown()) {
            if (Collision.AABBToAABB(
                    Pos.x,
                    Pos.y,
                    width,
                    height,
                    TouchManager.Companion.Instance.GetPosX().toFloat(),
                    TouchManager.Companion.Instance.GetPosY().toFloat(),
                    1f,
                    1f
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
        _canvas.drawBitmap(bmp!!, Pos.x - width * 0.5f, Pos.y - height * 0.5f, null)
        _canvas.drawBitmap(itembmp!!, Pos.x - width * 0.5f, Pos.y - height * 0.5f - width, null)
        _canvas.drawText(text!!, Pos.x - textwidth * 0.5f, Pos.y + textheight * 0.5f, paint)
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

    fun GetWidth(): Float {
        return width
    }

    fun GetHeight(): Float {
        return height
    }

    fun SetHeader(_input: Boolean) {
        header = _input
    }

    fun SetPaintColor(_color: Int) {
        paint.color = _color
        defaultColor = _color
        onclickColor = _color
    }

    fun SetDefaultTextColor(_color: Int) {
        defaultColor = _color
    }

    fun SetOnClickTextColor(_color: Int) {
        onclickColor = _color
    }

    companion object {
        //Init any variables here
        protected val TAG: String? = null
        fun Create(
            _bmp: Bitmap?,
            _item: Bitmap?,
            xPos: Float,
            yPos: Float,
            buttonsize: Float,
            textSize: Float,
            cost: Int
        ): ParticleButtonEntity {
            val result = ParticleButtonEntity()
            result.Pos.x = xPos
            result.Pos.y = yPos
            result.paint.textSize = textSize
            result.paint.color = Color.WHITE
            result.width = buttonsize
            result.height = buttonsize * 0.3f
            result.SetBMP(_bmp)
            result.Cost = cost
            result.itembmp =
                Bitmap.createScaledBitmap(_item!!, result.width.toInt(), result.width.toInt(), true)

            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}