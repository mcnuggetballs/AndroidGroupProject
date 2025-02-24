package com.fishweeb.practical

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceView

class ResumeEntity : ButtonEntity() {
    private val pauseBG = Paint()

    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here

        // sheetRow = 1;
        // sheetCol = 3;
        // spritesheet = new Sprite(BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_ray),sheetRow,sheetCol,12);

        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.resume_button)

        //width = spritesheet.GetWidth();
        //height = spritesheet.GetHeight();
        width = bmp.width.toFloat()
        height = bmp.height.toFloat()

        //width = 30;
        //height = 30;
        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels

        velocity.x = 0f
        velocity.y = 0f

        radius = if (width > height) width * 0.5f
        else height * 0.5f

        pauseBG.color = Color.BLACK
        pauseBG.alpha = 200
    }

    override fun OnClickFunction() {
        GameSystem.Companion.Instance.SetGameSpeed(1f)
    }

    override fun Update(_dt: Float) {
        //Movement
        Pos.PlusEqual(velocity, _dt)

        if (spritesheet != null) spritesheet!!.Update(_dt)

        if (TouchManager.Companion.Instance.HasTouch()) {
            if (Collision.SphereToSphere(
                    TouchManager.Companion.Instance.GetPosX().toFloat(),
                    TouchManager.Companion.Instance.GetPosY().toFloat(),
                    0f,
                    Pos.x,
                    Pos.y,
                    radius
                )
            ) {
                OnClickFunction()
            }
        } else {
            OffClickFunction()
        }

        if (GameSystem.Companion.Instance.GetGameSpeed() != 0f) SetIsDone(true)
    }

    override fun Render(_canvas: Canvas) {
        if (GameSystem.Companion.Instance.GetGameSpeed() == 0f) _canvas.drawRect(
            0f,
            0f,
            ScreenWidth.toFloat(),
            ScreenHeight.toFloat(),
            pauseBG
        )

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

    companion object {
        fun Create(xPos: Float, yPos: Float): ResumeEntity {
            val result = ResumeEntity()
            result.Pos.x = xPos
            result.Pos.y = yPos
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}
