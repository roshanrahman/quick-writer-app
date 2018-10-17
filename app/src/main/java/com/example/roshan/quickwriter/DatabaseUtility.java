package com.example.roshan.quickwriter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseUtility extends SQLiteOpenHelper {
    private static DatabaseUtility ourInstance;

    private final static String DATABASE_NAME = "notesDB";
    private final static int DATABASE_VERSION = 1;
    private static String NOTES_TABLE = "notes";
    private static String BUILD_DATABASE_QUERY = "CREATE TABLE " + NOTES_TABLE + " (_id int PRIMARY KEY, title text, content text)";


    private Context context;

    public static synchronized DatabaseUtility getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new DatabaseUtility(context.getApplicationContext());
        }
        return ourInstance;
    }

    private DatabaseUtility(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public int getId() {
        SQLiteDatabase db = getReadableDatabase();
        int id = 0;
        Cursor result = db.rawQuery("SELECT _id FROM " + NOTES_TABLE + ";", null);
        try {
            result.moveToLast();
            id = result.getInt(result.getColumnIndexOrThrow("_id"));
            id = id + 1;
            return id;
        } catch(Exception e) {
            return id;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BUILD_DATABASE_QUERY);
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", 0);
        contentValues.put("title", "Get started");
        contentValues.put("content", "Hello friend!\n\nIt's easy to get started with this app. Go back and use the Add icon to create new notes, or try editing this note by using the Pencil icon on the bar on top. All your changes will be saved automatically. Press the Done icon when you've finished writing to go back to your list of notes. Play around and have fun writing!\n\nEnjoy :)");
        db.beginTransaction();
        db.insert(NOTES_TABLE, null, contentValues);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int createNewNote() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        int id = getId();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", id);
        contentValues.put("title", "Untitled Note");
        db.insert(NOTES_TABLE, null, contentValues);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return id;
    }

    public boolean updateNote(int id, String title, String content) {
        if(title.equals("")) {
            title = "Untitled Note";
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("content", content);
        String where = "_id=?";
        String[] whereArgs = new String[] {String.valueOf(id)};
        try {
            db.beginTransaction();
            db.update(NOTES_TABLE, contentValues, where, whereArgs);
            db.setTransactionSuccessful();
            db.endTransaction();
            db.close();
            Log.i("DATABASE:", "Successfully updated");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteNote(int id) {
        String where = "_id=?";
        String[] whereArgs = new String[] {String.valueOf(id)};
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.delete(NOTES_TABLE, where, whereArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return true;
    }

    public boolean deleteNotes(ArrayList<String> ids) {
        if(ids.size() == 0) {
            return false;
        }
        String list = "(";
        for(String item : ids) {
            list = list + item;
            if(!item.equals(ids.get(ids.size() - 1)))  {
                list = list + ", ";
            }
        }
        list = list + ")";
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        db.execSQL("DELETE from notes WHERE _id IN " + list + ";");
        db.setTransactionSuccessful();
        db.endTransaction();
        return true;
    }

}
