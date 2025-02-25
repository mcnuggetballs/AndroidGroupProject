package edu.androidgroupproject

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.SurfaceView
import com.edu.androidgroupproject.GameSystem

// Sample of an intro state - You can change to Splash page..
class IntroState : StateBase {
    private var timer = 5.0f
    private var background: Bitmap? = null
    private var logo: Bitmap? = null

    override fun GetName(): String {
        return "SplashScreen"
    }

    override fun OnEnter(_view: SurfaceView) {
        background = Bitmap.createScaledBitmap(
            ImageManager.Companion.Instance.GetImage(IMAGE.I_MENUBACKGROUND)!!,
            GameSystem.Instance.GetScreenScale()!!.x.toInt(),
            GameSystem.Instance.GetScreenScale()!!.y.toInt(),
            true
        )
        logo = Bitmap.createScaledBitmap(
            ImageManager.Companion.Instance.GetImage(IMAGE.I_LOGO)!!,
            (GameSystem.Instance.GetScreenScale()!!.x * 0.8f).toInt(),
            (GameSystem.Instance.GetScreenScale()!!.y * 0.2f).toInt(),
            true
        )
        timer = 5.0f
    }

    override fun OnExit() {
        EntityManager.Companion.Instance.EmptyEntityList()
    }

    override fun Render(_canvas: Canvas) {
        // Render anything
        _canvas.drawBitmap(background!!, 0f, 0f, null)
        _canvas.drawBitmap(
            logo!!,
            GameSystem.Instance.GetScreenScale()!!.x * 0.1f,
            GameSystem.Instance.GetScreenScale()!!.y * 0.4f,
            null
        )
    }

    override fun Update(_dt: Float) {
        timer -= _dt
        if (timer <= 0.0f || TouchManager.Instance.HasTouch()) {
            // We are done showing our splash screen
            // Move on time!
            StateManager.Instance.ChangeState("MainMenu")
        }
    }
}
