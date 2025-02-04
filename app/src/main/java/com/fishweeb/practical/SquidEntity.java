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



public class SquidEntity extends EnemyEntity
{
    private float xJumpSpeed;
    private float yJumpSpeed;
    private float JumpTimer;
    private float Jumptime;
    private float Friction;
    private boolean Jumped;
    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here

        health = 200;
        sheetRow = 3;
        sheetCol = 25;
        SheetInfectedStart = 1; SheetInfectedEnd = 25;
        SheetHitStart = 26; SheetHitEnd = 50;
        SheetNormalStart = 51; SheetNormalEnd = 75;
        bmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_squid2);
        spritesheet = new Sprite(bmp,sheetRow,sheetCol,8);
        spritesheet.SetAnimationFrames(SheetInfectedStart,SheetInfectedEnd);

        damage = 3;
        score = 100;
        gold = 25;
        BulletDamage = 3;

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
        Jumped = false;
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
                    xJumpSpeed = 250;
                else
                    xJumpSpeed = -250;

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

        if (spritesheet.GetCurrentFrameColumn() == 1)
        {
            if (!Jumped)
            {
                Jumped = true;
                Vel.y = yJumpSpeed;
                Vel.x = xJumpSpeed;
                if (!dead)
                EnemyProjectile.Create(BulletDamage,Pos.x,Pos.y);
            }
        }
        else
        {
            Jumped = false;
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

    public static SquidEntity Create()
    {
        SquidEntity result = new SquidEntity();
        EntityManager.Instance.AddEntity(result);
        return result;
    }
}