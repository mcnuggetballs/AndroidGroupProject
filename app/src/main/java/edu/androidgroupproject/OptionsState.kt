package edu.androidgroupproject

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceView
import com.edu.androidgroupproject.GameSystem
import com.fishweeb.practical.ParticleManager

class OptionsState : StateBase {
    private var background: Bitmap? = null
    private val bubbleSpawnTime = 2f
    private var headerText: TextEntity? = null
    private var backButton: BackEntity? = null
    private var ShowFpsText: TextEntity? = null
    private var ShowFpsButton: SwitchButtonEntity? = null
    private var Coinbmp: Bitmap? = null
    private val moneyPaint = Paint()
    private val moneyTextBounds = Rect()

    override fun GetName(): String {
        return "Options"
    }

    override fun OnEnter(_view: SurfaceView) {
        background = Bitmap.createScaledBitmap(
            ImageManager.Companion.Instance.GetImage(IMAGE.I_MENUBACKGROUND)!!,
            GameSystem.Instance.GetScreenScale()!!.x.toInt(),
            GameSystem.Instance.GetScreenScale()!!.y.toInt(),
            true
        )
        headerText = TextEntity.Create(
            GameSystem.Instance.ScreenScale!!.x * 0.5f,
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

        ShowFpsText =
            TextEntity.Create(0f, headerText!!.Pos.y + 100 * 2.5f, "Show FPS -", 70f)
        ShowFpsText!!.SetPosX(GameSystem.Instance.ScreenScale!!.x * 0.1f + ShowFpsText!!.GetWidth() * 0.5f)
        ShowFpsText!!.SetPaintColor(Color.BLACK)
        ShowFpsButton = SwitchButtonEntity.Create(
            GameSystem.Instance.ScreenScale!!.x * 0.9f,
            ShowFpsText!!.Pos.y,
            100f,
            70f,
            ImageManager.Instance.GetImage(IMAGE.I_BUTTON_OFF),
            ImageManager.Instance.GetImage(IMAGE.I_BUTTON_ONN),
            SETTINGTYPE.ST_FPSSHOW
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
        ParticleManager.Instance.Render(_canvas)
        EntityManager.Companion.Instance.Render(_canvas)

        //Money
        val moneyText = "" + PlayerInfo.Instance.GetMoney()
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
        if (TouchManager.Instance.HasTouch()) {
            for (i in 0..2) {
                ParticleManager.Instance.CreateParticle(
                    PlayerInfo.Instance.GetEffectType(),
                    TouchManager.Instance.GetPosX().toFloat(),
                    TouchManager.Instance.GetPosY().toFloat()
                )
            }
        }

        GameSystem.Instance.m_bubbleTimer += _dt
        if (GameSystem.Instance.m_bubbleTimer >= bubbleSpawnTime) {
            val newParticle: ParticleObject =
                ParticleManager.Instance.FetchParticle(PARTICLETYPE.P_BUBBLE)
            val diameter = (Math.random() * 50 + 15).toInt()
            newParticle.width = diameter.toFloat()
            newParticle.height = diameter.toFloat()
            newParticle.position.x = (Math.random()
                .toFloat() * (GameSystem.Instance.GetScreenScale()!!.x - newParticle.width)) + (newParticle.width * 0.5f)
            newParticle.position.y =
                GameSystem.Instance.GetScreenScale()!!.y + newParticle.height
            newParticle.velocity.x = Math.random().toFloat() * 40 - 20
            newParticle.velocity.y = -(Math.random().toFloat() * 160 + 80)
            newParticle.SetBMP(
                Bitmap.createScaledBitmap(
                    ImageManager.Companion.Instance.GetImage(
                        IMAGE.I_BUBBLE
                    )!!, diameter, diameter, true
                )
            )
            GameSystem.Instance.m_bubbleTimer = 0f
        }
        ParticleManager.Instance.Update(_dt)
        EntityManager.Companion.Instance.Update(_dt)
    }
}
