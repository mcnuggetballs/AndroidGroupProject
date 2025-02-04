package com.fishweeb.practical;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.os.Build;

import java.util.Random;



public class LitterEntity extends EnemyEntity
{
    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here
        health = 80;
        sheetRow = 2;
        sheetCol = 19;
        SheetInfectedStart = 1; SheetInfectedEnd = 19;
        SheetHitStart = 20; SheetHitEnd = 38;
        if ((float)Math.random() * 100 > 50.f)
            bmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_toxic1);
        else
            bmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_toxic2);
        spritesheet = new Sprite(bmp,sheetRow,sheetCol,5);
        spritesheet.SetAnimationFrames(SheetInfectedStart,SheetInfectedEnd);

        damage = 30;
        score = 80;
        gold = 15;

        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;;

        width = spritesheet.GetWidth();
        height = spritesheet.GetHeight();

        if (width > height)
            radius = width*0.5f;
        else
            radius = height*0.5f;

        Pos.x =((float)Math.random() * (ScreenWidth - width)) + (width*0.5f);
        Pos.y = -height;
        Vel.x = (float)Math.random()*40-20;
        Vel.y = (float)Math.random()*160+80;
    }

    @Override
    public void DieFunction(float _dt)
    {
        if (health <= 0)
        {
            if (!dead)
            {
                PlayerInfo.Instance.AddScore(score);
                SpawnPowerupStraight(100);
                SpawnGold();
            }
            dead = true;
            SetIsDone(true);
        }
    }

    @Override
    public void Contrain()
    {
        //Out Of Bounds

            if (Pos.x - (width * 0.5f) < 0)
            {
                Pos.x = 0 + (width * 0.5f);
                Vel.x = -Vel.x;
            } else if (Pos.x + (width * 0.5f) > ScreenWidth)
            {
                Pos.x = ScreenWidth - (width * 0.5f);
                Vel.x = -Vel.x;
            }
    }

    @Override
    public void Update(float _dt)
    {
        //Check if dead
        DieFunction(_dt);

        //Movement
        Pos.PlusEqual(Vel,_dt);

        Contrain();

        if (Pos.y - height > ScreenHeight)
        {
            SetIsDone(true);
        }

        if (hit)
        {
            hitcounter -= _dt;
            if (hitcounter <= 0)
            {
                spritesheet.ContinueAnimationFrames(SheetInfectedStart,SheetInfectedEnd);
                hit = false;
            }
        }

        spritesheet.Update(_dt);
    }

    public static LitterEntity Create()
    {
        LitterEntity result = new LitterEntity();
        EntityManager.Instance.AddEntity(result);
        return result;
    }

    @Override
    public void OnHit(Collidable _other)
    {
        if (_other.GetType() == "Bullet")
        {
            health -= _other.GetDamage();
            if (!dead)
            {
                hit = true;
                hitcounter = hitTime;
                spritesheet.ContinueAnimationFrames(SheetHitStart, SheetHitEnd);
            }
        }
        else if (_other.GetType() == "PlayerEntity")
        {
            if (!dead)
            {
                SetIsDone(true);
            }
        }
    }
}

