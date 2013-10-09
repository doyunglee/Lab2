package com.mobileproto.lab2;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by evan on 9/15/13.
 */
public class NoteListAdapter extends ArrayAdapter {

    public FeedReaderDbHelper mDbHelper;

    public List<String> data;
    private MainActivity activity;

    public NoteListAdapter(Activity a, int viewResourceId, List<String> data){
        super(a, viewResourceId, data);
        this.data = data;
        this.activity = (MainActivity) a;
        this.mDbHelper = activity.mDbHelper;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v==null){
            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.note_list_item, null);
        }

        final ImageButton del = (ImageButton) v.findViewById(R.id.deleteButton);
        final TextView name = (TextView) v.findViewById(R.id.titleTextView);
        name.setText(data.get(position));

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    try {
                        String selection = FeedReaderDbHelper.FeedEntry.COLUMN_NAME_TITLE + "=?";
                        // Specify arguments in placeholder order.
                        String fileName = name.getText().toString();
                        String[] selectionArgs = {String.valueOf(FeedReaderDbHelper.FeedEntry._ID)};
                        SQLiteDatabase db = mDbHelper.getWritableDatabase();
                        // Issue SQL statement.
                        db.delete(FeedReaderDbHelper.FeedEntry.TABLE_NAME, selection, selectionArgs);


                        activity.deleteFile(fileName);
                        data.remove(position);
                        NoteListAdapter.this.notifyDataSetChanged();
                    }
                    catch (Exception e){
                        Log.e("Exception", e.getMessage());
                    }
                }
        });
        return v;
    }
}
