package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.view.SurfaceView
import edu.androidgroupproject.R

open class Projectile : EntityBase, Collidable {
    protected var bmp: Bitmap? = null
    protected var spritesheet: Sprite? = null
    var isDone: Boolean = false
    var Pos: Vector2 = Vector2()
    protected var ScreenWidth: Int = 0
    protected var ScreenHeight: Int = 0
    protected var sheetRow: Int = 0
    protected var sheetCol: Int = 0
    var velocity: Vector2 = Vector2()
    protected var width: Float = 50f
    protected var height: Float = 50f
    protected var radius: Float = 0f
    protected var Damage: Float = 0f

    override fun IsDone(): Boolean = isDone

    override fun SetIsDone(_isDone: Boolean) {
        isDone = _isDone
    }

    override fun SetRenderLayer(_newLayer: Int) {
        // No implementation needed
    }

    override fun GetRenderLayer(): Int = LayerConstants.GAMEOBJECTS_LAYER

    override fun Init(_view: SurfaceView) {
        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.bullet)

        width = bmp?.width?.toFloat() ?: 30f
        height = bmp?.height?.toFloat() ?: 30f

        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels
        velocity.y = -1600f

        radius = maxOf(width, height) * 0.5f
    }

    fun Constrain() {
        if (Pos.y - radius < 0 || Pos.y + radius > ScreenHeight) {
            SetIsDone(true)
        }
    }

    override fun Update(_dt: Float) {
        Pos.PlusEqual(velocity, _dt)
        Constrain()
        spritesheet?.Update(_dt)
    }

    override fun Render(_canvas: Canvas) {
        when {
            spritesheet != null -> spritesheet!!.Render(_canvas, Pos.x.toInt(), Pos.y.toInt())
            bmp != null -> _canvas.drawBitmap(
                bmp!!,
                Pos.x - (width * 0.5f),
                Pos.y - (height * 0.5f),
                null
            )
            else -> _canvas.drawOval(
                Pos.x - width * 0.5f,
                Pos.y - height * 0.5f,
                Pos.x + width * 0.5f,
                Pos.y + height * 0.5f,
                PaintColor.Instance.GetPaint(Color.BLUE) ?: return
            )
        }
    }

    override fun GetType(): String = "Bullet"

    override fun GetPosX(): Float = Pos.x

    override fun GetPosY(): Float = Pos.y

    override fun GetRadius(): Float = radius

    override fun GetWidth(): Float = width

    override fun GetHeight(): Float = height

    override fun GetDamage(): Float = Damage

    fun SetPosY(yPos: Float) {
        Pos.y = yPos
    }

    fun SetPosX(xPos: Float) {
        Pos.x = xPos
    }

    fun SetVelX(xVel: Float) {
        velocity.x = xVel
    }

    fun SetVelY(yVel: Float) {
        velocity.y = yVel
    }

    override fun OnHit(_other: Collidable) {
        if (_other.GetType() == "EnemyEntity" && !_other.GetDead()) {
            SetIsDone(true)
        }
    }

    override fun GetDead(): Boolean = isDone

    companion object {
        fun Create(Damage: Float, xPos: Float, yPos: Float): Projectile {
            return Projectile().apply {
                Pos.x = xPos
                Pos.y = yPos
                this.Damage = Damage
                EntityManager.Instance.AddEntity(this)
            }
        }
    }
}
