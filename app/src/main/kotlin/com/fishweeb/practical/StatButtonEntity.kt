package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.view.SurfaceView

open class StatButtonEntity : EntityBase {
    protected var bmp: Bitmap? = null
    var isDone: Boolean = false
    var Pos: Vector2 = Vector2()
    protected var width: Float = 0f
    protected var height: Float = 0f
    protected var radius: Float = 0f
    var paint: Paint = Paint()
    protected var textBounds: Rect = Rect()
    protected var textWidth: Int = 0
    var textHeight: Int = 0
    protected var myfont: Typeface? = null
    protected var pressed: Boolean = false
    protected var clicked: Boolean = false

    protected var Value: Int = 0
    protected var AddValue: Int = 0
    protected var Cost: Int = 0
    protected var CurrentCost: Int = 0
    protected var CostText: String? = null
    protected var coinBitMap: Bitmap? = null

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

    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here
        myfont = Typeface.createFromAsset(_view.context.assets, "fonts/akashi.ttf")
        paint.setTypeface(myfont)

        width = bmp!!.width.toFloat()
        height = bmp!!.height.toFloat()

        radius = if (width > height) width * 0.5f
        else height * 0.5f


        CostText = CurrentCost.toString()
        paint.getTextBounds(CostText, 0, CostText!!.length, textBounds)
        textWidth = textBounds.width()
        textHeight = textBounds.height()

        CurrentCost = Cost * (Value / AddValue)
    }

    open fun OnClickFunction() {
    }

    open fun OffClickFunction() {
    }

    override fun Update(_dt: Float) {
        CostText = CurrentCost.toString()
        paint.getTextBounds(CostText, 0, CostText!!.length, textBounds)
        textWidth = textBounds.width()
        textHeight = textBounds.height()
        CurrentCost = Cost * (Value / AddValue)

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
        if (bmp != null) _canvas.drawBitmap(
            bmp!!,
            Pos.x - width * 0.5f,
            Pos.y - height * 0.5f,
            null
        )

        _canvas.drawText(CostText!!, Pos.x + width * 0.5f, Pos.y + textHeight * 0.5f, paint)

        _canvas.drawBitmap(
            coinBitMap!!,
            Pos.x + width * 0.5f + textWidth,
            Pos.y - height * 0.5f,
            null
        )
    }

    fun SetPosY(yPos: Float) {
        Pos.y = yPos
    }

    fun SetPosX(xPos: Float) {
        Pos.x = xPos
    }

    fun GetWidth(): Float {
        return width
    }

    fun GetHeight(): Float {
        return height
    }

    companion object {
        //Init any variables here
        protected val TAG: String? = null
        fun Create(
            xPos: Float,
            yPos: Float,
            value: Int,
            addvalue: Int,
            cost: Int,
            buttonSize: Float,
            textSize: Float
        ): StatButtonEntity {
            val result = StatButtonEntity()
            result.Pos.x = xPos
            result.Pos.y = yPos
            result.bmp = Bitmap.createScaledBitmap(
                ResourceManager.Companion.Instance.GetBitmap(R.drawable.plusbutton)!!,
                buttonSize.toInt(),
                buttonSize.toInt(),
                true
            )
            result.coinBitMap = Bitmap.createScaledBitmap(
                ResourceManager.Companion.Instance.GetBitmap(R.drawable.coin)!!,
                buttonSize.toInt(),
                buttonSize.toInt(),
                true
            )
            result.Value = value
            result.AddValue = addvalue
            result.Cost = cost
            result.paint.color = Color.BLACK
            result.paint.textSize = textSize
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}