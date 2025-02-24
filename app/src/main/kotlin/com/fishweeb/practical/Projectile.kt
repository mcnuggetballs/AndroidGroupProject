package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.view.SurfaceView

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
    protected var width: Float = 0f
    protected var height: Float = 0f
    protected var radius: Float = 0f
    protected var Damage: Float = 0f

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
        // Define anything you need to use here

        // sheetRow = 1;
        // sheetCol = 3;
        // spritesheet = new Sprite(BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_ray),sheetRow,sheetCol,12);

        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.bullet)

        //width = spritesheet.GetWidth();
        //height = spritesheet.GetHeight();
        width = bmp.getWidth().toFloat()
        height = bmp.getHeight().toFloat()

        //width = 30;
        //height = 30;
        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels
        velocity.y = -1600f

        radius = if (width > height) width * 0.5f
        else height * 0.5f
    }

    fun Constrain() {
        //out of bounds
        if (Pos.y - (height * 0.5f) < 0) {
            SetIsDone(true)
        }

        if (Pos.y + (height * 0.5f) > ScreenHeight) {
            SetIsDone(true)
        }
    }

    override fun Update(_dt: Float) {
        //Movement
        Pos.PlusEqual(velocity, _dt)

        //Out Of Bounds
        Constrain()

        if (spritesheet != null) spritesheet!!.Update(_dt)
    }

    override fun Render(_canvas: Canvas) {
        // Render anything
        if (spritesheet != null) spritesheet!!.Render(_canvas, Pos.x.toInt(), Pos.y.toInt())
        else if (bmp != null) _canvas.drawBitmap(
            bmp!!,
            Pos.x - (width * 0.5f),
            Pos.y - (height * 0.5f),
            null
        )
        else _canvas.drawOval(
            Pos.x - width * 0.5f,
            Pos.y - height * 0.5f,
            Pos.x + width * 0.5f,
            Pos.y + height * 0.5f,
            PaintColor.Companion.Instance.GetPaint(Color.BLUE)!!
        )
    }

    override fun GetType(): String {
        return "Bullet"
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
        return Damage
    }

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
        if (_other.GetType() === "EnemyEntity") {
            if (!_other.GetDead()) SetIsDone(true)
        }
    }

    override fun GetDead(): Boolean {
        return IsDone()
    }

    companion object {
        //Init any variables here
        protected val TAG: String? = null
        fun Create(Damage: Float, xPos: Float, yPos: Float): Projectile {
            val result = Projectile()
            result.Pos.x = xPos
            result.Pos.y = yPos
            result.Damage = Damage
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}

