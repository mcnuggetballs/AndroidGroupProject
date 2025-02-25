package edu.androidgroupproject

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.view.SurfaceView
import com.edu.androidgroupproject.GameSystem

open class TextEntity : EntityBase {
    protected var bmp: Bitmap? = null
    protected var spritesheet: Sprite? = null
    var isDone: Boolean = false
    var Pos: Vector2 = Vector2()
    protected var sheetRow: Int = 0
    protected var sheetCol: Int = 0
    protected var velocity: Vector2 = Vector2(0f, 0f)
    protected var width: Float = 0f
    var height: Float = 0f
    protected var radius: Float = 0f
    protected var text: String? = null
    protected var paint: Paint = Paint()
    protected var textBounds: Rect = Rect()
    protected var myfont: Typeface? = null
    protected var pressed: Boolean = false
    var clicked: Boolean = false
    protected var border: Boolean = false
    protected var header: Boolean = false
    protected var defaultColor: Int = Color.WHITE
    protected var onclickColor: Int = Color.YELLOW

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

        paint.getTextBounds(text, 0, text!!.length, textBounds)
        width = textBounds.width().toFloat()
        height = textBounds.height().toFloat()

        radius = if (width > height) width * 0.5f
        else height * 0.5f
    }

    open fun OnClickFunction() {
        paint.color = onclickColor
    }

    open fun OffClickFunction() {
        paint.color = defaultColor
    }

    override fun Update(_dt: Float) {
        paint.getTextBounds(text, 0, text!!.length, textBounds)
        width = textBounds.width().toFloat()
        height = textBounds.height().toFloat()

        radius = if (width > height) width * 0.5f
        else height * 0.5f

        //Movement
        Pos.PlusEqual(velocity, _dt)

        if (spritesheet != null) spritesheet!!.Update(_dt)

        clicked = false
        if (TouchManager.Instance.IsDown()) {
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
        if (bmp != null) _canvas.drawBitmap(
            bmp!!,
            Pos.x - width * 0.5f,
            Pos.y - height * 0.5f,
            null
        )

        if (border == true) _canvas.drawRect(
            Pos.x - width * 0.5f,
            Pos.y - height * 0.5f,
            Pos.x + width * 0.5f,
            Pos.y + height * 0.5f,
            PaintColor.Companion.Instance.GetPaint(Color.BLACK)!!
        )

        if (header == true) _canvas.drawRect(
            0f,
            Pos.y - height * 0.8f,
            GameSystem.Instance.GetScreenScale()!!.x,
            Pos.y + height * 0.8f,
            PaintColor.Companion.Instance.GetPaint(Color.BLACK)!!
        )

        _canvas.drawText(text!!, Pos.x - width * 0.5f, Pos.y + height * 0.5f, paint)
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
        fun Create(xPos: Float, yPos: Float, text: String?, textSize: Float): TextEntity {
            val result = TextEntity()
            result.Pos.x = xPos
            result.Pos.y = yPos
            result.SetText(text)
            result.paint.textSize = textSize
            result.paint.color = Color.WHITE
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}