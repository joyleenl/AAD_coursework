package com.example.llesson1.EmergencyButton;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.llesson1.R;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainEmergency extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationManagerInterface {

    private String [] arraySpinner;
    public static final String TAG =MainEmergency.class.getSimpleName();
    private String latitude, longitude ="";

    SmartLocationManager mLocationManager;
    TextView mLocalTV, mLocationProviderTV, mlocationTimeTV;

    private SQLiteDatabase db;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDatabase();
//access to gps
        mLocationManager = new SmartLocationManager(getApplicationContext(), this,this,SmartLocationManager.ALL_PROVIDES, LocationRequest);

        FloatingActionButton fab =(FloatingActionButton) findViewById(R.id.fabEmergency)
    }
}
