package edu.androidgroupproject

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.MotionEvent

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback, SensorEventListener {
    private val updateThread = UpdateThread(this)
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var baseTiltX = 0f
    private var baseTiltY = 0f
    private var isCalibrated = false

    init {
        holder.addCallback(this)
        accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (!updateThread.isRunning()) {
            updateThread.Initialize()
            updateThread.start()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        updateThread.Terminate()
        sensorManager.unregisterListener(this)
        try {
            updateThread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val rawTiltX = event.values[0]  // Left-right tilt
            val rawTiltY = event.values[2]  // Up-down tilt

            // ✅ Calibrate the tilt only ONCE when the game starts
            if (!isCalibrated) {
                baseTiltX = rawTiltX
                baseTiltY = rawTiltY
                isCalibrated = true
            }

            // ✅ Subtract the base values to ensure no initial movement
            val tiltX = rawTiltX - baseTiltX
            val tiltY = rawTiltY - baseTiltY

            // ✅ Update the player’s movement values
            PlayerInfo.Instance.SetTiltValues(-tiltX, tiltY)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        TouchManager.Instance.Update(event.x.toInt(), event.y.toInt(), event.action)
        return true
    }
}
