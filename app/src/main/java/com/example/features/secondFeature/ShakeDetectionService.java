package com.example.features.secondFeature;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class ShakeDetectionService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    ShakeEventListener shakeEventListener = null;
    static ShakeDetectionService shakeDetectionService = null;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

        @Override
    public void onCreate() {
        Log.d("salam", "onCreate: service created");
        shakeDetectionService = this;
//        Toast.makeText(this, "Service created!", Toast.LENGTH_SHORT).show();
        register();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0 ; i < 100; i++){
//                    Log.d("salam", "run: ddddddddddddd" + i);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        }).start();
//        handler = new Handler();
//        runnable = new Runnable() {
//            public void run() {
//                Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
//                handler.postDelayed(runnable, 10000);
//            }
//        };
//
//        handler.postDelayed(runnable, 15000);
    }

    public static boolean kill = false;
    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        if (kill){
            //handler.removeCallbacks(runnable);
            unregister();
        }
        Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_SHORT).show();
    }

    private void makeToast(String str){
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void unregister(){
        shakeEventListener.unregisterListener();
    }

    private void register(){
        ShakeEventListener.ShakeListener shakeListener = new ShakeEventListener.ShakeListener();
        shakeEventListener = new ShakeEventListener(shakeListener);
    }
}
