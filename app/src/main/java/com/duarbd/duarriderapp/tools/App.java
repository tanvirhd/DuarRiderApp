package com.duarbd.duarriderapp.tools;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.duarbd.duarriderapp.R;

public class App extends Application {
    private static final String TAG = "App";
    private static App instance;
    public static final String CHANNEL_ID ="duar_rider_channel_id";
    public static final String CHANNEL_NAME="duar_client_channel";

    public static App getInstance() {
        return instance;
    }

    public Context getContext() {
        return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance=this;
        super.onCreate();

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(resourceToUri(R.raw.alarmtone1),attributes);
            NotificationManager notificationManager=getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    Uri resourceToUri(int resourceId){
        return Uri.parse("android.resource://com.duarbd.duarriderapp/" + resourceId);
    }
}
