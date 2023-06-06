package com.starrynight.tourapiproject.starPage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.starrynight.tourapiproject.R;

import java.util.Objects;

/**
 * className :  StarCameraActivity
 * description : 별자리 가이드라인 카메라  Activity
 * modification : 2022.11.01(박진혁) 액티비티 생성
 * author : jinhyeok
 * date : 2022-11-01
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2022-11-01      jinhyeok      액티비티 생성
 */
public class StarCameraActivity extends AppCompatActivity implements SensorEventListener {

    CameraSurfaceView cameraSurfaceView;
    int PERMISSIONS_REQUEST_CODE = 101;
    String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA}; // 권한 코드
    ImageView guideLine;

    //가이드 라인 회전 관련 Sensor
    private SensorManager mSensorManger;
    private Sensor mAcclerometer;
    private Sensor mMagnetometer;
    private final float[] mLastAcceleromter = new float[3];
    private final float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private final float[] mR = new float[9];
    private final float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_camera);
        Button onOffButton = findViewById(R.id.onoffButton);
        guideLine = findViewById(R.id.guideLine);
        cameraSurfaceView =findViewById(R.id.surfaceView);

        //Sensor 선언
        mSensorManger = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAcclerometer = mSensorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManger.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);


        if (allPermissionsGranted()) {
        } else {
            ActivityCompat.requestPermissions(this, CAMERA_PERMISSION, PERMISSIONS_REQUEST_CODE);
        }

        //가이드라인 onoff 버튼
        onOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(guideLine.getVisibility()==View.VISIBLE){
                    guideLine.setVisibility(View.INVISIBLE);
                    onOffButton.setEnabled(false);
                    Handler handle = new Handler();
                    handle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onOffButton.setEnabled(true);
                        }
                    },1000);
                }else{guideLine.setVisibility(View.VISIBLE);}
            }
        });

    }


    // 카메라 관련 허가 확인 boolean
    private boolean allPermissionsGranted() {

        for (String permission : CAMERA_PERMISSION) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManger.registerListener((SensorEventListener) this, mAcclerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManger.registerListener((SensorEventListener) this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManger.unregisterListener((SensorEventListener) this, mAcclerometer);
        mSensorManger.unregisterListener((SensorEventListener) this, mMagnetometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAcclerometer) {
            System.arraycopy(event.values, 0, mLastAcceleromter, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAcceleromter, mLastMagnetometer);
            float azimuthinDegress = (int) (Math.toDegrees(SensorManager.getOrientation(mR, mOrientation)[0]) + 360) % 360;
            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,
                    -azimuthinDegress,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            ra.setDuration(250);
            ra.setFillAfter(true);
            guideLine.startAnimation(ra);
            mCurrentDegree = -azimuthinDegress;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}