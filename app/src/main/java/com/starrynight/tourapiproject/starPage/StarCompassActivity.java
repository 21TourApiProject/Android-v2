package com.starrynight.tourapiproject.starPage;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.starrynight.tourapiproject.R;

public class StarCompassActivity extends AppCompatActivity implements SensorEventListener {

    TextView latitude;
    TextView direction;
    ImageView compass;
    LinearLayout back_btn;

    //나침반 관련
    private SensorManager mSensorManger;
    private Sensor mAcclerometer;
    private Sensor mMagnetometer;
    private float[] lastAcceleromter = new float[3];
    private float[] lastMagnetometer = new float[3];
    private boolean lastAccelerometerSet = false;
    private boolean lastMagnetometerSet = false;
    private final float[] mR = new float[9];
    private final float[] mOrientation = new float[3];
    private float currentDegree = 0f;
    private static final float ALPHA = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_compass);

        compass = findViewById(R.id.Big_copass);
        latitude = findViewById(R.id.latitude_text);
        direction = findViewById(R.id.compass_direction);
        back_btn = findViewById(R.id.compass_back_btn);

        //나침반
        mSensorManger = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAcclerometer = mSensorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManger.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        onclickBackBtn();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManger.registerListener(this, mAcclerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManger.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManger.unregisterListener(this, mAcclerometer);
        mSensorManger.unregisterListener(this, mMagnetometer);
    }

    private float[] applyLowPassFilter(float[] input, float[] output) { //방위각 결과값을 부드럽게 출력하기위한 필터
        if ( output == null ) return input;

        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAcclerometer) {
            lastAcceleromter = applyLowPassFilter(event.values.clone(),lastAcceleromter);
            //System.arraycopy(event.values, 0, lastAcceleromter, 0, event.values.length);
            lastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            lastMagnetometer = applyLowPassFilter(event.values.clone(),lastMagnetometer);
            //System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
            lastMagnetometerSet = true;
        }
        if (lastAccelerometerSet && lastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, lastAcceleromter, lastMagnetometer);
            float azimuthinDegrees = (int) (Math.toDegrees(SensorManager.getOrientation(mR, mOrientation)[0]) + 360) % 360;
            RotateAnimation ra = new RotateAnimation(
                    currentDegree,
                    -azimuthinDegrees,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );
            ra.setDuration(250);
            ra.setFillAfter(true);
            compass.startAnimation(ra);
            currentDegree = -azimuthinDegrees;
            latitude.setText(""+Float.toString(azimuthinDegrees));

            //direction 정하기
            if(azimuthinDegrees>0&&azimuthinDegrees<22.5||azimuthinDegrees>=337.5){
                direction.setText("북");
            }else if(azimuthinDegrees>=22.5&&azimuthinDegrees<67.5){
                direction.setText("북동");
            }
            else if(azimuthinDegrees>=67.5&&azimuthinDegrees<112.5){
                direction.setText("동");
            }
            else if(azimuthinDegrees>=112.5&&azimuthinDegrees<157.5){
                direction.setText("남동");
            }
            else if(azimuthinDegrees>=157.5&&azimuthinDegrees<202.5){
                direction.setText("남");
            }
            else if(azimuthinDegrees>=202.5&&azimuthinDegrees<247.5){
                direction.setText("남서");
            }
            else if(azimuthinDegrees>=247.5&&azimuthinDegrees<292.5){
                direction.setText("서");
            }
            else if(azimuthinDegrees>=292.5&&azimuthinDegrees<337.5){
                direction.setText("북서");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // 별자리 뒤로가기 버튼 클릭 이벤트
    public void onclickBackBtn() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}