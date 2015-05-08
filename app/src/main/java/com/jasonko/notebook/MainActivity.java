package com.jasonko.notebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainActivity extends Activity {

    private SharedPreferences sp;
    private ListView listView;
    private Cursor query;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getPreferences(Context.MODE_PRIVATE);
        boolean isFirstOpen = sp.getBoolean("FirstOpen", true);
        if (isFirstOpen) {
            showFirstDialog();
        }

        listView = (ListView) findViewById(R.id.listview_main);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idText = (TextView) view.findViewById(android.R.id.text2);
                String idString = idText.getText().toString();
                Intent newIntent = new Intent(MainActivity.this, ContentActivity.class);
                newIntent.putExtra("NOTE_ID", Integer.valueOf(idString));
                startActivity(newIntent);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        query = getContentResolver().query(
                DbContract.CONTENT_URI,
                null,
                null,
                null,
                DbContract.DEFAULT_SORT);
        String[] from = new String[]{DbContract.Column.CONTENT, DbContract.Column.ID};
        int[] to = new int[]{android.R.id.text1, android.R.id.text2};

        adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,query,from,to,0);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent newIntent = new Intent(MainActivity.this, ContentActivity.class);
            startActivity(newIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFirstDialog() {

        AlertDialog.Builder firstDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("設置倒數時間")
                .setMessage("請先點選右上方新增文章")
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                sp.edit().putBoolean("FirstOpen",false).commit();
                            }
                        });
        firstDialog.show();

    }

}
