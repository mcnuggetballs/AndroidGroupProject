package com.fishweeb.practical;

import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

public class RenderBackground implements EntityBase
{
    boolean isDone = false;
    Vector2 Pos;
    int ScreenWidth;
    int ScreenHeight;

    @Override
    public boolean IsDone() {
        return isDone;
    }

    @Override
    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    @Override
    public void Init(SurfaceView _view) {
    Pos.x = 0.0f;
    Pos.y = 0.0f;
    DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
    ScreenWidth = metrics.widthPixels;
    ScreenHeight = metrics.heightPixels;
    }

    @Override
    public void Update(float _dt)
    {
        Pos.y += _dt * 500;

        PlayerInfo.Instance.DepthUpdate(_dt);

        if (Pos.y > ScreenHeight)
        {
            Pos.y = 0;
        }
    }

    @Override
    public void Render(Canvas _canvas)
    {
        _canvas.drawBitmap(ResourceManager.Instance.GetBitmap(R.drawable.gamescene),Pos.x,Pos.y,null);
        _canvas.drawBitmap(ResourceManager.Instance.GetBitmap(R.drawable.gamescene),Pos.x,Pos.y-ScreenHeight,null);
    }

    public int GetRenderLayer()
    {
        return LayerConstants.BACKGROUND_LAYER;
    }

    public void SetRenderLayer(int _newLayer)
    {
        return;
    }

    public static RenderBackground Create()
    {
        RenderBackground result = new RenderBackground();
        EntityManager.Instance.AddEntity(result);
        return result;
    }
}