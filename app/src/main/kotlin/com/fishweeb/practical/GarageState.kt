package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.SurfaceView

class GarageState : StateBase {
    private var background: Bitmap? = null
    private val bubbleSpawnTime = 2f
    private var headerText: TextEntity? = null
    private var backButton: BackEntity? = null
    private var Coinbmp: Bitmap? = null
    private val moneyPaint = Paint()
    private val moneyTextBounds = Rect()
    private var bubbleButton: ParticleButtonSelectEntity? = null
    private var bloodButton: ParticleButtonSelectEntity? = null
    private var fishButton: ParticleButtonSelectEntity? = null

    override fun GetName(): String {
        return "Garage"
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
            "Playground",
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

        bubbleButton = ParticleButtonSelectEntity.Companion.Create(
            ResourceManager.Companion.Instance.GetBitmap(R.drawable.buybutton),
            ResourceManager.Companion.Instance.GetBitmap(R.drawable.bubbleparticlebutton),
            100f,
            GameSystem.Companion.Instance.GetScreenScale()!!.y - 200 * 0.15f,
            200f,
            60f
        )
        bubbleButton!!.locked = false
        if (PlayerInfo.Companion.Instance.GetEffectType() == PARTICLETYPE.P_BUBBLE) bubbleButton!!.selected =
            true
        bloodButton = ParticleButtonSelectEntity.Companion.Create(
            ResourceManager.Companion.Instance.GetBitmap(R.drawable.buybutton),
            ResourceManager.Companion.Instance.GetBitmap(R.drawable.bloodparticlebutton),
            bubbleButton!!.Pos.x + bubbleButton!!.GetWidth(),
            bubbleButton!!.Pos.y,
            200f,
            60f
        )
        if (PlayerInfo.Companion.Instance.GetEffectType() == PARTICLETYPE.P_BLOOD) bloodButton!!.selected =
            true
        if (GameSystem.Companion.Instance.GetBoolFromSave("bloodeffect") == true) bloodButton!!.locked =
            false
        fishButton = ParticleButtonSelectEntity.Companion.Create(
            ResourceManager.Companion.Instance.GetBitmap(R.drawable.buybutton),
            ResourceManager.Companion.Instance.GetBitmap(R.drawable.fishparticlebutton),
            bloodButton!!.Pos.x + bubbleButton!!.GetWidth(),
            bubbleButton!!.Pos.y,
            200f,
            60f
        )
        if (PlayerInfo.Companion.Instance.GetEffectType() == PARTICLETYPE.P_FISH) fishButton!!.selected =
            true
        if (GameSystem.Companion.Instance.GetBoolFromSave("fisheffect") == true) fishButton!!.locked =
            false
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
        if (bubbleButton!!.clicked == true) {
            bubbleButton!!.selected = true
            bloodButton!!.selected = false
            fishButton!!.selected = false
            PlayerInfo.Companion.Instance.SetEffectType(PARTICLETYPE.P_BUBBLE)
            GameSystem.Companion.Instance.SaveEditBegin()
            GameSystem.Companion.Instance.SetIntInSave("effecttype", PARTICLETYPE.P_BUBBLE.ordinal)
            GameSystem.Companion.Instance.SaveEditEnd()
        } else if (bloodButton!!.clicked == true && bloodButton!!.locked == false) {
            bubbleButton!!.selected = false
            bloodButton!!.selected = true
            fishButton!!.selected = false
            PlayerInfo.Companion.Instance.SetEffectType(PARTICLETYPE.P_BLOOD)
            GameSystem.Companion.Instance.SaveEditBegin()
            GameSystem.Companion.Instance.SetIntInSave("effecttype", PARTICLETYPE.P_BLOOD.ordinal)
            GameSystem.Companion.Instance.SaveEditEnd()
        } else if (fishButton!!.clicked == true && fishButton!!.locked == false) {
            bubbleButton!!.selected = false
            bloodButton!!.selected = false
            fishButton!!.selected = true
            PlayerInfo.Companion.Instance.SetEffectType(PARTICLETYPE.P_FISH)

            GameSystem.Companion.Instance.SaveEditBegin()
            GameSystem.Companion.Instance.SetIntInSave("effecttype", PARTICLETYPE.P_FISH.ordinal)
            GameSystem.Companion.Instance.SaveEditEnd()
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
