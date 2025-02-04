package com.fishweeb.practical;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

public class DynamicButtonEntity extends ButtonEntity
{
    private Bitmap bmpPress = null;
    private Bitmap bmpOriginal = null;
    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here
        bmp = bmpOriginal;

        velocity.x = 0;
        velocity.y = 0;

        if (width > height)
            radius = width*0.5f;
        else
            radius = height*0.5f;
    }

    @Override
    public void OnClickFunction()
    {
        bmp = bmpPress;
    }

    @Override
    public void OffClickFunction()
    {
        bmp = bmpOriginal;
    }

    public static DynamicButtonEntity Create(float xPos,float yPos,float _width,float _height,Bitmap buttonImage,Bitmap buttonPressImage)
    {
        DynamicButtonEntity result = new DynamicButtonEntity();
        result.Pos.x = xPos;
        result.Pos.y = yPos;
        result.width = _width;
        result.height = _height;
        result.bmpOriginal = Bitmap.createScaledBitmap(buttonImage,(int)result.width,(int)result.height,true);
        result.bmpPress = Bitmap.createScaledBitmap(buttonPressImage,(int)result.width,(int)result.height,true);
        EntityManager.Instance.AddEntity(result);
        return result;
    }
}
