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



public class MantisEntity extends EnemyEntity
{
    private boolean goingRight;

    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here
        goingRight = true;

        health = 50;
        sheetRow = 3;
        sheetCol = 3;
        SheetInfectedStart = 1; SheetInfectedEnd = 3;
        SheetHitStart = 4; SheetHitEnd = 6;
        SheetNormalStart = 7; SheetNormalEnd = 9;
        bmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_ray);
        spritesheet = new Sprite(bmp,sheetRow,sheetCol,12);
        spritesheet.SetAnimationFrames(SheetInfectedStart,SheetInfectedEnd);

        damage = 10;
        score = 45;
        gold = 10;

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
        Vel.x = (float)Math.random()*160-80;
        Vel.y = (float)Math.random()*160+80;
    }

    @Override
    public void Contrain()
    {
        //Out Of Bounds
        if (!dead)
        {
            if (Pos.x - (width * 0.5f) < 0)
            {
                goingRight = true;
                Pos.x = 0 + (width * 0.5f);
                Vel.x = 0;
            } else if (Pos.x + (width * 0.5f) > ScreenWidth)
            {
                goingRight = false;
                Pos.x = ScreenWidth - (width * 0.5f);
                Vel.x = 0;
            }
        }
        else
        {
            if (Pos.x + (width * 0.5f) < 0)
            {
                SetIsDone(true);
                Log.v(TAG,"Out Of Bounds! *Deleted*");
            } else if (Pos.x - (width * 0.5f) > ScreenWidth)
            {
                SetIsDone(true);
                Log.v(TAG,"Out Of Bounds! *Deleted*");
            }
        }
    }

    public void DieFunction(float _dt)
    {
        if (health <= 0)
        {
            if (!dead)
            {
                PlayerInfo.Instance.AddScore(score);
                if ((float)Math.random() * 100 <= 50)
                    SpawnPowerupStraight(20);
                else
                    SpawnPowerupSpread(20);
                SpawnGold();
            }

            dead = true;
            if (Pos.x > ScreenWidth*0.5f)
                Vel.x += 500*_dt;
            else
                Vel.x -= 500*_dt;
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

        if (!dead)
        {
            if (goingRight)
            {
                Vel.x += _dt * 500;
                if (Vel.x > 300)
                    goingRight = false;

            } else
            {
                Vel.x -= _dt * 500;
                if (Vel.x < -300)
                    goingRight = true;

            }
        }


        if (Pos.y - height > ScreenHeight)
        {
            SetIsDone(true);
        }

        if (hit)
        {
            hitcounter -= _dt;
            if (hitcounter <= 0)
            {
                if (dead)
                    spritesheet.ContinueAnimationFrames(SheetNormalStart,SheetNormalEnd);
                else
                    spritesheet.ContinueAnimationFrames(SheetInfectedStart,SheetInfectedEnd);

                hit = false;
            }
        }

        spritesheet.Update(_dt);
    }

    public static MantisEntity Create()
    {
        MantisEntity result = new MantisEntity();
        EntityManager.Instance.AddEntity(result);
        return result;
    }
}

