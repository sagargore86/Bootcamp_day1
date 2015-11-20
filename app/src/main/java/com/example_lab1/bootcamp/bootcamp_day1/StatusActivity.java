package com.example_lab1.bootcamp.bootcamp_day1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends AppCompatActivity implements
        View.OnClickListener, TextWatcher {

    private static String TAG = StatusActivity.class.getSimpleName();
    private Button mPostButton;
    private EditText mTextStatus;
    private TextView mTextCount;
    private int mDefaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Action bar stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_status);
        mPostButton = (Button) findViewById(R.id.status_button);
        mTextStatus = (EditText) findViewById(R.id.status_text);
        mTextCount = (TextView) findViewById(R.id.status_text_count);
        mDefaultColor = mTextCount.getTextColors().getDefaultColor();

        mPostButton.setOnClickListener(this);
        mTextStatus.addTextChangedListener(this);
        mTextStatus.setText(getIntent().getStringExtra(StatusUpdateService.EXTRA_MESSAGE));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mTextCount.setTextColor(Color.BLUE);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int count = 10 - s.length();
        mTextCount.setText(Integer.toString(count) + "/10");

        if (count < 4)
            mTextCount.setTextColor(Color.RED);
        else if (count > 3 && count < 10)
            mTextCount.setTextColor(mDefaultColor);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show();

        Log.d(TAG, " Input Text " + mTextStatus.getText().toString());
        Intent intent = new Intent(this, StatusUpdateService.class);
        intent.putExtra(StatusUpdateService.EXTRA_MESSAGE, mTextStatus.getText().toString());

        startService(intent);
        mTextStatus.getText().clear();
        finish();
    }
}