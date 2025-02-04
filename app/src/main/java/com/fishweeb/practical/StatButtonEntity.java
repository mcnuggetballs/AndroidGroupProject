package com.fishweeb.practical;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.style.BulletSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.os.Build;

import java.util.Random;

public class StatButtonEntity implements EntityBase
{
    //Init any variables here
    protected static final String TAG = null;
    protected Bitmap bmp = null;
    boolean isDone = false;
    Vector2 Pos = new Vector2();
    protected float width,height,radius;
    protected Paint paint = new Paint();
    protected Rect textBounds = new Rect();
    protected int textWidth;
    protected int textHeight;
    protected Typeface myfont;
    protected boolean pressed = false;
    protected boolean clicked = false;

    protected int Value;
    protected int AddValue;
    protected int Cost;
    protected int CurrentCost;
    protected String CostText;
    protected Bitmap coinBitMap;

    public void SetBMP(Bitmap _bmp)
    {
        bmp = Bitmap.createScaledBitmap(_bmp,(int)width,(int)height,true);
    }

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
        return LayerConstants.UI_LAYER;
    }

    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here
        myfont = Typeface.createFromAsset(_view.getContext().getAssets(),"fonts/akashi.ttf");
        paint.setTypeface(myfont);

        width = bmp.getWidth();
        height = bmp.getHeight();

        if (width > height)
            radius = width*0.5f;
        else
            radius = height*0.5f;


        CostText = String.valueOf(CurrentCost);
        paint.getTextBounds(CostText,0,CostText.length(),textBounds);
        textWidth = textBounds.width();
        textHeight = textBounds.height();

        CurrentCost = Cost * (Value/AddValue);
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
        CostText = String.valueOf(CurrentCost);
        paint.getTextBounds(CostText,0,CostText.length(),textBounds);
        textWidth = textBounds.width();
        textHeight = textBounds.height();
        CurrentCost = Cost * (Value/AddValue);

        clicked = false;
        if (TouchManager.Instance.IsDown())
        {
            if (Collision.AABBToAABB(Pos.x,Pos.y,width,height,TouchManager.Instance.GetPosX(),TouchManager.Instance.GetPosY(),1,1))
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
        if (bmp != null)
            _canvas.drawBitmap(bmp,Pos.x-width*0.5f,Pos.y-height*0.5f,null);

        _canvas.drawText(CostText,Pos.x+width*0.5f,Pos.y+textHeight*0.5f,paint);

        _canvas.drawBitmap(coinBitMap,Pos.x+width*0.5f+textWidth,Pos.y-height*0.5f,null);
    }

    public static StatButtonEntity Create(float xPos,float yPos,int value,int addvalue,int cost,float buttonSize,float textSize)
    {
        StatButtonEntity result = new StatButtonEntity();
        result.Pos.x = xPos;
        result.Pos.y = yPos;
        result.bmp = Bitmap.createScaledBitmap(ResourceManager.Instance.GetBitmap(R.drawable.plusbutton),(int)buttonSize,(int)buttonSize,true);
        result.coinBitMap = Bitmap.createScaledBitmap(ResourceManager.Instance.GetBitmap(R.drawable.coin),(int)buttonSize,(int)buttonSize,true);
        result.Value =value;
        result.AddValue = addvalue;
        result.Cost = cost;
        result.paint.setColor(Color.BLACK);
        result.paint.setTextSize(textSize);
        EntityManager.Instance.AddEntity(result);
        return result;
    }

    public void SetPosY(float yPos){
        this.Pos.y = yPos;
    }

    public void SetPosX(float xPos){
        this.Pos.x = xPos;
    }

    public float GetWidth()
    {
        return width;
    }

    public float GetHeight()
    {
        return height;
    }
}