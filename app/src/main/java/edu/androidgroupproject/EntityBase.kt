package edu.androidgroupproject

import android.graphics.Canvas
import android.view.SurfaceView

interface EntityBase {
    fun IsDone(): Boolean
    fun SetIsDone(_isDone: Boolean)
    fun Init(_view: SurfaceView)
    fun Update(_dt: Float)
    fun Render(_canvas: Canvas)

    fun GetRenderLayer(): Int
    fun SetRenderLayer(_newLayer: Int)
}

