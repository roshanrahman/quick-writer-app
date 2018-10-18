package com.example.roshan.quickwriter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ViewNoteActivity extends AppCompatActivity {

    int noteId;
    TextView viewNoteTitle, viewNoteContent;
    DatabaseUtility databaseUtility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ThemeHelper.isDarkTheme(ViewNoteActivity.this)) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_view_note);
        viewNoteTitle = findViewById(R.id.viewNoteTitle);
        viewNoteContent = findViewById(R.id.viewNoteContent);
        final Toolbar toolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noteId = getIntent().getIntExtra("id", -1);
        if(noteId == -1) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                    alertBuilder.setTitle("An error occurred")
                    .setMessage("You were not supposed to see this. Something went wrong. We'll go back now.")
                            .setCancelable(false)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create()
                    .show();
        } else {
            populate();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.roshan.quickwriter.FontPreference", Context.MODE_PRIVATE);
        String fontFamily = sharedPreferences.getString("fontFamily", "robotoSlab");
        Typeface typeface;
        switch(fontFamily) {
            case "robotoSlab":
                 typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_slab);
                viewNoteTitle.setTypeface(typeface);
                viewNoteContent.setTypeface(typeface);
                break;
            case "roboto":
                viewNoteTitle.setTypeface(Typeface.DEFAULT);
                viewNoteContent.setTypeface(Typeface.DEFAULT);
                break;
            case "montserrat":
                typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserrat);
                viewNoteTitle.setTypeface(typeface);
                viewNoteContent.setTypeface(typeface);
                break;
            case "ptSerif":
                typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.pt_serif);
                viewNoteTitle.setTypeface(typeface);
                viewNoteContent.setTypeface(typeface);
                break;

                case "openSans":
                typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.open_sans);
                viewNoteTitle.setTypeface(typeface);
                viewNoteContent.setTypeface(typeface);
                break;

                default:
                    viewNoteTitle.setTypeface(Typeface.DEFAULT);
                    viewNoteContent.setTypeface(Typeface.DEFAULT);
        }
    }

    private void populate() {
        databaseUtility = DatabaseUtility.getInstance(this);
        SQLiteDatabase database = databaseUtility.getReadableDatabase();
        String args[] = {String.valueOf(noteId)};
        Cursor cursor = database.query("notes", null, "_id=?", args, null, null,null,null);
        if(cursor.getCount() > 0) {
            cursor.moveToLast();
            viewNoteTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            viewNoteContent.setText(cursor.getString(cursor.getColumnIndexOrThrow("content")));
            cursor.close();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuOptionEdit:
                Intent editExistingNoteIntent = new Intent(this, EditNoteActivity.class);
                editExistingNoteIntent.putExtra("id", noteId);
                startActivity(editExistingNoteIntent);
                return true;

            case R.id.menuOptionShare:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, viewNoteTitle.getText().toString() + "\n\n" + viewNoteContent.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share note via"));
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

    private void performDelete() {
        new AlertDialog.Builder(this).setTitle("Delete note?").setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseUtility = DatabaseUtility.getInstance(ViewNoteActivity.this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_note_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        populate();
        super.onResume();
    }
}
