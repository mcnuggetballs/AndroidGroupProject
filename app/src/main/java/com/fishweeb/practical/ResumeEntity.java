package com.fishweeb.practical;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

public class ResumeEntity extends ButtonEntity
{
    private Paint pauseBG = new Paint();

    @Override
    public void Init(SurfaceView _view) {
        // Define anything you need to use here

        // sheetRow = 1;
        // sheetCol = 3;
        // spritesheet = new Sprite(BitmapFactory.decodeResource(_view.getResources(),R.drawable.sprite_ray),sheetRow,sheetCol,12);

        bmp = BitmapFactory.decodeResource(_view.getResources(),R.drawable.resume_button);

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

        pauseBG.setColor(Color.BLACK);
        pauseBG.setAlpha(200);
    }

    @Override
    public void OnClickFunction()
    {
        GameSystem.Instance.SetGameSpeed(1);
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

    public static ResumeEntity Create(float xPos,float yPos)
    {
        ResumeEntity result = new ResumeEntity();
        result.Pos.x = xPos;
        result.Pos.y = yPos;
        EntityManager.Instance.AddEntity(result);
        return result;
    }

    @Override
    public void Render(Canvas _canvas)
    {
        if (GameSystem.Instance.GetGameSpeed() == 0)
            _canvas.drawRect(0,0,ScreenWidth,ScreenHeight,pauseBG);

        // Render anything
        if (spritesheet != null)
            spritesheet.Render(_canvas,(int)Pos.x,(int)Pos.y);
        else if (bmp != null)
            _canvas.drawBitmap(bmp,Pos.x-(width*0.5f),Pos.y-(height*0.5f),null);
        else
            _canvas.drawRect(Pos.x - width*0.5f,Pos.y - height*0.5f,Pos.x + width*0.5f,Pos.y + height*0.5f,PaintColor.Instance.GetPaint(Color.BLUE));
    }
}
