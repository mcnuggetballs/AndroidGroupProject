package com.fishweeb.practical;

import android.graphics.Color;
import android.graphics.Paint;

public class PaintColor
{
    static PaintColor Instance = new PaintColor();

    private Paint Red;
    private Paint Blue;
    private Paint Black;
    private Paint Yellow;

    private PaintColor()
    {
        Red = new Paint();
        Red.setColor(Color.RED);

        Blue = new Paint();
        Blue.setColor(Color.BLUE);

        Black = new Paint();
        Black.setColor(Color.BLACK);

        Yellow = new Paint();
        Yellow.setColor(Color.YELLOW);
    }

    public Paint GetPaint(int _color)
    {
        switch (_color)
        {
            case Color.RED:
                return Red;
            case Color.BLUE:
                return Blue;
            case Color.BLACK:
                return Black;
            case Color.YELLOW:
                return Yellow;
            default:
                return null;
        }
    }
}
