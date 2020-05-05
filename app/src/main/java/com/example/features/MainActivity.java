package com.example.features;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.features.secondFeature.ShakeDetectionService;
import com.example.features.secondFeature.ShakeEventListener;

import java.util.Calendar;

import static com.example.features.secondFeature.ShakeEventListener.SHAKE_MAX;
import static com.example.features.secondFeature.ShakeEventListener.SHAKE_MIN;

public class MainActivity extends AppCompatActivity {

    int mMin, mHour;
    int SelectedHour;
    int SelectedMin;
    int SelectedItem = 0;
    AlarmManager alarm_manager;
    Context context;
    PendingIntent pending_intent;
    int sound_select;
    Button alarm_off;
    Intent my_intent;
    int number_state = 0;
    Intent shakeService = null;
    boolean alarmIsOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarm_off = (Button) findViewById(R.id.alarm_off);

        final AlarmManager alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create intent for AlarmReceiver class, send only once
        my_intent = new Intent(MainActivity.this, AlarmReceiver.class);

        Spinner spinner = findViewById(R.id.spinnerAlarmSound);
        String[] items = getResources().getStringArray(R.array.stepbrothers_array);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(), "Spinner item is " + id, Toast.LENGTH_SHORT).show();
                sound_select = (int) id;;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btn = findViewById(R.id.timeButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMin = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Toast.makeText(MainActivity.this, ""+hourOfDay+":"+minute, Toast.LENGTH_SHORT).show();
                        SelectedHour = hourOfDay;
                        SelectedMin = minute;
                    }
                }, mHour, mMin,true);
                timePickerDialog.show();
            }
        });


        final CheckBox checkBoxAlarm = findViewById(R.id.check_box_alarm);
        checkBoxAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxAlarm.isChecked()) {
                    alarmIsOn = true;
                    my_intent.putExtra("extra", "alarm on");
                    my_intent.putExtra("sound_choice", sound_select);
                    pending_intent = PendingIntent.getBroadcast
                            (MainActivity.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, SelectedHour);
                    calendar.set(Calendar.MINUTE, SelectedMin);
                    alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
                } else {
                    alarmIsOn = false;
                }
            }
        });


        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alarmIsOn) {
                    alarm_manager.cancel(pending_intent);
                    my_intent.putExtra("extra", "alarm off");
                    my_intent.putExtra("sound_choice", sound_select);
                    sendBroadcast(my_intent);
                }
            }
        });

        // shake part //////////////////////////////
        final CheckBox checkBoxShake = findViewById(R.id.check_box_Shake);
        final SeekBar seekBar = findViewById(R.id.seekBar);

        checkBoxShake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxShake.isChecked()) {
                    if(shakeService == null){
                        shakeService = new Intent(MainActivity.this, ShakeDetectionService.class);
                        startService(shakeService);
                        ShakeEventListener.setShake_rate(SHAKE_MIN + (SHAKE_MAX - SHAKE_MIN) * seekBar.getProgress() / 100);
                        ShakeDetectionService.kill = false;
                    }
                    Toast.makeText(MainActivity.this, "Shaking phone turn on", Toast.LENGTH_SHORT).show();
                } else {
                    if (shakeService != null){
                        ShakeDetectionService.kill = true;
                        stopService(shakeService);
                        shakeService = null;
                        Toast.makeText(MainActivity.this, "Shaking phone turn off", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (shakeService != null){
                    ShakeEventListener.setShake_rate(SHAKE_MIN + (SHAKE_MAX - SHAKE_MIN) * seekBar.getProgress() / 100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //
        final CheckBox checkBoxSleep = findViewById(R.id.check_box_sleep);
        checkBoxSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxSleep.isChecked()) {

                    Toast.makeText(MainActivity.this, "Putting phone turn on", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (number_state > 0) {
            alarm_off.performClick();
        }
        number_state++;
    }

    public void cancelAlarm() {

    }

}
