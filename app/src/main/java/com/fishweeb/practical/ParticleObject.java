package com.fishweeb.practical;

import android.graphics.Bitmap;
import android.graphics.Paint;

enum PARTICLETYPE
{
    P_MONEY,
    P_BLOOD,
    P_POWERUP_STRAIGHT,
    P_POWERUP_SPREAD,
    P_BUBBLE,
    P_FISH,
    P_NUM;
    private static final int size = PARTICLETYPE.values().length;

    public int GetSize()
    {
        return size;
    }
}
public class ParticleObject
{
    private Bitmap bmp;
    public PARTICLETYPE type;
    public boolean active;
    public Vector2 position = new Vector2();
    public Vector2 velocity = new Vector2();
    public Vector2 target = new Vector2();
    public float width;
    public float height;
    public float timer;
    public Paint paint = null;

    public ParticleObject(PARTICLETYPE type)
    {
        this.type = type;
        active = false;
        width = 0;
        height = 0;
        timer = 0;
    }

    public void SetBMP(Bitmap _bmp)
    {
        bmp = _bmp;
        width = _bmp.getWidth();
        height = _bmp.getHeight();
    }

    public Bitmap GetBMP()
    {
        return bmp;
    }
}
