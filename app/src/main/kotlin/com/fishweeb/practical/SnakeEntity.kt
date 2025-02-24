package com.fishweeb.practical

import android.graphics.BitmapFactory
import android.view.SurfaceView

class SnakeEntity : EnemyEntity() {
    private var goingRight = false

    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here
        goingRight = true

        health = 40f
        sheetRow = 3
        sheetCol = 3
        SheetInfectedStart = 1
        SheetInfectedEnd = 3
        SheetHitStart = 4
        SheetHitEnd = 6
        SheetNormalStart = 7
        SheetNormalEnd = 9
        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.sprite_snake)
        spritesheet = Sprite(bmp, sheetRow, sheetCol, 12)
        spritesheet!!.SetAnimationFrames(SheetInfectedStart, SheetInfectedEnd)

        damage = 5f
        score = 30
        gold = 13

        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels



        width = spritesheet!!.GetWidth().toFloat()
        height = spritesheet!!.GetHeight().toFloat()

        radius = if (width > height) width * 0.5f
        else height * 0.5f

        Pos.x = (Math.random().toFloat() * (ScreenWidth - width)) + (width * 0.5f)
        Pos.y = -height
        Vel.x = Math.random().toFloat() * 360 - 180
        Vel.y = Math.random().toFloat() * 360 + 180
    }

    override fun Contrain() {
        //Out Of Bounds
        if (!dead) {
            if (Pos.x - (width * 0.5f) < 0) {
                Pos.x = 0 + (width * 0.5f)
                Vel.x = 0f
            } else if (Pos.x + (width * 0.5f) > ScreenWidth) {
                Pos.x = ScreenWidth - (width * 0.5f)
                Vel.x = 0f
            }
        } else {
            if (Pos.x + (width * 0.5f) < 0) {
                SetIsDone(true)
            } else if (Pos.x - (width * 0.5f) > ScreenWidth) {
                SetIsDone(true)
            }
        }
    }

    override fun Update(_dt: Float) {
        //Check if dead
        DieFunction(_dt)

        //Movement
        Pos.PlusEqual(Vel, _dt)

        Contrain()

        if (!dead) {
            var direction = Vector2()
            direction = (PlayerInfo.Companion.Instance.GetPos().Minus(Pos)).Normalized()
            Vel.x += direction.x * 300 * _dt
        }


        if (Pos.y - height > ScreenHeight) {
            SetIsDone(true)
        }

        if (hit) {
            hitcounter -= _dt
            if (hitcounter <= 0) {
                if (dead) spritesheet!!.ContinueAnimationFrames(SheetNormalStart, SheetNormalEnd)
                else spritesheet!!.ContinueAnimationFrames(SheetInfectedStart, SheetInfectedEnd)

                hit = false
            }
        }

        spritesheet!!.Update(_dt)
    }

    companion object {
        fun Create(): SnakeEntity {
            val result = SnakeEntity()
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}