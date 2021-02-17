package com.example.llesson1.pillReminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.loader.app.LoaderManager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.llesson1.R;
import com.example.llesson1.notes.database.NotesDatabase;
import com.example.llesson1.notes.entities.Note;
import com.example.llesson1.pillReminder.data.views.DayViewCheckBox;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import static androidx.camera.core.CameraX.getContext;

public class addReminder extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener ,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_VEHICLE_LOADER = 0;

    private Toolbar mToolbar;
    private EditText mMedecineName;
    private String mMedecineNameText;
    private String mHourText;
    private String mMinuteText;
    private String doseUnit;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;
    private DayViewCheckBox everyDay;
    private DayViewCheckBox dvSunday;
    private DayViewCheckBox dvMonday;
    private DayViewCheckBox dvTuesday;
    private DayViewCheckBox dvWednesday;
    private DayViewCheckBox dvThursday;
    private DayViewCheckBox dvFriday;
    private DayViewCheckBox dvSaturday;
    private LinearLayout checkboxLayout;
    private TextView tvMedecineTime;
    private EditText tvDoseQuantity;
    private AppCompatSpinner spinnerDoseUnit;
    private List<String> doseUnitList;
    private int hour,minute;
    private boolean[] dayOfWeekList = new boolean[7];
    private Uri mCureentReminderUri;
    private boolean mVehicleHasChanged = false;

    //values for orientation
    private static final String KEY_PILL_NAME = "pill_name_key";
    private static final String KEY_HOUR = "hour_key";
    private static final String KEY_DAY_WEEK = "day_of_week_key";
    private static final String KEY_MINUTE = "minute_key";
    private static final String KEY_DOSE_QUANTITY = "dose_quantity_key";
    private static final String KEY_DOSE_UNITS = "dose_units";

    // constant value in millisecond
    private static final long milMinute = 60000L;
    private static final long milHour = 360000L;

    private View.OnTouchListener mTouchListener = (view, motionevent) {
            mVehicleHasChanged = true;
            return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        ImageView ImageBack = findViewById(R.id.Imageback);
        ImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        mCureentReminderUri = intent.getData();
        //initialize views
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvMedecineTime = (TextView) findViewById(R.id.tv_medicine_time);
        tvDoseQuantity = (EditText) findViewById(R.id.tv_dose_quantity);
        spinnerDoseUnit = (AppCompatSpinner) findViewById(R.id.spinner_dose_units);
        checkboxLayout = (LinearLayout) findViewById(R.id.checkbox_layout);
        dvSunday = (DayViewCheckBox) findViewById(R.id.dv_sunday);
        dvMonday = (DayViewCheckBox) findViewById(R.id.dv_monday);
        dvTuesday = (DayViewCheckBox) findViewById(R.id.dv_tuesday);
        dvWednesday = (DayViewCheckBox) findViewById(R.id.dv_wednesday);
        dvThursday = (DayViewCheckBox) findViewById(R.id.dv_thursday);
        dvFriday = (DayViewCheckBox) findViewById(R.id.dv_friday);
        dvSaturday = (DayViewCheckBox) findViewById(R.id.dv_saturday);
        everyDay = (DayViewCheckBox) findViewById(R.id.every_day);
        mMedecineName = (EditText) findViewById(R.id.pillName);

        //Setup reminde medicine name edit text

        mMedecineName.addTextChangedListener(new TextWatcher() {
            @Override
            public void  beforeTextChanged(CharSequence s, int start, int count, int after){}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMedecineNameText = s.toString().trim();
                mMedecineName.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

   setSupportActionBar(mToolbar);
   getSupportActionBar().set("Add Reminder");
   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
   getSupportActionBar().setHomeButtonEnabled(true);

    }
    private View.OnClickListener findViewById
    private class  = (DayViewCheckBox findViewById) ({R.id.every_day, R.id.dv_monday, R.id.dv_tuesday, R.id.dv_wednesday,
            R.id.dv_thursday, R.id.dv_friday, R.id.dv_saturday, R.id.dv_sunday})
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        /** Checking which checkbox was clicked */
        switch (view.getId()) {
            case R.id.dv_monday:
                if (checked) {
                    dayOfWeekList[1] = true;
                } else {
                    dayOfWeekList[1] = false;
                    everyDay.setChecked(false);
                }
                break;
            case R.id.dv_tuesday:
                if (checked) {
                    dayOfWeekList[2] = true;
                } else {
                    dayOfWeekList[2] = false;
                    everyDay.setChecked(false);
                }
                break;
            case R.id.dv_wednesday:
                if (checked) {
                    dayOfWeekList[3] = true;
                } else {
                    dayOfWeekList[3] = false;
                    everyDay.setChecked(false);
                }
                break;
            case R.id.dv_thursday:
                if (checked) {
                    dayOfWeekList[4] = true;
                } else {
                    dayOfWeekList[4] = false;
                    everyDay.setChecked(false);
                }
                break;
            case R.id.dv_friday:
                if (checked) {
                    dayOfWeekList[5] = true;
                } else {
                    dayOfWeekList[5] = false;
                    everyDay.setChecked(false);
                }
                break;
            case R.id.dv_saturday:
                if (checked) {
                    dayOfWeekList[6] = true;
                } else {
                    dayOfWeekList[6] = false;
                    everyDay.setChecked(false);
                }
                break;
            case R.id.dv_sunday:
                if (checked) {
                    dayOfWeekList[0] = true;
                } else {
                    dayOfWeekList[0] = false;
                    everyDay.setChecked(false);
                }
                break;
            case R.id.every_day:
                LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.checkbox_layout);
                for (int i = 0; i < ll.getChildCount(); i++) {
                    View v = ll.getChildAt(i);
                    ((DayViewCheckBox) v).setChecked(checked);
                    onCheckboxClicked(v);
                }
                break;
        }
    }

    @OnClick(R.id.tv_medicine_time)
    void onMedicineTimeClick() {
        showTimePicker();
    }

    private void showTimePicker() {
        Calendar mCurrentTime = Calendar.getInstance();
        hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                tvMedecineTime.setText(String.format(Locale.getDefault(), "%d:%d", selectedHour, selectedMinute));
            }
        }, hour, minute, false);//No 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void setCurrentTime() {
        Calendar mCurrentTime = Calendar.getInstance();
        hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        minute = mCurrentTime.get(Calendar.MINUTE);

        tvMedecineTime.setText(String.format(Locale.getDefault(), "%d:%d", hour, minute));
    }

    private void setSpinnerDoseUnits() {
        doseUnitList = Arrays.asList(getResources().getStringArray(R.array.medications_shape_array));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_dropdown_item_1line, doseUnitList);
        spinnerDoseUnit.setAdapter(adapter);
    }

    @OnItemSelected(R.id.spinner_dose_units)
    void onSpinnerItemSelected(int position) {
        if (doseUnitList == null || doseUnitList.isEmpty()) {
            return;
        }

        doseUnit = doseUnitList.get(position);
        Log.d("TAG", doseUnit);
    }

    private View.OnClickListener setClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int checkBoxCounter = 0;

            String pill_name = mMedecineName.getText().toString();
            String doseQuantity = tvDoseQuantity.getText().toString();

            Calendar takeTime = Calendar.getInstance();
            Date date = takeTime.getTime();
            String dateString = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(date);

            /** Updating model */
            pillReminderMain alarm = new pillReminderMain();
            int alarmId = new Random().nextInt(100);

            /** If Pill does not already exist */
            if (!mPresenter.isMedicineExits(pill_name)) {
                Pills pill = new Pills();
                pill.setPillName(pill_name);
                alarm.setDateString(dateString);
                alarm.setHour(hour);
                alarm.setMinute(minute);
                alarm.setPillName(pill_name);
                alarm.setDayOfWeek(dayOfWeekList);
                alarm.setDoseUnit(doseUnit);
                alarm.setDoseQuantity(doseQuantity);
                alarm.setAlarmId(alarmId);
                pill.addAlarm(alarm);
                long pillId = mPresenter.addPills(pill);
                pill.setPillId(pillId);
                mPresenter.saveMedicine(alarm, pill);
            } else { // If Pill already exists
                Pills pill = mPresenter.getPillsByName(pill_name);
                alarm.setDateString(dateString);
                alarm.setHour(hour);
                alarm.setMinute(minute);
                alarm.setPillName(pill_name);
                alarm.setDayOfWeek(dayOfWeekList);
                alarm.setDoseUnit(doseUnit);
                alarm.setDoseQuantity(doseQuantity);
                alarm.setAlarmId(alarmId);
                pill.addAlarm(alarm);
                mPresenter.saveMedicine(alarm, pill);

            }