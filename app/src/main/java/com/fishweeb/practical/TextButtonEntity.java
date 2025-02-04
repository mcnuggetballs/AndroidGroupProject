package com.fishweeb.practical;

import android.graphics.Color;

public class TextButtonEntity extends TextEntity
{
    protected String nextState;


    @Override
    public void OnClickFunction()
    {
        paint.setColor(onclickColor);
        StateManager.Instance.ChangeState(nextState);
    }

    @Override
    public void OffClickFunction()
    {
        paint.setColor(defaultColor);
    }

    public static TextButtonEntity Create(float xPos,float yPos,String text,float textSize,String _nextState)
    {
        TextButtonEntity result = new TextButtonEntity();
        result.Pos.x = xPos;
        result.Pos.y = yPos;
        result.SetText(text);
        result.nextState = _nextState;
        result.paint.setTextSize(textSize);
        EntityManager.Instance.AddEntity(result);
        return result;
    }
}
