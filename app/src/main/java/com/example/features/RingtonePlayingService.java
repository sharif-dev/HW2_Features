package com.example.features;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;


public class RingtonePlayingService extends Service {

    MediaPlayer media_song;
    int startId;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LocalService", "Received start id " + startId + ": " + intent);

        String state = intent.getExtras().getString("extra");

        int sound_id = intent.getExtras().getInt("sound_choice");

        NotificationManager notify_manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent_main_activity = new Intent(this, MainActivity.class);

        PendingIntent pending_intent_main_activity =
                PendingIntent.getActivity(this, 0, intent_main_activity, 0);

        Notification notify_popup =
                new Notification.Builder(this).setContentTitle("An alarm is going off!")
                        .setContentText("Click me!")
                        .setContentIntent(pending_intent_main_activity)
                        .setAutoCancel(true).build();

        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                Log.e("Start ID is ", state);
                break;
            default:
                startId = 0;
                break;
        }

        if(startId == 1) {

            if(sound_id == 1) {
                media_song = MediaPlayer.create(this, R.raw.catalina_wine_mixer);
                media_song.start();
            } else if(sound_id == 2) {
                media_song = MediaPlayer.create(this, R.raw.inclement_weather);
                media_song.start();
            } else if(sound_id == 3) {
                media_song = MediaPlayer.create(this, R.raw.lightning_bolt);
                media_song.start();
            } else if(sound_id == 4) {
                media_song = MediaPlayer.create(this, R.raw.johnny_hopkins);
                media_song.start();
            } else if(sound_id == 5) {
                media_song = MediaPlayer.create(this, R.raw.butt_buddy);
                media_song.start();
            } else if(sound_id == 6) {
                media_song = MediaPlayer.create(this, R.raw.robert_better_not_get_in_my_face);
                media_song.start();
            }
            else {
                media_song = MediaPlayer.create(this, R.raw.kalimba);
                media_song.start();
            }


        } else {
             Log.d("there is music, ", "and you want end");
            // Stop ringtone
            media_song.stop();
            media_song.reset();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
         Log.d("onDestroy called", "ta da");

        super.onDestroy();
        this.isRunning = false;
    }

}
