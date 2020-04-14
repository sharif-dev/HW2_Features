package com.example.features;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    int mMin, mHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    }
                }, mHour, mMin,true);
                timePickerDialog.show();
            }
        });

        Spinner spinner = findViewById(R.id.spinnerAlarmSound);
        String[] items = getResources().getStringArray(R.array.items);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:

                        Toast.makeText(MainActivity.this, "you selected item1", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:

                        Toast.makeText(MainActivity.this, "you selected item2", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:

                        Toast.makeText(MainActivity.this, "you selected item3", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final CheckBox checkBoxShake = findViewById(R.id.check_box_Shake);
        checkBoxShake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxShake.isChecked()) {

                    Toast.makeText(MainActivity.this, "Shaking phone turn on", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
}
