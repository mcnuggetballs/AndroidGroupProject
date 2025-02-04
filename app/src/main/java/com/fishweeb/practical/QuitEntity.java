package com.fishweeb.practical;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.SurfaceView;

public class QuitEntity extends ButtonEntity
{
    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here

        // sheetRow = 1;
        // sheetCol = 3;
        // spritesheet = new Sprite(BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_ray),sheetRow,sheetCol,12);

        bmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.quit_button);

        //width = spritesheet.GetWidth();
        //height = spritesheet.GetHeight();

        width = bmp.getWidth();
        height = bmp.getHeight();

        //width = 30;
        //height = 30;

        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        velocity.x = 0;
        velocity.y = 0;

        if (width > height)
            radius = width*0.5f;
        else
            radius = height*0.5f;
    }

    @Override
    public void Update(float _dt)
    {
        //Movement
        Pos.PlusEqual(velocity,_dt);

        if (spritesheet != null)
            spritesheet.Update(_dt);

        if (TouchManager.Instance.HasTouch())
        {
            if (Collision.SphereToSphere(TouchManager.Instance.GetPosX(),TouchManager.Instance.GetPosY(),0,Pos.x,Pos.y,radius))
            {
                OnClickFunction();
            }
        }
        else
        {
           OffClickFunction();
        }

        if (GameSystem.Instance.GetGameSpeed() != 0)
            SetIsDone(true);
    }

    @Override
    public void OnClickFunction()
    {
        if(GameSystem.Instance.GetGameSpeed()==0)
        {
            GameSystem.Instance.SaveEditBegin();
            GameSystem.Instance.SetIntInSave("money",PlayerInfo.Instance.GetMoney());
            GameSystem.Instance.SaveEditEnd();
            StateManager.Instance.ChangeState("MainMenu");
        }
    }

    public static QuitEntity Create(float xPos,float yPos)
    {
        QuitEntity result = new QuitEntity();
        result.Pos.x = xPos;
        result.Pos.y = yPos;
        EntityManager.Instance.AddEntity(result);
        return result;
    }
}
