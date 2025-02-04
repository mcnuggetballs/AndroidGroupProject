package com.fishweeb.practical;

import android.graphics.Canvas;
import android.view.SurfaceView;

import java.util.HashMap;

public class StateManager
{
    public static final StateManager Instance = new StateManager();

    private HashMap<String,StateBase> stateMap = new HashMap<String, StateBase>();

    private StateBase currState = null;
    private StateBase nextState = null;

    private SurfaceView view = null;

    private StateManager()
    {
    }

    public void Init(SurfaceView _view)
    {
        view = _view;
    }

    public void AddState(StateBase _newState)
    {
        stateMap.put(_newState.GetName(),_newState);
    }

    public void ChangeState(String _nextState)
    {
        nextState = stateMap.get(_nextState);

        if (nextState == null)
            nextState = currState;
    }

    public void Update(float _dt)
    {
        if (nextState != currState)
        {
            currState.OnExit();
            nextState.OnEnter(view);
            currState = nextState;
        }
        if (currState == null)
            return;

        currState.Update(_dt);
    }

    public void Render(Canvas _canvas)
    {
        currState.Render(_canvas);
    }

    String GetCurrentState()
    {
        if(currState == null)
            return "INVALID";

        return currState.GetName();
    }

    void Start(String _newCurrent)
    {
        if (currState!= null || nextState != null)
            return;

        currState = stateMap.get(_newCurrent);
        if (currState != null)
        {
            currState.OnEnter(view);
            nextState = currState;
        }
    }
}
