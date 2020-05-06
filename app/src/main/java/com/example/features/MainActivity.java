package com.example.features;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.features.secondFeature.ShakeDetectionService;
import com.example.features.secondFeature.ShakeEventListener;
import com.example.features.thirdFeature.MyAdmin;

import java.util.Calendar;

import static com.example.features.secondFeature.ShakeEventListener.SHAKE_MAX;
import static com.example.features.secondFeature.ShakeEventListener.SHAKE_MIN;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

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
    public static final int RESULT_ENABLE = 11;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName compName;
    private SensorManager sensorManager;
    private Sensor sensor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);
        alarm_off = (Button) findViewById(R.id.alarm_off);
        final AlarmManager alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        // Create intent for AlarmReceiver class, send only once
        my_intent = new Intent(MainActivity.this, AlarmReceiver.class);

        Spinner spinner = findViewById(R.id.spinnerAlarmSound);
        String[] items = getResources().getStringArray(R.array.stepbrothers_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(), "Spinner item is " + id, Toast.LENGTH_SHORT).show();
                sound_select = (int) id;
                ;
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
                        Toast.makeText(MainActivity.this, "" + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
                        SelectedHour = hourOfDay;
                        SelectedMin = minute;
                    }
                }, mHour, mMin, true);
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
                    if (shakeService == null) {
                        shakeService = new Intent(MainActivity.this, ShakeDetectionService.class);
                        startService(shakeService);
                        ShakeEventListener.setShake_rate(SHAKE_MIN + (SHAKE_MAX - SHAKE_MIN) * seekBar.getProgress() / 100);
                        ShakeDetectionService.kill = false;
                    }
                    Toast.makeText(MainActivity.this, "Shaking phone turn on", Toast.LENGTH_SHORT).show();
                } else {
                    if (shakeService != null) {
                        ShakeDetectionService.kill = true;
                        stopService(shakeService);
                        shakeService = null;
                    }
                    Toast.makeText(MainActivity.this, "Shaking phone turn off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (shakeService != null) {
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
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Additional text explaining why we need this permission");
                    startActivityForResult(intent, RESULT_ENABLE);
                    Toast.makeText(MainActivity.this, "Putting phone turn on", Toast.LENGTH_SHORT).show();
                }else {
                    devicePolicyManager.removeActiveAdmin(compName);
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
        sensorManager.unregisterListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onResume() {
        super.onResume();
        if (number_state > 0) {
            alarm_off.performClick();
        }
        number_state++;
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            sensorManager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(MainActivity.this, "You have enabled the Admin Device features", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Problem to enable the Admin Device features", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void cancelAlarm() {

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x =event.values[0];
        float y =event.values[1];
        float z =event.values[2];
        boolean active = devicePolicyManager.isAdminActive(compName);
        if (active) {
            if (x < 1 && x > -1 && y < 1 && y > -1) {
                devicePolicyManager.lockNow();
            }
        }
    }


}
