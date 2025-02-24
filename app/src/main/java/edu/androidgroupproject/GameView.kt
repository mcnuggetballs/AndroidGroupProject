package com.fishweeb.practical

import android.content.Context
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private val updateThread = UpdateThread(this)

    init {
        holder.addCallback(this) // Register the callback
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (!updateThread.isRunning()) {
            updateThread.Initialize()
            updateThread.start()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // No action needed
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        updateThread.Terminate()
        try {
            updateThread.join() // Wait for the thread to finish
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
