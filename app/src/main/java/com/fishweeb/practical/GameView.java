package com.fishweeb.practical;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView
{
    //Used to hold the contex
    private SurfaceHolder holder = null;

    //Thread to be known for its existence
    private updateThread updateThread = new updateThread(this);

    public GameView(Context _context)
    {
        super(_context);
        holder = getHolder();

        if (holder != null)
        {
            holder.addCallback(new SurfaceHolder.Callback()
            {
                @Override
                public void surfaceCreated(SurfaceHolder holder)
                        //Setup some stuff to indicate whether thread is running and initialize
                {
                    if (!updateThread.IsRunning())
                        updateThread.Initialize();
                    if (!updateThread.isAlive())
                        updateThread.start();
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
                        //Nothing to type here cuz it will be handle by the thread
                {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder)
                        //Done then should not run too
                {
                    updateThread.Terminate();
                }
            });
        }
    }
}
