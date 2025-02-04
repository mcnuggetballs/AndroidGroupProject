package com.fishweeb.practical;

import android.graphics.Canvas;
import android.view.SurfaceView;

public interface EntityBase
{
    boolean IsDone();
    void SetIsDone(boolean _isDone);
    void Init(SurfaceView _view);
    void Update(float _dt);
    void Render(Canvas _canvas);

    int GetRenderLayer();
    void SetRenderLayer(int _newLayer);
}

