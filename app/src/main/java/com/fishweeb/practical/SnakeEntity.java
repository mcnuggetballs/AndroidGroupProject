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



public class SnakeEntity extends EnemyEntity
{
    private boolean goingRight;

    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here
        goingRight = true;

        health = 40;
        sheetRow = 3;
        sheetCol = 3;
        SheetInfectedStart = 1; SheetInfectedEnd = 3;
        SheetHitStart = 4; SheetHitEnd = 6;
        SheetNormalStart = 7; SheetNormalEnd = 9;
        bmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_snake);
        spritesheet = new Sprite(bmp,sheetRow,sheetCol,12);
        spritesheet.SetAnimationFrames(SheetInfectedStart,SheetInfectedEnd);

        damage = 5;
        score = 30;
        gold = 13;

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
        Vel.x = (float)Math.random()*360-180;
        Vel.y = (float)Math.random()*360+180;
    }

    @Override
    public void Contrain()
    {
        //Out Of Bounds
        if (!dead)
        {
            if (Pos.x - (width * 0.5f) < 0)
            {
                Pos.x = 0 + (width * 0.5f);
                Vel.x = 0;
            } else if (Pos.x + (width * 0.5f) > ScreenWidth)
            {
                Pos.x = ScreenWidth - (width * 0.5f);
                Vel.x = 0;
            }
        }
        else
        {
            if (Pos.x + (width * 0.5f) < 0)
            {
                SetIsDone(true);
            } else if (Pos.x - (width * 0.5f) > ScreenWidth)
            {
                SetIsDone(true);
            }
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
            Vector2 direction = new Vector2();
            direction = (PlayerInfo.Instance.GetPos().Minus(Pos)).Normalized();
            Vel.x += direction.x * 300 * _dt;
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

    public static SnakeEntity Create()
    {
        SnakeEntity result = new SnakeEntity();
        EntityManager.Instance.AddEntity(result);
        return result;
    }
}