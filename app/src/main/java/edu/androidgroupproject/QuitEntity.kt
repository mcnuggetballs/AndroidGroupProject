package com.fishweeb.practical

import android.graphics.BitmapFactory
import android.view.SurfaceView
import edu.androidgroupproject.R

class QuitEntity : ButtonEntity() {
    override fun Init(_view: SurfaceView) {
        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.quit_button)

        width = bmp?.width?.toFloat() ?: 50f
        height = bmp?.height?.toFloat() ?: 50f

        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels

        velocity.x = 0f
        velocity.y = 0f

        radius = maxOf(width, height) * 0.5f
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

    override fun OnClickFunction() {
        if (GameSystem.Instance.GetGameSpeed() == 0f) {
            GameSystem.Instance.SaveEditBegin()
            GameSystem.Instance.SetIntInSave("money", PlayerInfo.Instance.GetMoney())
            GameSystem.Instance.SaveEditEnd()
            StateManager.Instance.ChangeState("MainMenu")
        }
    }

    companion object {
        fun Create(xPos: Float, yPos: Float): QuitEntity {
            return QuitEntity().apply {
                Pos.x = xPos
                Pos.y = yPos
                EntityManager.Instance.AddEntity(this)
            }
        }
    }
}
