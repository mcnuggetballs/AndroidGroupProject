package com.fishweeb.practical

import android.graphics.BitmapFactory
import android.util.Log
import android.view.SurfaceView
import edu.androidgroupproject.R

class MantisEntity : EnemyEntity() {
    private var goingRight = false

    override fun Init(_view: SurfaceView) {
        goingRight = true

        health = 50f
        sheetRow = 3
        sheetCol = 3
        SheetInfectedStart = 1
        SheetInfectedEnd = 3
        SheetHitStart = 4
        SheetHitEnd = 6
        SheetNormalStart = 7
        SheetNormalEnd = 9

        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.sprite_ray)
        spritesheet = bmp?.let { Sprite(it, sheetRow, sheetCol, 12) }
        spritesheet?.SetAnimationFrames(SheetInfectedStart, SheetInfectedEnd)

        damage = 10f
        score = 45
        gold = 10

        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels

        width = spritesheet?.GetWidth()?.toFloat() ?: 50f
        height = spritesheet?.GetHeight()?.toFloat() ?: 50f

        radius = if (width > height) width * 0.5f else height * 0.5f

        Pos.x = (Math.random().toFloat() * (ScreenWidth - width)) + (width * 0.5f)
        Pos.y = -height
        Vel.x = Math.random().toFloat() * 160 - 80
        Vel.y = Math.random().toFloat() * 160 + 80
    }

    override fun Contrain() {
        if (!dead) {
            when {
                Pos.x - (width * 0.5f) < 0 -> {
                    goingRight = true
                    Pos.x = width * 0.5f
                    Vel.x = 0f
                }
                Pos.x + (width * 0.5f) > ScreenWidth -> {
                    goingRight = false
                    Pos.x = ScreenWidth - (width * 0.5f)
                    Vel.x = 0f
                }
            }
        } else {
            if (Pos.x + (width * 0.5f) < 0 || Pos.x - (width * 0.5f) > ScreenWidth) {
                SetIsDone(true)
                Log.v("MantisEntity", "Out Of Bounds! *Deleted*")
            }
        }
    }

    override fun DieFunction(_dt: Float) {
        if (health <= 0) {
            if (!dead) {
                PlayerInfo.Instance.AddScore(score)
                if (Math.random().toFloat() * 100 <= 50) {
                    SpawnPowerupStraight(20f)
                } else {
                    SpawnPowerupSpread(20f)
                }
                SpawnGold()
            }
            dead = true
            Vel.x += if (Pos.x > ScreenWidth * 0.5f) 500 * _dt else -500 * _dt
        }
    }

    override fun Update(_dt: Float) {
        DieFunction(_dt)

        Pos.PlusEqual(Vel, _dt)
        Contrain()

        if (!dead) {
            Vel.x += if (goingRight) _dt * 500 else -_dt * 500
            if (Vel.x > 300) goingRight = false
            if (Vel.x < -300) goingRight = true
        }

        if (Pos.y - height > ScreenHeight) {
            SetIsDone(true)
        }

        if (hit) {
            hitcounter -= _dt
            if (hitcounter <= 0) {
                spritesheet?.ContinueAnimationFrames(
                    if (dead) SheetNormalStart else SheetInfectedStart,
                    if (dead) SheetNormalEnd else SheetInfectedEnd
                )
                hit = false
            }
        }

        spritesheet?.Update(_dt)
    }

    companion object {
        fun Create(): MantisEntity {
            return MantisEntity().apply {
                EntityManager.Instance.AddEntity(this)
            }
        }
    }
}
