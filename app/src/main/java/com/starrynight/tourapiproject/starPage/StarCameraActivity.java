package com.starrynight.tourapiproject.starPage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.starrynight.tourapiproject.R;

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
    TextView az,ro;
    LinearLayout test;

    //가이드 라인 회전 관련 Sensor
    private SensorManager mSensorManger;
    private Sensor mAcclerometer;
    private Sensor mMagnetometer;
    private Sensor mGgyroSensor = null;
    private Sensor mPressure;
    private float[] mLastAcceleromter = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private final float[] mR = new float[9];
    private final float[] adjustedR = new float[9];
    private final float[] mL = new float[9];
    private final float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    private SensorEventListener mGyroLis;
    private float yrAngle;

    boolean north=false;
    boolean northeast=false;
    boolean polar = false;
    float azimuthinDegress=0;

    private double RAD2DGR = 180 / Math.PI;
    private static final float ALPHA = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_camera);
        Button onOffButton = findViewById(R.id.onoffButton);
        guideLine = findViewById(R.id.guideLine);
        test = findViewById(R.id.guideLine2);
        cameraSurfaceView =findViewById(R.id.surfaceView);
        az = findViewById(R.id.azimuth);
        ro = findViewById(R.id.roll);

        //Sensor 선언
        mSensorManger = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAcclerometer = mSensorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManger.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mGgyroSensor = mSensorManger.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mPressure = mSensorManger.getDefaultSensor(Sensor.TYPE_GRAVITY);

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
        mSensorManger.registerListener(mGyroLis, mGgyroSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManger.registerListener((SensorEventListener) this,mPressure, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManger.unregisterListener((SensorEventListener) this, mAcclerometer);
        mSensorManger.unregisterListener((SensorEventListener) this, mMagnetometer);
        mSensorManger.unregisterListener(mGyroLis);
        mSensorManger.unregisterListener((SensorEventListener) this, mPressure);
    }

    private float[] applyLowPassFilter(float[] input, float[] output) {
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            double r = Math.sqrt(x*x + y*y + z*z);
            yrAngle = (int)(90 - Math.acos(y / r) * 180 / Math.PI);

        }
        if (event.sensor == mAcclerometer) {
            mLastAcceleromter = applyLowPassFilter(event.values.clone(),mLastAcceleromter);
            //System.arraycopy(event.values, 0, mLastAcceleromter, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            mLastMagnetometer = applyLowPassFilter(event.values.clone(),mLastMagnetometer);
            //System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            int axisX =SensorManager.AXIS_X;
            int axisZ = SensorManager.AXIS_Z;
            SensorManager.getRotationMatrix(mR, mL, mLastAcceleromter, mLastMagnetometer);
            SensorManager.remapCoordinateSystem(mR, axisX, axisZ, adjustedR);
            azimuthinDegress = (int) (Math.toDegrees(SensorManager.getOrientation(adjustedR, mOrientation)[0]) + 360) % 360;
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

            SensorManager.getOrientation(mR,mOrientation);
            Log.e("LOG",
                    "y-rotation: " + Float.toString(90-yrAngle)
                            + "           [Z]:" + Float.toString(azimuthinDegress)
            );
            float y = (90-yrAngle);
            az.setText("방위각: "+ azimuthinDegress);
            ro.setText("고도: "+ y);

            north = azimuthinDegress >= 355 && azimuthinDegress <= 360;
            northeast = azimuthinDegress >= 0 && azimuthinDegress <= 5;
            polar = y >= 36 && y <= 40;
            if(north ||northeast){
                if(polar){
                    test.setVisibility(View.VISIBLE);
                }
            }else{
                test.setVisibility(View.GONE);
            }


//            SensorManager.getOrientation(mR,mOrientation);
//            float z =(float)Math.toDegrees(mOrientation[0]);
//            float x =(float)Math.toDegrees(mOrientation[1]);
//            float y =(float)Math.toDegrees(mOrientation[2]);
//            roll = Math.atan2(x,z)*RAD2DGR;
//            pitch = Math.atan2(y,z)*RAD2DGR;
//            yaw = Math.atan2(y,x)*RAD2DGR;
//            Log.e("LOG", "           [X]:" + String.format("%.4f", x)
//                    + "           [Y]:" + String.format("%.4f", y)
//                    + "           [Z]:" + String.format("%.4f", z)
//                    + "           [roll]:" + String.format("%.4f", roll)
//                    + "           [pitch]:" + String.format("%.4f", pitch)
//                    + "           [yaw]:" + String.format("%.4f", yaw));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}