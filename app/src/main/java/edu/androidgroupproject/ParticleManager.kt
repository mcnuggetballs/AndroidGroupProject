package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import edu.androidgroupproject.*
import kotlin.math.atan2
import kotlin.math.PI
import kotlin.random.Random

class ParticleManager {
    private val particleList = mutableListOf<ParticleObject>()
    private val particleQueue = mutableListOf<ParticleObject>()
    private val POWERUP_SPEED = 300
    private var ScreenWidth = 0
    private var ScreenHeight = 0

    fun Init(_ScreenWidth: Int, _ScreenHeight: Int) {
        ScreenWidth = _ScreenWidth
        ScreenHeight = _ScreenHeight

        for (i in PARTICLETYPE.values().indices) {
            repeat(100) {
                particleList.add(ParticleObject(PARTICLETYPE.values()[i]))
            }
        }
    }

    fun FetchParticle(type: PARTICLETYPE): ParticleObject {
        particleList.firstOrNull { !it.active && it.type == type }?.let {
            it.active = true
            return it
        }

        return ParticleObject(type).apply {
            active = true
            particleQueue.add(this)
        }
    }

    fun CreateParticle(type: PARTICLETYPE, x: Float, y: Float) {
        when (type) {
            PARTICLETYPE.P_BUBBLE -> {
                val newParticle = Instance.FetchParticle(PARTICLETYPE.P_BUBBLE)
                val diameter = Random.nextInt(15, 65)
                newParticle.width = diameter.toFloat()
                newParticle.height = diameter.toFloat()
                newParticle.position.x = x
                newParticle.position.y = y
                newParticle.velocity.x = Random.nextFloat() * 40 - 20
                newParticle.velocity.y = -(Random.nextFloat() * 160 + 80)
                newParticle.SetBMP(
                    Bitmap.createScaledBitmap(
                        ImageManager.Instance.GetImage(IMAGE.I_BUBBLE)!!, diameter, diameter, true
                    )
                )
            }
            PARTICLETYPE.P_BLOOD -> {
                val tempParticle = Instance.FetchParticle(PARTICLETYPE.P_BLOOD)
                tempParticle.position.x = x
                tempParticle.position.y = y
                tempParticle.width = 20f
                tempParticle.height = 20f
                tempParticle.velocity.x = Random.nextFloat() * 300 - 150
                tempParticle.velocity.y = Random.nextFloat() * 300 - 150
                tempParticle.paint = Paint().apply {
                    color = Color.RED
                    alpha = 255
                }
                tempParticle.timer = 0f
            }
            PARTICLETYPE.P_FISH -> {
                val tempParticle = Instance.FetchParticle(PARTICLETYPE.P_FISH)
                tempParticle.position.x = x
                tempParticle.position.y = y
                tempParticle.velocity.x = Random.nextFloat() * 500 - 250
                tempParticle.velocity.y = Random.nextFloat() * 500 - 250
                tempParticle.SetBMP(ResourceManager.Instance.GetBitmap(R.drawable.sprite_fish)!!)
            }
            else -> {}
        }
    }

    fun Update(dt: Float) {
        particleList.addAll(particleQueue)
        particleQueue.clear()

        for (p in particleList) {
            if (!p.active) continue
            p.position.x += p.velocity.x * dt
            p.position.y += p.velocity.y * dt

            if (p.type == PARTICLETYPE.P_BLOOD) {
                p.timer += dt
                if (p.timer > 0.4) {
                    p.timer = 0f
                    p.paint?.alpha = (p.paint?.alpha ?: 255) - 30
                }
                if ((p.paint?.alpha ?: 255) < 30) {
                    p.active = false
                }
            }
        }
    }

    fun Render(canvas: Canvas) {
        for (p in particleList) {
            if (!p.active) continue

            when (p.type) {
                PARTICLETYPE.P_MONEY -> canvas.drawOval(
                    p.position.x - p.width * 0.5f,
                    p.position.y - p.height * 0.5f,
                    p.position.x + p.width * 0.5f,
                    p.position.y + p.height * 0.5f,
                    PaintColor.Instance.GetPaint(Color.YELLOW)!!
                )
                PARTICLETYPE.P_BLOOD -> p.paint?.let {
                    canvas.drawOval(
                        p.position.x - p.width * 0.5f,
                        p.position.y - p.height * 0.5f,
                        p.position.x + p.width * 0.5f,
                        p.position.y + p.height * 0.5f,
                        it
                    )
                }
                PARTICLETYPE.P_FISH -> {
                    val transform = Matrix()
                    val angle = atan2(p.velocity.y, p.velocity.x) * (180f / PI.toFloat()) - 90f
                    transform.postRotate(angle)
                    transform.postTranslate(p.position.x, p.position.y)
                    canvas.drawBitmap(p.GetBMP()!!, transform, null)
                }
                else -> canvas.drawBitmap(p.GetBMP()!!, p.position.x - (p.width * 0.5f), p.position.y - (p.height * 0.5f), null)
            }
        }
    }

    fun RemoveParticles() {
        particleList.clear()
    }

    companion object {
        val Instance: ParticleManager = ParticleManager()
    }
}