package com.example_lab1.bootcamp.bootcamp_day1;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements TimelineFragment.OnTimelineItemSelectedListener{
    private static final String TAG = MainActivity.class.getSimpleName();

    //Global notifier of when timeline is in the foreground
    private static boolean inTimeline = false;
    public static boolean isInTimeline() {
        return inTimeline;
    }

    @Override
    protected void onResume() {
        super.onResume();
        inTimeline = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        inTimeline = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_yamba, menu);
        setContentView(R.layout.activity_main);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Log.d(TAG, "Settings selected");
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_refresh:
                Log.d(TAG, "Refresh selected");
                startService(new Intent(this, RefreshService.class));
                return true;
            case R.id.action_purge:
                Log.d(TAG, "Purge selected");
                int rows = getContentResolver().delete(StatusContract.CONTENT_URI, null, null);
                Toast.makeText(this, "Deleted" + rows + "rows", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_post:
                Log.d(TAG, "Post selected");
                startActivity(new Intent(this, StatusActivity.class));
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimelineItemSelected(long id) {
        //Toast.makeText(this, "Click " + id, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Sagar: Clik " + id);
        //startActivity(new Intent(this, DetailsActivity.class));
        // Get the details fragment
        DetailsFragment fragment = (DetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_details);

        // Is details fragment visible?
        if (fragment != null && fragment.isVisible()) {
            fragment.updateView(id);
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(StatusContract.Column.ID, id);

            startActivity(intent);
        }
    }
//
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        //Return all items from StatusProvider
//        return new CursorLoader(this, StatusContract.CONTENT_URI,
//                null, null, null, StatusContract.DEFAULT_SORT);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        Log.d(TAG, "onLoadFinished with cursor: " + data.getCount());
//        mAdapter.swapCursor(data);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        mAdapter.swapCursor(null);
//    }
//
//    @Override
//    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//        long timestamp;
//
//        // Custom binding
//        switch (view.getId()) {
//            case R.id.text_created_at:
//                timestamp = cursor.getLong(columnIndex);
//                CharSequence relTime = DateUtils
//                        .getRelativeTimeSpanString(timestamp);
//                ((TextView) view).setText(relTime);
//                return true;
//            default:
//                return false;
//        }
//    }
}
