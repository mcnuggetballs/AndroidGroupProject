package com.fishweeb.practical

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback, SensorEventListener {
    private val updateThread = UpdateThread(this)
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

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
            val tiltX = event.values[0] // Left-right tilt
            val tiltY = event.values[1] // Up-down tilt

            // Pass tilt values to PlayerInfo for processing
            PlayerInfo.Instance.SetTiltValues(-tiltX, tiltY) // Adjust to match game coordinates
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
