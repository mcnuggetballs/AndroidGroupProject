package com.fishweeb.practical

import android.content.Context
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(_context: Context?) : SurfaceView(_context) {
    //Used to hold the contex
    private var holder: SurfaceHolder? = null

    //Thread to be known for its existence
    private val updateThread = updateThread(this)

    init {
        holder = getHolder()

        holder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) //Setup some stuff to indicate whether thread is running and initialize
            {
                if (!updateThread.IsRunning()) updateThread.Initialize()
                if (!updateThread.isAlive) updateThread.start()
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) //Nothing to type here cuz it will be handle by the thread
            {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) //Done then should not run too
            {
                updateThread.Terminate()
            }
        })
    }
}
