package com.example_lab1.bootcamp.bootcamp_day1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class StatusProvider extends ContentProvider {
    private static final String TAG = StatusProvider.class.getSimpleName();
    private DbHelper mDbHelper;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        //content://com.example_lab1.bootcamp.bootcamp_day1.StatusProvider/status
        sUriMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE, StatusContract.STATUS_DIR);
        //content://com.example_lab1.bootcamp.bootcamp_day1.StatusProvider/status/id
        sUriMatcher.addURI(StatusContract.AUTHORITY, StatusContract.TABLE + "/#", StatusContract.STATUS_ITEM);
    }

    public StatusProvider() {
    }

    @Override
    public String getType(Uri uri) {
        Log.d(TAG, "Null MIME type");
        return null;
    }

    // DELETE FROM status WHERE id=? AND user='?'
    // uri: content://com.example.android.yamba.StatusProvider/status/47
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String where = null;

        switch(sUriMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                //so we count deleted rows
                break;
            case StatusContract.STATUS_ITEM:
                long id = ContentUris.parseId(uri);
                where = StatusContract.Column.ID +
                        "=" + id +
                        (TextUtils.isEmpty(selection) ?  "" : " and ( "
                                + selection + " )");
                break;
            default:
                throw new IllegalArgumentException("Illegal uri "+ uri);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int ret = db.delete(StatusContract.TABLE, where, selectionArgs);

        if(ret>0) {
            // Notify that data for this uri has changed
            getContext().getContentResolver().notifyChange(uri, null);
        }
        Log.d(TAG, "deleted records: " + ret);
        return ret;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri ret = null;

        Log.d(TAG, "insert "+ uri + " id "+ values.getAsString(StatusContract.Column.ID));
        // Assert correct uri
        if (sUriMatcher.match(uri) != StatusContract.STATUS_DIR) {
            throw new IllegalArgumentException("Illegal uri: " + uri);
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long rowId = db.insertWithOnConflict(StatusContract.TABLE, null,
                values, SQLiteDatabase.CONFLICT_IGNORE);

        Log.d(TAG, "ret = "+ rowId);
        // Was insert successful?
        if (rowId != -1) {
            long id = values.getAsLong(StatusContract.Column.ID);
            ret = ContentUris.withAppendedId(uri, id);
            Log.d(TAG, "inserted uri: " + ret);

            // Notify that data for this uri has changed
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return ret;
    }

    @Override
    public boolean onCreate() {
        mDbHelper = (DbHelper) new DbHelper(getContext(), null, null, 0);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(StatusContract.TABLE);
        switch (sUriMatcher.match(uri)) {
            case StatusContract.STATUS_DIR:
                break;
            case StatusContract.STATUS_ITEM:
                qb.appendWhere(StatusContract.Column.ID + "="
                    + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Illegal Uri: " + uri);
        }
        String orderBy = (TextUtils.isEmpty(sortOrder)) ?
                StatusContract.DEFAULT_SORT : sortOrder;

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs,
                null, null, orderBy);

        //register for uri changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        Log.d(TAG, "queried record: " + cursor.getCount());
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
