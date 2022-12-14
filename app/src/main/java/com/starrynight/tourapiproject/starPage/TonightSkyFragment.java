package com.starrynight.tourapiproject.starPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.ortiz.touchview.TouchImageView;
import com.starrynight.tourapiproject.MainActivity;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.starPage.horItemPage.HorItem;
import com.starrynight.tourapiproject.starPage.horItemPage.HoroscopeAdapter;
import com.starrynight.tourapiproject.starPage.starItemPage.OnStarItemClickListener;
import com.starrynight.tourapiproject.starPage.starItemPage.StarItem;
import com.starrynight.tourapiproject.starPage.starItemPage.StarViewAdapter;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.GridItemDecoration;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.weatherPage.WeatherActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @className : TonightSkyFragment
 * @description : ????????? ?????? Fragment?????????.
 * @modification : 2022-09-15 (hyeonz) ????????????
 * @author : hyeonz
 * @date : 2022-09-15
 * @version : 1.0
====????????????(Modification Information)====
?????????        ?????????        ????????????
-----------------------------------------
2022-09-15   hyeonz      ????????????
 */
public class TonightSkyFragment extends Fragment implements SensorEventListener {
    //bottomSheet ??????
    private LinearLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    LinearLayout topBar;
    ImageView topIcon;
    ImageView starCamera;

    //????????? ??????
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

    //recyclerview ??????
    RecyclerView constList;
    StarViewAdapter constAdapter;

    RecyclerView allConstList;
    LinearLayout allConstBtn;

    ImageView helpBtn;
    ImageView compass;
    ImageView starBackBtn;
    ImageView starSearchBtn;
    ConstraintLayout todayWeather;

    TouchImageView touchImageView;

    //????????? ?????? ????????? ??????
    String spring = "0321";
    String summer = "0622";
    String fall = "0923";
    String winter = "1221";
    String yearEnd = "1231";
    String yearStart = "0101";

    Date springDate;
    Date summerDate;
    Date fallDate;
    Date winterDate;
    Date todayDate;
    Date yearEndDate;
    Date yearStartDate;

    //????????? ??????
    List<HorItem> horItems = new ArrayList<>();

    ImageView horPrevBtn, horNextBtn;

    private HoroscopeAdapter horAdapter;
    private ViewPager2 horViewpager;

    String todayMonth;

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatMonth = new SimpleDateFormat("MM");

    Calendar cal = Calendar.getInstance();


    Integer compareDataSpring, compareDataSummer, compareDataFall, compareDataWinter, compareDataYearEnd, compareDataYearStart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tonight_sky, container, false);


        //?????????
        mSensorManger = (SensorManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SENSOR_SERVICE);
        mAcclerometer = mSensorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManger.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        topBar = v.findViewById(R.id.top_bar);
        bottomSheet = v.findViewById(R.id.bottom_sheet);
        topIcon = v.findViewById(R.id.icon);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        // bottomSheet ??????
        topBar.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        topBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                        bottomSheetBehavior.setPeekHeight(50);
                        bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 50.f, getResources().getDisplayMetrics()));
                    }
                }
        );

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    topBar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    });
                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    topBar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset >= 0.0 && slideOffset < 0.5) {
                    topIcon.setImageResource(R.drawable.tonight__bottom_up);
                } else if (slideOffset >= 0.5 && slideOffset <= 1.0) {
                    topIcon.setImageResource(R.drawable.tonight__bottom_down);
                }
            }
        });

        // ?????? ?????? ?????? ?????? ?????????
        starBackBtn = v.findViewById(R.id.star_back_btn);
        starBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).onBackPressed();
            }
        });

        // ?????? ?????? ?????? ?????? ?????? ?????????
        allConstList = v.findViewById(R.id.all_const_recycler);
        allConstBtn = v.findViewById(R.id.all_const_btn);

        allConstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), StarAllActivity.class);
                startActivity(intent);
            }
        });

        //?????? ?????? ?????? ?????????
        starSearchBtn = v.findViewById(R.id.star_search_btn);
        starSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), StarSearchActivity.class);
                startActivity(intent);
            }
        });

        //????????? ?????? ?????? ?????????
        starCamera = v.findViewById(R.id.star_camera);
        starCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(),StarCameraActivity.class);
                startActivity(intent);
            }
        });

        // recyclerview ??????
        constList = v.findViewById(R.id.today_cel_recycler);
        constList.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        constAdapter = new StarViewAdapter();
        constList.setAdapter(constAdapter);

        // ????????? ????????? ????????? ???????????? api
        Call<List<StarItem>> todayConstCall = RetrofitClient.getApiService().getTodayConst();
        todayConstCall.enqueue(new Callback<List<StarItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<StarItem>> call, Response<List<StarItem>> response) {
                if (response.isSuccessful()) {
                    List<StarItem> result = response.body();
                    for (StarItem si : result) {
                        constAdapter.addItem(new StarItem(si.getConstId(), si.getConstName(), si.getConstEng()));
                    }
                    constList.setAdapter(constAdapter);
                } else {
                    Log.d("todayConst", "????????? ????????? ???????????? ??????");
                }
            }

            @Override
            public void onFailure(Call<List<StarItem>> call, Throwable t) {
                Log.e("????????????", t.getMessage());
            }
        });

        // item ?????? ??? ?????? ????????? constId ????????????
        constAdapter.setOnItemClickListener(new OnStarItemClickListener() {
            @Override
            public void onItemClick(StarViewAdapter.ViewHolder holder, View view, int position) {
                StarItem item = constAdapter.getItem(position);
                Intent intent = new Intent(getActivity().getApplicationContext(), StarActivity.class);
                intent.putExtra("constName", item.getConstName());
                Log.d("itemConstName", item.getConstName());
                startActivity(intent);
            }
        });


        helpBtn = v.findViewById(R.id.star_help_btn);
        touchImageView = v.findViewById(R.id.touchImage);
        compass = v.findViewById(R.id.compass_needle);
        starBackBtn = v.findViewById(R.id.star_back_btn);

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), StarHelpActivity.class);
                startActivity(intent);
            }
        });

        // ?????? ?????? ?????? ????????? ??????
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");

        try {
            Date today = new Date();
            springDate = sdf.parse(spring);
            summerDate = sdf.parse(summer);
            fallDate = sdf.parse(fall);
            winterDate = sdf.parse(winter);
            yearEndDate = sdf.parse(yearEnd);
            yearStartDate = sdf.parse(yearStart);
            todayDate = sdf.parse(sdf.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        compareDataSpring = todayDate.compareTo(springDate);            //3??? 21???
        compareDataSummer = todayDate.compareTo(summerDate);            //6??? 22???
        compareDataFall = todayDate.compareTo(fallDate);                //9??? 23???
        compareDataWinter = todayDate.compareTo(winterDate);            //12??? 21???
        compareDataYearStart = todayDate.compareTo(yearStartDate);      //1??? 1???
        compareDataYearEnd = todayDate.compareTo(yearEndDate);          //12??? 31???


        // ???(3/21 ~ 6/21)
        if ((compareDataSpring == 1 || compareDataSpring == 0) && compareDataSummer == -1) {
            touchImageView.setImageResource(R.drawable.star__spring);
        }
        // ??????(6/22 ~ 9/22)
        else if ((compareDataSummer == 1 || compareDataSummer == 0) && compareDataFall == -1) {
            touchImageView.setImageResource(R.drawable.star__summer);
        }
        // ??????(9/23 ~ 12/20)
        else if ((compareDataFall == 1 || compareDataFall == 0) && compareDataWinter == -1) {
            touchImageView.setImageResource(R.drawable.star__fall);
        }
        // ??????(12/21 ~ 12/31)
        else if ((compareDataWinter == 1 || compareDataWinter == 0) && compareDataYearEnd == -1) {
            touchImageView.setImageResource(R.drawable.star__winter);
        }
        // ??????(01/01 ~ 03/20)
        else if ((compareDataYearStart == 1 || compareDataYearStart == 0) && compareDataSpring == -1) {
            touchImageView.setImageResource(R.drawable.star__winter);
        }


        return v;
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
            compass.startAnimation(ra);
            mCurrentDegree = -azimuthinDegress;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private int getItem(int i) {
        return horViewpager.getCurrentItem() + i;
    }
}