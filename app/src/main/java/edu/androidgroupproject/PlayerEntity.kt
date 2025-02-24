package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.view.SurfaceView
import edu.androidgroupproject.R

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
    private var MAX_VEL = 600f
    private var MOVE_SPEED = 1600f
    private var DeathDelay = 2f

    private var ShootCounterMain = 0f
    private var ShootCounterSubLeft = 0f
    private var ShootCounterSubRight = 0f

    private var spritesheet: Sprite? = null

    override fun IsDone(): Boolean = isDone

    override fun SetIsDone(_isDone: Boolean) {
        isDone = _isDone
    }

    override fun SetRenderLayer(_newLayer: Int) {
        // No implementation needed
    }

    override fun GetRenderLayer(): Int = LayerConstants.GAMEOBJECTS_LAYER

    override fun Init(_view: SurfaceView) {
        DeathDelay = 2f

        spritesheet = Sprite(
            BitmapFactory.decodeResource(_view.resources, R.drawable.sprite_spaceship),
            1, 3, 12
        )
        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.sprite_spaceship)

        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels

        width = spritesheet?.GetWidth()?.toFloat() ?: 50f
        height = spritesheet?.GetHeight()?.toFloat() ?: 50f
        radius = maxOf(width, height) * 0.5f

        Pos.x = ScreenWidth * 0.5f
        Pos.y = ScreenHeight - (height * 2)

        ShootCounterMain = PlayerInfo.Instance.GetMainWeapon()?.GetFireRate() ?: 0f
        ShootCounterSubLeft = PlayerInfo.Instance.GetSubWeaponLeft()?.GetFireRate() ?: 0f
        ShootCounterSubRight = PlayerInfo.Instance.GetSubWeaponRight()?.GetFireRate() ?: 0f
    }

    override fun Update(_dt: Float) {
        if (PlayerInfo.Instance.GetHealth() <= 0) {
            PlayerInfo.Instance.SetHealth(0f)
            DeathDelay -= _dt
            if (DeathDelay <= 0) {
                GameSystem.Instance.AddHighScore(
                    HighScore(PlayerInfo.Instance.GetScore(), "PLAYER")
                )
                StateManager.Instance.ChangeState("GameOver")
                SetIsDone(true)
            }
            return
        }

        // Movement
        Pos.PlusEqual(Vel, _dt)

        // Out of Bounds Check
        Pos.x = Pos.x.coerceIn(width * 0.5f, ScreenWidth - (width * 0.5f))
        Pos.y = Pos.y.coerceIn(height * 0.5f, ScreenHeight - (height * 0.5f))

        // Velocity Limit
        Vel.x = Vel.x.coerceIn(-MAX_VEL, MAX_VEL)
        Vel.y = Vel.y.coerceIn(-MAX_VEL, MAX_VEL)

        if (TouchManager.Instance.HasTouch()) {
            val touchPos = Vector2(
                TouchManager.Instance.GetPosX().toFloat(),
                TouchManager.Instance.GetPosY().toFloat()
            )
            val direction = touchPos.Minus(Pos).Normalized()
            Vel.PlusEqual(direction.Times(MOVE_SPEED * _dt))
        } else {
            Vel.x = if (Vel.x > MOVE_SPEED * _dt) Vel.x - MOVE_SPEED * _dt else 0f
            Vel.y = if (Vel.y > MOVE_SPEED * _dt) Vel.y - MOVE_SPEED * _dt else 0f
        }

        spritesheet?.Update(_dt)

        // 🛠 FIXED SHOOTING LOGIC
        ShootCounterMain -= _dt
        if (ShootCounterMain <= 0) {
            val mainWeapon = PlayerInfo.Instance.GetMainWeapon() ?: return
            ShootCounterMain = mainWeapon.GetFireRate()

            val shootAmount = mainWeapon.GetShootAmount()
            for (i in 0 until shootAmount) {
                val currProj = Projectile.Create(
                    mainWeapon.GetWeaponDamage().toFloat(),
                    Pos.x, Pos.y - (height * 0.5f)
                )
                currProj.SetPosY(currProj.GetPosY() - (currProj.GetHeight() * 0.5f))

                when (mainWeapon.GetShootType()) {
                    SHOOTTYPE.S_STRAIGHT -> {
                        // Straight bullets, spread evenly
                        if (shootAmount % 2 == 0) {
                            currProj.SetPosX(
                                Pos.x + (i - (shootAmount / 2) + 0.5f) * currProj.GetWidth()
                            )
                        } else {
                            currProj.SetPosX(
                                Pos.x + (i - (shootAmount / 2)) * currProj.GetWidth()
                            )
                        }
                    }

                    SHOOTTYPE.S_SPREAD -> {
                        val baseSpreadAngle = 15f  // Angle step for each bullet
                        val spread = baseSpreadAngle * (shootAmount - 1) / 2f  // Total spread range

                        currProj.SetPosY(currProj.GetPosY() - (currProj.GetHeight() * 0.5f))

                        // Calculate angle dynamically based on bullet index
                        val angleOffset = Math.toRadians((-spread + (i * baseSpreadAngle)).toDouble())

                        // Corrected velocity calculation
                        val spreadX = Math.sin(angleOffset).toFloat()  // Adjust X movement
                        val spreadY = -Math.cos(angleOffset).toFloat() // Keep bullets moving forward

                        currProj.SetVelX(spreadX * 500)  // Apply horizontal movement
                    }

                    null -> TODO()
                }
            }
        }

        ShootCounterSubLeft -= _dt
        if (ShootCounterSubLeft <= 0) {
            ShootCounterSubLeft = PlayerInfo.Instance.GetSubWeaponLeft()?.GetFireRate() ?: 0f
            // Fire sub-weapon left logic here...
        }

        ShootCounterSubRight -= _dt
        if (ShootCounterSubRight <= 0) {
            ShootCounterSubRight = PlayerInfo.Instance.GetSubWeaponRight()?.GetFireRate() ?: 0f
            // Fire sub-weapon right logic here...
        }

        PlayerInfo.Instance.SetPos(Pos)
    }

    override fun Render(_canvas: Canvas) {
        if (PlayerInfo.Instance.GetHealth() <= 0) return
        spritesheet?.Render(_canvas, Pos.x.toInt(), Pos.y.toInt())
    }

    override fun GetType(): String = "PlayerEntity"

    override fun GetPosX(): Float = Pos.x

    override fun GetPosY(): Float = Pos.y

    override fun GetRadius(): Float = radius

    override fun GetWidth(): Float = width

    override fun GetHeight(): Float = height

    override fun GetDamage(): Float = 0f

    override fun OnHit(_other: Collidable) {
        if (PlayerInfo.Instance.GetHealth() <= 0) return

        when (_other.GetType()) {
            "EnemyEntity" -> {
                if (!_other.GetDead()) {
                    VibrateManager.Instance.startVibrate()
                    PlayerInfo.Instance.MinusHealth(_other.GetDamage())
                }
            }
            "EnemyBullet" -> {
                VibrateManager.Instance.startVibrate()
                PlayerInfo.Instance.MinusHealth(_other.GetDamage())
            }
        }
    }

    override fun GetDead(): Boolean = PlayerInfo.Instance.GetHealth() <= 0

    companion object {
        fun Create(): PlayerEntity {
            return PlayerEntity().apply {
                EntityManager.Instance.AddEntity(this)
            }
        }
    }
}
