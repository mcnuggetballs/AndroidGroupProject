package edu.androidgroupproject

import android.graphics.*
import kotlin.math.atan2

class ParticleManager {
    private val particleList: MutableList<ParticleObject> = mutableListOf()
    private val particleQueue: MutableList<ParticleObject> = mutableListOf()
    private val POWERUP_SPEED = 300
    private var ScreenWidth = 0
    private var ScreenHeight = 0

    fun FetchParticle(type: PARTICLETYPE): ParticleObject {
        for (p in particleList) {
            if (p.active || p.type != type) continue
            p.active = true
            return p
        }

        val newParticle = ParticleObject(type)
        newParticle.active = true
        particleQueue.add(newParticle)

        return newParticle
    }

    fun CreateParticle(type: PARTICLETYPE, x: Float, y: Float) {
        when (type) {
            PARTICLETYPE.P_BUBBLE -> {
                val newParticle = Instance.FetchParticle(PARTICLETYPE.P_BUBBLE)
                val diameter = (Math.random() * 50 + 15).toInt()
                newParticle.width = diameter.toFloat()
                newParticle.height = diameter.toFloat()
                newParticle.position.x = x
                newParticle.position.y = y
                newParticle.velocity.x = Math.random().toFloat() * 40 - 20
                newParticle.velocity.y = -(Math.random().toFloat() * 160 + 80)
                newParticle.SetBMP(
                    Bitmap.createScaledBitmap(
                        ImageManager.Companion.Instance.GetImage(
                            IMAGE.I_BUBBLE
                        )!!, diameter, diameter, true
                    )
                )
            }

            PARTICLETYPE.P_BLOOD -> {
                val tempParticle = Instance.FetchParticle(PARTICLETYPE.P_BLOOD)
                tempParticle.position.x = x
                tempParticle.position.y = y
                tempParticle.width = 20f
                tempParticle.height = 20f
                tempParticle.velocity.x = Math.random().toFloat() * 500 - 250
                tempParticle.velocity.y = Math.random().toFloat() * 500 - 250
            }

            PARTICLETYPE.P_FISH -> {
                val tempParticle2 = Instance.FetchParticle(PARTICLETYPE.P_FISH)
                tempParticle2.position.x = x
                tempParticle2.position.y = y
                tempParticle2.velocity.x = Math.random().toFloat() * 500 - 250
                tempParticle2.velocity.y = Math.random().toFloat() * 500 - 250
                tempParticle2.SetBMP(ResourceManager.Instance.GetBitmap(R.drawable.sprite_fish)!!)
            }

            else -> {}
        }
    }

    fun Init(_ScreenWidth: Int, _ScreenHeight: Int) {
        ScreenWidth = _ScreenWidth
        ScreenHeight = _ScreenHeight

        for (i in 0 until PARTICLETYPE.P_NUM.GetSize()) {
            for (i2 in 0..99) {
                particleList.add(ParticleObject(PARTICLETYPE.values()[i]))
            }
        }
    }

    fun LimitVelocity(p: ParticleObject, _limit: Float) {
        if (p.velocity.x > _limit) p.velocity.x = _limit
        else if (p.velocity.x < -_limit) p.velocity.x = -_limit

        if (p.velocity.y > _limit) p.velocity.y = _limit
        else if (p.velocity.y < -_limit) p.velocity.y = -_limit
    }

    fun DeleteOutOfBounds(p: ParticleObject) {
        if (p.position.x + (p.width * 0.5f) < 0) {
            p.active = false
        } else if (p.position.x - (p.width * 0.5f) > ScreenWidth) {
            p.active = false
        }

        if (p.position.y + (p.height * 0.5f) < 0) {
            p.active = false
        } else if (p.position.y - (p.height * 0.5f) > ScreenHeight) {
            p.active = false
        }
    }

    fun Update(_dt: Float) {
        for (newParticle in particleQueue) {
            particleList.add(newParticle)
        }
        particleQueue.clear()

        for (p in particleList) {
            if (!p.active) continue

            p.position.PlusEqual(p.velocity, _dt)

            if (p.type == PARTICLETYPE.P_MONEY) {
                p.target = (PlayerInfo.Instance.GetPos().Minus(p.position)).Normalized()
                var DistanceFromPlayer =
                    p.position.DistanceSquaredFrom(PlayerInfo.Instance.GetPos())
                if (DistanceFromPlayer <= 1) DistanceFromPlayer = 1f
                p.velocity.PlusEqual(p.target.Times(100000000 / DistanceFromPlayer + 10000), _dt)

                LimitVelocity(p, 1000f)

                if (Collision.SphereToSphere(
                        p.position.x,
                        p.position.y,
                        0f,
                        PlayerInfo.Instance.GetPos().x,
                        PlayerInfo.Instance.GetPos().y,
                        30f
                    )
                ) {
                    p.active = false
                    PlayerInfo.Instance.AddMoney(1)
                }
            }

            DeleteOutOfBounds(p)
        }
    }

    fun Render(_canvas: Canvas) {
        for (p in particleList) {
            if (!p.active) continue

            if (p.type == PARTICLETYPE.P_MONEY) _canvas.drawOval(
                p.position.x - p.width * 0.5f,
                p.position.y - p.height * 0.5f,
                p.position.x + p.width * 0.5f,
                p.position.y + p.height * 0.5f,
                PaintColor.Companion.Instance.GetPaint(Color.YELLOW)!!
            )
            else if (p.type == PARTICLETYPE.P_BUBBLE) _canvas.drawBitmap(
                p.GetBMP()!!,
                p.position.x - p.width * 0.5f,
                p.position.y - p.height * 0.5f,
                null
            )
            else if (p.type == PARTICLETYPE.P_FISH) {
                val transform = Matrix()
                val dir = p.velocity.Normalized()
                val rotation =
                    atan2(dir.y.toDouble(), dir.x.toDouble()).toFloat() * 180 / 3.142f - 90.0f
                transform.postRotate(rotation)
                transform.postTranslate(p.position.x, p.position.y)
                _canvas.drawBitmap(p.GetBMP()!!, transform, null)
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
