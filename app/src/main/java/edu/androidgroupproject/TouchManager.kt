package edu.androidgroupproject

import android.view.MotionEvent

class TouchManager
private constructor() {
    enum class TouchState {
        NONE,
        DOWN,
        MOVE
    }

    private var status = TouchState.NONE
    private var posX = 0
    private var posY = 0

    fun HasTouch(): Boolean {
        return status == TouchState.DOWN || status == TouchState.MOVE
    }

    fun IsDown(): Boolean {
        return status == TouchState.DOWN
    }

    fun GetPosX(): Int {
        return posX
    }

    fun GetPosY(): Int {
        return posY
    }

    fun Update(_posX: Int, _posY: Int, _motionEventStatus: Int) {
        posX = _posX
        posY = _posY

        when (_motionEventStatus) {
            MotionEvent.ACTION_DOWN -> status = TouchState.DOWN
            MotionEvent.ACTION_MOVE -> status = TouchState.MOVE
            MotionEvent.ACTION_UP -> status = TouchState.NONE
        }
    }

    companion object {
        val Instance: TouchManager = TouchManager()
    }
}
