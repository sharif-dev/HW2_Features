package com.example.features;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
         Log.d("In receiver", "Yay!");

        String fetch_string = intent.getExtras().getString("extra");
         Log.d("What is the key?", fetch_string);

        int get_sound_choice = intent.getExtras().getInt("sound_choice");

        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        service_intent.putExtra("extra", fetch_string);

        service_intent.putExtra("sound_choice", get_sound_choice);

        context.startService(service_intent);

    }
}
