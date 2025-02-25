package edu.androidgroupproject

import android.graphics.BitmapFactory
import android.view.SurfaceView

class EnemyProjectile : Projectile() {
    override fun Init(_view: SurfaceView) {
        // Load bitmap safely
        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.enemybullet)

        // Ensure bmp is not null before accessing width/height
        width = bmp?.width?.toFloat() ?: 30f  // Default width if null
        height = bmp?.height?.toFloat() ?: 30f // Default height if null

        // Get screen dimensions
        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels

        velocity.y = 300f
        radius = if (width > height) width * 0.5f else height * 0.5f
    }

    override fun GetType(): String {
        return "EnemyBullet"
    }

    override fun OnHit(_other: Collidable) {
        if (_other.GetType() == "PlayerEntity") { // Use == for string comparison
            if (!_other.GetDead()) {
                SetIsDone(true)
            }
        }
    }

    companion object {
        fun Create(Damage: Float, xPos: Float, yPos: Float): EnemyProjectile {
            return EnemyProjectile().apply {
                Pos.x = xPos
                Pos.y = yPos
                this.Damage = Damage
                EntityManager.Instance.AddEntity(this) // Removed redundant Companion
            }
        }
    }
}
