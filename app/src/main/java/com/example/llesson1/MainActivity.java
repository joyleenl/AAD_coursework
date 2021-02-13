package com.example.llesson1;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.llesson1.notes.activities.Notes;
import com.kishan.askpermission.AskPermission;
import com.kishan.askpermission.ErrorCallback;
import com.kishan.askpermission.PermissionCallback;
import com.kishan.askpermission.PermissionInterface;

public class MainActivity extends AppCompatActivity implements PermissionCallback, ErrorCallback {


    private static final int REQUEST_PERMISSIONS = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reqPermission();
        //open notes
        Button notesButton = (Button) findViewById(R.id.noteButton);
        notesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Notes.class);
                startActivity(intent);
            }

        });


    }
    //request for permission
    private void reqPermission() {
        new AskPermission.Builder(this).setPermissions(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                .setCallback(this)
                .setErrorCallback(this)
                .request(REQUEST_PERMISSIONS);

    }
    //show the settings
    @Override
    public void onShowSettings(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app. Open setting screen?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onSettingsShown();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    // show rational dialog to tell user that they need to grant permission for this app
    @Override
    public void onShowRationalDialog(final PermissionInterface permissionInterface, int requestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permissions for this app.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                permissionInterface.onDialogShown();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    //display "permission granted"
    @Override
    public void onPermissionsGranted(int requestCode) {
        Toast.makeText(this, "Permission Granted.", Toast.LENGTH_LONG).show();
    }
    //display "permission denied"
    @Override
    public void onPermissionsDenied(int requestCode) {
        Toast.makeText(this, "Permission Denied.", Toast.LENGTH_LONG).show();
    }
}





