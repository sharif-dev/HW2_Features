package com.example.features;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import static android.os.VibrationEffect.DEFAULT_AMPLITUDE;
import static android.os.VibrationEffect.createOneShot;


public class Activity2 extends AppCompatActivity {

    private TextView textView_timer;
    private CountDownTimer countDownTimer;
    private long timeLeftMillisecond = 600000;
    private boolean timeRunning;


    //final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


    }

    @Override
    protected void onStart() {
        super.onStart();
        timeRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        timeRunning = false;
    }
}



