package com.jasonko.notebook;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by kolichung on 5/7/15.
 */
public class ContentActivity extends Activity {

    private EditText mEditText;
    private int note_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_content);
        mEditText = (EditText) findViewById(R.id.edittext_content);

        note_id = getIntent().getIntExtra("NOTE_ID",0);
        if (note_id != 0){
            // query cusor
            Cursor query = queryByID(note_id);
            String content = query.getString(query.getColumnIndex(DbContract.Column.CONTENT));
            mEditText.setText(content);
        }

    }

    private Cursor queryByID(int id) {
        Cursor query = getContentResolver().query(
                DbContract.CONTENT_URI,
                null,
                DbContract.Column.ID + "=" + id,
                null,
                DbContract.DEFAULT_SORT);
        query.moveToFirst();
//        String title = query.getString(query.getColumnIndex(DbContract.Column.TITLE));
        return query;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_save:
                Calendar c = Calendar.getInstance();
                int currentSeconds = (int) (c.getTimeInMillis()/1000);

                ContentValues values = new ContentValues();
                values.put(DbContract.Column.TITLE,"title test");
                values.put(DbContract.Column.CONTENT,mEditText.getText().toString());
                values.put(DbContract.Column.CREATED_AT, currentSeconds);

                if (queryByID(note_id).getCount() != 0){
                    getContentResolver().update(DbContract.CONTENT_URI,values,DbContract.Column.ID + "=" + note_id, null);
                    Toast.makeText(ContentActivity.this, "action update", Toast.LENGTH_SHORT).show();
                }else {
                    Uri uri = getContentResolver().insert(DbContract.CONTENT_URI, values);
                    note_id = (int)ContentUris.parseId(uri);
                    Toast.makeText(ContentActivity.this, "action save", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.action_delete:
                Toast.makeText(ContentActivity.this,"action delete",Toast.LENGTH_SHORT).show();
                getContentResolver().delete(DbContract.CONTENT_URI,DbContract.Column.ID + "=" + note_id,null);
                note_id = 0;
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}