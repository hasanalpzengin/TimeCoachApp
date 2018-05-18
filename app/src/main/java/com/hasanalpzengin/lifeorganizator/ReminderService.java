package com.hasanalpzengin.lifeorganizator;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

/**
 * Created by hasalp on 08.04.2018.
 */

public class ReminderService extends BroadcastReceiver {

    private static final String CHANNEL_ID = "com.hasanalpzengin.lifeorganizator";
    private static final int NOTIFICATION_ID = 9909;

    @Override
    public void onReceive(Context context, Intent intent) {

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.notification)
                .setContentTitle(context.getString(R.string.dontForgetActivity))
                .setContentText(context.getString(R.string.itsTimeTo)+" "+intent.getStringExtra("activityName"))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
