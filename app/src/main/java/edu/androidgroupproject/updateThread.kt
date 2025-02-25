package edu.androidgroupproject

import android.graphics.Color
import android.view.SurfaceHolder
import com.edu.androidgroupproject.GameSystem
import com.fishweeb.practical.ParticleManager

class UpdateThread(private val view: GameView) : Thread() {
    private val holder: SurfaceHolder = view.holder
    private var isRunning = false

    init {
        VibrateManager.Instance.Init(view)

        view.let {
            ImageManager.Instance.Init(it)
            ResourceManager.Instance.Init(it)
            GameSystem.Instance.Init(it)
        }
        AudioManager.init(view)
        StateManager.Instance.Init(view)

        EntityManager.Instance.Init(view)
        ParticleManager.Instance.Init(
            GameSystem.Instance.ScreenScale!!.x.toInt(),
            GameSystem.Instance.ScreenScale!!.y.toInt()
        )
    }

    fun isRunning(): Boolean = isRunning

    fun Initialize() {
        isRunning = true
        StateManager.Instance.Start("SplashScreen") // Moved here
    }

    fun Terminate() {
        isRunning = false
    }

    override fun run() {
        val framePerSecond = 1000 / targetFPS
        var prevTime = System.nanoTime()

        while (isRunning() && StateManager.Instance.GetCurrentState() != "INVALID") {
            val startTime = System.currentTimeMillis()

            // Calculate delta time (dt) properly
            val currTime = System.nanoTime()
            val dt = ((currTime - prevTime) / 1_000_000_000.0f) * GameSystem.Instance.GetGameSpeed()
            prevTime = currTime

            // Update game state
            StateManager.Instance.Update(dt)

            // Render
            val canvas = holder.lockCanvas()
            if (canvas != null) {
                synchronized(holder) {
                    canvas.drawColor(Color.BLACK)
                    StateManager.Instance.Render(canvas)
                }
                holder.unlockCanvasAndPost(canvas)
            }

            // Sleep to maintain target FPS
            val sleepTime = framePerSecond - (System.currentTimeMillis() - startTime)
            if (sleepTime > 0) {
                try {
                    sleep(sleepTime)
                } catch (e: InterruptedException) {
                    Terminate()
                }
            }
        }
    }

    companion object {
        const val targetFPS: Long = 60
    }
}
