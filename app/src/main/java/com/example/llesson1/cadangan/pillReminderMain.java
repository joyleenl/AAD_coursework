package com.example.llesson1.cadangan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toolbar;

import com.example.llesson1.R;
import com.example.llesson1.pillReminder.data.source.AlarmReminderDBHelper;
import com.getbase.floatingactionbutton.FloatingActionButton;

/*public class pillReminderMain extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private FloatingActionButton mAddReminderButton;
    private Toolbar mToolbar;
    AlarmCursorAdapter mCursorAdapter;
    alarmReminderDBHelper alarmReminderDBHelper = new AlarmReminderDBHelper(this);
    ListView reminderListView;
    ProgressDialog progressDialog;

    private static final int VEHICLE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_reminder_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("PillReminder");

        reminderListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        reminderListView.setEmptyView(emptyView);
        mCursorAdapter = new AlarmCursorAdapter(this,null);
        reminderListView.setAdapter(mCursorAdapter);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(pillReminderMain.this, addReminder.class);
                Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, id);

                //set the URI on the daya field of the intent
                intent.setData(currentVehicleUri);
                startActivity(intent);
            }
        });
        mAddReminderButton = (FloatingActionButton) findViewById(R.id.fab);

        mAddReminderButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), addReminder.class);
            startActivity(intent);
        });
        getLoaderManager().initLoader(VEHICLE_LOADER, null, this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){
        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_PILL_NAME,
                AlarmReminderContract.AlarmReminderEntry.KEY_HOUR,
                AlarmReminderContract.AlarmReminderEntry.KEY_MINUTE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DAY_WEEK,
                AlarmReminderContract.AlarmReminderEntry.KEY_DOSE_QUANTITY,
                AlarmReminderContract.AlarmReminderEntry.KEY_DOSE_UNITS

        };
        return new CursorLoader(this, //parent activity context
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, //provider content URI to query
                projection, //columns to incl in the resulting cursor
                null, //no selection cause
                null, //no selection argument
                null ); //default sort order

        }
        @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        mCursorAdapter.swapCursor(cursor);
    }
    @Override
    public void onLoaderReset (Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null)
    }
}

 */