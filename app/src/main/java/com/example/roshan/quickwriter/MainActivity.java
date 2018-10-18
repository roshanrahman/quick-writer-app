package com.example.roshan.quickwriter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Cursor cursor;
    ListView notesList;
    SQLiteDatabase database;
    DatabaseUtility databaseUtility;
    MyNoteRecyclerViewAdapter adapter;
    LinearLayout emptyView;
    ArrayList<String> selectedIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ThemeHelper.isDarkTheme(MainActivity.this)) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.appToolbar);
        toolbar.setOverflowIcon(getDrawable(R.drawable.ic_more_vert_24dp));
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        databaseUtility = DatabaseUtility.getInstance(this);
        database = databaseUtility.getReadableDatabase();
        cursor = database.rawQuery("SELECT * FROM notes;", null);
        emptyView = findViewById(R.id.emptyView);
        showOrHideEmptyView(cursor.getCount());
        notesList = findViewById(R.id.notesList);
        adapter =  new MyNoteRecyclerViewAdapter(this, cursor);
        notesList.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int noteId = databaseUtility.createNewNote();
                Intent newNoteIntent = new Intent(getApplicationContext(), EditNoteActivity.class);
                newNoteIntent.putExtra("id", noteId);
                databaseUtility.close();
                startActivity(newNoteIntent);
            }
        });
        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int noteId = Integer.parseInt(view.getTag().toString());
                Intent viewNoteIntent = new Intent(getApplicationContext(), ViewNoteActivity.class);
                viewNoteIntent.putExtra("id", noteId);
                databaseUtility.close();
                startActivity(viewNoteIntent);
            }
        });
        adapter.setFilterQueryProvider(new FilterQueryProvider() {

            public Cursor runQuery(CharSequence s) {
                Cursor cur = database.rawQuery("SELECT * FROM notes WHERE title LIKE ? OR content LIKE ?;", new String[] {"%"+ s.toString()+ "%" });
                Log.i("FILTER: ", "Filter working, obtained" + cur.getCount());

                return cur;
            }

        });
        notesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        notesList.setItemsCanFocus(false);
        notesList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                View currentItem = (View) notesList.getChildAt(position);
                if(checked) {
                    if(ThemeHelper.isDarkTheme(MainActivity.this))
                    currentItem.setBackgroundColor(getResources().getColor(R.color.pressedDark));
                    else
                        currentItem.setBackgroundColor(getResources().getColor(R.color.pressed));
                    Log.i("NOTESAPP: Checked", currentItem.getTag().toString());
                    selectedIds.add(currentItem.getTag().toString());
                } else {
                    currentItem.setBackgroundColor(getResources().getColor(R.color.defaultColor));
                    Log.i("NOTESAPP: Unchecked", currentItem.getTag().toString());
                    selectedIds.remove(currentItem.getTag().toString());
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.selected, menu);
                selectedIds = new ArrayList<>();
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuOptionDelete:
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Delete selected notes?")
                                .setMessage("Are you sure you want to permanently delete these notes?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteSelectedItems();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .setCancelable(false)
                                .create().show();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                for(int i = 0; i < notesList.getCount(); i++) {
                    notesList.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
    }

    private void deleteSelectedItems() {
        Log.i("NOTESAPP: To be deleted: ", selectedIds.toString());
        database = databaseUtility.getWritableDatabase();
        databaseUtility.deleteNotes(selectedIds);
        cursor = database.rawQuery("SELECT * FROM notes;", null);
        adapter.changeCursor(cursor);
        adapter.notifyDataSetChanged();
        showOrHideEmptyView(cursor.getCount());
    }

    private void showOrHideEmptyView(int count) {
        if(count > 0) {
            emptyView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuOptionSettings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.menuOptionAbout:
                startActivity(new Intent(this, AboutActivity.class));
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
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menuOptionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        database = databaseUtility.getReadableDatabase();
        cursor = database.rawQuery("SELECT * FROM notes;", null);
        adapter = new MyNoteRecyclerViewAdapter(this, cursor);
        adapter.setFilterQueryProvider(new FilterQueryProvider() {

            public Cursor runQuery(CharSequence s) {
                Cursor cur = database.rawQuery("SELECT * FROM notes WHERE title LIKE ? OR content LIKE ?;", new String[] {"%"+ s.toString()+ "%", "%"+ s.toString()+ "%" });
                Log.i("FILTER: ", "Filter working, obtained" + cur.getCount());

                return cur;
            }

        });
        notesList.setAdapter(adapter);
        showOrHideEmptyView(cursor.getCount());
        super.onResume();
    }
}
