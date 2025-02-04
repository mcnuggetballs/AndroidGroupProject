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

public class ButtonEntity implements EntityBase
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
    protected boolean pressed = false;
    protected boolean clicked = false;

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
        return LayerConstants.BUTTON_LAYER;
    }

    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here

        // sheetRow = 1;
        // sheetCol = 3;
        // spritesheet = new Sprite(BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_ray),sheetRow,sheetCol,12);

        //bmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.bullet);

        //width = spritesheet.GetWidth();
        //height = spritesheet.GetHeight();

        // = bmp.getWidth();
        //height = bmp.getHeight();

        width = 30;
        height = 30;

        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        velocity.x = 0;
        velocity.y = 0;

        if (width > height)
            radius = width*0.5f;
        else
            radius = height*0.5f;
    }

    public void OnClickFunction()
    {

    }

    public void OffClickFunction()
    {

    }

    @Override
    public void Update(float _dt)
    {
        //Movement
        Pos.PlusEqual(velocity,_dt);

        if (spritesheet != null)
            spritesheet.Update(_dt);

        clicked = false;
        if (TouchManager.Instance.IsDown())
        {
            if (Collision.SphereToSphere(TouchManager.Instance.GetPosX(),TouchManager.Instance.GetPosY(),0,Pos.x,Pos.y,radius))
            {
                if (pressed == false)
                {
                    clicked = true;
                    OnClickFunction();
                    pressed = true;
                }
            }
        }
        else
        {
            if (pressed == true)
            {
                OffClickFunction();
                pressed = false;
            }
        }
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
            _canvas.drawRect(Pos.x - width*0.5f,Pos.y - height*0.5f,Pos.x + width*0.5f,Pos.y + height*0.5f,PaintColor.Instance.GetPaint(Color.BLUE));
    }

    public static ButtonEntity Create(float xPos,float yPos)
    {
        ButtonEntity result = new ButtonEntity();
        result.Pos.x = xPos;
        result.Pos.y = yPos;
        EntityManager.Instance.AddEntity(result);
        return result;
    }

    public void SetPosY(float yPos){
        this.Pos.y = yPos;
    }

    public void SetPosX(float xPos){
        this.Pos.x = xPos;
    }

    public void SetVelX(float xVel){this.velocity.x = xVel;}

    public void SetVelY(float yVel){this.velocity.y = yVel;}
}

