package com.fishweeb.practical

import android.graphics.Canvas
import android.view.SurfaceView

class RenderBackground : EntityBase {
    var isDone: Boolean = false
    var Pos: Vector2? = null
    var ScreenWidth: Int = 0
    var ScreenHeight: Int = 0

    override fun IsDone(): Boolean {
        return isDone
    }

    override fun SetIsDone(_isDone: Boolean) {
        isDone = _isDone
    }

    override fun Init(_view: SurfaceView) {
        Pos!!.x = 0.0f
        Pos!!.y = 0.0f
        val metrics = _view.resources.displayMetrics
        ScreenWidth = metrics.widthPixels
        ScreenHeight = metrics.heightPixels
    }

    override fun Update(_dt: Float) {
        Pos!!.y += _dt * 500

        PlayerInfo.Companion.Instance.DepthUpdate(_dt)

        if (Pos!!.y > ScreenHeight) {
            Pos!!.y = 0f
        }
    }

    override fun Render(_canvas: Canvas) {
        _canvas.drawBitmap(
            ResourceManager.Companion.Instance.GetBitmap(R.drawable.gamescene)!!,
            Pos!!.x,
            Pos!!.y,
            null
        )
        _canvas.drawBitmap(
            ResourceManager.Companion.Instance.GetBitmap(R.drawable.gamescene)!!,
            Pos!!.x,
            Pos!!.y - ScreenHeight,
            null
        )
    }

    override fun GetRenderLayer(): Int {
        return LayerConstants.BACKGROUND_LAYER
    }

    override fun SetRenderLayer(_newLayer: Int) {
        return
    }

    companion object {
        fun Create(): RenderBackground {
            val result = RenderBackground()
            EntityManager.Companion.Instance.AddEntity(result)
            return result
        }
    }
}