package com.fishweeb.practical

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceView
import edu.androidgroupproject.R

class ResumeEntity : ButtonEntity() {
    private val pauseBG = Paint().apply {
        color = Color.BLACK
        alpha = 200
    }

    override fun Init(_view: SurfaceView) {
        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.resume_button)

        width = bmp?.width?.toFloat() ?: 50f
        height = bmp?.height?.toFloat() ?: 50f

        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels

        velocity.x = 0f
        velocity.y = 0f

        radius = maxOf(width, height) * 0.5f
    }

    override fun OnClickFunction() {
        GameSystem.Instance.SetGameSpeed(1f)
    }

    override fun Update(_dt: Float) {
        Pos.PlusEqual(velocity, _dt)
        spritesheet?.Update(_dt)

        if (TouchManager.Instance.HasTouch()) {
            if (Collision.SphereToSphere(
                    TouchManager.Instance.GetPosX().toFloat(),
                    TouchManager.Instance.GetPosY().toFloat(),
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

        if (GameSystem.Instance.GetGameSpeed() != 0f) SetIsDone(true)
    }

    override fun Render(_canvas: Canvas) {
        if (GameSystem.Instance.GetGameSpeed() == 0f) {
            _canvas.drawRect(
                0f,
                0f,
                ScreenWidth.toFloat(),
                ScreenHeight.toFloat(),
                pauseBG
            )
        }

        when {
            spritesheet != null -> spritesheet!!.Render(_canvas, Pos.x.toInt(), Pos.y.toInt())
            bmp != null -> _canvas.drawBitmap(
                bmp!!,
                Pos.x - (width * 0.5f),
                Pos.y - (height * 0.5f),
                null
            )
            else -> _canvas.drawRect(
                Pos.x - width * 0.5f,
                Pos.y - height * 0.5f,
                Pos.x + width * 0.5f,
                Pos.y + height * 0.5f,
                PaintColor.Instance.GetPaint(Color.BLUE) ?: return
            )
        }
    }

    companion object {
        fun Create(xPos: Float, yPos: Float): ResumeEntity {
            return ResumeEntity().apply {
                Pos.x = xPos
                Pos.y = yPos
                EntityManager.Instance.AddEntity(this)
            }
        }
    }
}
