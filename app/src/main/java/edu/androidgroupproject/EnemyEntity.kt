package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.SurfaceView

open class EnemyEntity : EntityBase, Collidable {
    protected var bmp: Bitmap? = null
    protected var spritesheet: Sprite? = null
    var isDone: Boolean = false
    protected var Pos: Vector2 = Vector2()
    protected var ScreenWidth: Int = 0
    protected var ScreenHeight: Int = 0
    protected var sheetRow: Int = 0
    protected var sheetCol: Int = 0
    protected var SheetInfectedStart: Int = 0
    protected var SheetInfectedEnd: Int = 0
    protected var SheetHitStart: Int = 0
    protected var SheetHitEnd: Int = 0
    protected var SheetNormalStart: Int = 0
    protected var SheetNormalEnd: Int = 0
    protected var Vel: Vector2 = Vector2()
    protected var width: Float = 0f
    protected var height: Float = 0f
    protected var radius: Float = 0f
    protected var health: Float = 30f
    protected var hit: Boolean = false
    protected var hitcounter: Float = 0f
    protected var hitTime: Float = 0.1f
    protected var dead: Boolean = false
    protected var damage: Float = 5f
    protected var BulletDamage: Float = 0f
    protected var score: Int = 10
    protected var gold: Int = 3
    protected var renderLayer: Int = 0

    override fun IsDone(): Boolean = isDone

    override fun SetIsDone(_isDone: Boolean) {
        isDone = _isDone
    }

    override fun SetRenderLayer(_newLayer: Int) {
        renderLayer = _newLayer
    }

    override fun GetRenderLayer(): Int = renderLayer

    override fun Init(_view: SurfaceView) {
        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels

        spritesheet = bmp?.let { Sprite(it, sheetRow, sheetCol, 12) }
        spritesheet?.SetAnimationFrames(SheetInfectedStart, SheetInfectedEnd)

        width = spritesheet?.GetWidth()?.toFloat() ?: 50f
        height = spritesheet?.GetHeight()?.toFloat() ?: 50f

        radius = if (width > height) width * 0.5f else height * 0.5f

        Pos.x = (Math.random().toFloat() * (ScreenWidth - width)) + (width * 0.5f)
        Pos.y = -height
        Vel.x = Math.random().toFloat() * 40 - 20
        Vel.y = Math.random().toFloat() * 160 + 80
    }

    fun SpawnGold() {
        repeat(gold) {
            ParticleManager.Instance.FetchParticle(PARTICLETYPE.P_MONEY)?.apply {
                position.x = Pos.x
                position.y = Pos.y
                width = 20f
                height = 20f
                velocity.x = Math.random().toFloat() * 1000 - 500
                velocity.y = Math.random().toFloat() * 1000 - 500
            }
        }
    }

    fun SpawnBlood() {
        repeat(radius.toInt()) {
            ParticleManager.Instance.FetchParticle(PARTICLETYPE.P_BLOOD)?.apply {
                position.x = Pos.x
                position.y = Pos.y
                width = 10f
                height = 10f
                velocity.x = Math.random().toFloat() * 500 - 250
                velocity.y = Math.random().toFloat() * 500 - 250
            }
        }
    }

    fun SpawnPowerupStraight(chanceOverHundred: Float) {
        SpawnPowerup(PARTICLETYPE.P_POWERUP_STRAIGHT, IMAGE.I_POWERUP_STRAIGHT, chanceOverHundred)
    }

    fun SpawnPowerupSpread(chanceOverHundred: Float) {
        SpawnPowerup(PARTICLETYPE.P_POWERUP_SPREAD, IMAGE.I_POWERUP_SPREAD, chanceOverHundred)
    }

    private fun SpawnPowerup(type: PARTICLETYPE, image: IMAGE, chanceOverHundred: Float) {
        if (Math.random().toFloat() * 100 <= chanceOverHundred) {
            ParticleManager.Instance.FetchParticle(type)?.apply {
                position.x = Pos.x
                position.y = Pos.y
                SetBMP(ImageManager.Instance.GetImage(image) ?: return)
            }
        }
    }

    open fun DieFunction(_dt: Float) {
        if (health <= 0) {
            if (!dead) {
                PlayerInfo.Instance.AddScore(score)
                SpawnGold()

                if (Math.random().toFloat() * 100 <= 50) SpawnPowerupStraight(5f)
                else SpawnPowerupSpread(5f)
            }

            dead = true
            Vel.x += if (Pos.x > ScreenWidth * 0.5f) 500 * _dt else -500 * _dt
        }
    }

    override fun GetDead(): Boolean = dead

    open fun Contrain() {
        if (!dead) {
            if (Pos.x - (width * 0.5f) < 0 || Pos.x + (width * 0.5f) > ScreenWidth) {
                Pos.x = maxOf(0 + (width * 0.5f), minOf(Pos.x, ScreenWidth - (width * 0.5f)))
                Vel.x = -Vel.x
            }
        } else if (Pos.x + width < 0 || Pos.x > ScreenWidth) {
            SetIsDone(true)
        }
    }

    override fun Update(_dt: Float) {
        DieFunction(_dt)
        Pos.PlusEqual(Vel, _dt)
        Contrain()

        if (Pos.y - height > ScreenHeight) {
            Log.v("EnemyEntity", "Out Of Bounds! *Deleted*")
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

    override fun Render(_canvas: Canvas) {
        spritesheet?.Render(_canvas, Pos.x.toInt(), Pos.y.toInt())
    }

    override fun GetType(): String = "EnemyEntity"
    override fun GetPosX(): Float = Pos.x
    override fun GetPosY(): Float = Pos.y
    override fun GetRadius(): Float = radius
    override fun GetWidth(): Float = width
    override fun GetHeight(): Float = height
    override fun GetDamage(): Float = damage

    override fun OnHit(_other: Collidable) {
        if (_other.GetType() == "Bullet") {
            health -= _other.GetDamage()
            if (!dead) {
                hit = true
                hitcounter = hitTime
                spritesheet?.ContinueAnimationFrames(SheetHitStart, SheetHitEnd)
            }
        } else if (_other.GetType() == "PlayerEntity" && !dead) {
            SetIsDone(true)
            SpawnBlood()
        }
    }

    companion object {
        fun Create(
            health: Float,
            bmp: Bitmap?,
            row: Int,
            col: Int
        ): EnemyEntity {
            val enemy = EnemyEntity()
            enemy.health = health
            enemy.bmp = bmp
            enemy.sheetRow = row
            enemy.sheetCol = col
            return enemy
        }
    }
}
