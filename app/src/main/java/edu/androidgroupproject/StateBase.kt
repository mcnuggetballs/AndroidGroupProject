package edu.androidgroupproject

import android.graphics.Canvas
import android.view.SurfaceView

interface StateBase {
    fun GetName(): String

    fun OnEnter(_view: SurfaceView)
    fun OnExit()
    fun Render(_canvas: Canvas)
    fun Update(_dt: Float)
}
