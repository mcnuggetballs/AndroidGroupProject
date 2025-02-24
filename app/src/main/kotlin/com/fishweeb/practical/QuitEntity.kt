package com.fishweeb.practical

import android.graphics.BitmapFactory
import android.view.SurfaceView

class QuitEntity : ButtonEntity() {
    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here

        // sheetRow = 1;
        // sheetCol = 3;
        // spritesheet = new Sprite(BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_ray),sheetRow,sheetCol,12);

        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.quit_button)

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

    override fun OnClickFunction() {
        if (GameSystem.Companion.Instance.GetGameSpeed() == 0f) {
            GameSystem.Companion.Instance.SaveEditBegin()
            GameSystem.Companion.Instance.SetIntInSave(
                "money",
                PlayerInfo.Companion.Instance.GetMoney()
            )
            GameSystem.Companion.Instance.SaveEditEnd()
            StateManager.Companion.Instance.ChangeState("MainMenu")
        }
    }

    companion object {
        fun Create(xPos: Float, yPos: Float): QuitEntity {
            val result = QuitEntity()
            result.Pos.x = xPos
            result.Pos.y = yPos
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}
