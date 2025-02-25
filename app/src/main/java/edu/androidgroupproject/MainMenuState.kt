package edu.androidgroupproject

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceView
import com.edu.androidgroupproject.GameSystem
import com.fishweeb.practical.ParticleManager

// Sample of an intro state - You can change to Splash page..
class MainMenuState : Activity(), StateBase {
    private var background: Bitmap? = null
    private var logo: Bitmap? = null
    private var PlayButton: TextButtonEntity? = null
    private var ShopButton: TextButtonEntity? = null
    private var GarageButton: TextButtonEntity? = null
    private var OptionsButton: TextButtonEntity? = null
    private var ScoresButton: TextButtonEntity? = null
    private val bubbleSpawnTime = 2f
    private var Coinbmp: Bitmap? = null
    private val moneyPaint = Paint()
    private val moneyTextBounds = Rect()

    override fun GetName(): String {
        return "MainMenu"
    }

    override fun OnEnter(_view: SurfaceView) {
        background = Bitmap.createScaledBitmap(
            ImageManager.Companion.Instance.GetImage(IMAGE.I_MENUBACKGROUND)!!,
            GameSystem.Instance.GetScreenScale()!!.x.toInt(),
            GameSystem.Instance.GetScreenScale()!!.y.toInt(),
            true
        )
        logo = Bitmap.createScaledBitmap(
            ImageManager.Companion.Instance.GetImage(IMAGE.I_LOGO)!!,
            (GameSystem.Instance.GetScreenScale()!!.x * 0.8f).toInt(),
            (GameSystem.Instance.GetScreenScale()!!.y * 0.2f).toInt(),
            true
        )
        val textSize = 100f
        val xOffset = -330f
        PlayButton = TextButtonEntity.Create(0f, 0f, "Play", textSize, "MainGame")
        PlayButton!!.SetPosX(GameSystem.Instance.ScreenScale!!.x * 0.5f + PlayButton!!.GetWidth() * 0.5f + xOffset)
        PlayButton!!.SetPosY(GameSystem.Instance.ScreenScale!!.y * 0.5f - textSize * 2.5f)
        PlayButton!!.SetDefaultTextColor(Color.BLACK)
        PlayButton!!.SetOnClickTextColor(Color.YELLOW)

        ShopButton = TextButtonEntity.Create(0f, 0f, "Shop", textSize, "Shop")
        ShopButton!!.SetPosX(GameSystem.Instance.ScreenScale!!.x * 0.5f + ShopButton!!.GetWidth() * 0.5f + xOffset)
        ShopButton!!.SetPosY(PlayButton!!.Pos.y + textSize * 1.5f)
        ShopButton!!.SetDefaultTextColor(Color.BLACK)
        ShopButton!!.SetOnClickTextColor(Color.YELLOW)

        GarageButton = TextButtonEntity.Create(0f, 0f, "Playground", textSize, "Garage")
        GarageButton!!.SetPosX(GameSystem.Instance.ScreenScale!!.x * 0.5f + GarageButton!!.GetWidth() * 0.5f + xOffset)
        GarageButton!!.SetPosY(ShopButton!!.Pos.y + textSize * 1.5f)
        GarageButton!!.SetDefaultTextColor(Color.BLACK)
        GarageButton!!.SetOnClickTextColor(Color.YELLOW)

        OptionsButton = TextButtonEntity.Create(0f, 0f, "Options", textSize, "Options")
        OptionsButton!!.SetPosX(GameSystem.Instance.ScreenScale!!.x * 0.5f + OptionsButton!!.GetWidth() * 0.5f + xOffset)
        OptionsButton!!.SetPosY(GarageButton!!.Pos.y + textSize * 1.5f)
        OptionsButton!!.SetDefaultTextColor(Color.BLACK)
        OptionsButton!!.SetOnClickTextColor(Color.YELLOW)

        ScoresButton = TextButtonEntity.Create(0f, 0f, "Scores", textSize, "Scores")
        ScoresButton!!.SetPosX(GameSystem.Instance.ScreenScale!!.x * 0.5f + ScoresButton!!.GetWidth() * 0.5f + xOffset)
        ScoresButton!!.SetPosY(OptionsButton!!.Pos.y + textSize * 1.5f)
        ScoresButton!!.SetDefaultTextColor(Color.BLACK)
        ScoresButton!!.SetOnClickTextColor(Color.YELLOW)

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
        _canvas.drawBitmap(
            logo!!,
            GameSystem.Instance.GetScreenScale()!!.x * 0.1f,
            GameSystem.Instance.GetScreenScale()!!.y * 0.1f,
            null
        )

        //Money
        val moneyText = "" + PlayerInfo.Instance.GetMoney()
        moneyPaint.getTextBounds(moneyText, 0, moneyText.length, moneyTextBounds)
        _canvas.drawBitmap(Coinbmp!!, Coinbmp!!.width * 0.5f, Coinbmp!!.height * 0.5f, null)
        _canvas.drawText(
            moneyText,
            Coinbmp!!.width + Coinbmp!!.width * 0.7f,
            Coinbmp!!.height + moneyTextBounds.height() * 0.5f,
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
