package com.example.features.secondFeature;


import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.util.Log;

public class ShakeEventListener implements SensorEventListener {
    public final static int SHAKE_MAX = 50;
    public final static int SHAKE_MIN = 5;

    private SensorManager mSensorManager;
    private float mAccel = 0.00f;
    private float mAccelCurrent = SensorManager.GRAVITY_EARTH;
    private float mAccelLast = SensorManager.GRAVITY_EARTH;

    private ShakeListener listener;
    private static float shake_rate = 10;

    public static void setShake_rate(float shake_ratee) {
        Log.d("salam", "setShake_rate changed to: " + shake_ratee);
        shake_rate = shake_ratee;
    }

    public static class ShakeListener {
        public void onShake(){
            Log.d("salam", "onShake: shaked");
            SensorManager sensorManager = (SensorManager) ShakeDetectionService.shakeDetectionService.getSystemService(Context.SENSOR_SERVICE);
            PowerManager pm = (PowerManager) ShakeDetectionService.shakeDetectionService.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP, "CHESS");
            wl.acquire();
            wl.release();

        }
    }


    public ShakeEventListener(ShakeListener l) {
        Service s = ShakeDetectionService.shakeDetectionService;
        mSensorManager = (SensorManager) s.getSystemService(Context.SENSOR_SERVICE);
        listener = l;
        registerListener();
    }

    public void registerListener() {
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        Log.d("salam", "registerListener: registered");
    }

    public void unregisterListener() {
        mSensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent se) {
        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.pow((x*x + y*y + z*z),0.5);
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta;
        //float shake_rate = 10;//activity.findViewById(R.id.seekBar).getAlpha();
        //Log.d("salam", "onSensorChanged: " + shake_rate);
        if(mAccel > shake_rate)
            listener.onShake();

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}