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
    protected var health: Float = 0f
    protected var hit: Boolean = false
    protected var hitcounter: Float = 0f
    protected var hitTime: Float = 0.1f
    protected var dead: Boolean = false
    protected var damage: Float = 0f
    protected var BulletDamage: Float = 0f
    protected var score: Int = 0
    protected var gold: Int = 0

    protected var renderLayer: Int = 0


    override fun IsDone(): Boolean {
        return isDone
    }

    override fun SetIsDone(_isDone: Boolean) {
        isDone = _isDone
    }

    override fun SetRenderLayer(_newLayer: Int) {
        renderLayer = _newLayer
    }

    override fun GetRenderLayer(): Int {
        return renderLayer
    }

    override fun Init(_view: SurfaceView) {
        // Define anything you need to use here
        spritesheet = Sprite(bmp, sheetRow, sheetCol, 12)
        spritesheet!!.SetAnimationFrames(SheetInfectedStart, SheetInfectedEnd)

        damage = 5f
        score = 10
        gold = 3

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


    fun SpawnGold() {
        for (i in 0 until gold) {
            val tempParticle: ParticleObject =
                ParticleManager.Companion.Instance.FetchParticle(PARTICLETYPE.P_MONEY)
            tempParticle.position.x = Pos.x
            tempParticle.position.y = Pos.y
            tempParticle.width = 20f
            tempParticle.height = 20f
            tempParticle.velocity.x = Math.random().toFloat() * 1000 - 500
            tempParticle.velocity.y = Math.random().toFloat() * 1000 - 500
        }
    }

    fun SpawnBlood() {
        var i = 0
        while (i < radius) {
            val tempParticle: ParticleObject =
                ParticleManager.Companion.Instance.FetchParticle(PARTICLETYPE.P_BLOOD)
            tempParticle.position.x = Pos.x
            tempParticle.position.y = Pos.y
            tempParticle.width = 10f
            tempParticle.height = 10f
            tempParticle.velocity.x = Math.random().toFloat() * 500 - 250
            tempParticle.velocity.y = Math.random().toFloat() * 500 - 250
            ++i
        }
    }

    fun SpawnPowerupStraight(chanceOverHundred: Float) {
        if (Math.random().toFloat() * 100 <= chanceOverHundred) {
            val tempParticle: ParticleObject =
                ParticleManager.Companion.Instance.FetchParticle(PARTICLETYPE.P_POWERUP_STRAIGHT)
            tempParticle.position.x = Pos.x
            tempParticle.position.y = Pos.y
            tempParticle.SetBMP(ImageManager.Companion.Instance.GetImage(IMAGE.I_POWERUP_STRAIGHT)!!)
        }
    }

    fun SpawnPowerupSpread(chanceOverHundred: Float) {
        if (Math.random().toFloat() * 100 <= chanceOverHundred) {
            val tempParticle: ParticleObject =
                ParticleManager.Companion.Instance.FetchParticle(PARTICLETYPE.P_POWERUP_SPREAD)
            tempParticle.position.x = Pos.x
            tempParticle.position.y = Pos.y
            tempParticle.SetBMP(ImageManager.Companion.Instance.GetImage(IMAGE.I_POWERUP_SPREAD)!!)
        }
    }

    open fun DieFunction(_dt: Float) {
        if (health <= 0) {
            if (!dead) {
                //AudioManager.Instance.PlayAudio(R.raw.correct,2);
                PlayerInfo.Companion.Instance.AddScore(score)
                SpawnGold()

                if (Math.random().toFloat() * 100 <= 50) SpawnPowerupStraight(5f)
                else SpawnPowerupSpread(5f)
            }

            dead = true
            if (Pos.x > ScreenWidth * 0.5f) Vel.x += 500 * _dt
            else Vel.x -= 500 * _dt
        }
    }

    override fun GetDead(): Boolean {
        return dead
    }

    open fun Contrain() {
        //Out Of Bounds
        if (!dead) {
            if (Pos.x - (width * 0.5f) < 0) {
                Pos.x = 0 + (width * 0.5f)
                Vel.x = -Vel.x
            } else if (Pos.x + (width * 0.5f) > ScreenWidth) {
                Pos.x = ScreenWidth - (width * 0.5f)
                Vel.x = -Vel.x
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

        //Constrains
        Contrain()

        if (Pos.y - height > ScreenHeight) {
            Log.v(TAG, "Out Of Bounds! *Deleted*")
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

    override fun Render(_canvas: Canvas) {
        // Render anything

        //Rotation
        //Matrix transform = new Matrix();
        //transform.postTranslate(-bmp.getWidth()*0.5f,-bmp.getHeight()*0.5f);
        //transform.postRotate(100);
        //_canvas.drawBitmap(bmp,transform,null);

        spritesheet!!.Render(_canvas, Pos.x.toInt(), Pos.y.toInt())
    }

    override fun GetType(): String {
        return "EnemyEntity"
    }

    override fun GetPosX(): Float {
        return Pos.x
    }

    override fun GetPosY(): Float {
        return Pos.y
    }

    override fun GetRadius(): Float {
        return radius
    }

    override fun GetWidth(): Float {
        return width
    }

    override fun GetHeight(): Float {
        return height
    }

    override fun GetDamage(): Float {
        return damage
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
                SpawnBlood()
            }
        }
    }

    companion object {
        //Init any variables here
        protected val TAG: String? = null
        fun Create(health: Float, image: Bitmap?, sheetRow: Int, sheetCol: Int): EnemyEntity {
            val result = EnemyEntity()

            result.health = 30f

            result.bmp = image
            result.sheetRow = sheetRow
            result.sheetCol = sheetCol

            result.SheetInfectedStart = 1
            result.SheetInfectedEnd = sheetCol
            result.SheetHitStart = 1 + (sheetCol * 1)
            result.SheetHitEnd = sheetCol * 2
            result.SheetNormalStart = 1 + (sheetCol * 2)
            result.SheetNormalEnd = sheetCol * 3

            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }

        fun Create(
            health: Float,
            image: Bitmap?,
            sheetRow: Int,
            sheetCol: Int,
            _layer: Int
        ): EnemyEntity {
            val result = Create(health, image, sheetRow, sheetCol)
            result.SetRenderLayer(_layer)
            return result
        }
    }
}

