package com.example.llesson1.magnify;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.PeriodicSync;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.llesson1.MainActivity;
import com.example.llesson1.R;

import static android.content.pm.PackageManager.*;

public class camera extends AppCompatActivity {

    Camera camera;
    FrameLayout frameLayout;
    showCam showCam;
    Switch flashControl;
    SeekBar zoomDrag;

    //api that allow us to control the flashlight and zoom
    CameraManager cameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        zoomDrag = (SeekBar) findViewById(R.id.zoomDrag);
        flashControl = findViewById(R.id.flashButton);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        //check if device has camera
        if (getPackageManager().hasSystemFeature(FEATURE_CAMERA_ANY)) {

            //check if have flash or not
            if (getPackageManager().hasSystemFeature(FEATURE_CAMERA_FLASH)) {
                Toast.makeText(com.example.llesson1.magnify.camera.this, "this device has flash", Toast.LENGTH_SHORT).show();
                flashControl.setEnabled(true);
            } else {
                Toast.makeText(com.example.llesson1.magnify.camera.this, "This device has no flash feature", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(com.example.llesson1.magnify.camera.this, "This device has no camera", Toast.LENGTH_SHORT).show();
        }
        @Override
        public float getCurrentZoom () {
            return zoomLevel;
        }

        @Override
        public void setCurrentZoom ( float zoomLevel){
            Rect zoomRect = getZoomRect(zoomLevel);
            if (zoomRect != null) {
                try {
                    //you can try to add the synchronized object here
                    previewRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, zoomRect);
                    captureSession.setRepeatingRequest(previewRequestBuilder.build(), captureCallback, backgroundHandler);
                } catch (Exception e) {
                    Log.e(TAG, "Error updating preview: ", e);
                }
                this.zoomLevel = (int) zoomLevel;
            }
        }

        private Rect getZoomRect ( float zoomLevel){
            try {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(this.camera);
                float maxZoom = (characteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM)) * 10;
                Rect activeRect = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
                if ((zoomLevel <= maxZoom) && (zoomLevel > 1)) {
                    int minW = (int) (activeRect.width() / maxZoom);
                    int minH = (int) (activeRect.height() / maxZoom);
                    int difW = activeRect.width() - minW;
                    int difH = activeRect.height() - minH;
                    int cropW = difW / 100 * (int) zoomLevel;
                    int cropH = difH / 100 * (int) zoomLevel;
                    cropW -= cropW & 3;
                    cropH -= cropH & 3;
                    return new Rect(cropW, cropH, activeRect.width() - cropW, activeRect.height() - cropH);
                } else if (zoomLevel == 0) {
                    new Rect(0, 0, activeRect.width(), activeRect.height());
                    return;
                }
                return null;
            } catch (Exception e) {
                Log.e(TAG, "Error during camera init");
                return null;
            }
        }

        @Override
        public float getMaxZoom () {
            try {
                return (manager.getCameraCharacteristics(this.camera).get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM)) * 10;
            } catch (Exception e) {
                Log.e(TAG, "Error during camera init");
                return -1;
            }
            //open cam
            camera = Camera.open();

            //call the showCam class to be inititaed adn surface cerated method is calles
            showCam = new showCam(this, camera);
            frameLayout.addView(showCam);

            //seekbar zoom
            int MaxZoom = parameters.getMaxZoom();
            zoomDrag.setProgress(0);
            zoomDrag.setMax(maxZoom * 100);

            zoomDrag.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setZoom((int) (((progress * (1.0f / (maxZoom * 100))) * maxZoom)));
                    camera.setParameters(parameters);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            int mProgress;
            {
                minZoom = getMinZoom();
                maxZoom = getMaxZoom() - 1;
                final int zoomStep = 1;

                zoomDrag.setMax(Math.round(maxZoom - minZoom));
                zoomDrag.setOnSeekBarChangeListener(
                        new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                setCurrentZoom(Math.round(minZoom + (mProgress * zoomStep)));
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                            }
                            //to do auto generated mothod stub

                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                setCurrentZoom(Math.round(minZoom + (progress * zoomStep)));
                                if (fromUser) mProgress = progress;
                            }
                        }
                );
            }

            // turn the flash on and off
            flashControl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // if flash control is checked
                    if (isChecked) {
                        //back camera is 0 (get from cameraManager.getCameraIdList)
                        try {
                            cameraManager.setTorchMode("0", false);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            cameraManager.setTorchMode("0", true);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


        }


    }
}