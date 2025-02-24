package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.view.SurfaceView

class PlayerEntity : EntityBase, Collidable {
    private var bmp: Bitmap? = null
    var isDone: Boolean = false
    private var ScreenWidth = 0
    private var ScreenHeight = 0
    private val Pos = Vector2()
    private val Vel = Vector2()
    private var width = 0f
    private var height = 0f
    private var radius = 0f
    private var MAX_VEL = 0f
    private var MOVE_SPEED = 0f
    private var DeathDelay = 0f

    private var ShootCounterMain = 0f
    private var ShootCounterSubLeft = 0f
    private var ShootCounterSubRight = 0f


    private var spritesheet: Sprite? = null
    private var sheetRow = 0
    private var sheetCol = 0

    override fun IsDone(): Boolean {
        return isDone
    }

    override fun SetIsDone(_isDone: Boolean) {
        isDone = _isDone
    }

    override fun SetRenderLayer(_newLayer: Int) {
        return
    }

    override fun GetRenderLayer(): Int {
        return LayerConstants.GAMEOBJECTS_LAYER
    }

    override fun Init(_view: SurfaceView) {
        DeathDelay = 2f
        // Define anything you need to use here
        sheetRow = 1
        sheetCol = 3
        spritesheet = Sprite(
            BitmapFactory.decodeResource(_view.resources, R.drawable.sprite_spaceship),
            sheetRow,
            sheetCol,
            12
        )
        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.sprite_spaceship)

        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels



        width = spritesheet!!.GetWidth().toFloat()
        height = spritesheet!!.GetHeight().toFloat()
        radius = if (width > height) width * 0.5f
        else height * 0.5f

        Pos.x = ScreenWidth * 0.5f
        Pos.y = ScreenHeight - (height * 2)
        Vel.x = 0f
        Vel.y = 0f

        MAX_VEL = 600f
        MOVE_SPEED = 1600f

        ShootCounterMain = 0f
        ShootCounterSubLeft = 0f
        ShootCounterSubRight = 0f

        if (PlayerInfo.Companion.Instance.GetMainWeapon() != null) ShootCounterMain =
            PlayerInfo.Companion.Instance.GetMainWeapon().GetFireRate()

        if (PlayerInfo.Companion.Instance.GetSubWeaponLeft() != null) ShootCounterSubLeft =
            PlayerInfo.Companion.Instance.GetSubWeaponLeft().GetFireRate()



        if (PlayerInfo.Companion.Instance.GetSubWeaponRight() != null) ShootCounterSubRight =
            PlayerInfo.Companion.Instance.GetSubWeaponRight().GetFireRate()
    }

    override fun Update(_dt: Float) {
        //Health
        if (PlayerInfo.Companion.Instance.GetHealth() <= 0) {
            PlayerInfo.Companion.Instance.SetHealth(0f)
            DeathDelay -= _dt
            if (DeathDelay <= 0) {
                GameSystem.Companion.Instance.AddHighScore(
                    HighScore(
                        PlayerInfo.Companion.Instance.GetScore(),
                        "PLAYER"
                    )
                )
                StateManager.Companion.Instance.ChangeState("GameOver")
                SetIsDone(true)
            }
        }

        if (PlayerInfo.Companion.Instance.GetHealth() <= 0) return

        //Movement
        Pos.PlusEqual(Vel, _dt)

        //Out Of Bounds
        if (Pos.x - (width * 0.5f) < 0) {
            Pos.x = 0 + (width * 0.5f)
            Vel.x = 0f
        } else if (Pos.x + (width * 0.5f) > ScreenWidth) {
            Pos.x = ScreenWidth - (width * 0.5f)
            Vel.x = 0f
        }

        if (Pos.y - (height * 0.5f) < 0) {
            Pos.y = 0 + (height * 0.5f)
            Vel.y = 0f
        } else if (Pos.y + (height * 0.5f) > ScreenHeight) {
            Pos.y = ScreenHeight - (height * 0.5f)
            Vel.y = 0f
        }

        //Limit Vel
        if (Vel.x > MAX_VEL) Vel.x = MAX_VEL
        else if (Vel.x < -MAX_VEL) Vel.x = -MAX_VEL

        if (Vel.y > MAX_VEL) Vel.y = MAX_VEL
        else if (Vel.y < -MAX_VEL) Vel.y = -MAX_VEL

        if (TouchManager.Companion.Instance.HasTouch()) {
            val touchPos = Vector2(
                TouchManager.Companion.Instance.GetPosX().toFloat(),
                TouchManager.Companion.Instance.GetPosY().toFloat()
            )
            val direction = touchPos.Minus(Pos).Normalized()
            Vel.PlusEqual(direction.Times(MOVE_SPEED * _dt))
        } else {
            if (Vel.x > MOVE_SPEED * _dt) Vel.x -= MOVE_SPEED * _dt
            else if (Vel.x < -MOVE_SPEED * _dt) Vel.x += MOVE_SPEED * _dt
            else Vel.x = 0f

            if (Vel.y > MOVE_SPEED * _dt) Vel.y -= MOVE_SPEED * _dt
            else if (Vel.y < -MOVE_SPEED * _dt) Vel.y += MOVE_SPEED * _dt
            else Vel.y = 0f
        }

        spritesheet!!.Update(_dt)

        ShootCounterMain -= _dt
        ShootCounterSubLeft -= _dt
        ShootCounterSubRight -= _dt

        if (PlayerInfo.Companion.Instance.GetMainWeapon() != null && ShootCounterMain <= 0) {
            ShootCounterMain = PlayerInfo.Companion.Instance.GetMainWeapon().GetFireRate()
            if (PlayerInfo.Companion.Instance.GetMainWeapon()
                    .GetShootType() == SHOOTTYPE.S_STRAIGHT
            ) {
                for (i in 0 until PlayerInfo.Companion.Instance.GetMainWeapon().GetShootAmount()) {
                    val currProj: Projectile = Projectile.Companion.Create(
                        PlayerInfo.Companion.Instance.GetMainWeapon().GetWeaponDamage().toFloat(),
                        Pos.x,
                        Pos.y - (height * 0.5f)
                    )
                    currProj.SetPosY(currProj.GetPosY() - (currProj.GetHeight() * 0.5f))

                    if (PlayerInfo.Companion.Instance.GetMainWeapon()
                            .GetShootAmount() % 2 == 0
                    ) currProj.SetPosX(
                        currProj.GetPosX() + i * currProj.GetWidth() - ((PlayerInfo.Companion.Instance.GetMainWeapon()
                            .GetShootAmount() / 2) - 1) * (currProj.GetWidth()) - (currProj.GetWidth() * 0.5f)
                    )
                    else currProj.SetPosX(
                        currProj.GetPosX() + i * currProj.GetWidth() - (PlayerInfo.Companion.Instance.GetMainWeapon()
                            .GetShootAmount() / 2) * (currProj.GetWidth())
                    )
                }
            } else if (PlayerInfo.Companion.Instance.GetMainWeapon()
                    .GetShootType() == SHOOTTYPE.S_SPREAD
            ) {
                for (i in 0 until PlayerInfo.Companion.Instance.GetMainWeapon().GetShootAmount()) {
                    val currProj: Projectile = Projectile.Companion.Create(
                        PlayerInfo.Companion.Instance.GetMainWeapon().GetWeaponDamage().toFloat(),
                        Pos.x,
                        Pos.y - (height * 0.5f)
                    )
                    currProj.SetPosY(currProj.GetPosY() - (currProj.GetHeight() * 0.5f))

                    if (PlayerInfo.Companion.Instance.GetMainWeapon().GetShootAmount() % 2 == 0) {
                        val multiply: Int =
                            PlayerInfo.Companion.Instance.GetMainWeapon().GetShootAmount() / 2
                        val spread = 100
                        var currentSpread = 0

                        currentSpread = if (i < multiply) ((multiply - i) * -spread) + (spread / 2)
                        else (((i + 1) - multiply) * spread) - (spread / 2)

                        currProj.SetVelX(currentSpread.toFloat())
                    } else {
                        val multiply: Int =
                            (PlayerInfo.Companion.Instance.GetMainWeapon().GetShootAmount() / 2)
                        val spread = 100

                        var currentSpread = 0

                        currentSpread = if (i < multiply) ((multiply - i) * -spread)
                        else ((i - multiply) * spread)

                        currProj.SetVelX(currentSpread.toFloat())
                    }
                }
            }
        }

        if (PlayerInfo.Companion.Instance.GetSubWeaponLeft() != null && ShootCounterSubLeft <= 0) {
        }

        if (PlayerInfo.Companion.Instance.GetSubWeaponRight() != null && ShootCounterSubRight <= 0) {
        }

        PlayerInfo.Companion.Instance.SetPos(Pos)
    }

    override fun Render(_canvas: Canvas) {
        if (PlayerInfo.Companion.Instance.GetHealth() <= 0) return

        spritesheet!!.Render(_canvas, Pos.x.toInt(), Pos.y.toInt())
    }

    override fun GetType(): String {
        return "PlayerEntity"
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
        return 0
    }

    override fun OnHit(_other: Collidable) {
        if (PlayerInfo.Companion.Instance.GetHealth() <= 0) return

        if (_other.GetType() === "EnemyEntity") {
            if (!_other.GetDead()) {
                VibrateManager.Companion.Instance.startVibrate()
                PlayerInfo.Companion.Instance.MinusHealth(_other.GetDamage())
            }
        } else if (_other.GetType() === "EnemyBullet") {
            VibrateManager.Companion.Instance.startVibrate()
            PlayerInfo.Companion.Instance.MinusHealth(_other.GetDamage())
        }
    }

    override fun GetDead(): Boolean {
        if (PlayerInfo.Companion.Instance.GetHealth() <= 0) return true
        return false
    }

    companion object {
        //Init any variables here
        protected val TAG: String? = null
        fun Create(): PlayerEntity {
            val result = PlayerEntity()
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}

