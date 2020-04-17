package com.example.features;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity2 extends AppCompatActivity {

    private TextView textView_timer;
    private CountDownTimer countDownTimer;
    private long timeLeftMillisecond = 600000;
    private boolean timeRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        textView_timer = findViewById(R.id.text_view_timer);

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
            }

            @Override
            public void onFinish() {

            }
        }.start();

    }
}
