package com.duarbd.duarriderapp.services;

import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.duarbd.duarriderapp.tools.NotificationHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "CustomFirebaseMessaging";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if(remoteMessage!=null){
            String title=remoteMessage.getNotification().getTitle();
            String message_body=remoteMessage.getNotification().getBody();

            NotificationHelper notificationHelper=new NotificationHelper(getApplicationContext());
            notificationHelper.displayNotification(getApplicationContext(),title,message_body);
        }
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
    }
}