package com.example.sutharnil.buggy;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    public int counter=0;
    private int NOTIFICATION = 1; // Unique identifier for our notification
    public static boolean isRunning = false;
    public static MyService instance = null;

    private NotificationManager notificationManager = null;

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        instance = this;
        isRunning = true;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startMyOwnForeground();

        super.onCreate();
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
//        else
//            startForeground(1, new Notification());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                splashActivity.class), 0);

        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_logopit_1534081965403) // the status icon
                .setTicker("Your App running...") // the status text
                .setWhen(System.currentTimeMillis()) // the time stamp
                .setContentTitle("Online") // the label of the entry
                .setContentText("Application running...") // the content of the entry
                .setContentIntent(contentIntent) // the intent to send when the entry is
                .setOngoing(true) // make persistent (disable swipe-away)
                .build();
        // Start service in foreground mode
        startForeground(NOTIFICATION, notification);



    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, BroadCastService.class);
        this.sendBroadcast(broadcastIntent);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, BroadCastService.class);
        this.sendBroadcast(broadcastIntent);
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
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }
}
