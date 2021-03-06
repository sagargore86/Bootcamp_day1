package com.example_lab1.bootcamp.bootcamp_day1;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends android.support.v4.app.Fragment {

    private TextView mTextUser;
    private TextView mTextMessage;
    private TextView mTextCreatedAt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_resource, container, false);

        mTextUser = (TextView) view.findViewById(R.id.text_user);
        mTextMessage = (TextView) view.findViewById(R.id.text_message);
        mTextCreatedAt = (TextView) view.findViewById(R.id.text_created_at);

        return view;
    }
    //Best practice is to use a factory and feed parameters into arguments
    public static DetailsFragment newInstance(long statusId) {
        DetailsFragment fragment = new DetailsFragment();

        Bundle args = new Bundle();
        args.putLong(StatusContract.Column.ID, statusId);
        fragment.setArguments(args);

        return fragment;
    }

    public void updateView(long id) {
        if (id == -1) {
            //Can't query an invalid id
            return;
        }

        Uri uri = ContentUris.withAppendedId(StatusContract.CONTENT_URI, id);

        Cursor cursor = getActivity().getContentResolver().query(uri, null,
                null, null, null);

        if (!cursor.moveToFirst()) {
            cursor.close();
            return;
        }

        String user = cursor.getString(cursor
                .getColumnIndex(StatusContract.Column.USER));
        String message = cursor.getString(cursor
                .getColumnIndex(StatusContract.Column.MESSAGE));
        long createdAt = cursor.getLong(cursor
                .getColumnIndex(StatusContract.Column.CREATED_AT));

        mTextUser.setText(user);
        mTextMessage.setText(message);
        mTextCreatedAt.setText(DateUtils.getRelativeTimeSpanString(createdAt));

        cursor.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            long id = getArguments().getLong(StatusContract.Column.ID);
            updateView(id);
        }
    }   
}
