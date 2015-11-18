package com.example_lab1.bootcamp.bootcamp_day1;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
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

    public RefreshService() {
        super("RefreshService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Enter");
        if (intent != null) {
            try {
                String username = "student";
                String password = "password";

                YambaClientInterface cloud = YambaClient.getClient(username, password);
                List<YambaStatus> timeline= cloud.getTimeline(20);

                Log.e(TAG, "Successfully retrived" +  timeline.size() +"entries from cloud");

            } catch (Exception e) {
                Log.e(TAG, "Failed to connect to cloud");
            }
        }
    }
}
