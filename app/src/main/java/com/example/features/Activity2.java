package com.example.features;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import static android.os.VibrationEffect.DEFAULT_AMPLITUDE;
import static android.os.VibrationEffect.createOneShot;


public class Activity2 extends AppCompatActivity implements SensorEventListener {

    private TextView textView_timer;
    private CountDownTimer countDownTimer;
    private long timeLeftMillisecond = 600000;
    private boolean timeRunning;
    Button buttonOff;


    //final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


    private float minZSpeed;
    private SensorManager sensorManager;
    final Intent my_intent = new Intent(this, AlarmReceiver.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        minZSpeed = 4.0f;

        setContentView(R.layout.activity_2);
        textView_timer = findViewById(R.id.text_view_timer);
        final long[] pattern = {1200 , 1500 , 900 , 500, 1050, 700, 1000, 800, 470, 950};
        countDownTimer = new CountDownTimer(timeLeftMillisecond , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillisecond = millisUntilFinished;
                int minutes = (int) timeLeftMillisecond / 60000;
                int seconds = (int) timeLeftMillisecond % 60000 / 1000;
                String timeLeftText;
                timeLeftText = "" + minutes;
                timeLeftText += ":";
                if (seconds < 10) {
                    timeLeftText += "0";
                }
                timeLeftText += seconds;
                textView_timer.setText(timeLeftText);

                if (seconds % 5 == 0 && timeRunning) {
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    Random random = new Random();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(createOneShot(pattern[random.nextInt(10)], DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(pattern[random.nextInt(10)]);
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            Sensor gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        timeRunning = true;
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
        timeRunning = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float zRotationalSpeed = Math.abs(event.values[2]);
        if (zRotationalSpeed > minZSpeed) {
            timeRunning = false;
            sensorManager.unregisterListener(this);
            Toast.makeText(this, "You succeeded", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}



