package com.fishweeb.practical;

import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

public class PauseEntity extends ButtonEntity
{
    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here

        // sheetRow = 1;
        // sheetCol = 3;
        // spritesheet = new Sprite(BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_ray),sheetRow,sheetCol,12);

        bmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.pause_button);

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
    public void OnClickFunction()
    {
        if (GameSystem.Instance.GetGameSpeed() != 0)
        {
            GameSystem.Instance.SetGameSpeed(0);
            float distanceBetween = 200;
            ResumeEntity.Create(ScreenWidth*0.5f - distanceBetween,ScreenHeight*0.5f);
            QuitEntity.Create(ScreenWidth*0.5f + distanceBetween,ScreenHeight*0.5f);
        }
    }

    public static PauseEntity Create(float xPos,float yPos)
    {
        PauseEntity result = new PauseEntity();
        result.Pos.x = xPos;
        result.Pos.y = yPos;
        EntityManager.Instance.AddEntity(result);
        return result;
    }
}
