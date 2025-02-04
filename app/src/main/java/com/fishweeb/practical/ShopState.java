package com.fishweeb.practical;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;

public class ShopState implements StateBase
{
    private Bitmap background = null;
    private float bubbleSpawnTime = 2;
    private TextEntity headerText;
    private BackEntity backButton;
    private Bitmap Coinbmp;
    private Paint moneyPaint = new Paint();
    private Rect moneyTextBounds = new Rect();
    private HealthStatButton HealthStat;
    private DamageStatButton DamageStat;
    private FireAmountStatButton FireCountStat;
    private ParticleButtonEntity bubbleButton;
    private ParticleButtonEntity bloodButton;
    private ParticleButtonEntity fishButton;

    @Override
    public String GetName() {
        return "Shop";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
        background = Bitmap.createScaledBitmap(ImageManager.Instance.GetImage(IMAGE.I_MENUBACKGROUND),(int)GameSystem.Instance.GetScreenScale().x,(int)GameSystem.Instance.GetScreenScale().y,true);
        headerText = TextEntity.Create(GameSystem.Instance.ScreenScale.x*0.5f,0,"Shop",100);
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

        HealthStat = HealthStatButton.Create(GameSystem.Instance.GetScreenScale().x *0.5f,GameSystem.Instance.GetScreenScale().y *0.5f,(int)PlayerInfo.Instance.GetMaxHealth(),10,200,60,60);
        DamageStat = DamageStatButton.Create(GameSystem.Instance.GetScreenScale().x *0.5f,GameSystem.Instance.GetScreenScale().y *0.5f + HealthStat.GetHeight()*1,(int)PlayerInfo.Instance.GetMainWeapon().GetWeaponDamage(),5,100,60,60);
        FireCountStat = FireAmountStatButton.Create(GameSystem.Instance.GetScreenScale().x *0.5f,GameSystem.Instance.GetScreenScale().y *0.5f + HealthStat.GetHeight()*2,(int)PlayerInfo.Instance.GetMainWeapon().GetShootAmount(),1,500,60,60);
        HealthStat.SetPosX(GameSystem.Instance.ScreenScale.x *0.75f); HealthStat.SetPosY(GameSystem.Instance.GetScreenScale().y*0.2f - HealthStat.textHeight*0.5f);
        DamageStat.SetPosX(GameSystem.Instance.ScreenScale.x *0.75f); DamageStat.SetPosY(GameSystem.Instance.GetScreenScale().y*0.2f - HealthStat.textHeight*0.5f+ HealthStat.GetHeight()*1);
        FireCountStat.SetPosX(GameSystem.Instance.ScreenScale.x *0.75f); FireCountStat.SetPosY(GameSystem.Instance.GetScreenScale().y*0.2f - HealthStat.textHeight*0.5f+ HealthStat.GetHeight()*2);
        //shoot amount //fire rate //damage //speed
        bloodButton = ParticleButtonEntity.Create(ResourceManager.Instance.GetBitmap(R.drawable.buybutton),ResourceManager.Instance.GetBitmap(R.drawable.bloodparticlebutton) ,GameSystem.Instance.GetScreenScale().x *0.5f,GameSystem.Instance.GetScreenScale().y*0.8f,200,60,100);
        if (GameSystem.Instance.GetBoolFromSave("bloodeffect") == true)
            bloodButton.bought = true;
        bubbleButton = ParticleButtonEntity.Create(ResourceManager.Instance.GetBitmap(R.drawable.buybutton),ResourceManager.Instance.GetBitmap(R.drawable.bubbleparticlebutton) ,GameSystem.Instance.GetScreenScale().x *0.5f - bloodButton.width*1.5f,GameSystem.Instance.GetScreenScale().y*0.8f,200,60,0);
            bubbleButton.bought = true;

        fishButton = ParticleButtonEntity.Create(ResourceManager.Instance.GetBitmap(R.drawable.buybutton),ResourceManager.Instance.GetBitmap(R.drawable.fishparticlebutton) ,GameSystem.Instance.GetScreenScale().x *0.5f + bloodButton.width*1.5f,GameSystem.Instance.GetScreenScale().y*0.8f,200,60,100);
        if (GameSystem.Instance.GetBoolFromSave("fisheffect") == true)
            bloodButton.bought = true;
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

        _canvas.drawText("Ship HP - " + String.valueOf(PlayerInfo.Instance.GetMaxHealth()),GameSystem.Instance.ScreenScale.x *0.1f,GameSystem.Instance.GetScreenScale().y*0.2f,HealthStat.paint);
        _canvas.drawText("Damage - " + String.valueOf(PlayerInfo.Instance.GetMainWeapon().GetWeaponDamage()),GameSystem.Instance.ScreenScale.x *0.1f,GameSystem.Instance.GetScreenScale().y*0.2f + HealthStat.GetHeight()*1,HealthStat.paint);
        _canvas.drawText("Fire Count - " + String.valueOf(PlayerInfo.Instance.GetMainWeapon().GetShootAmount()),GameSystem.Instance.ScreenScale.x *0.1f,GameSystem.Instance.GetScreenScale().y*0.2f+ HealthStat.GetHeight()*2,HealthStat.paint);

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

        if (bloodButton.clicked == true)
        {
            if (!bloodButton.bought)
            {
                if (PlayerInfo.Instance.GetMoney() >= bloodButton.Cost)
                {
                    bloodButton.bought = true;
                    PlayerInfo.Instance.MinusMoney(bloodButton.Cost);
                    GameSystem.Instance.SaveEditBegin();
                    GameSystem.Instance.SetBoolInSave("bloodeffect",true);
                    GameSystem.Instance.SaveEditEnd();
                }
            }
        }

        if (fishButton.clicked == true)
        {
            if (!fishButton.bought)
            {
                if (PlayerInfo.Instance.GetMoney() >= fishButton.Cost)
                {
                    fishButton.bought = true;
                    PlayerInfo.Instance.MinusMoney(fishButton.Cost);
                    GameSystem.Instance.SaveEditBegin();
                    GameSystem.Instance.SetBoolInSave("fisheffect",true);
                    GameSystem.Instance.SaveEditEnd();
                }
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
