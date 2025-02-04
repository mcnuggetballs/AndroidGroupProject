package com.fishweeb.practical;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceView;

public class VibrateManager
{
    public final static VibrateManager Instance = new VibrateManager();
    protected static final String TAG = null;
    private Vibrator _vibrator;

    public void Init(SurfaceView _view)
    {
        _vibrator = (Vibrator)_view.getContext().getSystemService(_view.getContext().VIBRATOR_SERVICE);
    }

    public void startVibrate()
    {
        if (Build.VERSION.SDK_INT >= 26)
        {
            _vibrator.vibrate(VibrationEffect.createOneShot(150, 10));
            Log.v(TAG,"VIBRATED");
        }
        else
        {
            long pattern[] = {0,50,0};
            _vibrator.vibrate(pattern,-1);
            Log.v(TAG,"VIBRATED");
        }
    }

    public void stopVibrate()
    {
        _vibrator.cancel();
    }
}
