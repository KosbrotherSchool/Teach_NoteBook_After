package com.jasonko.notebook;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by kolichung on 5/8/15.
 */
public class NoteProvider extends ContentProvider {

    private DbHelper dbHelper;


    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(DbContract.TABLE,projection,
                                selection,selectionArgs,null,null,
                                DbContract.DEFAULT_SORT);

        // notify cursor when insert, delete, update
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(DbContract.TABLE, null, values);

        if (rowId != -1){
            getContext().getContentResolver().notifyChange(uri,null);
            Log.d("NoteProvider", "success insert data");
        }

        return ContentUris.withAppendedId(uri,rowId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(DbContract.TABLE,selection,selectionArgs);
        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.update(DbContract.TABLE,values,selection,selectionArgs);
        if (rows > 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return 0;
    }

}
