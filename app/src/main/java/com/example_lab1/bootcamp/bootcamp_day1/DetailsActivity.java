package com.example_lab1.bootcamp.bootcamp_day1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Check if this activity was created before
        if (savedInstanceState == null) {
            // Create a fragment
            long statusId =
                    getIntent().getLongExtra(StatusContract.Column.ID, -1);

            Log.e(TAG, "statusId " + statusId);
            DetailsFragment fragment =
                    DetailsFragment.newInstance(statusId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment,
                            fragment.getClass().getSimpleName()).commit();
        }
    }
}
