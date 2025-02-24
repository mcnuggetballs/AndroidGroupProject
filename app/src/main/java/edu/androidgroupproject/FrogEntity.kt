package com.fishweeb.practical

import android.graphics.BitmapFactory
import android.view.SurfaceView
import edu.androidgroupproject.R

class FrogEntity : EnemyEntity() {
    private var xJumpSpeed = 0f
    private var yJumpSpeed = 0f
    private var JumpTimer = 0f
    private var Jumptime = 0f
    private var Friction = 0f
    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here

        health = 50f
        sheetRow = 3
        sheetCol = 3
        SheetInfectedStart = 1
        SheetInfectedEnd = 3
        SheetHitStart = 4
        SheetHitEnd = 6
        SheetNormalStart = 7
        SheetNormalEnd = 9
        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.sprite_frog)
        spritesheet = Sprite(bmp, sheetRow, sheetCol, 12)
        spritesheet!!.SetAnimationFrames(SheetInfectedStart, SheetInfectedEnd)

        damage = 8f
        score = 15
        gold = 5

        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels



        width = spritesheet!!.GetWidth().toFloat()
        height = spritesheet!!.GetHeight().toFloat()

        radius = if (width > height) width * 0.5f
        else height * 0.5f

        Pos.x = (Math.random().toFloat() * (ScreenWidth - width)) + (width * 0.5f)
        Pos.y = -height
        xJumpSpeed = Math.random().toFloat() * 160 - 80
        yJumpSpeed = Math.random().toFloat() * 260 + 130
        Vel.x = 0f
        Vel.y = 0f
        Jumptime = 2.0f
        JumpTimer = Jumptime
        Friction = yJumpSpeed * 0.5f
    }

    override fun DieFunction(_dt: Float) {
        if (health <= 0) {
            if (!dead) {
                PlayerInfo.Companion.Instance.AddScore(score)
                if (Math.random().toFloat() * 100 <= 50) SpawnPowerupStraight(10f)
                else SpawnPowerupSpread(10f)

                SpawnGold()
                xJumpSpeed = if (Pos.x > ScreenWidth * 0.5f) 500f
                else -500f
            }

            dead = true
        }
    }

    override fun Update(_dt: Float) {
        //Check if dead
        DieFunction(_dt)

        //Movement
        Pos.PlusEqual(Vel, _dt)

        if (Vel.y > Friction * _dt) {
            Vel.y -= Friction * _dt
        } else if (Vel.y < -Friction * _dt) {
            Vel.y += Friction * _dt
        } else {
            Vel.y = 0f
        }

        if (Vel.x > Friction * _dt) {
            Vel.x -= Friction * _dt
        } else if (Vel.x < -Friction * _dt) {
            Vel.x += Friction * _dt
        } else {
            Vel.x = 0f
        }

        JumpTimer -= _dt
        if (JumpTimer <= 0) {
            Vel.y = yJumpSpeed
            Vel.x = xJumpSpeed
            JumpTimer = Jumptime
        }

        Contrain()

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
        fun Create(): FrogEntity {
            val result = FrogEntity()
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}