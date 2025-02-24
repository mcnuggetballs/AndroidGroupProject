package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceView
import edu.androidgroupproject.R

class MainGameState : StateBase {
    private var bmp: Bitmap? = null // Background image
    private var Scaledbmp: Bitmap? = null // Scaled version
    private var Coinbmp: Bitmap? = null

    var offset: Float = 0.0f

    var ScreenWidth: Int = 0
    var ScreenHeight: Int = 0 // Screen dimensions
    var xPos: Float = 0.0f
    var yPos: Float = 0.0f // Positioning variables

    private val depthPaint = Paint()
    private val scorePaint = Paint()
    private val moneyPaint = Paint()

    private var pauseButton: ButtonEntity? = null

    private var SpawnBooleanTest = false

    override fun GetName(): String = "MainGame"

    override fun OnEnter(_view: SurfaceView) {
        SpawnManager.Instance.Init(_view)

        // Reset Entity List
        EntityManager.Instance.EmptyEntityList()
        ParticleManager.Instance.RemoveParticles()

        // Paint settings
        depthPaint.apply {
            color = Color.WHITE
            isFakeBoldText = true
            textSize = 50f
            textAlign = Paint.Align.RIGHT
        }

        scorePaint.apply {
            color = Color.WHITE
            alpha = 200
            textSize = 60f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }

        moneyPaint.apply {
            color = Color.YELLOW
            alpha = 200
            textSize = 50f
            textAlign = Paint.Align.LEFT
            isFakeBoldText = true
        }

        // Load images safely
        val resources = _view.resources
        bmp = BitmapFactory.decodeResource(resources, R.drawable.gamescene)
        Coinbmp = BitmapFactory.decodeResource(resources, R.drawable.coin)

        val metrics = resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels

        // Create scaled bitmap safely
        Scaledbmp = bmp?.let {
            Bitmap.createScaledBitmap(it, ScreenWidth, ScreenHeight, true)
        }

        // Restart Player
        PlayerInfo.Instance.RestartGame()
        PlayerEntity.Create()

        // Pause Button
        pauseButton = PauseEntity.Create(0f, 0f)?.apply {
            SetPosX(width * 0.5f)
            SetPosY(height * 0.5f)
        }

        // Show FPS text if enabled
        if (SettingsManager.Instance.GetFpsShow()) {
            RenderTextEntity.Create()
        }
    }

    override fun OnExit() {
        EntityManager.Instance.EmptyEntityList()
        ParticleManager.Instance.RemoveParticles()
        GameSystem.Instance.SetGameSpeed(1f)
    }

    override fun Render(_canvas: Canvas) {
        val currOffset = (offset * 100f).toInt()

        // Safely draw background
        Scaledbmp?.let {
            _canvas.drawBitmap(it, xPos, yPos, null)
            _canvas.drawBitmap(it, xPos, yPos - ScreenHeight, null)
        }

        // Render entities and particles
        ParticleManager.Instance.Render(_canvas)
        EntityManager.Instance.Render(_canvas)

        // Ensure pauseButton is not null before using it
        pauseButton?.let { btn ->
            // Health Bar
            val bgOffset = 4f
            val healthXSize = 120f
            val healthYSize = 40f
            _canvas.drawRect(
                20 - bgOffset + btn.width,
                20 - bgOffset,
                20 + healthXSize + bgOffset + btn.width,
                20 + healthYSize + bgOffset,
                PaintColor.Instance.GetPaint(Color.BLACK) ?: Paint()
            )
            _canvas.drawRect(
                20 + btn.width,
                20f,
                20 + ((PlayerInfo.Instance.GetHealth() / PlayerInfo.Instance.GetMaxHealth()) * healthXSize) + btn.width,
                20 + healthYSize,
                PaintColor.Instance.GetPaint(Color.RED) ?: Paint()
            )
        }

        // Display Depth
        _canvas.drawText(
            "${PlayerInfo.Instance.GetDepth().toInt()}ft",
            ScreenWidth.toFloat(),
            depthPaint.textSize,
            depthPaint
        )

        // Display Score
        _canvas.drawText("SCORE", ScreenWidth * 0.5f, scorePaint.textSize, scorePaint)
        scorePaint.textSize = 50f
        scorePaint.alpha = 255
        _canvas.drawText(
            "${PlayerInfo.Instance.GetScore()}",
            ScreenWidth * 0.5f,
            scorePaint.textSize * 2.2f,
            scorePaint
        )
        scorePaint.textSize = 60f
        scorePaint.alpha = 200

        // Display Money
        Coinbmp?.let {
            _canvas.drawBitmap(
                it,
                20 + (pauseButton?.width ?: 0f),
                20 + 40 + it.height * 0.2f,
                null
            )
        }
        _canvas.drawText(
            "${PlayerInfo.Instance.GetMoney()}",
            20 + (Coinbmp?.width ?: 0) + moneyPaint.textSize * 0.2f + (pauseButton?.width ?: 0f),
            20 + 40 + (Coinbmp?.height ?: 0) * 0.2f + moneyPaint.textSize,
            moneyPaint
        )
    }

    override fun Update(_dt: Float) {
        offset += _dt

        EntityManager.Instance.Update(_dt)
        ParticleManager.Instance.Update(_dt)

        yPos += _dt * 500

        if (PlayerInfo.Instance.GetHealth() > 0) {
            PlayerInfo.Instance.DepthUpdate(_dt)
        }

        if (yPos > ScreenHeight) {
            yPos = 0f
        }

        SpawnManager.Instance.Update(_dt)

        // Ensure that SpawnBooleanTest is triggered only once when depth > 3000
        if (!SpawnBooleanTest && PlayerInfo.Instance.GetDepth() > 3000) {
            SpawnBooleanTest = true
            ParticleManager.Instance.FetchParticle(PARTICLETYPE.P_POWERUP_SPREAD)?.apply {
                SetBMP(ImageManager.Instance.GetImage(IMAGE.I_POWERUP_SPREAD) ?: return)
                position.x = ScreenWidth * 0.5f
                position.y = 0f
            }
        }
    }
}
