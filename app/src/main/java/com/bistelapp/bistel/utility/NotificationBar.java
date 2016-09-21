package com.bistelapp.bistel.utility;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.bistelapp.bistel.R;
import com.bistelapp.bistel.riders.DisplayBookingsActivity;

/**
 * Created by Control & Inst. LAB on 15-Aug-16.
 */
public class NotificationBar {
    Context context;

    public NotificationBar(Context context) {
        this.context = context;
    }

    public void ShowNotification(String content) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle("Bistel Bookings Alert!");
        mBuilder.setContentText(content);
        Intent resultIntent = new Intent(context, DisplayBookingsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(DisplayBookingsActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(9999, mBuilder.build());

    }
}
