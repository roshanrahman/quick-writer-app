package com.example.roshan.quickwriter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.roshan.quickwriter.dummy.DummyContent.DummyItem;


public class MyNoteRecyclerViewAdapter extends CursorAdapter {
    public MyNoteRecyclerViewAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_note, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView noteTitleTextView = (TextView) view.findViewById(R.id.noteTitle);
        TextView noteTextTextView = (TextView) view.findViewById(R.id.noteText);
        String noteTitle = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String noteContent = cursor.getString(cursor.getColumnIndexOrThrow("content"));
        // Populate fields with extracted properties
        view.setTag(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
        noteTitleTextView.setText(noteTitle);
        noteTextTextView.setText(noteContent);
    }



}