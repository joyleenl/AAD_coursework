package com.example.llesson1.pillReminder.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.llesson1.Injection;
import com.example.llesson1.R;
import com.example.llesson1.pillReminder.ActivityUtils;


public class ReminderActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private int itemView;
    private ReminderPresenter mReminderPresenter;

    ReminderActivity(View itemView){
        super();
        bindViews (itemView);
    }
    private void bindViews (View root){
        toolbar = (Toolbar) root.findViewById(R.id.toolbar);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_actvity);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (!intent.hasExtra(ReminderFragment.EXTRA_ID)) {
            finish();
            return;
        }
        long id = intent.getLongExtra(ReminderFragment.EXTRA_ID, 0);
        ReminderFragment reminderFragment = (ReminderFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (reminderFragment == null) {
            reminderFragment = ReminderFragment.newInstance(id);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), reminderFragment, R.id.contentFrame);
        }

        //Create MedicinePresenter
        mReminderPresenter = new ReminderPresenter(Injection.provideMedicineRepository(com.example.llesson1.pillReminder.alarm.ReminderActivity.this), reminderFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mReminderPresenter != null) {
                mReminderPresenter.finishActivity();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mReminderPresenter != null) {
            mReminderPresenter.finishActivity();
        }
    }
}
