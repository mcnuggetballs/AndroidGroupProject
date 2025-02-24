package com.fishweeb.practical

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.SurfaceView

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
            GameSystem.Companion.Instance.GetScreenScale()!!.x.toInt(),
            GameSystem.Companion.Instance.GetScreenScale()!!.y.toInt(),
            true
        )
        logo = Bitmap.createScaledBitmap(
            ImageManager.Companion.Instance.GetImage(IMAGE.I_LOGO)!!,
            (GameSystem.Companion.Instance.GetScreenScale()!!.x * 0.8f).toInt(),
            (GameSystem.Companion.Instance.GetScreenScale()!!.y * 0.2f).toInt(),
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
            GameSystem.Companion.Instance.GetScreenScale()!!.x * 0.1f,
            GameSystem.Companion.Instance.GetScreenScale()!!.y * 0.4f,
            null
        )
    }

    override fun Update(_dt: Float) {
        timer -= _dt
        if (timer <= 0.0f || TouchManager.Companion.Instance.HasTouch()) {
            // We are done showing our splash screen
            // Move on time!
            StateManager.Companion.Instance.ChangeState("MainMenu")
        }
    }
}
