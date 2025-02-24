package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.SurfaceView

class GameOverState : StateBase {
    private var background: Bitmap? = null
    private val bubbleSpawnTime = 2f
    private var headerText: TextEntity? = null
    private var backButton: BackEntity? = null
    private var gameOverText: TextEntity? = null
    private var RetryButton: TextButtonEntity? = null
    private var LeaderboardButton: TextButtonEntity? = null
    private var DepthText: TextEntity? = null
    private var ShareButton: TextEntity? = null

    override fun GetName(): String {
        return "GameOver"
    }

    override fun OnEnter(_view: SurfaceView) {
        GameSystem.Companion.Instance.SaveEditBegin()
        GameSystem.Companion.Instance.SetIntInSave(
            "money",
            PlayerInfo.Companion.Instance.GetMoney()
        )
        GameSystem.Companion.Instance.SetStringInSave(
            "highscore1_name", GameSystem.Companion.Instance.GetHighScores()!!
                .get(0).GetName()
        )
        GameSystem.Companion.Instance.SetIntInSave(
            "highscore1_score", GameSystem.Companion.Instance.GetHighScores()!!
                .get(0).GetScore()
        )
        GameSystem.Companion.Instance.SetStringInSave(
            "highscore2_name", GameSystem.Companion.Instance.GetHighScores()!!
                .get(1).GetName()
        )
        GameSystem.Companion.Instance.SetIntInSave(
            "highscore2_score", GameSystem.Companion.Instance.GetHighScores()!!
                .get(1).GetScore()
        )
        GameSystem.Companion.Instance.SetStringInSave(
            "highscore3_name", GameSystem.Companion.Instance.GetHighScores()!!
                .get(2).GetName()
        )
        GameSystem.Companion.Instance.SetIntInSave(
            "highscore3_score", GameSystem.Companion.Instance.GetHighScores()!!
                .get(2).GetScore()
        )
        GameSystem.Companion.Instance.SetStringInSave(
            "highscore4_name", GameSystem.Companion.Instance.GetHighScores()!!
                .get(3).GetName()
        )
        GameSystem.Companion.Instance.SetIntInSave(
            "highscore4_score", GameSystem.Companion.Instance.GetHighScores()!!
                .get(3).GetScore()
        )
        GameSystem.Companion.Instance.SetStringInSave(
            "highscore5_name", GameSystem.Companion.Instance.GetHighScores()!!
                .get(4).GetName()
        )
        GameSystem.Companion.Instance.SetIntInSave(
            "highscore5_score", GameSystem.Companion.Instance.GetHighScores()!!
                .get(4).GetScore()
        )
        GameSystem.Companion.Instance.SaveEditEnd()
        background = Bitmap.createScaledBitmap(
            ImageManager.Companion.Instance.GetImage(IMAGE.I_MENUBACKGROUND)!!,
            GameSystem.Companion.Instance.GetScreenScale()!!.x.toInt(),
            GameSystem.Companion.Instance.GetScreenScale()!!.y.toInt(),
            true
        )
        headerText = TextEntity.Companion.Create(
            GameSystem.Companion.Instance.ScreenScale!!.x * 0.5f,
            0f,
            "GameOver",
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

        gameOverText = TextEntity.Companion.Create(
            GameSystem.Companion.Instance.ScreenScale!!.x * 0.5f,
            GameSystem.Companion.Instance.ScreenScale!!.y * 0.4f,
            "You Lose!",
            100f
        )
        gameOverText!!.SetPaintColor(Color.RED)

        DepthText = TextEntity.Companion.Create(
            GameSystem.Companion.Instance.ScreenScale!!.x * 0.5f,
            GameSystem.Companion.Instance.ScreenScale!!.y * 0.5f,
            "Score: " + PlayerInfo.Companion.Instance.GetScore(),
            70f
        )
        DepthText!!.SetPaintColor(Color.BLACK)

        ShareButton = TextEntity.Companion.Create(
            DepthText!!.Pos.x,
            DepthText!!.Pos.y + DepthText!!.height * 1.5f,
            "Share",
            70f
        )
        ShareButton!!.SetBMP(ResourceManager.Companion.Instance.GetBitmap(R.drawable.buttonbg))
        ShareButton!!.SetOnClickTextColor(Color.YELLOW)

        RetryButton = TextButtonEntity.Companion.Create(
            0f,
            GameSystem.Companion.Instance.ScreenScale!!.y * 0.7f,
            "Retry",
            60f,
            "MainGame"
        )
        RetryButton!!.SetPosX(GameSystem.Companion.Instance.ScreenScale!!.x * 0.3f - RetryButton!!.GetWidth() * 0.5f)

        LeaderboardButton = TextButtonEntity.Companion.Create(
            GameSystem.Companion.Instance.ScreenScale!!.x * 0.2f,
            GameSystem.Companion.Instance.ScreenScale!!.y * 0.7f,
            "HighScores",
            60f,
            "Scores"
        )
        LeaderboardButton!!.SetPosX(GameSystem.Companion.Instance.ScreenScale!!.x * 0.7f + RetryButton!!.GetWidth() * 0.5f)
    }

    override fun OnExit() {
        EntityManager.Companion.Instance.EmptyEntityList()
    }

    override fun Render(_canvas: Canvas) {
        // Render anything
        _canvas.drawBitmap(background!!, 0f, 0f, null)
        ParticleManager.Companion.Instance.Render(_canvas)
        EntityManager.Companion.Instance.Render(_canvas)
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
            if (ShareButton!!.clicked) {
                StateManager.Companion.Instance.ChangeState("ScorePage")
                GamePage.Companion.Instance!!.ChangePage(scorepage::class.java)
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
