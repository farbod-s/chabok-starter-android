package com.adpdigital.chabok.starter.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

public class OtherProcessService extends Service {
    private final static String TAG = OtherProcessService.class.getSimpleName();

    public static void startPeriodicTask(Context context) {
        Log.d(TAG, "startPeriodicTask()");

        Intent intent = new Intent(context, OtherProcessService.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getService(context, 0, intent, 0);
        if (alarmManager != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    1000 * 10, alarmIntent); // repeat every 10 seconds
        }
    }

    public static void stopPeriodicTask(Context context) {
        Log.d(TAG, "stopPeriodicTask()");

        Intent intent = new Intent(context, OtherProcessService.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = PendingIntent.getService(context, 0, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(alarmIntent);
        }
    }

    public OtherProcessService() {
        Log.d(TAG, "OtherProcessService()");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");

        stopSelf();

        return START_NOT_STICKY;
    }
}