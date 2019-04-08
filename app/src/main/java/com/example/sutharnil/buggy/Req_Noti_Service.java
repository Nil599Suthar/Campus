package com.example.sutharnil.buggy;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import java.util.Timer;
import java.util.TimerTask;

public class Req_Noti_Service extends Service {
    public int counter = 0;

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();

        startMyOwnForeground();
   }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(getApplicationContext(), BroadCastService.class);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        Notification mNotification = new Notification.Builder(this)

                .setContentTitle("New Request")
                .setContentText("Here's an awesome update for you!")
                .setSmallIcon(R.drawable.ic_logopit_1534081965403)
                .setContentIntent(pIntent)
                .setSound(soundUri)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        notificationManager.notify(0, mNotification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        stoptimertask();

    }


    private Timer timer;
    public TimerTask timerTask;

    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {

            }
        };
        timer.schedule(timerTask, 1000, 1000); //
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
