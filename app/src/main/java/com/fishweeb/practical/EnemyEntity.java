package com.fishweeb.practical;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.os.Build;

import java.util.Random;



public class EnemyEntity implements EntityBase, Collidable
{
	//Init any variables here
    protected static final String TAG = null;
    protected Bitmap bmp = null;
    protected Sprite spritesheet = null;
    boolean isDone = false;
    protected Vector2 Pos = new Vector2();
    protected int ScreenWidth,ScreenHeight;
    protected int sheetRow,sheetCol;
    protected int SheetInfectedStart,SheetInfectedEnd, SheetHitStart, SheetHitEnd,SheetNormalStart,SheetNormalEnd;
    protected Vector2 Vel = new Vector2();
    protected float width,height,radius;
    protected float health;
    protected boolean hit = false;
    protected float hitcounter = 0;
    protected float hitTime = 0.1f;
    protected boolean dead = false;
    protected float damage;
    protected float BulletDamage = 0;
    protected int score;
    protected int gold;

    protected int renderLayer = 0;



    @Override
    public boolean IsDone() {
        return isDone;
    }

    @Override
    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    @Override
    public void SetRenderLayer(int _newLayer)
    {
        renderLayer = _newLayer;
    }

    @Override
    public int GetRenderLayer()
    {
        return renderLayer;
    }

    @Override
    public void Init(SurfaceView _view) {
		// Define anything you need to use here
        spritesheet = new Sprite(bmp,sheetRow,sheetCol,12);
        spritesheet.SetAnimationFrames(SheetInfectedStart,SheetInfectedEnd);

        damage = 5;
        score = 10;
        gold = 3;

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


    public void SpawnGold()
    {
        for (int i = 0;i<gold;++i)
        {
            ParticleObject tempParticle = ParticleManager.Instance.FetchParticle(PARTICLETYPE.P_MONEY);
            tempParticle.position.x = Pos.x;
            tempParticle.position.y = Pos.y;
            tempParticle.width = 20;
            tempParticle.height = 20;
            tempParticle.velocity.x = (float)Math.random() * 1000 - 500;
            tempParticle.velocity.y = (float)Math.random() * 1000 - 500;
        }
    }

    public void SpawnBlood()
    {
        for (int i = 0;i<radius;++i)
        {
            ParticleObject tempParticle = ParticleManager.Instance.FetchParticle(PARTICLETYPE.P_BLOOD);
            tempParticle.position.x = Pos.x;
            tempParticle.position.y = Pos.y;
            tempParticle.width = 10;
            tempParticle.height = 10;
            tempParticle.velocity.x = (float)Math.random() * 500 - 250;
            tempParticle.velocity.y = (float)Math.random() * 500 - 250;
        }
    }

    public void SpawnPowerupStraight(float chanceOverHundred)
    {
        if ((float)Math.random() * 100 <= chanceOverHundred)
        {
            ParticleObject tempParticle = ParticleManager.Instance.FetchParticle(PARTICLETYPE.P_POWERUP_STRAIGHT);
            tempParticle.position.x = Pos.x;
            tempParticle.position.y = Pos.y;
            tempParticle.SetBMP(ImageManager.Instance.GetImage(IMAGE.I_POWERUP_STRAIGHT));
        }
    }

    public void SpawnPowerupSpread(float chanceOverHundred)
    {
        if ((float)Math.random() * 100 <= chanceOverHundred)
        {
            ParticleObject tempParticle = ParticleManager.Instance.FetchParticle(PARTICLETYPE.P_POWERUP_SPREAD);
            tempParticle.position.x = Pos.x;
            tempParticle.position.y = Pos.y;
            tempParticle.SetBMP(ImageManager.Instance.GetImage(IMAGE.I_POWERUP_SPREAD));
        }
    }

    public void DieFunction(float _dt)
    {
        if (health <= 0)
        {
            if (!dead)
            {
                //AudioManager.Instance.PlayAudio(R.raw.correct,2);
                PlayerInfo.Instance.AddScore(score);
                SpawnGold();

                if ((float)Math.random() * 100 <= 50)
                    SpawnPowerupStraight(5);
                else
                    SpawnPowerupSpread(5);
            }

            dead = true;
            if (Pos.x > ScreenWidth*0.5f)
                Vel.x += 500*_dt;
            else
                Vel.x -= 500*_dt;
        }
    }

    public boolean GetDead()
    {
        return dead;
    }

    public void Contrain()
    {
        //Out Of Bounds
        if (!dead)
        {
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

        //Constrains
        Contrain();

        if (Pos.y - height > ScreenHeight)
        {
            Log.v(TAG,"Out Of Bounds! *Deleted*");
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

    @Override
    public void Render(Canvas _canvas)
    {
        // Render anything

        //Rotation
        //Matrix transform = new Matrix();
        //transform.postTranslate(-bmp.getWidth()*0.5f,-bmp.getHeight()*0.5f);
        //transform.postRotate(100);
        //_canvas.drawBitmap(bmp,transform,null);

        spritesheet.Render(_canvas,(int)Pos.x,(int)Pos.y);
    }

    public static EnemyEntity Create(float health,Bitmap image,int sheetRow,int sheetCol)
    {
        EnemyEntity result = new EnemyEntity();

        result.health = 30;

        result.bmp = image;
        result.sheetRow = sheetRow;
        result.sheetCol = sheetCol;

        result.SheetInfectedStart = 1; result.SheetInfectedEnd = sheetCol;
        result.SheetHitStart = 1+(sheetCol*1); result.SheetHitEnd = sheetCol*2;
        result.SheetNormalStart = 1+(sheetCol*2); result.SheetNormalEnd = sheetCol*3;

        EntityManager.Instance.AddEntity(result);
        return result;
    }

    public static EnemyEntity Create(float health,Bitmap image,int sheetRow,int sheetCol,int _layer)
    {
        EnemyEntity result = Create(health,image,sheetRow,sheetCol);
        result.SetRenderLayer(_layer);
        return result;
    }

    @Override
    public String GetType() {
        return "EnemyEntity";
    }

    @Override
    public float GetPosX() {
        return Pos.x;
    }

    @Override
    public float GetPosY() {
        return Pos.y;
    }

    @Override
    public float GetRadius()
    {
        return radius;
    }

    @Override
    public float GetWidth()
    {
        return width;
    }

    @Override
    public float GetHeight()
    {
        return height;
    }

    @Override
    public float GetDamage()
    {
        return damage;
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
                SpawnBlood();
            }
        }
    }
}

