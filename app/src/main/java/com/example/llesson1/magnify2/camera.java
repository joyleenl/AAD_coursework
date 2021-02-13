package com.example.llesson1.magnify2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.core.impl.ImageCaptureConfig;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.core.impl.UseCaseConfig;
import androidx.camera.core.impl.UseCaseConfig.Builder;
import androidx.camera.core.internal.ThreadConfig;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.pm.PackageManager;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.ViewGroup;

import com.example.llesson1.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class camera extends AppCompatActivity {

    private int REQUEST_CODE_PERMISSION = 100;
    private final String[] REQUIRED_PERMISSIONS= new String[]{android.Manifest.permission.CAMERA};
    TextureView textureView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);

        getSupportActionBar().hide();

        textureView = (TextureView) findViewById(R.id.viewFinder);
        if(allPermissionGranted()){

            startCamera();
        }
        else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION);
        }
    }
    private Executor executor = Executors.newSingleThreadExecutor();

    private void startCamera() {

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();


    }

        //private void  startCamera(){

        //CameraX.unbindAll();
        //Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
        //Size Screen = new Size(textureView.getWidth(), textureView.getHeight());
        //PreviewConfig pConfig;
        //pConfig = new Builder().setTargetAspectRatio(aspectRatio).setTargetResolution(screen).build();
        //Preview preview = new Preview(pConfig);

        //preview.setOnPreviewOutputUpdateListener{
          //  new Preview.OnPreviewOutputUpdateListener(){
            //    @Override
              //  public void onUpdated(Preview.PreviewOuput output) {
                //    ViewGroup parent = (ViewGroup) textureView.getParent();
                  //  parent.removeView(textureView);
                    //parent.addView(textureView);

                    //textureView.setSurfaceTexture(output.getSurfaceTexture());
                    //updateTransform();
                //}
            //};
            

        //}
        //private void updateTransform() {

          //  Matrix mx = new Matrix();
            //float w = textureView.getMeasuredWidth();
            //float h = textureView.getMeasuredHeight();

            //float cx = w/2f;
            //float cy = h/2f;
            //int rotationDgr;
            //int rotation = (int)textureView.getRotation();

            //switch (rotation){
              //  case Surface.ROTATION_0;
                //    rotationDgr= 0;
                  //  break;
                //case Surface.ROTATION_90;
                  //  rotationDgr= 90 ;
                    //break;

                //case Surface.ROTATION_180;
                  //  rotationDgr= 180;
                    //break;

                //case Surface.ROTATION_270;
                  //  rotationDgr= 270;
                    //break;
                //default:
                  //  return;


           // }
            //mx.postRotate((float)rotationDgr, cx , cy);
            //textureView.setTransform();
       // }

    private boolean allPermissionGranted(){
        for (String permission:REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
}