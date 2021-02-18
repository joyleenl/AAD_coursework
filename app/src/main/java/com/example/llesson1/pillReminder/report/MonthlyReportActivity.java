package com.example.llesson1.pillReminder.report;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.llesson1.Injection;
import com.example.llesson1.R;
import com.example.llesson1.pillReminder.ActivityUtils;


public class MonthlyReportActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_TYPE = "current_filtering_type";


     private Toolbar toolbar;

    private MonthlyReportPresenter presenter;

    MonthlyReportActivity (View itemView){
    super();
    bindViews(itemView);
    }
    private void bindViews (View root){
        toolbar  = (Toolbar) root.findViewById(R.id.toolbar);
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_report);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_clear);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        //Create Fragment
        MonthlyReportFragment monthlyReportFragment = (MonthlyReportFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (monthlyReportFragment == null) {
            com.example.llesson1.pillReminder.report.MonthlyReportFragment = MonthlyReportFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), monthlyReportFragment, R.id.contentFrame);
        }

        //Create TaskPresenter
        presenter = new MonthlyReportPresenter(Injection.provideMedicineRepository(com.example.llesson1.pillReminder.report.MonthlyReportActivity.this), monthlyReportFragment);

        //Load previous saved Instance
        if (savedInstanceState != null) {
            FilterType taskFilterType = (FilterType) savedInstanceState.getSerializable(CURRENT_FILTERING_TYPE);
            presenter.setFiltering(taskFilterType);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_TYPE, presenter.getFilterType());
        super.onSaveInstanceState(outState);
    }
}
