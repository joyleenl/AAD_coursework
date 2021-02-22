package com.example.llesson1.magnify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.llesson1.R;

import java.util.Arrays;

import static android.content.pm.PackageManager.FEATURE_CAMERA_ANY;
import static android.content.pm.PackageManager.FEATURE_CAMERA_FLASH;

public class camera2 extends AppCompatActivity {
    TextureView textureView;
    SeekBar zoomDrag;
    Zoom zoom;
    Switch flashControl;
    private int maxZoom;
    private static final SparseIntArray ORIENTATIONS= new SparseIntArray();


    //fro orientation
    static {
        ORIENTATIONS.append(Surface.ROTATION_0,90);
        ORIENTATIONS.append(Surface.ROTATION_90,0);
        ORIENTATIONS.append(Surface.ROTATION_180,270);
        ORIENTATIONS.append(Surface.ROTATION_270,180);
    }

    private String cameraId;
    CameraDevice cameraDevice;
    private Size imageDimensions;
    private ImageReader imageReader;
    CameraManager cameraManager;
    CameraCaptureSession cameraCaptureSession;
    CaptureRequest captureRequest;
    CaptureRequest.Builder captureRequestBuilder;
    Handler mBgHandler;
    HandlerThread mBgThread;
    CameraCharacteristics characteristics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        textureView = (TextureView) findViewById(R.id.viewFinder);
        zoomDrag = (SeekBar) findViewById(R.id.zoomDrag);
        textureView.setSurfaceTextureListener(textureListener);
        flashControl = (Switch) findViewById(R.id.flashButton);

        //check if device has camera
        if (getPackageManager().hasSystemFeature(FEATURE_CAMERA_ANY)) {

            //check if have flash or not
            if (getPackageManager().hasSystemFeature(FEATURE_CAMERA_FLASH)) {
                Toast.makeText(com.example.llesson1.magnify.camera2.this, "this device has flash", Toast.LENGTH_SHORT).show();
                flashControl.setEnabled(true);
            } else {
                Toast.makeText(com.example.llesson1.magnify.camera2.this, "This device has no flash feature", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(com.example.llesson1.magnify.camera2.this, "This device has no camera", Toast.LENGTH_SHORT).show();
        }

        //seekbar for zoom
        zoomDrag.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                zoom = new Zoom(characteristics);
                zoom.setZoom(captureRequestBuilder, (float) 4.0*progress/100);
                try {
                    cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, mBgHandler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                System.out.println("!!! start tracking");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.println("!!! stop tracking");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode== 101){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED)
            {
                Toast.makeText(this, "camera permission is necessary", Toast.LENGTH_SHORT).show();

            }
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
            //specify method to open cam
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {

            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

        }
    };

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened( CameraDevice camera) {
            //create camera preview
            cameraDevice = camera;

            try {
                createCameraPreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected( CameraDevice camera) {
            //close camera
            cameraDevice.close();
        }

        @Override
        public void onError( CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };
    //create and open camera preview

    private void createCameraPreview() throws  CameraAccessException{
        //create surface texture
        SurfaceTexture texture = textureView.getSurfaceTexture();
        //create buffer size of image
        texture.setDefaultBufferSize(imageDimensions.getHeight(),imageDimensions.getWidth());
        Surface surface = new Surface(texture);
        //initialize camrea request to the builder
        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        // add target to surface
        captureRequestBuilder.addTarget(surface);
        //update preview
        cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                if (cameraDevice ==null )
                {
                    return;
                }
                cameraCaptureSession = session;
                try {
                    updatePreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                Toast.makeText(getApplicationContext(), "Config changed", Toast.LENGTH_SHORT).show();

            }
        }, null);
    }

    private void updatePreview() throws CameraAccessException {
        if(cameraDevice==null) {
            return;
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(),null, mBgHandler);
    }

    private void openCamera() throws CameraAccessException {
        //specify camera manager to get acces to it
        CameraManager manager = (CameraManager)getSystemService(Context.CAMERA_SERVICE);
        //get cameraID
        cameraId = manager.getCameraIdList()[0];
        //get proprerty using camera id
        characteristics = manager.getCameraCharacteristics(cameraId);

        //specify param
        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        //specify image dimension

        imageDimensions = map.getOutputSizes(SurfaceTexture.class) [0];
        //check whether hv permission or not

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(camera2.this,new String[]{Manifest.permission.CAMERA},101);
            return;
        }
        //speicify camera id and specify state contact
        manager.openCamera(cameraId,stateCallback ,null);
    }

    @Override
    protected void onResume() {
        super.onResume();

        startBgThread();
        if (textureView.isAvailable())
        {
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        else
        {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    private void startBgThread() {

        mBgThread = new HandlerThread("Camera Background");
        mBgThread.start();
        mBgHandler = new Handler (mBgThread.getLooper());
    }

    @Override
    protected void onPause(){
        try {
            stopBgThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.onPause();
    }
    protected void stopBgThread() throws InterruptedException {
        mBgThread.quitSafely();

        mBgThread.join();
        mBgThread = null;
        mBgThread = null;
    }


    }

    /*/ turn the flash on and off
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


}*/
