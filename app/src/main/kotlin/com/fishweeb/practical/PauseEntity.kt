package com.fishweeb.practical

import android.graphics.BitmapFactory
import android.view.SurfaceView

class PauseEntity : ButtonEntity() {
    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here

        // sheetRow = 1;
        // sheetCol = 3;
        // spritesheet = new Sprite(BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_ray),sheetRow,sheetCol,12);

        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.pause_button)

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

    override fun OnClickFunction() {
        if (GameSystem.Companion.Instance.GetGameSpeed() != 0f) {
            GameSystem.Companion.Instance.SetGameSpeed(0f)
            val distanceBetween = 200f
            ResumeEntity.Companion.Create(ScreenWidth * 0.5f - distanceBetween, ScreenHeight * 0.5f)
            QuitEntity.Companion.Create(ScreenWidth * 0.5f + distanceBetween, ScreenHeight * 0.5f)
        }
    }

    companion object {
        fun Create(xPos: Float, yPos: Float): PauseEntity {
            val result = PauseEntity()
            result.Pos.x = xPos
            result.Pos.y = yPos
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}
