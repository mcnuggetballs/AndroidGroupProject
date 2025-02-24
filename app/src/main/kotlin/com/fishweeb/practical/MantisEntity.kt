package com.fishweeb.practical

import android.graphics.BitmapFactory
import android.util.Log
import android.view.SurfaceView

class MantisEntity : EnemyEntity() {
    private var goingRight = false

    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here
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
        spritesheet = Sprite(bmp, sheetRow, sheetCol, 12)
        spritesheet!!.SetAnimationFrames(SheetInfectedStart, SheetInfectedEnd)

        damage = 10f
        score = 45
        gold = 10

        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels



        width = spritesheet!!.GetWidth().toFloat()
        height = spritesheet!!.GetHeight().toFloat()

        radius = if (width > height) width * 0.5f
        else height * 0.5f

        Pos.x = (Math.random().toFloat() * (ScreenWidth - width)) + (width * 0.5f)
        Pos.y = -height
        Vel.x = Math.random().toFloat() * 160 - 80
        Vel.y = Math.random().toFloat() * 160 + 80
    }

    override fun Contrain() {
        //Out Of Bounds
        if (!dead) {
            if (Pos.x - (width * 0.5f) < 0) {
                goingRight = true
                Pos.x = 0 + (width * 0.5f)
                Vel.x = 0f
            } else if (Pos.x + (width * 0.5f) > ScreenWidth) {
                goingRight = false
                Pos.x = ScreenWidth - (width * 0.5f)
                Vel.x = 0f
            }
        } else {
            if (Pos.x + (width * 0.5f) < 0) {
                SetIsDone(true)
                Log.v(EnemyEntity.Companion.TAG, "Out Of Bounds! *Deleted*")
            } else if (Pos.x - (width * 0.5f) > ScreenWidth) {
                SetIsDone(true)
                Log.v(EnemyEntity.Companion.TAG, "Out Of Bounds! *Deleted*")
            }
        }
    }

    override fun DieFunction(_dt: Float) {
        if (health <= 0) {
            if (!dead) {
                PlayerInfo.Companion.Instance.AddScore(score)
                if (Math.random().toFloat() * 100 <= 50) SpawnPowerupStraight(20f)
                else SpawnPowerupSpread(20f)
                SpawnGold()
            }

            dead = true
            if (Pos.x > ScreenWidth * 0.5f) Vel.x += 500 * _dt
            else Vel.x -= 500 * _dt
        }
    }

    override fun Update(_dt: Float) {
        //Check if dead
        DieFunction(_dt)

        //Movement
        Pos.PlusEqual(Vel, _dt)

        Contrain()

        if (!dead) {
            if (goingRight) {
                Vel.x += _dt * 500
                if (Vel.x > 300) goingRight = false
            } else {
                Vel.x -= _dt * 500
                if (Vel.x < -300) goingRight = true
            }
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
        fun Create(): MantisEntity {
            val result = MantisEntity()
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}

