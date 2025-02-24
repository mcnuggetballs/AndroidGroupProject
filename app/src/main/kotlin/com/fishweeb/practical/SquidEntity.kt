package com.fishweeb.practical

import android.graphics.BitmapFactory
import android.view.SurfaceView

class SquidEntity : EnemyEntity() {
    private var xJumpSpeed = 0f
    private var yJumpSpeed = 0f
    private var JumpTimer = 0f
    private var Jumptime = 0f
    private var Friction = 0f
    private var Jumped = false
    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here

        health = 200f
        sheetRow = 3
        sheetCol = 25
        SheetInfectedStart = 1
        SheetInfectedEnd = 25
        SheetHitStart = 26
        SheetHitEnd = 50
        SheetNormalStart = 51
        SheetNormalEnd = 75
        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.sprite_squid2)
        spritesheet = Sprite(bmp, sheetRow, sheetCol, 8)
        spritesheet!!.SetAnimationFrames(SheetInfectedStart, SheetInfectedEnd)

        damage = 3f
        score = 100
        gold = 25
        BulletDamage = 3f

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
        Jumped = false
    }

    override fun DieFunction(_dt: Float) {
        if (health <= 0) {
            if (!dead) {
                PlayerInfo.Companion.Instance.AddScore(score)
                if (Math.random().toFloat() * 100 <= 50) SpawnPowerupStraight(10f)
                else SpawnPowerupSpread(10f)

                SpawnGold()
                xJumpSpeed = if (Pos.x > ScreenWidth * 0.5f) 250f
                else -250f
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

        if (spritesheet!!.GetCurrentFrameColumn() == 1) {
            if (!Jumped) {
                Jumped = true
                Vel.y = yJumpSpeed
                Vel.x = xJumpSpeed
                if (!dead) EnemyProjectile.Companion.Create(BulletDamage, Pos.x, Pos.y)
            }
        } else {
            Jumped = false
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
        fun Create(): SquidEntity {
            val result = SquidEntity()
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}