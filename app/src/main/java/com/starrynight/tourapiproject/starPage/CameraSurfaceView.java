package com.starrynight.tourapiproject.starPage;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * className :  CameraSurfaceView
 * description : 별자리 카메라 기능 구현 View
 * modification : 2022.08.01(박진혁)
 * author : jinhyeok
 * date : 2022-11-01
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2022-11-01      jinhyeok      최초생성
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder holder;
    Camera camera = null;
    private Camera.CameraInfo cameraInfo;
    int cameraID; // 카메라 전면 후면 id
    private int mDisplayOrientation; // 카메라 각도

    public CameraSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context){
        holder = getHolder();
        holder.addCallback(this);
        cameraID=0;

        mDisplayOrientation =((Activity)context).getWindowManager().getDefaultDisplay().getRotation();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open(cameraID);
        Camera.CameraInfo mCameraInfo= new Camera.CameraInfo();
        Camera.getCameraInfo(cameraID,mCameraInfo);
        cameraInfo= mCameraInfo;

        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        int orientation = calculatePreviewOrientation(cameraInfo,mDisplayOrientation);
        camera.setDisplayOrientation(orientation);//각도 조정하기

        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public int calculatePreviewOrientation (Camera.CameraInfo info,int rotation){
        int degrees=0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;

        if(info.facing==Camera.CameraInfo.CAMERA_FACING_FRONT){
            result =(info.orientation +degrees)%360;
            result =(360-result)%360;
        }else{
            result=(info.orientation-degrees+360)%360;
        }

        return result;
    }
}
