package com.fishweeb.practical

import android.graphics.Color
import android.view.SurfaceHolder
import com.fishweeb.practical.AudioManager.Init

class updateThread(_view: GameView) : Thread() {
    private var view: GameView? = null
    private var holder: SurfaceHolder? = null
    private var isRunning = false

    init {
        view = _view
        holder = _view.holder

        VibrateManager.Companion.Instance.Init(_view)
        ImageManager.Companion.Instance.Init(view)
        ResourceManager.Companion.Instance.Init(view)
        //SampleGame.Instance.Init(view);
        AudioManager.Instance.Init(view)
        StateManager.Companion.Instance.Init(view)
        GameSystem.Companion.Instance.Init(view)

        EntityManager.Companion.Instance.Init(_view)
        ParticleManager.Companion.Instance.Init(
            GameSystem.Companion.Instance.ScreenScale!!.x.toInt(),
            GameSystem.Companion.Instance.ScreenScale!!.y.toInt()
        )
    }

    fun IsRunning(): Boolean {
        return isRunning
    }

    fun Initialize() {
        isRunning = true
    }

    fun Terminate() {
        isRunning = false
    }

    override fun run() {
        // Init variables here
        val framePerSecond = 1000 / targetFPS // 1000 is milliseconds -> 1 second
        var startTime: Long = 0
        var prevTime = System.nanoTime()

        StateManager.Companion.Instance.Start("SplashScreen")

        while (IsRunning() && StateManager.Companion.Instance.GetCurrentState() !== "INVALID") {
            // Update
            startTime = System.currentTimeMillis()

            // Get delta time
            val currTime = System.nanoTime()
            prevTime = currTime

            // End delta time

            //SampleGame.Instance.Update(deltaTime);
            StateManager.Companion.Instance.Update(((currTime - prevTime) / 1000000000.0f) * GameSystem.Companion.Instance.GetGameSpeed())

            // Render
            val canvas = holder!!.lockCanvas()
            if (canvas != null) {
                synchronized(holder) {
                    // Start to do render
                    canvas.drawColor(Color.BLACK)

                    //SampleGame.Instance.Render(canvas);
                    StateManager.Companion.Instance.Render(canvas)
                }
                holder.unlockCanvasAndPost(canvas)
            }

            // Post Update
            try {
                val sleepTime = framePerSecond - (System.currentTimeMillis() - startTime)

                if (sleepTime > 0) sleep(sleepTime)
            } catch (e: InterruptedException) {
                Terminate()
            }
        }
    }

    companion object {
        const val targetFPS: Long = 60
    }
}