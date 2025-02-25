package edu.androidgroupproject

import android.graphics.BitmapFactory
import android.view.SurfaceView
import com.edu.androidgroupproject.GameSystem

class PauseEntity : ButtonEntity() {
    override fun Init(_view: SurfaceView) {
        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.pause_button)

        // Ensure bmp is not null before using its width/height
        width = bmp?.width?.toFloat() ?: 50f // Default to 50px if null
        height = bmp?.height?.toFloat() ?: 50f // Default to 50px if null

        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels

        velocity.x = 0f
        velocity.y = 0f

        radius = if (width > height) width * 0.5f else height * 0.5f
    }

    override fun OnClickFunction() {
        if (GameSystem.Instance.GetGameSpeed() != 0f) {
            GameSystem.Instance.SetGameSpeed(0f)

            val distanceBetween = 200f
            val centerX = ScreenWidth * 0.5f
            val centerY = ScreenHeight * 0.5f

            // Ensure ResumeEntity and QuitEntity are not already created
            if (!EntityManager.Instance.ContainsEntity(ResumeEntity::class.java)) {
                ResumeEntity.Create(centerX - distanceBetween, centerY)
            }
            if (!EntityManager.Instance.ContainsEntity(QuitEntity::class.java)) {
                QuitEntity.Create(centerX + distanceBetween, centerY)
            }
        }
    }

    companion object {
        fun Create(xPos: Float, yPos: Float): PauseEntity {
            return PauseEntity().apply {
                Pos.x = xPos
                Pos.y = yPos
                EntityManager.Instance.AddEntity(this)
            }
        }
    }
}
