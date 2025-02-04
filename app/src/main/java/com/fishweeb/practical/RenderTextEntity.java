package com.fishweeb.practical;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.SurfaceView;

public class RenderTextEntity implements EntityBase{

    // Paint object
    Paint paint = new Paint();
    private int red = 0, green = 0, blue = 0;

    private boolean isDone = false;
    private boolean isInit = false;

    int frameCount;
    long lastTime = 0;
    long lastFPSTime = 0;
    float fps;
    int ScreenWidth;
    int ScreenHeight;

    Typeface myfont;

    @Override
    public boolean IsDone() {
        return isDone;
    }

    @Override
    public void SetIsDone(boolean _isDone) {
        isDone = _isDone;
    }

    @Override
    public void SetRenderLayer(int _newLayer)
    {
        return;
    }

    @Override
    public int GetRenderLayer()
    {
        return LayerConstants.UI_LAYER;
    }

    @Override
    public void Init(SurfaceView _view) {

        myfont = Typeface.createFromAsset(_view.getContext().getAssets(),"fonts/akashi.ttf");
        // Week 8 Use my own fonts
        paint.setARGB(255, 0,255,0);
        paint.setStrokeWidth(200);
        paint.setTextSize(60);
        paint.setTypeface(myfont);
        ScreenWidth = _view.getWidth();
        ScreenHeight = _view.getHeight();
    }

    @Override
    public void Update(float _dt) {

        // get actual fps

        frameCount++;

        long currentTime = System.currentTimeMillis();

        lastTime = currentTime;

        if(currentTime - lastFPSTime > 1000)
        {
            fps = (frameCount * 1000.f) / (currentTime - lastFPSTime);
            lastFPSTime = currentTime;
            frameCount = 0;
        }
    }

    @Override
    public void Render(Canvas _canvas)
    {
        _canvas.drawText("FPS: " + (int)fps, 0, 180, paint);

    }

    public static RenderTextEntity Create()
    {
        RenderTextEntity result = new RenderTextEntity();
        EntityManager.Instance.AddEntity(result);
        return result;
    }
}
