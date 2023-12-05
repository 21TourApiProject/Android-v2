package com.starrynight.tourapiproject.starPage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.starrynight.tourapiproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    Context context = this;

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
        FloatingActionButton onOffButton = findViewById(R.id.cameraButton);
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

        long now = System.currentTimeMillis();//댓글을 쓴 현재시간 가져오기
        Date date = new Date(now);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("'0'yyD");
        String julianDate = dateFormat1.format(date);
        Log.d("율리우스력","time: "+julianDate);

        //기본 카메라 앱 실행 버튼
        onOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.sec.android.app.camera");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
//    @Override
//    public String getPackageName() {
//        PackageManager pm = getPackageManager();
//        try {
//            String packageName = pm.getPackageInfo(getPackageName(),0).packageName;
//            if("com.sec.android.app.camera".equals(packageName)){
//                return packageName;
//            }else if("com.lge.camera".equals(packageName)){
//                return packageName;
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_GRAVITY) { //고도 구하는 공식
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
            mCurrentDegree = -azimuthinDegress;

            SensorManager.getOrientation(mR,mOrientation);
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
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}