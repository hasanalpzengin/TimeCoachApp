package com.hasanalpzengin.lifeorganizator;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;


/**
 * Created by hasalp on 08.04.2018.
 */

public class BackgroundService extends IntentService {

    public BackgroundService(String name) {
        super(name);
    }

    public BackgroundService(){
        super("activity");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Intent reminder = new Intent(this, ReminderService.class);
        reminder.putExtra("activityName", intent.getStringExtra("activityName"));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), NotificationID.getID(), reminder, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, intent.getLongExtra("activityStartTime",0), pendingIntent);
    }

}
