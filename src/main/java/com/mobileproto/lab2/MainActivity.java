package com.mobileproto.lab2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

    public FeedReaderDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView title = (TextView) findViewById(R.id.titleField);
        final TextView note = (TextView) findViewById(R.id.noteField);

        mDbHelper = new FeedReaderDbHelper(this);

        List<String> files = new ArrayList<String>(Arrays.asList(fileList()));

        final NoteListAdapter aa = new NoteListAdapter(this, android.R.layout.simple_list_item_1, files);
        final ListView notes = (ListView) findViewById(R.id.noteList);

        notes.setAdapter(aa);

        //The buttons in this app:
        Button save = (Button)findViewById(R.id.saveButton);



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fileName = title.getText().toString();
                String noteText = note.getText().toString();
                if (fileName != null && noteText != null){
                    try{
                        SQLiteDatabase db = mDbHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_TITLE, fileName);
                        values.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_SUBTITLE, noteText);

                        long newRowId;
                        newRowId = db.insert(FeedReaderDbHelper.FeedEntry.TABLE_NAME,null,values);

                        title.setText("");
                        note.setText("");
                        aa.insert(fileName,0);
                        aa.notifyDataSetChanged();
                    }catch (Exception e){
                        Log.e("Exception", e.getMessage());
                    }
                }
            }
        });
        save.setFocusable(false);

        notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView title = (TextView) view.findViewById(R.id.titleTextView);
                String fileName = title.getText().toString();

                String[] allCols = {FeedReaderDbHelper.FeedEntry._ID, FeedReaderDbHelper.FeedEntry.COLUMN_NAME_TITLE, FeedReaderDbHelper.FeedEntry.COLUMN_NAME_SUBTITLE};

                //The following points to which row you'll be opening. You get the database, and change it to a query.
                Cursor notedb = mDbHelper.getWritableDatabase().query(FeedReaderDbHelper.FeedEntry.TABLE_NAME, allCols, "title=" + "\"" + fileName + "\"", null, null, null, null);
                notedb.moveToFirst();
                System.out.println("It opened!");

                Intent in = new Intent(getApplicationContext(), NoteDetailActivity.class);

                String file = notedb.getString(1);
                String text = notedb.getString(2);

                in.putExtra("file", file);
                in.putExtra("text", text);

                startActivity(in);
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}