package com.example_lab1.bootcamp.bootcamp_day1;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.thenewcircle.yamba.client.YambaClient;
import com.thenewcircle.yamba.client.YambaClientInterface;
import com.thenewcircle.yamba.client.YambaStatus;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RefreshService extends IntentService {
    private static final String TAG = RefreshService.class.getSimpleName();
    public static final int NOTIFCATION_ID = 42;

    NotificationManager mNotificationMgr;

    public RefreshService() {
        super("RefreshService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void postStatusNotification (int count) {
        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent operation = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
            .setContentTitle("New Tweets !")
            .setContentText("You've got " + count + "new tweets")
            .setSmallIcon(android.R.drawable.sym_action_email)
            .setContentIntent(operation)
            .setAutoCancel(true)
            .build();

        mNotificationMgr.notify(NOTIFCATION_ID, notification);

    }

    private void prepareValuesForDb(List<YambaStatus> timeline, ContentValues[] valuesList) {
        int count = 0, i =0;
        long start = System.currentTimeMillis();
        ContentValues values = new ContentValues();
        for (YambaStatus status :  timeline) {
            values.clear();
            values.put(StatusContract.Column.ID, status.getId());
            values.put(StatusContract.Column.USER, status.getUser());
            values.put(StatusContract.Column.MESSAGE, status.getMessage());
            values.put(StatusContract.Column.CREATED_AT, status.getCreatedAt().getTime());

            valuesList[i] = values;
            i++;

            Uri uri = getContentResolver().insert( StatusContract.CONTENT_URI, values);
            if (uri != null) {
                count++;
                Log.d(TAG,
                        String.format("%s: %s", status.getUser(), status.getMessage()));

            }
        }

        if (count > 0) {
            postStatusNotification(count);
        }
        Log.v(TAG, "Sagar Insert completed in "
                + (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Enter");
        if (intent != null) {
            try {
                String username = "student";
                String password = "password";

                YambaClientInterface cloud = YambaClient.getClient(username, password);
                List<YambaStatus> timeline = cloud.getTimeline(20);
                ContentValues[] valuesList = new ContentValues[timeline.size()];
                Log.e(TAG, "Successfully retrived" +  timeline.size() +"entries from cloud");
                prepareValuesForDb(timeline, valuesList);
            } catch (Exception e) {
                Log.e(TAG, "Failed to connect to cloud");
            }
        }
    }
}
