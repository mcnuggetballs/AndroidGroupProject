package com.fishweeb.practical;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.SeekBar;

public class VolumeBarEntity extends SeekBarEntity
{
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
            float xOffset2 = SliderPos.x-xStart;
            int value = (int)((xOffset2/width) * 100);
            SettingsManager.Instance.SetSoundVolume(value);
        }
    }

    public static VolumeBarEntity Create(float _xPos,float _yPos,float barWidth,float barHeight)
    {
        VolumeBarEntity result = new VolumeBarEntity();
        EntityManager.Instance.AddEntity(result);
        result.Pos.x = _xPos;
        result.Pos.y = _yPos;
        result.width = barWidth;
        result.height = barHeight;
        result.sliderWidth = barWidth*0.1f;
        result.sliderHeight = barHeight;
        result.xStart = result.Pos.x - barWidth*0.5f;
        result.textPaint.setColor(Color.BLACK);
        result.textPaint.setFakeBoldText(true);
        result.textPaint.setTextSize(barHeight);
        result.textPaint.getTextBounds("100",0,2,result.textBounds);

        result.SliderPos.x = result.xStart + (SettingsManager.Instance.GetSoundVolume()/100.f)*barWidth;
        result.SliderPos.y = _yPos;
        return result;
    }
}
