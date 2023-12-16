package com.starrynight.tourapiproject.starPage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.Constellation;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.RetrofitClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    ImageView guideImage,onOffButton,cameraButton,backButton,starIcon,starGuideArrow;
    TextView az,ro;
    TextView constName,finalConstName,crtGuideSubText,crtGuideText;
    LinearLayout guideLayout,guideImageLayout, constTitle,crtGuideLayout;
    Context context = this;

    String intentConstName;
    Constellation constData;

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
    float azimuthinDegress=0;
    boolean guideOn = true;
    boolean isAlt = false;
    boolean isAzi = true;
    float constAzimuth,constAltitude;

    private double RAD2DGR = 180 / Math.PI;
    private static final float ALPHA = 0.5f;

    //카운트 다운 관련 변수들
    private CountDownTimer countDownTimer;
    private boolean isCountDownActive = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_camera);
        cameraSurfaceView =findViewById(R.id.surfaceView);
        cameraButton = findViewById(R.id.cameraButton);
        onOffButton = findViewById(R.id.guideOnOff);
        backButton = findViewById(R.id.star_camera_back);
        crtGuideSubText=findViewById(R.id.crt_guide_subText);
        crtGuideText = findViewById(R.id.crt_guide_text);
        constTitle = findViewById(R.id.constTitle);//별자리 타이틀
        starIcon = findViewById(R.id.find_star);//찾았을 때 상단에 보여질 별 이미지
        starGuideArrow = findViewById(R.id.star_guide_arrow);
        crtGuideLayout =findViewById(R.id.crt_guide_layout);

        guideLayout = findViewById(R.id.guideLineFinal); // 최종적으로 나오는 별자리 이미지 LinearLayout
        guideImage = findViewById(R.id.guideLineImage); // 최종적으로 나오는 이미지
        guideImageLayout = findViewById(R.id.guideLineImageLayout); // 최종적으로 나오는 이미지 틀
        finalConstName = findViewById(R.id.guideLineConstName);

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

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("'0'yyD");
        String julianDate = dateFormat1.format(date);
        Log.d("율리우스력","time: "+julianDate);

        LMSTCalculator(new String[]{"127.0", "2023-12-15T21:00"});

        // 카메라 시작 전 안내 팝업
        AlertDialog.Builder builder =
                new AlertDialog.Builder(StarCameraActivity.this,R.style.DimDialog);
        AlertDialog pop = builder.create();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_star_camera_dialog, null);
        TextView starGuideBtn = view.findViewById(R.id.star_guide_btn);
        pop.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pop.setView(view);
        pop.show();

        starGuideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
                crtGuideLayout.setVisibility(View.VISIBLE);
            }
        });

        //별자리 이름 받아오기
        Intent intent = getIntent();
        intentConstName = (String) intent.getSerializableExtra("constName");
        Log.d("constName 받아오기", intentConstName);

        constName = findViewById(R.id.starName);
        constName.setText(intentConstName+" 찾기");

        //별자리 이름으로 별자리 정보 가져오는 api
        Call<Constellation> constCall = RetrofitClient.getApiService().getDetailConst(intentConstName);
        constCall.enqueue(new Callback<Constellation>() {
            @Override
            public void onResponse(Call<Constellation> call, Response<Constellation> response) {
                constData = response.body();
                if(constData.getAzimuth()!=null&&constData.getAltitude()!=null){
                    constAltitude = constData.getAltitude();
                    constAzimuth = constData.getAzimuth();
                }
                finalConstName.setText(constData.getConstName());
                Glide.with(StarCameraActivity.this).load("https://starry-night.s3.ap-northeast-2.amazonaws.com/constDetailImage/s_"
                        + constData.getConstEng() + ".png").fitCenter().into(guideImage);

            }

            @Override
            public void onFailure(Call<Constellation> call, Throwable t) {

            }
        });

        countDownTimer=createCountDownTimer();//카운트 다운 타이머 초기화화

       //기본 카메라 앱 실행 버튼
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.sec.android.app.camera");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //guideOnOff 버튼 클릭 이벤트
        onOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(guideOn){
                    guideOn = false;
                    view.setSelected(!view.isSelected());
                    guideLayout.setVisibility(View.GONE);
                    onOffButton.setEnabled(false);
                    Handler handle = new Handler(); //에러 방지를 위해 1.5초 딜레이 주기
                    handle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onOffButton.setEnabled(true);
                        }
                    },1500);
                }else{
                    guideOn = true;
                    view.setSelected(!view.isSelected());
                    guideLayout.setVisibility(View.VISIBLE);
                    onOffButton.setEnabled(false);
                    Handler handle = new Handler(); //에러 방지를 위해 1.5초 딜레이 주기
                    handle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onOffButton.setEnabled(true);
                        }
                    },1500);
                }
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

        // 카운트다운 중이면 센서 데이터 무시
        if (isCountDownActive) {
            return;
        }

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

            float crtAltitude = constAltitude-y; // 움직여야 할 위도
            float crtAzimuth=constAzimuth-azimuthinDegress;// 움직여야 할 방위각
            String xDirection=(crtAzimuth>0)?"오른쪽":"왼쪽";
            String yDirection=(crtAltitude>0)?"위쪽":"아래쪽";

            if(isAzi){
                crtGuideSubText.setText("제자리에 서서");
                crtGuideText.setText(xDirection+" 방향으로"+Math.abs(Math.floor(crtAzimuth))+"° 돌아주세요.");
            }
            if(Math.abs(Math.floor(crtAzimuth))==0&&isAzi){
                Log.d("countDown","카운트다운 시작");
                isAzi=false;
                isAlt=true;
                isCountDownActive=true;
                crtGuideSubText.setText("여기가 별을 바라보는 방향이에요!");
                crtGuideText.setText("3초간 가만히 멈춰주세요.");
                countDownTimer.start();

            }
            if(isAlt&&!isCountDownActive){
                crtGuideSubText.setText("방향을 유지한 채");
                crtGuideText.setText("핸드폰을 "+yDirection+"으로"+Math.abs(Math.floor(crtAltitude))+"° 돌아주세요.");
            }


            if(Math.abs(Math.floor(crtAltitude))==0&&isAlt){ //이 부분에 고도 방위각 수학 공식 구현
                crtGuideLayout.setVisibility(View.GONE);
                guideLayout.setVisibility(View.VISIBLE);
                onOffButton.setVisibility(View.VISIBLE);
                constTitle.setBackgroundResource(R.drawable.star_search);
                constName.setText(intentConstName+ "발견!");
                starIcon.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private CountDownTimer createCountDownTimer() {
        return new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // 남은 시간에 대한 처리 코드 작성
                if(millisUntilFinished / 1000==3){
                    starGuideArrow.setImageResource(R.drawable.countdown_3);
                }else if(millisUntilFinished / 1000==2){
                    starGuideArrow.setImageResource(R.drawable.countdown_2);
                }else{
                    starGuideArrow.setImageResource(R.drawable.countdown_1);
                }
            }

            // 카운트다운이 끝날 때 호출되는 메서드
            @Override
            public void onFinish() {
                isCountDownActive = false;
            }
        };
    }
    public void LMSTCalculator(String[] args) {
            double lon;
            long obstime;

            if (args.length == 2) {
                // Assume we just want the current LMST
                lon = Double.parseDouble(args[0]);
                obstime = System.currentTimeMillis() / 1000; // seconds since Unix epoch
            } else if (args.length == 3) {
                // LMST at a specified day+time
                lon = Double.parseDouble(args[0]);
                obstime = parseDateTime(args[1]);
            } else {
                System.err.println("Error: Incorrect usage.\n");
                System.err.println("Usage:");
                System.err.println("java LMSTCalculator longitude [date time]\n");
                System.err.println("Where longitude is in degrees and E is positive.");
                System.err.println("[date time] is optional and should be in the computer's time zone and in the format YYYY-MM-DDTHH:mm");
                return;
            }

            // add JD to get to the UNIX epoch, then subtract to get the days since 2000 Jan 01, 12h UT1
            double d = (obstime / 86400.0) + 2440587.5 - 2451545.0;
            double t = d / 36525;

            // GMST calculation
            double GMST_s = 24110.54841 + 8640184.812866 * t + 0.093104 * Math.pow(t, 2) - 0.0000062 * Math.pow(t, 3);
            // convert from UT1=0
            GMST_s += obstime;
            GMST_s = GMST_s - 86400.0 * Math.floor(GMST_s / 86400.0);

            // adjust to LMST
            double LMST_s = GMST_s + 3600 * lon / 15.0;

            if (LMST_s <= 0) {
                // LMST is between 0 and 24h
                LMST_s += 86400.0;
            }

            // time format conversion
            int hour = (int) Math.floor(LMST_s / 3600);
            LMST_s -= 3600 * Math.floor(LMST_s / 3600);
            int min = (int) Math.floor(LMST_s / 60);
            int sec = (int) (LMST_s - 60 * Math.floor(LMST_s / 60));

           Log.d("LMST","시간:"+hour+min+sec);
        }
        private static long parseDateTime(String dateTime) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                Date date = sdf.parse(dateTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar.getTimeInMillis() / 1000; // convert to seconds
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
}