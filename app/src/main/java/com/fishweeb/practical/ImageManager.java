package com.fishweeb.practical;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.SurfaceView;

enum IMAGE
{
    I_POWERUP_SPREAD(0),
    I_POWERUP_STRAIGHT(1),
    I_MENUBACKGROUND(2),
    I_LOGO(3),
    I_BUBBLE(4),
    I_BACKBUTTON(5),
    I_BACKBUTTON_PRESSED(6),
    I_BUTTON_ONN(7),
    I_BUTTON_OFF(8),
    I_NUM(9);
    private int value;

    IMAGE(int _value)
    {
        value = _value;
    }

    int GetValue()
    {
        return value;
    }
}

public class ImageManager
{
    static ImageManager Instance = new ImageManager();
    private Bitmap[] Images;
    private Sprite crabTest;

    public ImageManager()
    {
        Images = new Bitmap[IMAGE.I_NUM.GetValue()];
    }

    public void Init(SurfaceView _view)
    {
        Images[IMAGE.I_POWERUP_SPREAD.GetValue()] = BitmapFactory.decodeResource(_view.getResources(),R.drawable.powerup_duplicate_spread);
        Images[IMAGE.I_POWERUP_STRAIGHT.GetValue()] = BitmapFactory.decodeResource(_view.getResources(),R.drawable.powerup_duplicate_straight);
        Images[IMAGE.I_MENUBACKGROUND.GetValue()] = BitmapFactory.decodeResource(_view.getResources(),R.drawable.backgnd3);
        Images[IMAGE.I_LOGO.GetValue()] = BitmapFactory.decodeResource(_view.getResources(),R.drawable.logo);
        Images[IMAGE.I_BUBBLE.GetValue()] = BitmapFactory.decodeResource(_view.getResources(),R.drawable.bubble);
        Images[IMAGE.I_BACKBUTTON.GetValue()] = BitmapFactory.decodeResource(_view.getResources(),R.drawable.back_normal);
        Images[IMAGE.I_BACKBUTTON_PRESSED.GetValue()] = BitmapFactory.decodeResource(_view.getResources(),R.drawable.back_pressed);
        Images[IMAGE.I_BUTTON_OFF.GetValue()] = BitmapFactory.decodeResource(_view.getResources(),R.drawable.offswitch);
        Images[IMAGE.I_BUTTON_ONN.GetValue()] = BitmapFactory.decodeResource(_view.getResources(),R.drawable.onnswitch);

        int sheetRow = 3;
        int sheetCol = 17;
        crabTest = new Sprite(BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_crab),sheetRow,sheetCol,2);
    }

    public Bitmap GetImage(IMAGE name)
    {
        return Images[name.GetValue()];
    }

    public Sprite GetCrabSpriteSheet()
    {
        return crabTest;
    }
}
