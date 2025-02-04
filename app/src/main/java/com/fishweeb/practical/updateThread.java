package com.fishweeb.practical;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class updateThread extends Thread
{
    static final long targetFPS = 60;

    private GameView view = null;
    private SurfaceHolder holder = null;
    private boolean isRunning = false;

    public updateThread(GameView _view)
    {
        view = _view;
        holder = _view.getHolder();

        VibrateManager.Instance.Init(_view);
        ImageManager.Instance.Init(view);
        ResourceManager.Instance.Init(view);
        //SampleGame.Instance.Init(view);
        AudioManager.Instance.Init(view);
        StateManager.Instance.Init(view);
        GameSystem.Instance.Init(view);

        EntityManager.Instance.Init(_view);
        ParticleManager.Instance.Init((int)GameSystem.Instance.ScreenScale.x,(int)GameSystem.Instance.ScreenScale.y);
    }

    public boolean IsRunning()
    {
        return isRunning;
    }

    public void Initialize()
    {
        isRunning = true;
    }

    public void Terminate()
    {
        isRunning = false;
    }

    @Override
    public void run()
    {
        // Init variables here
        long framePerSecond = 1000 / targetFPS; // 1000 is milliseconds -> 1 second
        long startTime = 0;
        long prevTime = System.nanoTime();

        StateManager.Instance.Start("SplashScreen");

        while(IsRunning() && StateManager.Instance.GetCurrentState() != "INVALID")
        {
            // Update
            startTime = System.currentTimeMillis();

            // Get delta time
            long currTime = System.nanoTime();
            float deltaTime = (float)((currTime - prevTime) / 1000000000.0f);
            prevTime = currTime;
            // End delta time

            //SampleGame.Instance.Update(deltaTime);
            StateManager.Instance.Update(deltaTime * GameSystem.Instance.GetGameSpeed());

            // Render
            Canvas canvas = holder.lockCanvas();
            if (canvas != null)
            {
                synchronized (holder) // Lock the door
                {
                    // Start to do render
                    canvas.drawColor(Color.BLACK);

                    //SampleGame.Instance.Render(canvas);
                    StateManager.Instance.Render(canvas);
                }
                holder.unlockCanvasAndPost(canvas);
            }

            // Post Update
            try
            {
                long sleepTime = framePerSecond - (System.currentTimeMillis() - startTime);

                if (sleepTime > 0)
                    sleep(sleepTime);
            }
            catch(InterruptedException e)
            {
                Terminate();
            }
        }
    }
}