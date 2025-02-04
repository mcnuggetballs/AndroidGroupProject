package com.fishweeb.practical;

import android.graphics.Bitmap;
import android.view.SurfaceView;

public class SwitchButtonEntity extends DynamicButtonEntity
{
    private Bitmap bmpOff = null;
    private Bitmap bmpOnn = null;
    private SETTINGTYPE type;
    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here
        if (type == SETTINGTYPE.ST_FPSSHOW)
        {
            if (SettingsManager.Instance.GetFpsShow())
            {
                bmp = bmpOnn;
            } else
            {
                bmp = bmpOff;
            }
        }

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
    }

    @Override
    public void OffClickFunction()
    {
        if (type == SETTINGTYPE.ST_FPSSHOW)
        {
            SettingsManager.Instance.SetFpsShow(!SettingsManager.Instance.GetFpsShow());
            if (SettingsManager.Instance.GetFpsShow())
            {
                bmp = bmpOnn;
            } else
            {
                bmp = bmpOff;
            }
        }
    }

    public static SwitchButtonEntity Create(float xPos,float yPos,float _width,float _height,Bitmap buttonImageOff,Bitmap buttonImageOnn,SETTINGTYPE _settingtype)
    {
        SwitchButtonEntity result = new SwitchButtonEntity();
        result.Pos.x = xPos;
        result.Pos.y = yPos;
        result.width = _width;
        result.height = _height;
        result.bmpOff = Bitmap.createScaledBitmap(buttonImageOff,(int)result.width,(int)result.height,true);
        result.bmpOnn = Bitmap.createScaledBitmap(buttonImageOnn,(int)result.width,(int)result.height,true);
        result.type = _settingtype;
        EntityManager.Instance.AddEntity(result);
        return result;
    }
}
