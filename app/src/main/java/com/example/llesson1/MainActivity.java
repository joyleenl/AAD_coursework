package com.example.llesson1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.llesson1.EmergencyButton.Register;
import com.example.llesson1.EmergencyButton.dbHandler;
import com.example.llesson1.magnify.camera;
import com.example.llesson1.notes.activities.Notes;
import com.example.llesson1.pillReminder.medicine.MedicineActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationSettingsRequest;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;

import java.util.ArrayList;

import static android.Manifest.permission.CALL_PHONE;

public class MainActivity extends AppCompatActivity  {

Button addContact, emergency, howTo,notesButton , magnify,pill;
private FusedLocationProviderClient client;
dbHandler myDB;
private final int REQUEST_CHECK_CODE =8989;
private LocationSettingsRequest.Builder builder;
String latitude="", longitude= "";
private static final int REQUEST_LOCATION = 1;

LocationManager locationManager;
Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set the how to popup button activity
        howTo = (Button) findViewById(R.id.Howto);
        howTo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Popup.class);
                startActivity(i);
            }
        });
        addContact= (Button) findViewById(R.id.ContactList);
        emergency = (Button) findViewById(R.id.Emergency);
        myDB = new dbHandler (this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            onGPS();
        }
        else {
            startTrack();
        }
        addContact.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        }  );

        emergency.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getApplicationContext(),"emergency button activated",Toast.LENGTH_SHORT).show();
                return false;

            }
        });

        emergency.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loadData();
            }
        });


        //open notes
        notesButton = (Button) findViewById(R.id.noteButton);
        howTo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),Notes.class);
                startActivity(in);
            }
        });

        //open magnify
        magnify = (Button) findViewById(R.id.Magnify);
        howTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent(getApplicationContext(), camera.class);
                startActivity(inte);
            }
        });

        //open pill
        pill = (Button) findViewById(R.id.Pill);
        howTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentt = new Intent(getApplicationContext(), MedicineActivity.class);
                startActivity(intentt);
            }
        });


    }


//get the data fr db for emergency button
    private void loadData() {

        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListContent();
        //if there is no data in db prompt this
        if(data.getCount()==0){
            Toast.makeText(this, "there isn't any data", Toast.LENGTH_SHORT).show();
        }
        //if there is data,send this message
        else {
            String msg = "EMERGENCY! I NEED HELP!!! MY LOCATION LATITUDE:" + latitude +"LONGITUDE: " + longitude;
            String number ="";
            // get the number fr list of phone number
            while (data.moveToNext()){

                theList.add(data.getString(1));
                number= number+data.getString(1)  + (data.isLast()?"":";");
                call();
            }
            if(!theList.isEmpty()){
                sendSms(number,msg,true);
            }
       }
    }
// send the sms
    private void sendSms(String number, String msg, boolean b) {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        Uri.parse("smsto:"+number);
        smsIntent.putExtra("smsbody", msg);
        startActivity(smsIntent);
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:1000"));
        //if get permission from phone, then start  the activity
        if(ContextCompat.checkSelfPermission(getApplicationContext(),CALL_PHONE)== PackageManager.PERMISSION_GRANTED){
            startActivity(intent);
        }else {
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[]{CALL_PHONE},1);
            }
        }
    }
    // when starting the app
    private void startTrack() {

        if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION )
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }
        else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(locationGPS!=null){
                double lat = locationGPS.getLatitude();
                double lon = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude= String.valueOf(lon);
            }
            else {
                Toast.makeText(this, "Location Unknown", Toast.LENGTH_SHORT).show();
            }
        }

    }
    //enable GPS
    private void onGPS() {
        final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog =builder.create();
        alertDialog.show();
    }


}





