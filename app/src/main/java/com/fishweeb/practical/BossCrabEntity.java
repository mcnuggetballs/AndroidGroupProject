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



public class BossCrabEntity extends EnemyEntity
{
    private boolean shot;
    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here
        SpawnManager.Instance.bossTime = true;
        health = 1000;
        sheetRow = 3;
        sheetCol = 17;
        SheetInfectedStart = 1; SheetInfectedEnd = 17;
        SheetHitStart = 18; SheetHitEnd = 34;
        SheetNormalStart = 35; SheetNormalEnd = 51;
        spritesheet = ImageManager.Instance.GetCrabSpriteSheet();
        spritesheet.SetAnimationFrames(1,2);

        damage = PlayerInfo.Instance.GetMaxHealth();
        BulletDamage = 3;
        score = 300;
        gold = 100;

        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;;

        width = spritesheet.GetWidth();
        height = spritesheet.GetHeight();

        if (width > height)
            radius = width*0.5f;
        else
            radius = height*0.5f;

        Pos.x = ScreenWidth*0.5f;
        Pos.y = -height;
        Vel.x = 0;
        Vel.y = 200;
        shot = false;
    }

    public void DieFunction(float _dt)
    {
        if (health <= 0)
        {
            if (!dead)
            {
                PlayerInfo.Instance.AddScore(score);
                SpawnGold();

                SpawnManager.Instance.bossTime = false;
            }

            dead = true;
            if (Pos.x > ScreenWidth*0.5f)
                Vel.x += 500*_dt;
            else
                Vel.x -= 500*_dt;
        }
    }

    @Override
    public void Contrain()
    {
        //Out Of Bounds
        if (!dead)
        {
            if (Pos.y > ScreenWidth * 0.2f)
            {
                spritesheet.ContinueAnimationFrames(SheetInfectedStart,SheetInfectedEnd);
                Vel.y = 0;
                Pos.y = ScreenWidth*0.2f;
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
            if (spritesheet.GetCurrentFrameColumn() == 16)
            {
                if (!shot)
                {
                    for (int i = 0; i < 9; ++i)
                    {
                        EnemyProjectile temp = EnemyProjectile.Create(BulletDamage, Pos.x, Pos.y + height*0.5f);
                        temp.velocity.x = 70 * (i - 4);
                    }
                    shot = true;
                }
            }
            else
            {
                shot = false;
            }
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

    public static BossCrabEntity Create()
    {
        BossCrabEntity result = new BossCrabEntity();
        EntityManager.Instance.AddEntity(result);
        return result;
    }
}