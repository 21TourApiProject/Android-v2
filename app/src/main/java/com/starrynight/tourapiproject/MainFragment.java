package com.starrynight.tourapiproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.starrynight.tourapiproject.alarmPage.AlarmActivity;
import com.starrynight.tourapiproject.postPage.postRetrofit.MainPost;
import com.starrynight.tourapiproject.postPage.postRetrofit.MainPost_adapter;
import com.starrynight.tourapiproject.postPage.postRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.postWritePage.PostWriteActivity;
import com.starrynight.tourapiproject.weatherPage.GpsTracker;
import com.starrynight.tourapiproject.weatherPage.LocationDTO;
import com.starrynight.tourapiproject.weatherPage.WeatherActivity;
import com.starrynight.tourapiproject.weatherPage.WeatherLocationSearchActivity;
import com.starrynight.tourapiproject.weatherPage.weatherRetrofit.AreaTimeDTO;
import com.starrynight.tourapiproject.weatherPage.weatherRetrofit.MainInfo;
import com.starrynight.tourapiproject.weatherPage.weatherRetrofit.NearestDTO;
import com.starrynight.tourapiproject.weatherPage.weatherRetrofit.WeatherRetrofitClient;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

/**
 * @author : jinhyeok
 * @version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2022-09-02      jinhyeok       주석 수정
 * @className : MainFragment
 * @description : 홈 화면 Fragment 입니다.
 * @modification : 2022-09-02 (jinhyeok) 주석 수정
 * @date : 2022-09-02
 */
public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    Long userId;
    SwipeRefreshLayout swipeRefreshLayout;
    NestedScrollView nestedScrollView;
    List<Long> myhashTagIdList;
    int count = 20, end, limit;
    List<MainPost> mainPostList;
    List<MainPost> result;
    Boolean noMorePost;
    RecyclerView recyclerView;
    MainPost_adapter adapter;
    ProgressBar progressBar;
    LinearLayout weatherLocationSearch;
    FloatingActionButton postwritebtn;

    private static final String TAG = "Main Fragment";
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat hh = new SimpleDateFormat("HH");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat mm = new SimpleDateFormat("mm");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    GpsTracker gpsTracker;
    double latitude;
    double longitude;
    String location;
    Long areaId; // WEATHER_AREA id
    String hour; // 현재 hour ex) 18
    String min; // 현재 min ex) 10
    private TextView weatherComment;
    private ImageView star;
    private TextView mainBestObservationFit;
    private TextView recommendTime;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        swipeRefreshLayout = v.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        myhashTagIdList = new ArrayList<>();
        nestedScrollView = v.findViewById(R.id.scroll_layout);
        recyclerView = v.findViewById(R.id.recyclerView);
        progressBar = v.findViewById(R.id.mainProgressBar);
        weatherLocationSearch = v.findViewById(R.id.weather_location_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        View mainLocation = v.findViewById(R.id.main__location);
        weatherComment = v.findViewById(R.id.weather_comment);
        TextView currentLocation = v.findViewById(R.id.main__current_location);
        star = v.findViewById(R.id.main__star);
        mainBestObservationFit = v.findViewById(R.id.main_best_observation_fit);
        recommendTime = v.findViewById(R.id.recommend_time);
        View weatherDetail = v.findViewById(R.id.weather_detail);

        checkLocationPermission();

        gpsTracker = new GpsTracker(getContext());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        // 현 위치 불러오기
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addressList = new ArrayList<>();
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!addressList.isEmpty()) {
            Address address = addressList.get(0);
            String SD = address.getAdminArea();
            String SGG = address.getLocality() == null ? address.getSubLocality() : address.getLocality();

            System.out.println("SD = " + SD);
            System.out.println("SGG = " + SGG);

            NearestDTO nearestDTO = new NearestDTO(SGG, latitude, longitude);
            if (SD.contains("세종")) nearestDTO.setSgg("세종");

            WeatherRetrofitClient.getApiService()
                    .getNearestArea(nearestDTO)
                    .enqueue(new Callback<Map<String, String>>() {
                        @Override
                        public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                            if (response.isSuccessful()) {
                                Map<String, String> body = response.body();
                                String emd = body.get("EMD");
                                if (SD.contains("세종")) location = "세종특별자치시 " + emd;
                                else location = SD + " " + SGG + " " + emd;

                                currentLocation.setText(location);

                                long now = System.currentTimeMillis();
                                Date date = new Date(now);
                                hour = hh.format(date);
                                min = mm.format(date);

                                AreaTimeDTO areaTimeDTO = new AreaTimeDTO(yyyy_MM_dd.format(date), Integer.valueOf(hour), latitude, longitude);
                                areaTimeDTO.setAddress(location);
                                WeatherRetrofitClient.getApiService()
                                        .getMainInfo(areaTimeDTO)
                                        .enqueue(new Callback<MainInfo>() {
                                            @Override
                                            public void onResponse(Call<MainInfo> call, Response<MainInfo> response) {
                                                if (response.isSuccessful()) {
                                                    MainInfo info = response.body();
                                                    weatherComment.setText(info.getComment());
                                                    mainBestObservationFit.setText(info.getBestObservationalFit());
                                                    if (Objects.nonNull(info.getBestTime())) {
                                                        recommendTime.setText(info.getBestTime());
                                                    } else {
                                                        mainLocation.setBackground(ContextCompat.getDrawable(mainLocation.getContext(), R.drawable.main__location_back_red));
                                                        star.setImageDrawable(ContextCompat.getDrawable(star.getContext(), R.drawable.main__weather_star_gray));
                                                        recommendTime.setText(info.getMainEffect());
                                                    }
                                                    areaId = info.getAreaId();
                                                } else {
                                                    Log.e(TAG, "날씨 오류");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<MainInfo> call, Throwable t) {
                                                Log.e(TAG, t.getMessage());
                                            }
                                        });

                                weatherDetail.setOnClickListener(v1 -> {
                                    Intent intent = new Intent(getActivity().getApplicationContext(), WeatherActivity.class);
                                    LocationDTO locationDTO = new LocationDTO(latitude, longitude, areaId, null, emd);
                                    intent.putExtra("locationDTO", locationDTO);
                                    startActivityForResult(intent, 104);
                                });


                            } else {
                                Log.e(TAG, "날씨 오류");
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String, String>> call, Throwable t) {
                            Log.e(TAG, t.getMessage());
                        }
                    });
        }
        if (location == null) currentLocation.setText("새로고침이 필요합니다.");
        else if (location.equals("현위치를 불러올 수 없습니다.")) currentLocation.setText(location);

        String fileName = "userId"; // 유저 아이디 가져오기
        try {
            FileInputStream fis = getActivity().openFileInput(fileName);
            String line = new BufferedReader(new InputStreamReader(fis)).readLine();
            userId = Long.parseLong(line);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Call<List<MainPost>> call2 = RetrofitClient.getApiService().getMainPosts();
        call2.enqueue(new Callback<List<MainPost>>() {
            @Override
            public void onResponse(Call<List<MainPost>> call, Response<List<MainPost>> response) {
                if (response.isSuccessful()) {
                    Log.d("mainPostList", "메인 게시물 리스트 성공");
                    result = response.body();
                    end = count;
                    noMorePost = false;
                    limit = result.size();
                    mainPostList = new ArrayList<>();
                    if (limit < end) {
                        noMorePost = true;
                    }
                    adapter = new MainPost_adapter(result.subList(0, Math.min(end, limit)), getContext());
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("mainPostList", "메인 게시물 리스트 실패");
                }
            }

            @Override
            public void onFailure(Call<List<MainPost>> call, Throwable t) {
                Log.e("mainPostList", "인터넷 오류");
            }
        });
        // 밑으로 스크롤시 게시물 목록 업로딩
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if (!noMorePost) {
                        progressBar.setVisibility(View.VISIBLE);
                        end += count;
                        if (limit < end) {
                            noMorePost = true;
                        }
                        adapter = new MainPost_adapter(result.subList(0, Math.min(end, limit)), getContext());
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        // 게시물 작성 페이지로 넘어가는 이벤트
        Button postWrite = (Button) v.findViewById(R.id.postWrite);
        postWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PostWriteActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        //플로팅 버튼으로 게시글 작성 페이지로 넘어가기
        postwritebtn = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);
        postwritebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PostWriteActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        // 알림 페이지로 넘어가는 이벤트

        Button alarm = (Button) v.findViewById(R.id.main_alarm);
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AlarmActivity.class);
                startActivityForResult(intent, 104);
            }
        });

        //위치 검색바 클릭 시 위치 검색 페이지로 이동하는 이벤트
        weatherLocationSearch.setOnClickListener(v12 -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), WeatherLocationSearchActivity.class);
            startActivityForResult(intent, 105);
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            //프래그먼트 새로고침
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
        swipeRefreshLayout.setRefreshing(false);
    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        }
    }
}