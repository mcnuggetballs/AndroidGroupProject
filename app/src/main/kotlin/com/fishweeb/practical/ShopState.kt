package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceView

class ShopState : StateBase {
    private var background: Bitmap? = null
    private val bubbleSpawnTime = 2f
    private var headerText: TextEntity? = null
    private var backButton: BackEntity? = null
    private var Coinbmp: Bitmap? = null
    private val moneyPaint = Paint()
    private val moneyTextBounds = Rect()
    private var HealthStat: HealthStatButton? = null
    private var DamageStat: DamageStatButton? = null
    private var FireCountStat: FireAmountStatButton? = null
    private var bubbleButton: ParticleButtonEntity? = null
    private var bloodButton: ParticleButtonEntity? = null
    private var fishButton: ParticleButtonEntity? = null

    override fun GetName(): String {
        return "Shop"
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
            "Shop",
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

        Coinbmp = BitmapFactory.decodeResource(_view.resources, R.drawable.coin)
        moneyPaint.color = Color.YELLOW
        moneyPaint.alpha = 200
        moneyPaint.textSize = 50f
        moneyPaint.textAlign = Paint.Align.LEFT
        moneyPaint.isFakeBoldText = true

        HealthStat = HealthStatButton.Companion.Create(
            GameSystem.Companion.Instance.GetScreenScale()!!.x * 0.5f,
            GameSystem.Companion.Instance.GetScreenScale()!!.y * 0.5f,
            PlayerInfo.Companion.Instance.GetMaxHealth().toInt(),
            10,
            200,
            60f,
            60f
        )
        DamageStat = DamageStatButton.Companion.Create(
            GameSystem.Companion.Instance.GetScreenScale()!!.x * 0.5f,
            GameSystem.Companion.Instance.GetScreenScale()!!.y * 0.5f + HealthStat!!.GetHeight() * 1,
            PlayerInfo.Companion.Instance.GetMainWeapon()!!
                .GetWeaponDamage(),
            5,
            100,
            60f,
            60f
        )
        FireCountStat = FireAmountStatButton.Companion.Create(
            GameSystem.Companion.Instance.GetScreenScale()!!.x * 0.5f,
            GameSystem.Companion.Instance.GetScreenScale()!!.y * 0.5f + HealthStat!!.GetHeight() * 2,
            PlayerInfo.Companion.Instance.GetMainWeapon()!!
                .GetShootAmount(),
            1,
            500,
            60f,
            60f
        )
        HealthStat!!.SetPosX(GameSystem.Companion.Instance.ScreenScale!!.x * 0.75f)
        HealthStat!!.SetPosY(GameSystem.Companion.Instance.GetScreenScale()!!.y * 0.2f - HealthStat!!.textHeight * 0.5f)
        DamageStat!!.SetPosX(GameSystem.Companion.Instance.ScreenScale!!.x * 0.75f)
        DamageStat!!.SetPosY(GameSystem.Companion.Instance.GetScreenScale()!!.y * 0.2f - HealthStat!!.textHeight * 0.5f + HealthStat!!.GetHeight() * 1)
        FireCountStat!!.SetPosX(GameSystem.Companion.Instance.ScreenScale!!.x * 0.75f)
        FireCountStat!!.SetPosY(GameSystem.Companion.Instance.GetScreenScale()!!.y * 0.2f - HealthStat!!.textHeight * 0.5f + HealthStat!!.GetHeight() * 2)
        //shoot amount //fire rate //damage //speed
        bloodButton = ParticleButtonEntity.Companion.Create(
            ResourceManager.Companion.Instance.GetBitmap(R.drawable.buybutton),
            ResourceManager.Companion.Instance.GetBitmap(R.drawable.bloodparticlebutton),
            GameSystem.Companion.Instance.GetScreenScale()!!.x * 0.5f,
            GameSystem.Companion.Instance.GetScreenScale()!!.y * 0.8f,
            200f,
            60f,
            100
        )
        if (GameSystem.Companion.Instance.GetBoolFromSave("bloodeffect") == true) bloodButton!!.bought =
            true
        bubbleButton = ParticleButtonEntity.Companion.Create(
            ResourceManager.Companion.Instance.GetBitmap(R.drawable.buybutton),
            ResourceManager.Companion.Instance.GetBitmap(R.drawable.bubbleparticlebutton),
            GameSystem.Companion.Instance.GetScreenScale()!!.x * 0.5f - bloodButton!!.width * 1.5f,
            GameSystem.Companion.Instance.GetScreenScale()!!.y * 0.8f,
            200f,
            60f,
            0
        )
        bubbleButton!!.bought = true

        fishButton = ParticleButtonEntity.Companion.Create(
            ResourceManager.Companion.Instance.GetBitmap(R.drawable.buybutton),
            ResourceManager.Companion.Instance.GetBitmap(R.drawable.fishparticlebutton),
            GameSystem.Companion.Instance.GetScreenScale()!!.x * 0.5f + bloodButton!!.width * 1.5f,
            GameSystem.Companion.Instance.GetScreenScale()!!.y * 0.8f,
            200f,
            60f,
            100
        )
        if (GameSystem.Companion.Instance.GetBoolFromSave("fisheffect") == true) bloodButton!!.bought =
            true
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

        _canvas.drawText(
            "Ship HP - " + PlayerInfo.Companion.Instance.GetMaxHealth().toString(),
            GameSystem.Companion.Instance.ScreenScale!!.x * 0.1f,
            GameSystem.Companion.Instance.GetScreenScale()!!.y * 0.2f,
            HealthStat!!.paint
        )
        _canvas.drawText(
            "Damage - " + PlayerInfo.Companion.Instance.GetMainWeapon()!!.GetWeaponDamage()
                .toString(),
            GameSystem.Companion.Instance.ScreenScale!!.x * 0.1f,
            GameSystem.Companion.Instance.GetScreenScale()!!.y * 0.2f + HealthStat!!.GetHeight() * 1,
            HealthStat!!.paint
        )
        _canvas.drawText(
            "Fire Count - " + PlayerInfo.Companion.Instance.GetMainWeapon()!!.GetShootAmount()
                .toString(),
            GameSystem.Companion.Instance.ScreenScale!!.x * 0.1f,
            GameSystem.Companion.Instance.GetScreenScale()!!.y * 0.2f + HealthStat!!.GetHeight() * 2,
            HealthStat!!.paint
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

        if (bloodButton!!.clicked == true) {
            if (!bloodButton!!.bought) {
                if (PlayerInfo.Companion.Instance.GetMoney() >= bloodButton!!.Cost) {
                    bloodButton!!.bought = true
                    PlayerInfo.Companion.Instance.MinusMoney(bloodButton!!.Cost)
                    GameSystem.Companion.Instance.SaveEditBegin()
                    GameSystem.Companion.Instance.SetBoolInSave("bloodeffect", true)
                    GameSystem.Companion.Instance.SaveEditEnd()
                }
            }
        }

        if (fishButton!!.clicked == true) {
            if (!fishButton!!.bought) {
                if (PlayerInfo.Companion.Instance.GetMoney() >= fishButton!!.Cost) {
                    fishButton!!.bought = true
                    PlayerInfo.Companion.Instance.MinusMoney(fishButton!!.Cost)
                    GameSystem.Companion.Instance.SaveEditBegin()
                    GameSystem.Companion.Instance.SetBoolInSave("fisheffect", true)
                    GameSystem.Companion.Instance.SaveEditEnd()
                }
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
