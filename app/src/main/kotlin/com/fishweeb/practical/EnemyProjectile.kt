package com.fishweeb.practical

import android.graphics.BitmapFactory
import android.view.SurfaceView

class EnemyProjectile : Projectile() {
    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here

        // sheetRow = 1;
        // sheetCol = 3;
        // spritesheet = new Sprite(BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_ray),sheetRow,sheetCol,12);

        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.enemybullet)

        //width = spritesheet.GetWidth();
        //height = spritesheet.GetHeight();
        width = bmp.width.toFloat()
        height = bmp.height.toFloat()

        //width = 30;
        //height = 30;
        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels
        velocity.y = 300f

        radius = if (width > height) width * 0.5f
        else height * 0.5f
    }

    override fun GetType(): String {
        return "EnemyBullet"
    }

    override fun OnHit(_other: Collidable) {
        if (_other.GetType() === "PlayerEntity") {
            if (!_other.GetDead()) SetIsDone(true)
        }
    }

    companion object {
        fun Create(Damage: Float, xPos: Float, yPos: Float): EnemyProjectile {
            val result = EnemyProjectile()
            result.Pos.x = xPos
            result.Pos.y = yPos
            result.Damage = Damage
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}
