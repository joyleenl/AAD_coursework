package com.example.llesson1;

import android.hardware.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;


public class camera extends AppCompatActivity {
    Camera camera;
    FrameLayout framelayout;
    showcamera showcamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.magnify);
        framelayout = (FrameLayout) findViewById(R.id.framelayout);
        //open camera
        camera = Camera.open();

        showcamera = new showcamera(this,camera);
    }
}

