package com.fishweeb.practical;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

public class MainGameState implements StateBase
{
    private Bitmap bmp; //Picture like png files are known as bitmap
    // Scaledbmp - scaled version based on Screen width and height for the image bmp
    private Bitmap Scaledbmp;
    private Bitmap Coinbmp;

    float offset = 0.0f;

    int ScreenWidth,ScreenHeight; //Screen width and height
    float xPos = 0.0f, yPos = 0.0f; //Variable used for positioning of images

    private Paint depthPaint = new Paint();
    private Paint scorePaint = new Paint();
    private Paint moneyPaint = new Paint();

    private ButtonEntity pauseButton;

    private boolean SpawnBooleanTest = false;

    public String GetName()
    {
        return "MainGame";
    }

    public void OnEnter(SurfaceView _view)
    {
        SpawnManager.Instance.Init(_view);

        //Reset EntityList
        EntityManager.Instance.EmptyEntityList();
        ParticleManager.Instance.RemoveParticles();

        depthPaint.setColor(Color.WHITE);
        depthPaint.setFakeBoldText(true);
        depthPaint.setTextSize(50);
        depthPaint.setTextAlign(Paint.Align.RIGHT);

        scorePaint.setColor(Color.WHITE);
        scorePaint.setAlpha(200);
        scorePaint.setTextSize(60);
        scorePaint.setTextAlign(Paint.Align.CENTER);
        scorePaint.setFakeBoldText(true);

        moneyPaint.setColor(Color.YELLOW);
        moneyPaint.setAlpha(200);
        moneyPaint.setTextSize(50);
        moneyPaint.setTextAlign(Paint.Align.LEFT);
        moneyPaint.setFakeBoldText(true);

        bmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.gamescene);
        Coinbmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.coin);

        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        //Scaled version of bmp which is background of game scene
        Scaledbmp = Bitmap.createScaledBitmap(bmp,ScreenWidth,ScreenHeight,true);

        //Reinitialize values
        PlayerInfo.Instance.RestartGame();

        PlayerEntity.Create();

        //Pause Menu
        pauseButton = PauseEntity.Create(0,0);
        pauseButton.SetPosX(pauseButton.width * 0.5f);
        pauseButton.SetPosY(pauseButton.height * 0.5f);
        //Pause Menu

        //RenderBackground.Create();
        //RenderPauseButton.Create();

        if (SettingsManager.Instance.GetFpsShow())
            RenderTextEntity.Create();
    }
    public void OnExit()
    {
        EntityManager.Instance.EmptyEntityList();
        ParticleManager.Instance.RemoveParticles();
        GameSystem.Instance.SetGameSpeed(1);
    }
    public void Render(Canvas _canvas)
    {
        int currOffset = (int)(offset * 100f);
        _canvas.drawBitmap(Scaledbmp,xPos,yPos,null);
        _canvas.drawBitmap(Scaledbmp,xPos,yPos-ScreenHeight,null);
        // xPos = 0

        // _View.getWidth()
        ParticleManager.Instance.Render(_canvas);

        EntityManager.Instance.Render(_canvas);

        //Health
        float bgOffset = 4;
        float healthXSize = 120;
        float healthYSize = 40;
        _canvas.drawRect(20 - bgOffset + pauseButton.width,20 - bgOffset,20 + healthXSize + bgOffset+ pauseButton.width,20 + healthYSize + bgOffset,PaintColor.Instance.GetPaint(Color.BLACK));
        _canvas.drawRect(20 + pauseButton.width,20,20 + ((PlayerInfo.Instance.GetHealth()/PlayerInfo.Instance.GetMaxHealth()) * healthXSize) + pauseButton.width,20 + healthYSize ,PaintColor.Instance.GetPaint(Color.RED));
        //Health

        //Distance
        _canvas.drawText("" + (int)PlayerInfo.Instance.GetDepth() + "ft",ScreenWidth,depthPaint.getTextSize(),depthPaint);
        //Distance

        //Score
        _canvas.drawText("SCORE",ScreenWidth*0.5f,scorePaint.getTextSize(),scorePaint);
        scorePaint.setTextSize(50);
        scorePaint.setAlpha(255);
        _canvas.drawText("" + PlayerInfo.Instance.GetScore(),ScreenWidth*0.5f,scorePaint.getTextSize()*2.2f,scorePaint);
        scorePaint.setTextSize(60);
        scorePaint.setAlpha(200);
        //Score

        //Money
        _canvas.drawBitmap(Coinbmp,20+ pauseButton.width,20 + healthYSize + Coinbmp.getHeight()*0.2f,null);
        _canvas.drawText("" + PlayerInfo.Instance.GetMoney(),20 + Coinbmp.getWidth() + moneyPaint.getTextSize()*0.2f+ pauseButton.width,20 + healthYSize + Coinbmp.getHeight()*0.2f + moneyPaint.getTextSize(),moneyPaint);
        //Money

        //Buttons
    }
    public void Update(float _dt)
    {
        offset += _dt;

        EntityManager.Instance.Update(_dt);
        ParticleManager.Instance.Update(_dt);

        yPos += _dt * 500;

        if (PlayerInfo.Instance.GetHealth()>0)
        PlayerInfo.Instance.DepthUpdate(_dt);

        if (yPos > ScreenHeight)
        {
            yPos = 0;
        }

        SpawnManager.Instance.Update(_dt);

        if (SpawnBooleanTest ==  false && PlayerInfo.Instance.GetDepth() > 3000)
        {
            SpawnBooleanTest = true;
            ParticleObject tempParticle = ParticleManager.Instance.FetchParticle(PARTICLETYPE.P_POWERUP_SPREAD);
            tempParticle.SetBMP(ImageManager.Instance.GetImage(IMAGE.I_POWERUP_SPREAD));
            tempParticle.position.x = ScreenWidth*0.5f;
            tempParticle.position.y = 0;
        }
    }
}
