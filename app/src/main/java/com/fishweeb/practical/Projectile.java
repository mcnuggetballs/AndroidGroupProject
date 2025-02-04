package com.fishweeb.practical;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.BulletSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.os.Build;

import java.util.Random;

public class Projectile implements EntityBase, Collidable
{
    //Init any variables here
    protected static final String TAG = null;
    protected Bitmap bmp = null;
    protected Sprite spritesheet = null;
    boolean isDone = false;
    Vector2 Pos = new Vector2();
    protected int ScreenWidth,ScreenHeight;
    protected int sheetRow,sheetCol;
    protected Vector2 velocity = new Vector2();
    protected float width,height,radius;
    protected float Damage;

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
        return;
    }

    @Override
    public int GetRenderLayer()
    {
        return LayerConstants.GAMEOBJECTS_LAYER;
    }

    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here

        // sheetRow = 1;
        // sheetCol = 3;
        // spritesheet = new Sprite(BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_ray),sheetRow,sheetCol,12);

        bmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.bullet);

        //width = spritesheet.GetWidth();
        //height = spritesheet.GetHeight();

        width = bmp.getWidth();
        height = bmp.getHeight();

        //width = 30;
        //height = 30;

        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;
        velocity.y = -1600;

        if (width > height)
            radius = width*0.5f;
        else
            radius = height*0.5f;
    }

    public void Constrain()
    {
        //out of bounds
        if (Pos.y - (height*0.5f) < 0)
        {
            SetIsDone(true);
        }

        if (Pos.y + (height*0.5f) > ScreenHeight)
        {
            SetIsDone(true);
        }
    }

    @Override
    public void Update(float _dt)
    {
        //Movement
        Pos.PlusEqual(velocity,_dt);

        //Out Of Bounds
        Constrain();

        if (spritesheet != null)
        spritesheet.Update(_dt);
    }

    @Override
    public void Render(Canvas _canvas)
    {
        // Render anything
        if (spritesheet != null)
            spritesheet.Render(_canvas,(int)Pos.x,(int)Pos.y);
        else if (bmp != null)
            _canvas.drawBitmap(bmp,Pos.x-(width*0.5f),Pos.y-(height*0.5f),null);
        else
            _canvas.drawOval(Pos.x - width*0.5f,Pos.y - height*0.5f,Pos.x + width*0.5f,Pos.y + height*0.5f,PaintColor.Instance.GetPaint(Color.BLUE));
    }

    public static Projectile Create(float Damage,float xPos,float yPos)
    {
        Projectile result = new Projectile();
        result.Pos.x = xPos;
        result.Pos.y = yPos;
        result.Damage = Damage;
        EntityManager.Instance.AddEntity(result);
        return result;
    }

    @Override
    public String GetType() {
        return "Bullet";
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

    public float GetDamage(){return Damage;}

    public void SetPosY(float yPos){
        this.Pos.y = yPos;
    }

    public void SetPosX(float xPos){
        this.Pos.x = xPos;
    }

    public void SetVelX(float xVel){this.velocity.x = xVel;}

    public void SetVelY(float yVel){this.velocity.y = yVel;}

    @Override
    public void OnHit(Collidable _other)
    {
        if (_other.GetType() == "EnemyEntity")
        {
            if (!_other.GetDead())
            SetIsDone(true);
        }
    }

    @Override
    public boolean GetDead()
    {
        return IsDone();
    }
}

