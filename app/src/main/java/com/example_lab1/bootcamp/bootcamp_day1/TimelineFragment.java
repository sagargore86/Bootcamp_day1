package com.example_lab1.bootcamp.bootcamp_day1;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class TimelineFragment  extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        SimpleCursorAdapter.ViewBinder {
    private static final String TAG = TimelineFragment.class.getSimpleName();
    private SimpleCursorAdapter mAdapter;
    private static final int LOADER_ID = 42;
    private static final String[] FROM = {
            StatusContract.Column.USER,
            StatusContract.Column.MESSAGE,
            StatusContract.Column.CREATED_AT };
    private static final int[] TO = {
            R.id.text_user,
            R.id.text_message,
            R.id.text_created_at };
    private OnTimelineItemSelectedListener mItemSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mItemSelectedListener = (OnTimelineItemSelectedListener) getActivity();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(
                    getActivity().getClass().getSimpleName()
                            + " must implement OnTimelineItemSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mItemSelectedListener.onTimelineItemSelected(id);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        ListView timeline = new ListView(getActivity());
        //setContentView(timeline);
        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item,
                null, FROM, TO, 0);
        mAdapter.setViewBinder(this);
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    public interface OnTimelineItemSelectedListener {
        public void onTimelineItemSelected(long id);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Return all items from StatusProvider
        return new CursorLoader(getActivity(), StatusContract.CONTENT_URI,
                null, null, null, StatusContract.DEFAULT_SORT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished with cursor: " + data.getCount());
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        long timestamp;

        // Custom binding
        switch (view.getId()) {
            case R.id.text_created_at:
                timestamp = cursor.getLong(columnIndex);
                CharSequence relTime = DateUtils
                        .getRelativeTimeSpanString(timestamp);
                ((TextView) view).setText(relTime);
                return true;
            default:
                return false;
        }
    }
}
