package com.fishweeb.practical;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.SurfaceView;

// Sample of an intro state - You can change to Splash page..
public class IntroState implements StateBase
{
    private float timer = 5.0f;
    private Bitmap background = null;
    private Bitmap logo = null;

    @Override
    public String GetName() {
        return "SplashScreen";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
        background = Bitmap.createScaledBitmap(ImageManager.Instance.GetImage(IMAGE.I_MENUBACKGROUND),(int)GameSystem.Instance.GetScreenScale().x,(int)GameSystem.Instance.GetScreenScale().y,true);
        logo = Bitmap.createScaledBitmap(ImageManager.Instance.GetImage(IMAGE.I_LOGO),(int)(GameSystem.Instance.GetScreenScale().x*0.8f),(int)(GameSystem.Instance.GetScreenScale().y*0.2f),true);
        timer = 5.0f;
    }

    @Override
    public void OnExit()
    {
        EntityManager.Instance.EmptyEntityList();
    }

    @Override
    public void Render(Canvas _canvas)
    {
        // Render anything
        _canvas.drawBitmap(background,0,0,null);
        _canvas.drawBitmap(logo,GameSystem.Instance.GetScreenScale().x*0.1f,GameSystem.Instance.GetScreenScale().y*0.4f,null);
    }

    @Override
    public void Update(float _dt)
    {
        timer -= _dt;
        if (timer <= 0.0f || TouchManager.Instance.HasTouch())
        {
            // We are done showing our splash screen
            // Move on time!
            StateManager.Instance.ChangeState("MainMenu");
        }
    }
}
