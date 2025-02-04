package com.fishweeb.practical;

import android.view.MotionEvent;

public class TouchManager
{
    public final static TouchManager Instance = new TouchManager();

    private TouchManager()
    {
        //NOTHING
    }

    public enum TouchState
    {
        NONE,
        DOWN,
        MOVE
    }

    private TouchState status = TouchState.NONE;
    private int posX, posY;

    public boolean HasTouch()
    {
        return status == TouchState.DOWN || status == TouchState.MOVE;
    }

    public boolean IsDown()
    {
        return status == TouchState.DOWN;
    }

    public int GetPosX()
    {
        return posX;
    }

    public int GetPosY()
    {
        return posY;
    }

    public void Update(int  _posX, int _posY, int _motionEventStatus)
    {
        posX = _posX;
        posY = _posY;

        switch (_motionEventStatus)
        {
            case MotionEvent.ACTION_DOWN:
                status = TouchState.DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                status = TouchState.MOVE;
                break;

            case MotionEvent.ACTION_UP:
                status = TouchState.NONE;
                break;
        }
    }
}
