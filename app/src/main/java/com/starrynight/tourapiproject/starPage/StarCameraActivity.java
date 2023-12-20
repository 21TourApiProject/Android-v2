package com.starrynight.tourapiproject.starPage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
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

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.Constellation;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.weatherPage.GpsTracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;

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

    private static final String TAG = "방위각, 고도 구하기";
    CameraSurfaceView cameraSurfaceView;
    int PERMISSIONS_REQUEST_CODE = 101;
    String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA}; // 권한 코드
    ImageView guideImage,onOffButton,cameraButton,backButton,starIcon,countDown;
    LottieAnimationView starGuideArrow;
    TextView az,ro;
    TextView constName,finalConstName,crtGuideSubText,crtGuideText,crtGuideTextDir,crtGuideTextAngle;
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
    float RA,D;//적경, 적위
    double lat,lon,HA;
    double constAzimuth,constAltitude;

    private double RAD2DGR = 180 / Math.PI;
    private static final float ALPHA = 0.5f;

    //카운트 다운 관련 변수들
    private CountDownTimer countDownTimer;
    private boolean isCountDownActive = false;

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
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
        crtGuideTextDir=findViewById(R.id.crt_guide_text_dir);//방향
        crtGuideTextAngle=findViewById(R.id.crt_guide_text_angle);//움직여야할 각도
        constTitle = findViewById(R.id.constTitle);//별자리 타이틀
        starIcon = findViewById(R.id.find_star);//찾았을 때 상단에 보여질 별 이미지
        starGuideArrow = findViewById(R.id.star_guide_arrow);
        crtGuideLayout =findViewById(R.id.crt_guide_layout);
        countDown = findViewById(R.id.star_guide_countdown);

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
        lat = (double)intent.getSerializableExtra("lat");
        lon = (double)intent.getSerializableExtra("lon");

        constName = findViewById(R.id.starName);
        constName.setText(intentConstName+" 찾기");

        //별자리 이름으로 별자리 정보 가져오는 api
        Call<Constellation> constCall = RetrofitClient.getApiService().getDetailConst(intentConstName);
        constCall.enqueue(new Callback<Constellation>() {
            @Override
            public void onResponse(Call<Constellation> call, Response<Constellation> response) {
                constData = response.body();
                if(constData.getRightAsc()!=null&&constData.getDeclination()!=null){
                    RA = constData.getRightAsc();
                    D = constData.getDeclination();
                    Log.d(TAG,"RA: "+RA+" D: "+D);
                }

                //지방항성시 LST 계산
                long time = System.currentTimeMillis();
                double dtime = time/1000.0;
                Log.d(TAG,"현재 시각: "+dtime);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                String now = sdf.format(time);
                Log.d(TAG,"현재시간 :"+now);
                double LST = LMSTCalculator(lon,dtime);

                //방위각 위도 계산
                HA = LST-RA;
                Log.d(TAG,"HA: "+HA);
                constAzimuth=Math.atan2(-Math.cos(D)*Math.sin(HA),Math.sin(D)*Math.cos(lat)-Math.cos(D)*Math.sin(lat)*Math.cos(HA));
                constAltitude=Math.PI-Math.asin(Math.sin(lat)*Math.sin(D)+Math.cos(lat)*Math.cos(D)*Math.cos(HA));
                Log.d(TAG,"고도: "+String.format("%2f",constAltitude)+" 방위각: "+String.format("%2f",constAzimuth));


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

        //뒤로 가기 버튼
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
            yrAngle = (int)(Math.acos(y / r) * 180 / Math.PI);
            if (z> 0) {
                yrAngle = -yrAngle;
            }
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
            az.setText("방위각: "+ azimuthinDegress);
            ro.setText("고도: "+ yrAngle);

            double crtAltitude = constAltitude-yrAngle; // 움직여야 할 위도
            double crtAzimuth=constAzimuth-azimuthinDegress;// 움직여야 할 방위각
            if(crtAzimuth>180)crtAzimuth-=360;
            else if(crtAzimuth<-180)crtAzimuth+=360;
            String xDirection=(crtAzimuth>0)?"오른쪽":"왼쪽";
            String yDirection=(crtAltitude>0)?"위쪽":"아래쪽";
            if(isAzi){
                crtGuideSubText.setText("제자리에 서서");
                crtGuideTextDir.setText(xDirection+" 방향으로");
                starGuideArrow.setRotation((crtAzimuth>0)?180f:0f);
                crtGuideTextAngle.setText(+Math.abs(Math.floor(crtAzimuth))+"°");
                crtGuideText.setText(xDirection+"돌아주세요.");
            }
            if(Math.abs(Math.floor(crtAzimuth))==0&&isAzi){
                isAzi=false;
                isAlt=true;
                isCountDownActive=true;
                crtGuideSubText.setText("여기가 별을 바라보는 방향이에요!");
                crtGuideTextDir.setVisibility(View.GONE);
                starGuideArrow.setVisibility(View.GONE);
                countDown.setVisibility(View.VISIBLE);
                crtGuideTextAngle.setText("3초간");
                crtGuideText.setText(" 가만히 멈춰주세요.");
                countDownTimer.start();

            }
            if(isAlt&&!isCountDownActive){
                starGuideArrow.setVisibility(View.VISIBLE);
                starGuideArrow.setRotation((crtAltitude>0)?90f:270f);
                crtGuideSubText.setText("방향을 유지한 채");
                crtGuideTextDir.setVisibility(View.VISIBLE);
                crtGuideTextDir.setText("핸드폰을 "+yDirection+"으로");
                crtGuideTextAngle.setText(Math.abs(Math.floor(crtAltitude))+"°");
                crtGuideText.setText(" 돌아주세요.");
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
                    countDown.setImageResource(R.drawable.countdown_3);
                }else if(millisUntilFinished / 1000==2){
                    countDown.setImageResource(R.drawable.countdown_2);
                }else{
                    countDown.setImageResource(R.drawable.countdown_1);
                }
            }

            // 카운트다운이 끝날 때 호출되는 메서드
            @Override
            public void onFinish() {
                countDown.setVisibility(View.GONE);
                isCountDownActive = false;
            }
        };
    }
    public double LMSTCalculator(double lon, double obstime) {
            ZoneOffset seoulZoneOffset = ZoneOffset.of("+09:00");
            ZonedDateTime currentTime =ZonedDateTime.now();
            double JD = calculateJulianDate(currentTime);
            double GMST = calculateGMST(JD);
            Log.d(TAG,"zonedDateTime: "+currentTime+" JD: "+JD+" GMST: "+GMST);
//            double d = (obstime / 86400.0) + 2440587.5 - 2451545.0;
//            double t = d / 36525;
//
//            // GMST calculation
//            double GMST_s = 24110.54841 + 8640184.812866 * t + 0.093104 * Math.pow(t, 2) - 0.0000062 * Math.pow(t, 3);
//            // convert from UT1=0
//            GMST_s += obstime;
//            GMST_s = GMST_s - 86400.0 * Math.floor(GMST_s / 86400.0);

            // adjust to LMST
            double LMST_s = GMST + lon;

            if (LMST_s <= 0) {
                // LMST is between 0 and 24h
                LMST_s += 86400.0;
            }
            LMST_s*=4;
            // time format conversion
            int hour = (int) Math.floor(LMST_s / 3600);
            LMST_s -= 3600 * Math.floor(LMST_s / 3600);
            int min = (int) Math.floor(LMST_s / 60);
            int sec = (int) (LMST_s - 60 * Math.floor(LMST_s / 60));

           Log.d(TAG,"LMST:"+hour+":"+min+":"+sec);
           Log.d(TAG,"LMST_s:"+LMST_s);
           return LMST_s;
        }
        // 율리우스 일 (Julian Date) 계산
        private static double calculateJulianDate(ZonedDateTime dateTime) {
            long epochDay = dateTime.toLocalDate().toEpochDay();
            long secondsOfDay = dateTime.toLocalTime().toSecondOfDay();
            double julianDate = epochDay + (secondsOfDay / 86400.0) + 2440587.5;
            return julianDate; }

         // GMST 계산
         private static double calculateGMST(double JD) {
             double daysSinceJ2000 = JD - 2451545.0;
             double GMST = 280.46061837 + 360.98564736629 * daysSinceJ2000;
              GMST = GMST % 360; // 360도를 넘어가면 조절
              if (GMST < 0) {
                  GMST += 360; // 음수일 경우 조절
                }
               return GMST-135.0;
          }
}