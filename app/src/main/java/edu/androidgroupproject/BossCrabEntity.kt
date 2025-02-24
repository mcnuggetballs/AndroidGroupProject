package com.fishweeb.practical

import android.view.SurfaceView

class BossCrabEntity : EnemyEntity() {
    private var shot = false
    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here
        SpawnManager.Companion.Instance.bossTime = true
        health = 1000f
        sheetRow = 3
        sheetCol = 17
        SheetInfectedStart = 1
        SheetInfectedEnd = 17
        SheetHitStart = 18
        SheetHitEnd = 34
        SheetNormalStart = 35
        SheetNormalEnd = 51
        spritesheet = ImageManager.Companion.Instance.GetCrabSpriteSheet()
        spritesheet!!.SetAnimationFrames(1, 2)

        damage = PlayerInfo.Companion.Instance.GetMaxHealth()
        BulletDamage = 3f
        score = 300
        gold = 100

        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels



        width = spritesheet!!.GetWidth().toFloat()
        height = spritesheet!!.GetHeight().toFloat()

        radius = if (width > height) width * 0.5f
        else height * 0.5f

        Pos.x = ScreenWidth * 0.5f
        Pos.y = -height
        Vel.x = 0f
        Vel.y = 200f
        shot = false
    }

    override fun DieFunction(_dt: Float) {
        if (health <= 0) {
            if (!dead) {
                PlayerInfo.Companion.Instance.AddScore(score)
                SpawnGold()

                SpawnManager.Companion.Instance.bossTime = false
            }

            dead = true
            if (Pos.x > ScreenWidth * 0.5f) Vel.x += 500 * _dt
            else Vel.x -= 500 * _dt
        }
    }

    override fun Contrain() {
        //Out Of Bounds
        if (!dead) {
            if (Pos.y > ScreenWidth * 0.2f) {
                spritesheet!!.ContinueAnimationFrames(SheetInfectedStart, SheetInfectedEnd)
                Vel.y = 0f
                Pos.y = ScreenWidth * 0.2f
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
            if (spritesheet!!.GetCurrentFrameColumn() == 16) {
                if (!shot) {
                    for (i in 0..8) {
                        val temp: EnemyProjectile = EnemyProjectile.Companion.Create(
                            BulletDamage,
                            Pos.x,
                            Pos.y + height * 0.5f
                        )
                        temp.velocity.x = (70 * (i - 4)).toFloat()
                    }
                    shot = true
                }
            } else {
                shot = false
            }
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
        fun Create(): BossCrabEntity {
            val result = BossCrabEntity()
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}