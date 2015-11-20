package com.example_lab1.bootcamp.bootcamp_day1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Check if this activity was created before
        if (savedInstanceState == null) {
            // PreferenceFragment is not included in the support library
            // We must use the core fragment manager (API 11+)
            Log.d("SettingsActivty", "Fragment launched first time");

            SettingsFragment fragment = new SettingsFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fragment,
                            fragment.getClass().getSimpleName()).commit();
        }
    }
}
