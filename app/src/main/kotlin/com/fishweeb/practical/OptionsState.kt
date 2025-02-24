package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceView

class OptionsState : StateBase {
    private var background: Bitmap? = null
    private val bubbleSpawnTime = 2f
    private var headerText: TextEntity? = null
    private var backButton: BackEntity? = null
    private var SoundText: TextEntity? = null
    private var ShowFpsText: TextEntity? = null
    private var ShowFpsButton: SwitchButtonEntity? = null
    private var VolumeBar: VolumeBarEntity? = null
    private var Coinbmp: Bitmap? = null
    private val moneyPaint = Paint()
    private val moneyTextBounds = Rect()

    override fun GetName(): String {
        return "Options"
    }

    override fun OnEnter(_view: SurfaceView) {
        background = Bitmap.createScaledBitmap(
            ImageManager.Companion.Instance.GetImage(IMAGE.I_MENUBACKGROUND)!!,
            GameSystem.Companion.Instance.GetScreenScale()!!.x.toInt(),
            GameSystem.Companion.Instance.GetScreenScale()!!.y.toInt(),
            true
        )
        headerText = TextEntity.Companion.Create(
            GameSystem.Companion.Instance.ScreenScale!!.x * 0.5f,
            0f,
            "Options",
            100f
        )
        headerText!!.SetPosY(headerText!!.GetHeight() * 0.5f)
        headerText!!.SetHeader(true)
        backButton = BackEntity.Companion.Create(
            0f,
            0f,
            headerText!!.height * 1.3f,
            headerText!!.height * 1.3f,
            "MainMenu"
        )
        backButton!!.SetPosX(backButton!!.width * 0.5f)
        backButton!!.SetPosY(backButton!!.height * 0.5f)

        SoundText = TextEntity.Companion.Create(0f, headerText!!.Pos.y + 100 * 2.5f, "Sound -", 70f)
        SoundText!!.SetPosX(GameSystem.Companion.Instance.ScreenScale!!.x * 0.1f + SoundText!!.GetWidth() * 0.5f)
        SoundText!!.SetPaintColor(Color.BLACK)

        ShowFpsText =
            TextEntity.Companion.Create(0f, SoundText!!.Pos.y + 100 * 2, "Show FPS -", 70f)
        ShowFpsText!!.SetPosX(GameSystem.Companion.Instance.ScreenScale!!.x * 0.1f + ShowFpsText!!.GetWidth() * 0.5f)
        ShowFpsText!!.SetPaintColor(Color.BLACK)
        ShowFpsButton = SwitchButtonEntity.Companion.Create(
            GameSystem.Companion.Instance.ScreenScale!!.x * 0.9f,
            ShowFpsText!!.Pos.y,
            100f,
            70f,
            ImageManager.Companion.Instance.GetImage(IMAGE.I_BUTTON_OFF),
            ImageManager.Companion.Instance.GetImage(IMAGE.I_BUTTON_ONN),
            SETTINGTYPE.ST_FPSSHOW
        )

        VolumeBar = VolumeBarEntity.Companion.Create(
            ShowFpsButton!!.Pos.x - 300 * 0.5f - ShowFpsButton!!.width * 0.5f,
            SoundText!!.Pos.y,
            300f,
            40f
        )

        Coinbmp = BitmapFactory.decodeResource(_view.resources, R.drawable.coin)
        moneyPaint.color = Color.YELLOW
        moneyPaint.alpha = 200
        moneyPaint.textSize = 50f
        moneyPaint.textAlign = Paint.Align.LEFT
        moneyPaint.isFakeBoldText = true
    }

    override fun OnExit() {
        EntityManager.Companion.Instance.EmptyEntityList()
    }

    override fun Render(_canvas: Canvas) {
        // Render anything
        _canvas.drawBitmap(background!!, 0f, 0f, null)
        ParticleManager.Companion.Instance.Render(_canvas)
        EntityManager.Companion.Instance.Render(_canvas)

        //Money
        val moneyText = "" + PlayerInfo.Companion.Instance.GetMoney()
        moneyPaint.getTextBounds(moneyText, 0, moneyText.length, moneyTextBounds)
        _canvas.drawBitmap(
            Coinbmp!!,
            Coinbmp!!.width * 0.5f,
            Coinbmp!!.height * 0.5f + headerText!!.height * 1.3f,
            null
        )
        _canvas.drawText(
            moneyText,
            Coinbmp!!.width + Coinbmp!!.width * 0.7f,
            Coinbmp!!.height + headerText!!.height * 1.3f + moneyTextBounds.height() * 0.5f,
            moneyPaint
        )
        //Money
    }

    override fun Update(_dt: Float) {
        if (TouchManager.Companion.Instance.HasTouch()) {
            for (i in 0..2) {
                ParticleManager.Companion.Instance.CreateParticle(
                    PlayerInfo.Companion.Instance.GetEffectType(),
                    TouchManager.Companion.Instance.GetPosX().toFloat(),
                    TouchManager.Companion.Instance.GetPosY().toFloat()
                )
            }
        }

        GameSystem.Companion.Instance.m_bubbleTimer += _dt
        if (GameSystem.Companion.Instance.m_bubbleTimer >= bubbleSpawnTime) {
            val newParticle: ParticleObject =
                ParticleManager.Companion.Instance.FetchParticle(PARTICLETYPE.P_BUBBLE)
            val diameter = (Math.random() * 50 + 15).toInt()
            newParticle.width = diameter.toFloat()
            newParticle.height = diameter.toFloat()
            newParticle.position.x = (Math.random()
                .toFloat() * (GameSystem.Companion.Instance.GetScreenScale()!!.x - newParticle.width)) + (newParticle.width * 0.5f)
            newParticle.position.y =
                GameSystem.Companion.Instance.GetScreenScale()!!.y + newParticle.height
            newParticle.velocity.x = Math.random().toFloat() * 40 - 20
            newParticle.velocity.y = -(Math.random().toFloat() * 160 + 80)
            newParticle.SetBMP(
                Bitmap.createScaledBitmap(
                    ImageManager.Companion.Instance.GetImage(
                        IMAGE.I_BUBBLE
                    )!!, diameter, diameter, true
                )
            )
            GameSystem.Companion.Instance.m_bubbleTimer = 0f
        }
        ParticleManager.Companion.Instance.Update(_dt)
        EntityManager.Companion.Instance.Update(_dt)
    }
}
