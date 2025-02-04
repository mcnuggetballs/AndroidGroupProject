package com.fishweeb.practical;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;
import android.widget.SeekBar;

public class SeekBarEntity implements EntityBase
{
    protected static final String TAG = null;
    protected Bitmap bmp = null;
    protected Bitmap sliderBmp = null;
    boolean isDone = false;
    Vector2 Pos = new Vector2();
    Vector2 SliderPos = new Vector2();
    protected float sliderWidth,sliderHeight;
    protected float width,height;
    protected float xStart;
    protected boolean dragging = false;
    protected Paint textPaint = new Paint();
    protected Rect textBounds = new Rect();
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
    }
    @Override
    public void Update(float _dt)
    {
        if (TouchManager.Instance.HasTouch())
        {
            if (Collision.AABBToAABB(Pos.x,Pos.y,width,height,TouchManager.Instance.GetPosX(),TouchManager.Instance.GetPosY(),1,1))
            {
                dragging = true;
            }
        }
        else
        {
            dragging =false;
        }

        if (dragging == true)
        {
            float xOffset = TouchManager.Instance.GetPosX()-xStart;
            if (xOffset > 0 && xOffset < width)
            SliderPos.x = TouchManager.Instance.GetPosX();
            else if (xOffset <= 0)
            {
                SliderPos.x = Pos.x-width*0.5f;
            }
            else if (xOffset >= width)
            {
                SliderPos.x = Pos.x+width*0.5f;
            }
        }
    }

    @Override
    public void Render(Canvas _canvas)
    {
        if (bmp == null)
        {
            _canvas.drawRect(Pos.x - width*0.5f,Pos.y - height*0.5f,Pos.x + width*0.5f,Pos.y + height*0.5f,PaintColor.Instance.GetPaint(Color.BLACK));
        }

        if (sliderBmp == null)
        {
            _canvas.drawRect(SliderPos.x - sliderWidth*0.5f,SliderPos.y - sliderHeight*0.5f,SliderPos.x + sliderWidth*0.5f,SliderPos.y + sliderHeight*0.5f,PaintColor.Instance.GetPaint(Color.BLUE));
        }

        float xOffset = SliderPos.x-xStart;
        int value = (int)((xOffset/width) * 100);
        _canvas.drawText(""+value,Pos.x+width*0.6f,Pos.y+textBounds.height()*0.5f,textPaint);
    }

    public static SeekBarEntity Create(float _xPos,float _yPos,float barWidth,float barHeight)
    {
        SeekBarEntity result = new SeekBarEntity();
        EntityManager.Instance.AddEntity(result);
        result.Pos.x = _xPos;
        result.Pos.y = _yPos;
        result.SliderPos.x = _xPos;
        result.SliderPos.y = _yPos;
        result.width = barWidth;
        result.height = barHeight;
        result.sliderWidth = barWidth*0.1f;
        result.sliderHeight = barHeight;
        result.xStart = result.Pos.x - barWidth*0.5f;
        result.textPaint.setColor(Color.BLACK);
        result.textPaint.setFakeBoldText(true);
        result.textPaint.setTextSize(barHeight);
        result.textPaint.getTextBounds("50",0,2,result.textBounds);
        return result;
    }

    public void SetPosY(float yPos){
        this.Pos.y = yPos;
    }

    public void SetPosX(float xPos){
        this.Pos.x = xPos;
    }
}
