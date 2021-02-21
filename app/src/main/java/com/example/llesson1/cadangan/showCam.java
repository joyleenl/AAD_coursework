package com.example.llesson1.cadangan;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.List;

public class showCam extends SurfaceView implements SurfaceHolder.Callback {

    Camera camera;
    SurfaceHolder holder;

    public showCam(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
camera.stopPreview();
camera.release();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        //set camera param
        Camera.Parameters parameters = camera.getParameters();
        // change orientation
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        Camera.Size mSize = null;

        for (Camera.Size size : sizes)
        {
            mSize= size;
        }
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            parameters.set("orientation", "potrait");
            camera.setDisplayOrientation(90);
            parameters.setRotation(90);
        } else {
            parameters.set("orientation", "landscapre");
            camera.setDisplayOrientation(0);
            parameters.setRotation(0);

        }
    parameters.setPictureSize(mSize.width,mSize.height);
        camera.setParameters(parameters);
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
