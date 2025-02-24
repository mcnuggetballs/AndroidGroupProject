package com.fishweeb.practical

import android.graphics.BitmapFactory
import android.view.SurfaceView
import edu.androidgroupproject.R

class LitterEntity : EnemyEntity() {
    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here
        health = 80f
        sheetRow = 2
        sheetCol = 19
        SheetInfectedStart = 1
        SheetInfectedEnd = 19
        SheetHitStart = 20
        SheetHitEnd = 38
        bmp = if (Math.random()
                .toFloat() * 100 > 50f
        ) BitmapFactory.decodeResource(_view.resources, R.drawable.sprite_toxic1)
        else BitmapFactory.decodeResource(_view.resources, R.drawable.sprite_toxic2)
        spritesheet = Sprite(bmp, sheetRow, sheetCol, 5)
        spritesheet!!.SetAnimationFrames(SheetInfectedStart, SheetInfectedEnd)

        damage = 30f
        score = 80
        gold = 15

        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels



        width = spritesheet!!.GetWidth().toFloat()
        height = spritesheet!!.GetHeight().toFloat()

        radius = if (width > height) width * 0.5f
        else height * 0.5f

        Pos.x = (Math.random().toFloat() * (ScreenWidth - width)) + (width * 0.5f)
        Pos.y = -height
        Vel.x = Math.random().toFloat() * 40 - 20
        Vel.y = Math.random().toFloat() * 160 + 80
    }

    override fun DieFunction(_dt: Float) {
        if (health <= 0) {
            if (!dead) {
                PlayerInfo.Companion.Instance.AddScore(score)
                SpawnPowerupStraight(100f)
                SpawnGold()
            }
            dead = true
            SetIsDone(true)
        }
    }

    override fun Contrain() {
        //Out Of Bounds

        if (Pos.x - (width * 0.5f) < 0) {
            Pos.x = 0 + (width * 0.5f)
            Vel.x = -Vel.x
        } else if (Pos.x + (width * 0.5f) > ScreenWidth) {
            Pos.x = ScreenWidth - (width * 0.5f)
            Vel.x = -Vel.x
        }
    }

    override fun Update(_dt: Float) {
        //Check if dead
        DieFunction(_dt)

        //Movement
        Pos.PlusEqual(Vel, _dt)

        Contrain()

        if (Pos.y - height > ScreenHeight) {
            SetIsDone(true)
        }

        if (hit) {
            hitcounter -= _dt
            if (hitcounter <= 0) {
                spritesheet!!.ContinueAnimationFrames(SheetInfectedStart, SheetInfectedEnd)
                hit = false
            }
        }

        spritesheet!!.Update(_dt)
    }

    override fun OnHit(_other: Collidable) {
        if (_other.GetType() === "Bullet") {
            health -= _other.GetDamage()
            if (!dead) {
                hit = true
                hitcounter = hitTime
                spritesheet!!.ContinueAnimationFrames(SheetHitStart, SheetHitEnd)
            }
        } else if (_other.GetType() === "PlayerEntity") {
            if (!dead) {
                SetIsDone(true)
            }
        }
    }

    companion object {
        fun Create(): LitterEntity {
            val result = LitterEntity()
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}

