package com.example.llesson1.pillReminder.medicine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.example.llesson1.Injection;
import com.example.llesson1.R;
import com.example.llesson1.pillReminder.ActivityUtils;

import com.example.llesson1.pillReminder.report.MonthlyReportActivity;
import com.example.llesson1.pillReminder.report.MonthlyReportContract;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MedicineActivity extends AppCompatActivity {


   private CompactCalendarView mCompactCalendarView;

    private TextView datePickerTextView;

    public RelativeLayout datePickerButton;

   private Toolbar toolbar;

    public CollapsingToolbarLayout collapsingToolbarLayout;

    private AppBarLayout appBarLayout;

    public FrameLayout contentFrame;

    public FloatingActionButton fabAddTask;

    public CoordinatorLayout coordinatorLayout;

    private ImageView arrow;

    private com.example.llesson1.pillReminder.medicine.MedicinePresenter presenter;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", /*Locale.getDefault()*/Locale.ENGLISH);

    private boolean isExpanded = false;

    private View.OnClickListener datePickerOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onDatePickerButtonClicked();
        }
    };


    }
    private void bindViews (View root) {
        mCompactCalendarView = (CompactCalendarView) root.findViewById(R.id.compactcalendar_view);
        datePickerTextView = (TextView) root.findViewById(R.id.date_picker_text_view);
        datePickerButton = (RelativeLayout) root.findViewById(R.id.date_picker_button);
        toolbar =(Toolbar) root.findViewById(R.id.toolbar);
        collapsingToolbarLayout=(CollapsingToolbarLayout) root.findViewById(R.id.collapsingToolbarLayout);
        appBarLayout = (AppBarLayout) root.findViewById(R.id.app_bar_layout);
        contentFrame = (FrameLayout) root.findViewById(R.id.contentFrame);
        fabAddTask = (FloatingActionButton)root.findViewById(R.id.fab_add_task);
        coordinatorLayout = (CoordinatorLayout)root.findViewById(R.id.coordinatorLayout);
        arrow = (ImageView) root.findViewById(R.id.date_picker_arrow);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicince);
        bind binding = MedicineActivity.inflate(inflater,this, true);
        View view = binding.getRoot;
        setContentView(view);
        setSupportActionBar(toolbar);
        datePickerButton = new RelativeLayout(this);

        datePickerButton.setOnClickListener(datePickerOnClick);

        mCompactCalendarView.setLocale(TimeZone.getDefault(), /*Locale.getDefault()*/Locale.ENGLISH);

        mCompactCalendarView.setShouldDrawDaysHeader(true);

        mCompactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                setSubtitle(dateFormat.format(dateClicked));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateClicked);

                int day = calendar.get(Calendar.DAY_OF_WEEK);

                if (isExpanded) {
                    ViewCompat.animate(arrow).rotation(0).start();
                } else {
                    ViewCompat.animate(arrow).rotation(180).start();
                }
                isExpanded = !isExpanded;
                appBarLayout.setExpanded(isExpanded, true);
                presenter.reload(day);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setSubtitle(dateFormat.format(firstDayOfNewMonth));
            }
        });
        setCurrentDate(new Date());
        com.example.llesson1.pillReminder.medicine.MedicineFragment medicineFragment = (com.example.llesson1.pillReminder.medicine.MedicineFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (medicineFragment == null) {
            medicineFragment = com.example.llesson1.pillReminder.medicine.MedicineFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), medicineFragment, R.id.contentFrame);
        }

        //Create MedicinePresenter
        presenter = new com.example.llesson1.pillReminder.medicine.MedicinePresenter(Injection.provideMedicineRepository(com.example.llesson1.pillReminder.medicine.MedicineActivity.this), medicineFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.medicine_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_stats) {
            Intent intent = new Intent(this, MonthlyReportActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCurrentDate(Date date) {
        setSubtitle(dateFormat.format(date));
        mCompactCalendarView.setCurrentDate(date);
    }

    public void setSubtitle(String subtitle) {
        datePickerTextView.setText(subtitle);
    }


    private void onDatePickerButtonClicked() {
        if (isExpanded) {
            ViewCompat.animate(arrow).rotation(0).start();
        } else {
            ViewCompat.animate(arrow).rotation(180).start();
        }

        isExpanded = !isExpanded;
        appBarLayout.setExpanded(isExpanded, true);
    }
}
