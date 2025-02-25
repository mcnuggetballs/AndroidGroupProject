package edu.androidgroupproject

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.SurfaceView
import com.edu.androidgroupproject.GameSystem

class GameOverState : StateBase {
    private var background: Bitmap? = null
    private val bubbleSpawnTime = 2f
    private var headerText: TextEntity? = null
    private var backButton: BackEntity? = null
    private var gameOverText: TextEntity? = null
    private var RetryButton: TextButtonEntity? = null
    private var LeaderboardButton: TextButtonEntity? = null
    private var DepthText: TextEntity? = null

    override fun GetName(): String {
        return "GameOver"
    }

    override fun OnEnter(_view: SurfaceView) {
        GameSystem.Instance.SaveEditBegin()
        GameSystem.Instance.SetIntInSave(
            "money",
            PlayerInfo.Instance.GetMoney()
        )
        GameSystem.Instance.SetStringInSave(
            "highscore1_name", GameSystem.Instance.GetHighScores()!!
                .get(0).GetName()
        )
        GameSystem.Instance.SetIntInSave(
            "highscore1_score", GameSystem.Instance.GetHighScores()!!
                .get(0).GetScore()
        )
        GameSystem.Instance.SetStringInSave(
            "highscore2_name", GameSystem.Instance.GetHighScores()!!
                .get(1).GetName()
        )
        GameSystem.Instance.SetIntInSave(
            "highscore2_score", GameSystem.Instance.GetHighScores()!!
                .get(1).GetScore()
        )
        GameSystem.Instance.SetStringInSave(
            "highscore3_name", GameSystem.Instance.GetHighScores()!!
                .get(2).GetName()
        )
        GameSystem.Instance.SetIntInSave(
            "highscore3_score", GameSystem.Instance.GetHighScores()!!
                .get(2).GetScore()
        )
        GameSystem.Instance.SetStringInSave(
            "highscore4_name", GameSystem.Instance.GetHighScores()!!
                .get(3).GetName()
        )
        GameSystem.Instance.SetIntInSave(
            "highscore4_score", GameSystem.Instance.GetHighScores()!!
                .get(3).GetScore()
        )
        GameSystem.Instance.SetStringInSave(
            "highscore5_name", GameSystem.Instance.GetHighScores()!!
                .get(4).GetName()
        )
        GameSystem.Instance.SetIntInSave(
            "highscore5_score", GameSystem.Instance.GetHighScores()!!
                .get(4).GetScore()
        )
        GameSystem.Instance.SaveEditEnd()
        background = Bitmap.createScaledBitmap(
            ImageManager.Instance.GetImage(IMAGE.I_MENUBACKGROUND)!!,
            GameSystem.Instance.GetScreenScale()!!.x.toInt(),
            GameSystem.Instance.GetScreenScale()!!.y.toInt(),
            true
        )
        headerText = TextEntity.Create(
            GameSystem.Instance.ScreenScale!!.x * 0.5f,
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

        gameOverText = TextEntity.Create(
            GameSystem.Instance.ScreenScale!!.x * 0.5f,
            GameSystem.Instance.ScreenScale!!.y * 0.4f,
            "You Lose!",
            100f
        )
        gameOverText!!.SetPaintColor(Color.RED)

        DepthText = TextEntity.Create(
            GameSystem.Instance.ScreenScale!!.x * 0.5f,
            GameSystem.Instance.ScreenScale!!.y * 0.5f,
            "Score: " + PlayerInfo.Instance.GetScore(),
            70f
        )
        DepthText!!.SetPaintColor(Color.BLACK)

        RetryButton = TextButtonEntity.Create(
            0f,
            GameSystem.Instance.ScreenScale!!.y * 0.7f,
            "Retry",
            60f,
            "MainGame"
        )
        RetryButton!!.SetPosX(GameSystem.Instance.ScreenScale!!.x * 0.3f - RetryButton!!.GetWidth() * 0.5f)

        LeaderboardButton = TextButtonEntity.Create(
            GameSystem.Instance.ScreenScale!!.x * 0.2f,
            GameSystem.Instance.ScreenScale!!.y * 0.7f,
            "HighScores",
            60f,
            "Scores"
        )
        LeaderboardButton!!.SetPosX(GameSystem.Instance.ScreenScale!!.x * 0.7f + RetryButton!!.GetWidth() * 0.5f)
    }

    override fun OnExit() {
        EntityManager.Companion.Instance.EmptyEntityList()
    }

    override fun Render(_canvas: Canvas) {
        // Render anything
        _canvas.drawBitmap(background!!, 0f, 0f, null)
        ParticleManager.Instance.Render(_canvas)
        EntityManager.Companion.Instance.Render(_canvas)
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
                    ImageManager.Instance.GetImage(
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
