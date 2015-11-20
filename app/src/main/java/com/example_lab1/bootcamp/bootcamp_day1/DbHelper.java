package com.example_lab1.bootcamp.bootcamp_day1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    public static final String TAG = DbHelper.class.getSimpleName();

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, StatusContract.DB_NAME, null, StatusContract.DB_VERSION);
        Log.i(TAG, "Constructor DB_NAME " + StatusContract.DB_NAME + " version " + StatusContract.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate");
        String sql = String.format(
                "create table %s (%s int primary key, %s text, %s text, %s int)",
                        StatusContract.TABLE,
                        StatusContract.Column.ID,
                        StatusContract.Column.USER,
                        StatusContract.Column.MESSAGE,
                    StatusContract.Column.CREATED_AT);
        Log.d(TAG, "onCreate with SQL: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Typically you do ALTER TABLE ...
        Log.e(TAG, "Version changed old " +oldVersion + " new " + newVersion);
        db.execSQL("drop table if exists " + StatusContract.TABLE);
        onCreate(db);
    }
}
