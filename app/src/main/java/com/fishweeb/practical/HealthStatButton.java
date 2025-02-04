package com.fishweeb.practical;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.style.BulletSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.os.Build;

import java.util.Random;

public class HealthStatButton extends StatButtonEntity
{
    public void OnClickFunction()
    {
        if (PlayerInfo.Instance.GetMoney() >= Cost)
        {
            PlayerInfo.Instance.SetMaxHealth(PlayerInfo.Instance.GetMaxHealth() + AddValue);
            Value += AddValue;
            PlayerInfo.Instance.MinusMoney(Cost);

            GameSystem.Instance.SaveEditBegin();
            GameSystem.Instance.SetFloatInSave("maxhealth",PlayerInfo.Instance.GetMaxHealth());
            GameSystem.Instance.SetIntInSave("money",PlayerInfo.Instance.GetMoney());
            GameSystem.Instance.SaveEditEnd();
        }
    }

    public void OffClickFunction()
    {

    }

    public static HealthStatButton Create(float xPos,float yPos,int value,int addvalue,int cost,float buttonSize,float textSize)
    {
        HealthStatButton result = new HealthStatButton();
        result.Pos.x = xPos;
        result.Pos.y = yPos;
        result.bmp = Bitmap.createScaledBitmap(ResourceManager.Instance.GetBitmap(R.drawable.plusbutton),(int)buttonSize,(int)buttonSize,true);
        result.coinBitMap = Bitmap.createScaledBitmap(ResourceManager.Instance.GetBitmap(R.drawable.coin),(int)buttonSize,(int)buttonSize,true);
        result.Value =value;
        result.AddValue = addvalue;
        result.Cost = cost;
        result.paint.setColor(Color.BLACK);
        result.paint.setTextSize(textSize);
        EntityManager.Instance.AddEntity(result);
        return result;
    }
}