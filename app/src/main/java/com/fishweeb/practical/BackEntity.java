package com.fishweeb.practical;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

public class BackEntity extends ButtonEntity
{
    private Bitmap bmpPress = null;
    private Bitmap bmpOriginal = null;
    private String nextState;
    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here
        bmpOriginal = Bitmap.createScaledBitmap(ImageManager.Instance.GetImage(IMAGE.I_BACKBUTTON),(int)width,(int)height,true);
        bmpPress = Bitmap.createScaledBitmap(ImageManager.Instance.GetImage(IMAGE.I_BACKBUTTON_PRESSED),(int)width,(int)height,true);
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
        StateManager.Instance.ChangeState(nextState);
    }

    public static BackEntity Create(float xPos,float yPos,float _width,float _height,String _nextState)
    {
        BackEntity result = new BackEntity();
        result.Pos.x = xPos;
        result.Pos.y = yPos;
        result.width = _width;
        result.height = _height;
        result.nextState = _nextState;
        EntityManager.Instance.AddEntity(result);
        return result;
    }
}
