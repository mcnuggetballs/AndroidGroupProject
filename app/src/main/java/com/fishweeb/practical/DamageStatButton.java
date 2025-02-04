package com.fishweeb.practical;

import android.graphics.Bitmap;
import android.graphics.Color;

public class DamageStatButton extends StatButtonEntity
{
    public void OnClickFunction()
    {
        if (PlayerInfo.Instance.GetMoney() >= Cost)
        {
            PlayerInfo.Instance.GetMainWeapon().SetWeaponDamage(PlayerInfo.Instance.GetMainWeapon().GetWeaponDamage() + AddValue);
            Value += AddValue;
            PlayerInfo.Instance.MinusMoney(Cost);
            GameSystem.Instance.SaveEditBegin();
            GameSystem.Instance.SetIntInSave("maingunshootdamage",PlayerInfo.Instance.GetMainWeapon().GetWeaponDamage());
            GameSystem.Instance.SetIntInSave("money",PlayerInfo.Instance.GetMoney());
            GameSystem.Instance.SaveEditEnd();
        }
    }

    public void OffClickFunction()
    {

    }

    public static DamageStatButton Create(float xPos, float yPos, int value, int addvalue, int cost, float buttonSize, float textSize)
    {
        DamageStatButton result = new DamageStatButton();
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