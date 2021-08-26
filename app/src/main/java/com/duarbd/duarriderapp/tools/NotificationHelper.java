package com.duarbd.duarriderapp.tools;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.duarbd.duarriderapp.R;
import com.duarbd.duarriderapp.presenter.ActivityHome;

public class NotificationHelper {
    public static final String CHANNEL_ID ="duar_rider_channel_id";
    public static  final int NOTIFICATION_ID=1;

    public NotificationManager notificationManager;
    public Context context;

    public NotificationHelper(Context context){
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.context = context;
    }


    public  void displayNotification(Context context, String nTitle, String nBody){

        Intent fullScreenIntent = new Intent(context, ActivityHome.class);
        fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                fullScreenIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_notification)
                .setContentTitle(nTitle)
                .setContentText(nBody)
                .setSound(resourceToUri(R.raw.alarmtone1))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setFullScreenIntent(fullScreenPendingIntent, true);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    Uri resourceToUri(int resourceId){
        return Uri.parse("android.resource://com.duarbd.duarriderapp/" + resourceId);
    }
}
