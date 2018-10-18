package com.example.roshan.quickwriter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class EditNoteActivity extends AppCompatActivity {

    DatabaseUtility databaseUtility;
    int noteId;
    EditText editNoteText;
    EditText editNoteTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean isNewNote = false;
        super.onCreate(savedInstanceState);
        if(ThemeHelper.isDarkTheme(EditNoteActivity.this)) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_edit_note);
        editNoteText = findViewById(R.id.noteText);
        editNoteTitle = findViewById(R.id.noteTitle);
        final Toolbar toolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        databaseUtility = DatabaseUtility.getInstance(EditNoteActivity.this);
        noteId = getIntent().getIntExtra("id", -1);
        SQLiteDatabase database = databaseUtility.getReadableDatabase();
        if(noteId != -1) {
            String args[] = {String.valueOf(noteId)};
            Cursor cursor = database.query("notes", null, "_id=?", args, null, null, null, null);
            cursor.moveToLast();
            editNoteText.setText(cursor.getString(cursor.getColumnIndexOrThrow("content")));
            editNoteTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        } else {
            noteId = databaseUtility.getId();
            isNewNote = true;
        }
        final int workingNoteId = noteId;
        if(editNoteTitle.getText().toString().trim().equals("Untitled Note")) {
            editNoteTitle.setText("");
        }
        editNoteText.addTextChangedListener(new TextWatcher() {

            boolean isTyping = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            private Timer timer = new Timer();
            private final long DELAY = 1000; // milliseconds

            public void changeSubtitle(String s) {
                toolbar.setSubtitle(s);
            }

            @Override
            public void afterTextChanged(final Editable s) {
                Log.d("", "");
                if(!isTyping) {
                    Log.d("User:", "started typing");
                    // Send notification for start typing event
                    isTyping = true;
                    changeSubtitle("Saving..");
                }
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                isTyping = false;
                                Log.d("User: ", "stopped typing");
                                //send notification for stopped typing event
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        databaseUtility.updateNote(workingNoteId, editNoteTitle.getText().toString().trim(), editNoteText.getText().toString().trim());
                                        changeSubtitle("All changes saved");
                                        Runnable r = new Runnable() {
                                            @Override
                                            public void run(){
                                                changeSubtitle(null);
                                            }
                                        };

                                        Handler h = new Handler();
                                        h.postDelayed(r, 1500);
                                    }
                                });
                            }
                        },
                        DELAY
                );
            }
        });
        editNoteTitle.addTextChangedListener(new TextWatcher() {

            boolean isTyping = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            private Timer timer = new Timer();
            private final long DELAY = 1500; // milliseconds

            public void changeSubtitle(String s) {
                toolbar.setSubtitle(s);
            }

            @Override
            public void afterTextChanged(final Editable s) {
                Log.d("", "");
                if(!isTyping) {
                    Log.d("User:", "started typing");
                    // Send notification for start typing event
                    isTyping = true;
                    changeSubtitle("Saving..");
                }
                timer.cancel();
                timer = new Timer();
                timer.schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                isTyping = false;
                                Log.d("User: ", "stopped typing");
                                //send notification for stopped typing event
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        databaseUtility.updateNote(workingNoteId, editNoteTitle.getText().toString().trim(), editNoteText.getText().toString().trim());
                                        changeSubtitle("All changes saved");
                                        Runnable r = new Runnable() {
                                            @Override
                                            public void run(){
                                                changeSubtitle(null);
                                            }
                                        };

                                        Handler h = new Handler();
                                        h.postDelayed(r, 2000);
                                    }
                                });
                            }
                        },
                        DELAY
                );
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.roshan.quickwriter.FontPreference", Context.MODE_PRIVATE);
        String fontFamily = sharedPreferences.getString("fontFamily", "robotoSlab");
        Typeface typeface;
        switch(fontFamily) {
            case "robotoSlab":
                typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_slab);
                editNoteTitle.setTypeface(typeface);
                editNoteText.setTypeface(typeface);
                break;
            case "roboto":
                editNoteTitle.setTypeface(Typeface.DEFAULT);
                editNoteText.setTypeface(Typeface.DEFAULT);
                break;
            case "montserrat":
                typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat);
                editNoteTitle.setTypeface(typeface);
                editNoteText.setTypeface(typeface);
                break;
            case "ptSerif":
                typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.pt_serif);
                editNoteTitle.setTypeface(typeface);
                editNoteText.setTypeface(typeface);
                break;

            case "openSans":
                typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.open_sans);
                editNoteTitle.setTypeface(typeface);
                editNoteText.setTypeface(typeface);
                break;

            default:
                editNoteTitle.setTypeface(Typeface.DEFAULT);
                editNoteText.setTypeface(Typeface.DEFAULT);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuOptionDone:
                databaseUtility.updateNote(noteId, editNoteTitle.getText().toString().trim(), editNoteText.getText().toString().trim());
                finish();
                return true;

            case R.id.menuOptionDelete:
                performDelete();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_note_menu, menu);
        return true;
    }

    private void performDelete() {
        new AlertDialog.Builder(this).setTitle("Delete note?").setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseUtility = DatabaseUtility.getInstance(EditNoteActivity.this);
                        databaseUtility.deleteNote(noteId);
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
    }
}
