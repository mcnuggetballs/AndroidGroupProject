package com.fishweeb.practical;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.SurfaceView;

public class ParticleButtonEntity implements EntityBase
{
    //Init any variables here
    protected static final String TAG = null;
    protected Bitmap bmp = null;
    protected Bitmap itembmp = null;
    protected Sprite spritesheet = null;
    boolean isDone = false;
    Vector2 Pos = new Vector2();
    protected int sheetRow,sheetCol;
    protected Vector2 velocity = new Vector2(0,0);
    protected float width,height,radius;
    protected String text;
    protected Paint paint = new Paint();
    protected Rect textBounds = new Rect();
    protected float textwidth,textheight;
    protected Typeface myfont;
    protected boolean pressed = false;
    protected boolean clicked = false;
    protected boolean border = false;
    protected boolean header = false;
    protected int defaultColor = Color.WHITE;
    protected int onclickColor = Color.YELLOW;
    protected int Cost;
    protected boolean bought = false;

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

    public void SetText(String _text) {text = _text;}

    public String GetText(){return text;}

    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here
        myfont = Typeface.createFromAsset(_view.getContext().getAssets(),"fonts/akashi.ttf");
        paint.setTypeface(myfont);

        if (!bought)
            text = String.valueOf(Cost);
        else
            text = "OWN";
        paint.getTextBounds(text,0,text.length(),textBounds);
        textwidth = textBounds.width();
        textheight = textBounds.height();

        if (width > height)
            radius = width*0.5f;
        else
            radius = height*0.5f;

    }

    public void OnClickFunction()
    {
        paint.setColor(onclickColor);
    }

    public void OffClickFunction()
    {
        paint.setColor(defaultColor);
    }

    @Override
    public void Update(float _dt)
    {
        if (!bought)
            text = String.valueOf(Cost);
        else
            text = "OWN";
        paint.getTextBounds(text,0,text.length(),textBounds);
        textwidth = textBounds.width();
        textheight = textBounds.height();

        if (width > height)
            radius = width*0.5f;
        else
            radius = height*0.5f;

        //Movement
        Pos.PlusEqual(velocity,_dt);

        if (spritesheet != null)
            spritesheet.Update(_dt);

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
        _canvas.drawBitmap(bmp,Pos.x-width*0.5f,Pos.y-height*0.5f,null);
        _canvas.drawBitmap(itembmp,Pos.x -width*0.5f,Pos.y - height*0.5f - width,null);
        _canvas.drawText(text,Pos.x-textwidth*0.5f,Pos.y+textheight*0.5f,paint);
    }

    public static ParticleButtonEntity Create(Bitmap _bmp,Bitmap _item, float xPos, float yPos,float buttonsize,  float textSize,int cost)
    {
        ParticleButtonEntity result = new ParticleButtonEntity();
        result.Pos.x = xPos;
        result.Pos.y = yPos;
        result.paint.setTextSize(textSize);
        result.paint.setColor(Color.WHITE);
        result.width = buttonsize;
        result.height = buttonsize*0.3f;
        result.SetBMP(_bmp);
        result.Cost = cost;
        result.itembmp = Bitmap.createScaledBitmap(_item,(int)result.width,(int)result.width,true);;
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

    public float GetWidth()
    {
        return width;
    }

    public float GetHeight()
    {
        return height;
    }

    public void SetHeader(boolean _input)
    {
        header = _input;
    }

    public void SetPaintColor(int _color)
    {
        paint.setColor(_color);
        defaultColor = _color;
        onclickColor = _color;
    }

    public void SetDefaultTextColor(int _color)
    {
        defaultColor = _color;
    }

    public void SetOnClickTextColor(int _color)
    {
        onclickColor = _color;
    }
}