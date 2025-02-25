package edu.androidgroupproject

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.view.SurfaceView

class RenderTextEntity : EntityBase {
    // Paint object
    var paint: Paint = Paint()
    private val red = 0
    private val green = 0
    private val blue = 0

    private var isDone = false
    private val isInit = false

    var frameCount: Int = 0
    var lastTime: Long = 0
    var lastFPSTime: Long = 0
    var fps: Float = 0f
    var ScreenWidth: Int = 0
    var ScreenHeight: Int = 0

    var myfont: Typeface? = null

    override fun IsDone(): Boolean {
        return isDone
    }

    override fun SetIsDone(_isDone: Boolean) {
        isDone = _isDone
    }

    override fun SetRenderLayer(_newLayer: Int) {
        return
    }

    override fun GetRenderLayer(): Int {
        return LayerConstants.UI_LAYER
    }

    override fun Init(_view: SurfaceView) {
        myfont = Typeface.createFromAsset(_view.context.assets, "fonts/akashi.ttf")
        // Week 8 Use my own fonts
        paint.setARGB(255, 0, 255, 0)
        paint.strokeWidth = 200f
        paint.textSize = 60f
        paint.setTypeface(myfont)
        ScreenWidth = _view.width
        ScreenHeight = _view.height
    }

    override fun Update(_dt: Float) {
        // get actual fps

        frameCount++

        val currentTime = System.currentTimeMillis()

        lastTime = currentTime

        if (currentTime - lastFPSTime > 1000) {
            fps = (frameCount * 1000f) / (currentTime - lastFPSTime)
            lastFPSTime = currentTime
            frameCount = 0
        }
    }

    override fun Render(_canvas: Canvas) {
        _canvas.drawText("FPS: " + fps.toInt(), 0f, 180f, paint)
    }

    companion object {
        fun Create(): RenderTextEntity {
            val result = RenderTextEntity()
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}
