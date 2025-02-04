package com.fishweeb.practical;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.SurfaceView;

public class GameOverState implements StateBase
{
    private Bitmap background = null;
    private float bubbleSpawnTime = 2;
    private TextEntity headerText;
    private BackEntity backButton;
    private TextEntity gameOverText;
    private TextButtonEntity RetryButton;
    private TextButtonEntity LeaderboardButton;
    private TextEntity DepthText;
    private TextEntity ShareButton;

    @Override
    public String GetName() {
        return "GameOver";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
        GameSystem.Instance.SaveEditBegin();
        GameSystem.Instance.SetIntInSave("money",PlayerInfo.Instance.GetMoney());
        GameSystem.Instance.SetStringInSave("highscore1_name",GameSystem.Instance.GetHighScores().get(0).GetName());GameSystem.Instance.SetIntInSave("highscore1_score",(int)GameSystem.Instance.GetHighScores().get(0).GetScore());
        GameSystem.Instance.SetStringInSave("highscore2_name",GameSystem.Instance.GetHighScores().get(1).GetName());GameSystem.Instance.SetIntInSave("highscore2_score",(int)GameSystem.Instance.GetHighScores().get(1).GetScore());
        GameSystem.Instance.SetStringInSave("highscore3_name",GameSystem.Instance.GetHighScores().get(2).GetName());GameSystem.Instance.SetIntInSave("highscore3_score",(int)GameSystem.Instance.GetHighScores().get(2).GetScore());
        GameSystem.Instance.SetStringInSave("highscore4_name",GameSystem.Instance.GetHighScores().get(3).GetName());GameSystem.Instance.SetIntInSave("highscore4_score",(int)GameSystem.Instance.GetHighScores().get(3).GetScore());
        GameSystem.Instance.SetStringInSave("highscore5_name",GameSystem.Instance.GetHighScores().get(4).GetName());GameSystem.Instance.SetIntInSave("highscore5_score",(int)GameSystem.Instance.GetHighScores().get(4).GetScore());
        GameSystem.Instance.SaveEditEnd();
        background = Bitmap.createScaledBitmap(ImageManager.Instance.GetImage(IMAGE.I_MENUBACKGROUND),(int)GameSystem.Instance.GetScreenScale().x,(int)GameSystem.Instance.GetScreenScale().y,true);
        headerText = TextEntity.Create(GameSystem.Instance.ScreenScale.x*0.5f,0,"GameOver",100);
        headerText.SetPosY(headerText.GetHeight()*0.5f);
        headerText.SetHeader(true);
        backButton = BackEntity.Create(0,0,headerText.height*1.3f,headerText.height*1.3f,"MainMenu");
        backButton.SetPosX(backButton.width*0.5f);
        backButton.SetPosY(backButton.height*0.5f);

        gameOverText = TextEntity.Create(GameSystem.Instance.ScreenScale.x*0.5f,GameSystem.Instance.ScreenScale.y*0.4f,"You Lose!",100);
        gameOverText.SetPaintColor(Color.RED);

        DepthText = TextEntity.Create(GameSystem.Instance.ScreenScale.x*0.5f,GameSystem.Instance.ScreenScale.y*0.5f,"Score: "+ (int)PlayerInfo.Instance.GetScore(),70);
        DepthText.SetPaintColor(Color.BLACK);

        ShareButton = TextEntity.Create(DepthText.Pos.x,DepthText.Pos.y + DepthText.height*1.5f,"Share",70);
        ShareButton.SetBMP(ResourceManager.Instance.GetBitmap(R.drawable.buttonbg));
        ShareButton.SetOnClickTextColor(Color.YELLOW);

        RetryButton = TextButtonEntity.Create(0,GameSystem.Instance.ScreenScale.y*0.7f,"Retry",60,"MainGame");
        RetryButton.SetPosX(GameSystem.Instance.ScreenScale.x * 0.3f - RetryButton.GetWidth()*0.5f);

        LeaderboardButton = TextButtonEntity.Create(GameSystem.Instance.ScreenScale.x * 0.2f,GameSystem.Instance.ScreenScale.y*0.7f,"HighScores",60,"Scores");
        LeaderboardButton.SetPosX(GameSystem.Instance.ScreenScale.x * 0.7f + RetryButton.GetWidth()*0.5f);


    }

    @Override
    public void OnExit()
    {
        EntityManager.Instance.EmptyEntityList();
    }

    @Override
    public void Render(Canvas _canvas)
    {
        // Render anything
        _canvas.drawBitmap(background,0,0,null);
        ParticleManager.Instance.Render(_canvas);
        EntityManager.Instance.Render(_canvas);
    }

    @Override
    public void Update(float _dt)
    {
        if (TouchManager.Instance.HasTouch())
        {
            for (int i = 0;i<3;++i)
            {
                ParticleManager.Instance.CreateParticle(PlayerInfo.Instance.GetEffectType(),TouchManager.Instance.GetPosX(),TouchManager.Instance.GetPosY());
            }
            if (ShareButton.clicked)
            {
                StateManager.Instance.ChangeState("ScorePage");
                GamePage.Instance.ChangePage(scorepage.class);
            }
        }

        GameSystem.Instance.m_bubbleTimer += _dt;
        if (GameSystem.Instance.m_bubbleTimer >= bubbleSpawnTime)
        {
            ParticleObject newParticle = ParticleManager.Instance.FetchParticle(PARTICLETYPE.P_BUBBLE);
            int diameter = (int)(Math.random()*50 + 15);
            newParticle.width = diameter;
            newParticle.height = diameter;
            newParticle.position.x = ((float)Math.random() * (GameSystem.Instance.GetScreenScale().x - newParticle.width)) + (newParticle.width*0.5f);
            newParticle.position.y = GameSystem.Instance.GetScreenScale().y + newParticle.height;
            newParticle.velocity.x = (float)Math.random()*40-20;
            newParticle.velocity.y = -((float)Math.random()*160+80);
            newParticle.SetBMP(Bitmap.createScaledBitmap(ImageManager.Instance.GetImage(IMAGE.I_BUBBLE),diameter,diameter,true));
            GameSystem.Instance.m_bubbleTimer = 0;
        }
        ParticleManager.Instance.Update(_dt);
        EntityManager.Instance.Update(_dt);
    }
}
