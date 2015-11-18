package com.example_lab1.bootcamp.bootcamp_day1;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.support.v7.app.NotificationCompat;

import com.thenewcircle.yamba.client.YambaClient;
import com.thenewcircle.yamba.client.YambaClientInterface;

public class StatusUpdateService extends IntentService {

    private static final String TAG =
            StatusUpdateService.class.getSimpleName();
    private NotificationManager mNotificationMgr;
    public static final String EXTRA_MESSAGE = "message";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public StatusUpdateService() {
        super(TAG);
        Log.i(TAG, " class Name " + TAG + " canonical name " + StatusUpdateService.class.getCanonicalName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public static final int NOTIFICATION_ID = 43;

    private void PostProgressNotification() {
        Log.e(TAG, "show progress notification");
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Posting Status")
                .setContentText("Status update in progress...")
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setOngoing(true)
                .build();
        mNotificationMgr.notify(NOTIFICATION_ID, notification);
    }

    private void PostErrorNotification(String originalMessage) {
        Intent intent = new Intent(this, StatusActivity.class);
        intent.putExtra(EXTRA_MESSAGE, originalMessage);

        PendingIntent operation = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.e(TAG, "failed");
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Post error")
                .setContentText("Error posting message...Tap to try again")
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .build();
        mNotificationMgr.notify(NOTIFICATION_ID, notification);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Unpack intent and get string message passed to this service
        String message = "Testing: " + intent.getStringExtra(EXTRA_MESSAGE);
        Log.d(TAG, " Received message " + message);
        try {
            String username = "student";
            String password = "password";

            PostProgressNotification();

            YambaClientInterface cloud = YambaClient.getClient(username, password);
            cloud.postStatus(message);

            Log.d(TAG, "Successfully posted message to cloud: " + message);
            mNotificationMgr.cancel(NOTIFICATION_ID);
        } catch (Exception e) {
            Log.e(TAG, "Failed to post message to cloud: " + message);
            PostErrorNotification(message);
        }
    }
}
