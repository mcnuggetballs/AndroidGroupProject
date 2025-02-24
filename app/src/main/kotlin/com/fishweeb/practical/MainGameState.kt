package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceView
import com.fishweeb.practical.AudioManager.Init

class MainGameState : StateBase {
    private var bmp: Bitmap? = null //Picture like png files are known as bitmap

    // Scaledbmp - scaled version based on Screen width and height for the image bmp
    private var Scaledbmp: Bitmap? = null
    private var Coinbmp: Bitmap? = null

    var offset: Float = 0.0f

    var ScreenWidth: Int = 0
    var ScreenHeight: Int = 0 //Screen width and height
    var xPos: Float = 0.0f
    var yPos: Float = 0.0f //Variable used for positioning of images

    private val depthPaint = Paint()
    private val scorePaint = Paint()
    private val moneyPaint = Paint()

    private var pauseButton: ButtonEntity? = null

    private var SpawnBooleanTest = false

    override fun GetName(): String {
        return "MainGame"
    }

    override fun OnEnter(_view: SurfaceView) {
        SpawnManager.Companion.Instance.Init(_view)

        //Reset EntityList
        EntityManager.Companion.Instance.EmptyEntityList()
        ParticleManager.Companion.Instance.RemoveParticles()

        depthPaint.color = Color.WHITE
        depthPaint.isFakeBoldText = true
        depthPaint.textSize = 50f
        depthPaint.textAlign = Paint.Align.RIGHT

        scorePaint.color = Color.WHITE
        scorePaint.alpha = 200
        scorePaint.textSize = 60f
        scorePaint.textAlign = Paint.Align.CENTER
        scorePaint.isFakeBoldText = true

        moneyPaint.color = Color.YELLOW
        moneyPaint.alpha = 200
        moneyPaint.textSize = 50f
        moneyPaint.textAlign = Paint.Align.LEFT
        moneyPaint.isFakeBoldText = true

        bmp = BitmapFactory.decodeResource(_view.resources, R.drawable.gamescene)
        Coinbmp = BitmapFactory.decodeResource(_view.resources, R.drawable.coin)

        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels

        //Scaled version of bmp which is background of game scene
        Scaledbmp = Bitmap.createScaledBitmap(bmp, ScreenWidth, ScreenHeight, true)

        //Reinitialize values
        PlayerInfo.Companion.Instance.RestartGame()

        PlayerEntity.Companion.Create()

        //Pause Menu
        pauseButton = PauseEntity.Companion.Create(0f, 0f)
        pauseButton.SetPosX(pauseButton.width * 0.5f)
        pauseButton.SetPosY(pauseButton.height * 0.5f)

        //Pause Menu

        //RenderBackground.Create();
        //RenderPauseButton.Create();
        if (SettingsManager.Companion.Instance.GetFpsShow()) RenderTextEntity.Companion.Create()
    }

    override fun OnExit() {
        EntityManager.Companion.Instance.EmptyEntityList()
        ParticleManager.Companion.Instance.RemoveParticles()
        GameSystem.Companion.Instance.SetGameSpeed(1f)
    }

    override fun Render(_canvas: Canvas) {
        val currOffset = (offset * 100f).toInt()
        _canvas.drawBitmap(Scaledbmp!!, xPos, yPos, null)
        _canvas.drawBitmap(Scaledbmp!!, xPos, yPos - ScreenHeight, null)

        // xPos = 0

        // _View.getWidth()
        ParticleManager.Companion.Instance.Render(_canvas)

        EntityManager.Companion.Instance.Render(_canvas)

        //Health
        val bgOffset = 4f
        val healthXSize = 120f
        val healthYSize = 40f
        _canvas.drawRect(
            20 - bgOffset + pauseButton!!.width,
            20 - bgOffset,
            20 + healthXSize + bgOffset + pauseButton!!.width,
            20 + healthYSize + bgOffset,
            PaintColor.Companion.Instance.GetPaint(Color.BLACK)!!
        )
        _canvas.drawRect(
            20 + pauseButton!!.width,
            20f,
            20 + ((PlayerInfo.Companion.Instance.GetHealth() / PlayerInfo.Companion.Instance.GetMaxHealth()) * healthXSize) + pauseButton!!.width,
            20 + healthYSize,
            PaintColor.Companion.Instance.GetPaint(Color.RED)!!
        )

        //Health

        //Distance
        _canvas.drawText(
            "" + PlayerInfo.Companion.Instance.GetDepth().toInt() + "ft",
            ScreenWidth.toFloat(),
            depthPaint.textSize,
            depthPaint
        )

        //Distance

        //Score
        _canvas.drawText("SCORE", ScreenWidth * 0.5f, scorePaint.textSize, scorePaint)
        scorePaint.textSize = 50f
        scorePaint.alpha = 255
        _canvas.drawText(
            "" + PlayerInfo.Companion.Instance.GetScore(),
            ScreenWidth * 0.5f,
            scorePaint.textSize * 2.2f,
            scorePaint
        )
        scorePaint.textSize = 60f
        scorePaint.alpha = 200

        //Score

        //Money
        _canvas.drawBitmap(
            Coinbmp!!,
            20 + pauseButton!!.width,
            20 + healthYSize + Coinbmp!!.height * 0.2f,
            null
        )
        _canvas.drawText(
            "" + PlayerInfo.Companion.Instance.GetMoney(),
            20 + Coinbmp!!.width + moneyPaint.textSize * 0.2f + pauseButton!!.width,
            20 + healthYSize + Coinbmp!!.height * 0.2f + moneyPaint.textSize,
            moneyPaint
        )

        //Money

        //Buttons
    }

    override fun Update(_dt: Float) {
        offset += _dt

        EntityManager.Companion.Instance.Update(_dt)
        ParticleManager.Companion.Instance.Update(_dt)

        yPos += _dt * 500

        if (PlayerInfo.Companion.Instance.GetHealth() > 0) PlayerInfo.Companion.Instance.DepthUpdate(
            _dt
        )

        if (yPos > ScreenHeight) {
            yPos = 0f
        }

        SpawnManager.Companion.Instance.Update(_dt)

        if (SpawnBooleanTest == false && PlayerInfo.Companion.Instance.GetDepth() > 3000) {
            SpawnBooleanTest = true
            val tempParticle: ParticleObject =
                ParticleManager.Companion.Instance.FetchParticle(PARTICLETYPE.P_POWERUP_SPREAD)
            tempParticle.SetBMP(ImageManager.Companion.Instance.GetImage(IMAGE.I_POWERUP_SPREAD)!!)
            tempParticle.position.x = ScreenWidth * 0.5f
            tempParticle.position.y = 0f
        }
    }
}
