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



public class FrogEntity extends EnemyEntity
{
    private float xJumpSpeed;
    private float yJumpSpeed;
    private float JumpTimer;
    private float Jumptime;
    private float Friction;
    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here

        health = 50;
        sheetRow = 3;
        sheetCol = 3;
        SheetInfectedStart = 1; SheetInfectedEnd = 3;
        SheetHitStart = 4; SheetHitEnd = 6;
        SheetNormalStart = 7; SheetNormalEnd = 9;
        bmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_frog);
        spritesheet = new Sprite(bmp,sheetRow,sheetCol,12);
        spritesheet.SetAnimationFrames(SheetInfectedStart,SheetInfectedEnd);

        damage = 8;
        score = 15;
        gold = 5;

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
        xJumpSpeed = (float)Math.random()*160-80;
        yJumpSpeed = (float)Math.random()*260+130;
        Vel.x = 0;
        Vel.y = 0;
        Jumptime = 2.0f;
        JumpTimer = Jumptime;
        Friction = yJumpSpeed *0.5f;
    }

    @Override
    public void DieFunction(float _dt)
    {
        if (health <= 0)
        {
            if (!dead)
            {
                PlayerInfo.Instance.AddScore(score);
                if ((float)Math.random() * 100 <= 50)
                    SpawnPowerupStraight(10);
                else
                    SpawnPowerupSpread(10);

                SpawnGold();
                if (Pos.x > ScreenWidth*0.5f)
                    xJumpSpeed = 500;
                else
                    xJumpSpeed = -500;

            }

            dead = true;

        }
    }

    @Override
    public void Update(float _dt)
    {
        //Check if dead
        DieFunction(_dt);

        //Movement
        Pos.PlusEqual(Vel,_dt);

        if (Vel.y > Friction * _dt)
        {
            Vel.y -= Friction* _dt;
        }
        else if (Vel.y < -Friction* _dt)
        {
            Vel.y += Friction * _dt;
        }
        else
        {
            Vel.y = 0;
        }

        if (Vel.x > Friction * _dt)
        {
            Vel.x -= Friction* _dt;
        }
        else if (Vel.x < -Friction* _dt)
        {
            Vel.x += Friction * _dt;
        }
        else
        {
            Vel.x = 0;
        }

        JumpTimer -= _dt;
        if (JumpTimer<=0)
        {
            Vel.y = yJumpSpeed;
            Vel.x = xJumpSpeed;
            JumpTimer = Jumptime;
        }

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
                if (dead)
                    spritesheet.ContinueAnimationFrames(SheetNormalStart,SheetNormalEnd);
                else
                    spritesheet.ContinueAnimationFrames(SheetInfectedStart,SheetInfectedEnd);

                hit = false;
            }
        }

        spritesheet.Update(_dt);
    }

    public static FrogEntity Create()
    {
        FrogEntity result = new FrogEntity();
        EntityManager.Instance.AddEntity(result);
        return result;
    }
}