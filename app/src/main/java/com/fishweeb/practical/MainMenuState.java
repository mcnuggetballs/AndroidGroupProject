package com.fishweeb.practical;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.provider.ContactsContract;
import android.view.SurfaceView;

// Sample of an intro state - You can change to Splash page..
public class MainMenuState extends Activity implements StateBase
{
    private Bitmap background = null;
    private Bitmap logo = null;
    private TextButtonEntity PlayButton = null;
    private TextButtonEntity ShopButton = null;
    private TextButtonEntity GarageButton = null;
    private TextButtonEntity OptionsButton = null;
    private TextButtonEntity ScoresButton = null;
    private float bubbleSpawnTime = 2;
    private Bitmap Coinbmp;
    private Paint moneyPaint = new Paint();
    private Rect moneyTextBounds = new Rect();

    @Override
    public String GetName() {
        return "MainMenu";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
        background = Bitmap.createScaledBitmap(ImageManager.Instance.GetImage(IMAGE.I_MENUBACKGROUND),(int)GameSystem.Instance.GetScreenScale().x,(int)GameSystem.Instance.GetScreenScale().y,true);
        logo = Bitmap.createScaledBitmap(ImageManager.Instance.GetImage(IMAGE.I_LOGO),(int)(GameSystem.Instance.GetScreenScale().x*0.8f),(int)(GameSystem.Instance.GetScreenScale().y*0.2f),true);
        float textSize = 100;
        float xOffset = -330;
        PlayButton = TextButtonEntity.Create(0,0,"Play",textSize,"MainGame");
        PlayButton.SetPosX(GameSystem.Instance.ScreenScale.x*0.5f + PlayButton.GetWidth()*0.5f + xOffset);
        PlayButton.SetPosY(GameSystem.Instance.ScreenScale.y*0.5f - textSize*2.5f);
        PlayButton.SetDefaultTextColor(Color.BLACK);
        PlayButton.SetOnClickTextColor(Color.YELLOW);

        ShopButton = TextButtonEntity.Create(0,0,"Shop",textSize,"Shop");
        ShopButton.SetPosX(GameSystem.Instance.ScreenScale.x*0.5f + ShopButton.GetWidth()*0.5f + xOffset);
        ShopButton.SetPosY(PlayButton.Pos.y + textSize*1.5f);
        ShopButton.SetDefaultTextColor(Color.BLACK);
        ShopButton.SetOnClickTextColor(Color.YELLOW);

        GarageButton = TextButtonEntity.Create(0,0,"Playground",textSize,"Garage");
        GarageButton.SetPosX(GameSystem.Instance.ScreenScale.x*0.5f + GarageButton.GetWidth()*0.5f + xOffset);
        GarageButton.SetPosY(ShopButton.Pos.y + textSize*1.5f);
        GarageButton.SetDefaultTextColor(Color.BLACK);
        GarageButton.SetOnClickTextColor(Color.YELLOW);

        OptionsButton = TextButtonEntity.Create(0,0,"Options",textSize,"Options");
        OptionsButton.SetPosX(GameSystem.Instance.ScreenScale.x*0.5f + OptionsButton.GetWidth()*0.5f + xOffset);
        OptionsButton.SetPosY(GarageButton.Pos.y + textSize*1.5f);
        OptionsButton.SetDefaultTextColor(Color.BLACK);
        OptionsButton.SetOnClickTextColor(Color.YELLOW);

        ScoresButton = TextButtonEntity.Create(0,0,"Scores",textSize,"Scores");
        ScoresButton.SetPosX(GameSystem.Instance.ScreenScale.x*0.5f + ScoresButton.GetWidth()*0.5f + xOffset);
        ScoresButton.SetPosY(OptionsButton.Pos.y + textSize*1.5f);
        ScoresButton.SetDefaultTextColor(Color.BLACK);
        ScoresButton.SetOnClickTextColor(Color.YELLOW);

        Coinbmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.coin);
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
        _canvas.drawBitmap(logo,GameSystem.Instance.GetScreenScale().x*0.1f,GameSystem.Instance.GetScreenScale().y*0.1f,null);

        //Money
        String moneyText = "" + PlayerInfo.Instance.GetMoney();
        moneyPaint.getTextBounds(moneyText,0,moneyText.length(),moneyTextBounds);
        _canvas.drawBitmap(Coinbmp,Coinbmp.getWidth()*0.5f,Coinbmp.getHeight()*0.5f,null);
        _canvas.drawText(moneyText,Coinbmp.getWidth() + Coinbmp.getWidth()*0.7f,Coinbmp.getHeight() + moneyTextBounds.height()*0.5f,moneyPaint);
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
