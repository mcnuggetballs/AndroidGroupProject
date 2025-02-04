package com.fishweeb.practical;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;

public class GarageState implements StateBase
{
    private Bitmap background = null;
    private float bubbleSpawnTime = 2;
    private TextEntity headerText;
    private BackEntity backButton;
    private Bitmap Coinbmp;
    private Paint moneyPaint = new Paint();
    private Rect moneyTextBounds = new Rect();
    private ParticleButtonSelectEntity bubbleButton;
    private ParticleButtonSelectEntity bloodButton;
    private ParticleButtonSelectEntity fishButton;

    @Override
    public String GetName() {
        return "Garage";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
        background = Bitmap.createScaledBitmap(ImageManager.Instance.GetImage(IMAGE.I_MENUBACKGROUND),(int)GameSystem.Instance.GetScreenScale().x,(int)GameSystem.Instance.GetScreenScale().y,true);
        headerText = TextEntity.Create(GameSystem.Instance.ScreenScale.x*0.5f,0,"Playground",100);
        headerText.SetPosY(headerText.GetHeight()*0.5f);
        headerText.SetHeader(true);
        backButton = BackEntity.Create(0,0,headerText.height*1.3f,headerText.height*1.3f,"MainMenu");
        backButton.SetPosX(backButton.width*0.5f);
        backButton.SetPosY(backButton.height*0.5f);

        Coinbmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.coin);
        moneyPaint.setColor(Color.YELLOW);
        moneyPaint.setAlpha(200);
        moneyPaint.setTextSize(50);
        moneyPaint.setTextAlign(Paint.Align.LEFT);
        moneyPaint.setFakeBoldText(true);

        bubbleButton = ParticleButtonSelectEntity.Create(ResourceManager.Instance.GetBitmap(R.drawable.buybutton),ResourceManager.Instance.GetBitmap(R.drawable.bubbleparticlebutton) ,100,GameSystem.Instance.GetScreenScale().y - 200*0.15f ,200,60);
        bubbleButton.locked = false;
        if (PlayerInfo.Instance.GetEffectType() == PARTICLETYPE.P_BUBBLE)
            bubbleButton.selected = true;
        bloodButton = ParticleButtonSelectEntity.Create(ResourceManager.Instance.GetBitmap(R.drawable.buybutton),ResourceManager.Instance.GetBitmap(R.drawable.bloodparticlebutton) ,bubbleButton.Pos.x + bubbleButton.GetWidth(),bubbleButton.Pos.y,200,60);
        if (PlayerInfo.Instance.GetEffectType() == PARTICLETYPE.P_BLOOD)
            bloodButton.selected = true;
        if (GameSystem.Instance.GetBoolFromSave("bloodeffect") == true)
            bloodButton.locked = false;
        fishButton = ParticleButtonSelectEntity.Create(ResourceManager.Instance.GetBitmap(R.drawable.buybutton),ResourceManager.Instance.GetBitmap(R.drawable.fishparticlebutton) ,bloodButton.Pos.x + bubbleButton.GetWidth(),bubbleButton.Pos.y,200,60);
        if (PlayerInfo.Instance.GetEffectType() == PARTICLETYPE.P_FISH)
            fishButton.selected = true;
        if (GameSystem.Instance.GetBoolFromSave("fisheffect") == true)
            fishButton.locked = false;
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
        if (bubbleButton.clicked == true)
        {
            bubbleButton.selected = true;
            bloodButton.selected = false;
            fishButton.selected = false;
            PlayerInfo.Instance.SetEffectType(PARTICLETYPE.P_BUBBLE);
            GameSystem.Instance.SaveEditBegin();
            GameSystem.Instance.SetIntInSave("effecttype",PARTICLETYPE.P_BUBBLE.ordinal());
            GameSystem.Instance.SaveEditEnd();
        }
        else if (bloodButton.clicked == true && bloodButton.locked == false)
        {
            bubbleButton.selected = false;
            bloodButton.selected = true;
            fishButton.selected = false;
            PlayerInfo.Instance.SetEffectType(PARTICLETYPE.P_BLOOD);
            GameSystem.Instance.SaveEditBegin();
            GameSystem.Instance.SetIntInSave("effecttype",PARTICLETYPE.P_BLOOD.ordinal());
            GameSystem.Instance.SaveEditEnd();
        }
        else if (fishButton.clicked == true && fishButton.locked == false)
        {
            bubbleButton.selected = false;
            bloodButton.selected = false;
            fishButton.selected = true;
            PlayerInfo.Instance.SetEffectType(PARTICLETYPE.P_FISH);

            GameSystem.Instance.SaveEditBegin();
            GameSystem.Instance.SetIntInSave("effecttype",PARTICLETYPE.P_FISH.ordinal());
            GameSystem.Instance.SaveEditEnd();
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
