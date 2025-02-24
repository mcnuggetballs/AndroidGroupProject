package com.fishweeb.practical

import android.graphics.Canvas
import android.view.SurfaceView

class StateManager
private constructor() {
    private val stateMap = HashMap<String?, StateBase>()

    private var currState: StateBase? = null
    private var nextState: StateBase? = null

    private var view: SurfaceView? = null

    fun Init(_view: SurfaceView?) {
        view = _view
    }

    fun AddState(_newState: StateBase) {
        stateMap[_newState.GetName()] = _newState
    }

    fun ChangeState(_nextState: String?) {
        nextState = stateMap[_nextState]

        if (nextState == null) nextState = currState
    }

    fun Update(_dt: Float) {
        if (nextState !== currState) {
            currState!!.OnExit()
            nextState!!.OnEnter(view!!)
            currState = nextState
        }
        if (currState == null) return

        currState!!.Update(_dt)
    }

    fun Render(_canvas: Canvas) {
        currState!!.Render(_canvas)
    }

    fun GetCurrentState(): String? {
        if (currState == null) return "INVALID"

        return currState!!.GetName()
    }

    fun Start(_newCurrent: String?) {
        if (currState != null || nextState != null) return

        currState = stateMap[_newCurrent]
        if (currState != null) {
            currState!!.OnEnter(view!!)
            nextState = currState
        }
    }

    companion object {
        val Instance: StateManager = StateManager()
    }
}
