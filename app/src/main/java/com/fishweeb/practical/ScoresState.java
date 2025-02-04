package com.fishweeb.practical;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;

import javax.crypto.spec.DHGenParameterSpec;

public class ScoresState implements StateBase
{
    private Bitmap background = null;
    private float bubbleSpawnTime = 2;
    private TextEntity headerText;
    private BackEntity backButton;
    private Paint HighScorePaint = new Paint();
    private Rect HighScoreTextBounds = new Rect();
    private Bitmap Coinbmp;
    private Paint moneyPaint = new Paint();
    private Rect moneyTextBounds = new Rect();

    @Override
    public String GetName() {
        return "Scores";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
        background = Bitmap.createScaledBitmap(ImageManager.Instance.GetImage(IMAGE.I_MENUBACKGROUND),(int)GameSystem.Instance.GetScreenScale().x,(int)GameSystem.Instance.GetScreenScale().y,true);
        headerText = TextEntity.Create(GameSystem.Instance.ScreenScale.x*0.5f,0,"Scores",100);
        headerText.SetPosY(headerText.GetHeight()*0.5f);
        headerText.SetHeader(true);
        backButton = BackEntity.Create(0,0,headerText.height*1.3f,headerText.height*1.3f,"MainMenu");
        backButton.SetPosX(backButton.width*0.5f);
        backButton.SetPosY(backButton.height*0.5f);

        HighScorePaint.setColor(Color.BLACK);
        HighScorePaint.setTextSize(50);

        Coinbmp = BitmapFactory.decodeResource(_view.getResources(), R.drawable.coin);
        moneyPaint.setColor(Color.YELLOW);
        moneyPaint.setAlpha(200);
        moneyPaint.setTextSize(50);
        moneyPaint.setTextAlign(Paint.Align.LEFT);
        moneyPaint.setFakeBoldText(true);
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
        int count = 0;
        for (int i = 0;i<5;++i)
        {
            if (i >= GameSystem.Instance.GetHighScores().size())
            {
                _canvas.drawText((i+1) + ".---------",GameSystem.Instance.ScreenScale.x*0.1f,GameSystem.Instance.GetScreenScale().y*0.2f + HighScoreTextBounds.height()*1.2f*count,HighScorePaint);
                String scoreText = "0";
                HighScorePaint.getTextBounds(scoreText,0,scoreText.length(),HighScoreTextBounds);
                _canvas.drawText(scoreText,GameSystem.Instance.ScreenScale.x*0.9f - HighScoreTextBounds.width(),GameSystem.Instance.GetScreenScale().y*0.2f + HighScoreTextBounds.height()*1.2f*count,HighScorePaint);
                ++count;
                continue;
            }


            _canvas.drawText((i+1) + "." + GameSystem.Instance.GetHighScores().get(i).GetName(),GameSystem.Instance.ScreenScale.x*0.1f,GameSystem.Instance.GetScreenScale().y*0.2f + HighScoreTextBounds.height()*1.2f*count,HighScorePaint);
            String depthText = "" + (int)GameSystem.Instance.GetHighScores().get(i).GetScore();
            HighScorePaint.getTextBounds(depthText,0,depthText.length(),HighScoreTextBounds);
            _canvas.drawText(depthText,GameSystem.Instance.ScreenScale.x*0.9f - HighScoreTextBounds.width(),GameSystem.Instance.GetScreenScale().y*0.2f + HighScoreTextBounds.height()*1.2f*count,HighScorePaint);
            ++count;
        }

        //Money
        String moneyText = "" + PlayerInfo.Instance.GetMoney();
        moneyPaint.getTextBounds(moneyText,0,moneyText.length(),moneyTextBounds);
        _canvas.drawBitmap(Coinbmp,Coinbmp.getWidth()*0.5f,Coinbmp.getHeight()*0.5f + headerText.height*1.3f,null);
        _canvas.drawText(moneyText,Coinbmp.getWidth() + Coinbmp.getWidth()*0.7f,Coinbmp.getHeight() + headerText.height*1.3f + moneyTextBounds.height()*0.5f,moneyPaint);
        //Money
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
