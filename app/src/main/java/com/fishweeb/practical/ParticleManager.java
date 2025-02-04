package com.fishweeb.practical;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import java.util.LinkedList;

public class ParticleManager
{
    public final static ParticleManager Instance = new ParticleManager();
    private LinkedList<ParticleObject> particleList = new LinkedList<ParticleObject>();
    private LinkedList<ParticleObject> particleQueue = new LinkedList<ParticleObject>();
    private int POWERUP_SPEED = 300;
    private int ScreenWidth;
    private int ScreenHeight;

    public ParticleObject FetchParticle(PARTICLETYPE type)
    {
        for (ParticleObject p : particleList)
        {
            if (p.active || p.type != type)
                continue;
            p.active = true;
            return p;
        }

        ParticleObject newParticle = new ParticleObject(type);
        newParticle.active = true;
        particleQueue.add(newParticle);

        return newParticle;
    }

    public void CreateParticle(PARTICLETYPE type,float x, float y)
    {
        switch (type)
        {
            case P_BUBBLE:
                ParticleObject newParticle = ParticleManager.Instance.FetchParticle(PARTICLETYPE.P_BUBBLE);
                int diameter = (int)(Math.random()*50 + 15);
                newParticle.width = diameter;
                newParticle.height = diameter;
                newParticle.position.x = x;
                newParticle.position.y = y;
                newParticle.velocity.x = (float)Math.random()*40-20;
                newParticle.velocity.y = -((float)Math.random()*160+80);
                newParticle.SetBMP(Bitmap.createScaledBitmap(ImageManager.Instance.GetImage(IMAGE.I_BUBBLE),diameter,diameter,true));
                break;
            case P_BLOOD:
                ParticleObject tempParticle = ParticleManager.Instance.FetchParticle(PARTICLETYPE.P_BLOOD);
                tempParticle.position.x = x;
                tempParticle.position.y = y;
                tempParticle.width = 20;
                tempParticle.height = 20;
                tempParticle.velocity.x = (float)Math.random() * 500 - 250;
                tempParticle.velocity.y = (float)Math.random() * 500 - 250;
                break;
            case P_FISH:
                ParticleObject tempParticle2 = ParticleManager.Instance.FetchParticle(PARTICLETYPE.P_FISH);
                tempParticle2.position.x = x;
                tempParticle2.position.y = y;
                tempParticle2.velocity.x = (float)Math.random() * 500 - 250;
                tempParticle2.velocity.y = (float)Math.random() * 500 - 250;
                tempParticle2.SetBMP(ResourceManager.Instance.GetBitmap(R.drawable.sprite_fish));
                break;
            default:
                break;
        }
    }

    public void Init(int _ScreenWidth,int _ScreenHeight)
    {
        ScreenWidth = _ScreenWidth;
        ScreenHeight = _ScreenHeight;

        for (int i = 0;i<PARTICLETYPE.P_NUM.GetSize();++i)
        {
            for (int i2 = 0;i2<100;++i2)
            {
                particleList.add(new ParticleObject(PARTICLETYPE.values()[i]));
            }
        }
    }

    public void LimitVelocity(ParticleObject p, float _limit)
    {
        if (p.velocity.x > _limit)
            p.velocity.x = _limit;
        else if (p.velocity.x < -_limit)
            p.velocity.x = -_limit;

        if (p.velocity.y > _limit)
            p.velocity.y = _limit;
        else if (p.velocity.y < -_limit)
            p.velocity.y = -_limit;
    }

    public void DeleteOutOfBounds(ParticleObject p)
    {
        if (p.position.x + (p.width * 0.5f) < 0)
        {
            p.active = false;
        }
        else if (p.position.x - (p.width * 0.5f) > ScreenWidth)
        {
            p.active = false;
        }

        if (p.position.y + (p.height * 0.5f) < 0)
        {
            p.active = false;
        }
        else if (p.position.y - (p.height * 0.5f) > ScreenHeight)
        {
            p.active = false;
        }
    }

    public void Update(float _dt)
    {
        for (ParticleObject newParticle : particleQueue)
        {
            particleList.add(newParticle);
        }
        particleQueue.clear();

        for (ParticleObject p : particleList)
        {
            if (!p.active)
                continue;

            p.position.PlusEqual(p.velocity, _dt);

            if (p.type == PARTICLETYPE.P_MONEY)
            {
                p.target = (PlayerInfo.Instance.GetPos().Minus(p.position)).Normalized();
                float DistanceFromPlayer = p.position.DistanceSquaredFrom(PlayerInfo.Instance.GetPos());
                if (DistanceFromPlayer <= 1)
                    DistanceFromPlayer = 1;
                p.velocity.PlusEqual(p.target.Times(100000000 / DistanceFromPlayer + 10000), _dt);

                LimitVelocity(p, 1000);

                if (Collision.SphereToSphere(p.position.x, p.position.y, 0, PlayerInfo.Instance.GetPos().x, PlayerInfo.Instance.GetPos().y, 30))
                {
                    p.active = false;
                    PlayerInfo.Instance.AddMoney(1);
                }
            }
            else if (p.type == PARTICLETYPE.P_BLOOD)
            {
                float friction = 900;

                if (p.velocity.x > friction * _dt)
                    p.velocity.x -= friction * _dt;
                else if (p.velocity.x < -friction * _dt)
                    p.velocity.x += friction * _dt;

                if (p.velocity.y > friction * _dt)
                    p.velocity.y -= friction * _dt;
                else if (p.velocity.y < -friction * _dt)
                    p.velocity.y += friction * _dt;

                if (p.paint == null)
                {
                    p.paint = new Paint();
                    p.paint.setColor(Color.RED);
                    p.paint.setAlpha(255);
                    p.timer = 0;
                }
                p.timer += _dt;
                if (p.timer > 0.4)
                {
                    p.timer = 0;
                    p.paint.setAlpha(p.paint.getAlpha() - 30);
                }
                if (p.paint.getAlpha() < 30)
                {
                    p.active = false;
                    p.paint.setAlpha(255);
                }
            }
            else if (p.type == PARTICLETYPE.P_POWERUP_STRAIGHT)
            {
                p.velocity.y = POWERUP_SPEED;
                if (Collision.SphereToSphere(p.position.x, p.position.y, 0, PlayerInfo.Instance.GetPos().x, PlayerInfo.Instance.GetPos().y, p.width))
                {
                    p.active = false;
                    PlayerInfo.Instance.GetMainWeapon().AddShootAmount(SHOOTTYPE.S_STRAIGHT);
                }
                DeleteOutOfBounds(p);
            }
            else if (p.type == PARTICLETYPE.P_POWERUP_SPREAD)
            {
                p.velocity.y = POWERUP_SPEED;
                if (Collision.SphereToSphere(p.position.x, p.position.y, 0, PlayerInfo.Instance.GetPos().x, PlayerInfo.Instance.GetPos().y, p.width))
                {
                    p.active = false;
                    PlayerInfo.Instance.GetMainWeapon().AddShootAmount(SHOOTTYPE.S_SPREAD);
                }
                DeleteOutOfBounds(p);
            }
            else if (p.type == PARTICLETYPE.P_BUBBLE)
            {
                p.position.PlusEqual(p.velocity,_dt);

                if (p.position.x + (p.width * 0.5f) < 0)
                {
                    p.active = false;
                }
                else if (p.position.x - (p.width * 0.5f) > ScreenWidth)
                {
                    p.active = false;
                }
                if (p.position.y + (p.height * 0.5f) < 0)
                {
                    p.active = false;
                }
            }
            else if (p.type == PARTICLETYPE.P_FISH)
            {
                p.position.PlusEqual(p.velocity,_dt);

                if (p.position.x + (p.width * 0.5f) < 0)
                {
                    p.active = false;
                }
                else if (p.position.x - (p.width * 0.5f) > ScreenWidth)
                {
                    p.active = false;
                }
                if (p.position.y + (p.height * 0.5f) < 0)
                {
                    p.active = false;
                }
            }
        }
    }

    public void Render(Canvas _canvas)
    {
        for (ParticleObject p : particleList)
        {
            if (!p.active)
                continue;

            if (p.type == PARTICLETYPE.P_MONEY)
                _canvas.drawOval(p.position.x - p.width*0.5f,p.position.y - p.height*0.5f,p.position.x + p.width*0.5f,p.position.y + p.height*0.5f,PaintColor.Instance.GetPaint(Color.YELLOW));
            else if (p.type == PARTICLETYPE.P_POWERUP_STRAIGHT)
                _canvas.drawBitmap(p.GetBMP(),p.position.x-(p.width*0.5f),p.position.y-(p.height*0.5f),null);
            else if (p.type == PARTICLETYPE.P_POWERUP_SPREAD)
                _canvas.drawBitmap(p.GetBMP(),p.position.x-(p.width*0.5f),p.position.y-(p.height*0.5f),null);
            else if (p.type == PARTICLETYPE.P_BLOOD)
                _canvas.drawOval(p.position.x - p.width*0.5f,p.position.y - p.height*0.5f,p.position.x + p.width*0.5f,p.position.y + p.height*0.5f,p.paint);
            else if (p.type == PARTICLETYPE.P_BUBBLE)
                _canvas.drawBitmap(p.GetBMP(),p.position.x - p.width*0.5f,p.position.y - p.height*0.5f,null);
            else if (p.type == PARTICLETYPE.P_FISH)
            {
                Matrix transform = new Matrix();
                Vector2 dir = p.velocity.Normalized();
                float rotation = (float)Math.atan2(dir.y,dir.x) * 180/3.142f - 90.0f;
                transform.postRotate(rotation);
                transform.postTranslate(p.position.x,p.position.y);
                _canvas.drawBitmap(p.GetBMP(),transform,null);
            }
        }
    }

    public void RemoveParticles()
    {
        particleList.clear();
    }
}