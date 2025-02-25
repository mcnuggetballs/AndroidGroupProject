package edu.androidgroupproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager

class GamePage : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Instance = this

        setContentView(GameView(this))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // WE are hijacking the touch event into our own system
        val x = event.x.toInt()
        val y = event.y.toInt()

        TouchManager.Companion.Instance.Update(x, y, event.action)
        return true
    }

    fun ChangePage(nextclass: Class<*>?) {
        val intent = Intent()
        intent.setClass(this, nextclass!!)
        startActivity(intent)
    }

    companion object {
        var Instance: GamePage? = null
    }
}
