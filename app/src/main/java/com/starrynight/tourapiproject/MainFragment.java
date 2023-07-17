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
import android.widget.ImageButton;
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
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.Filter;
import com.starrynight.tourapiproject.weatherPage2.GpsTracker;
import com.starrynight.tourapiproject.weatherPage2.LocationDTO;
import com.starrynight.tourapiproject.weatherPage2.WeatherActivity2;
import com.starrynight.tourapiproject.weatherPage2.weatherRetrofit.AreaTimeDTO;
import com.starrynight.tourapiproject.weatherPage2.weatherRetrofit.MainInfo;
import com.starrynight.tourapiproject.weatherPage2.weatherRetrofit.WeatherRetrofitClient;
import com.starrynight.tourapiproject.weatherPage2.WeatherLocationSearchActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
/**
* @className : MainFragment
* @description : 홈 화면 Fragment 입니다.
* @modification : 2022-09-02 (jinhyeok) 주석 수정
* @author : jinhyeok
* @date : 2022-09-02
* @version : 1.0
   ====개정이력(Modification Information)====
  수정일        수정자        수정내용
   -----------------------------------------
   2022-09-02      jinhyeok       주석 수정

 */
public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    Long userId;
    String[] filename2 = new String[10];
    private ArrayList<String> urls = new ArrayList<>();
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
    private TextView currentLocation;
    private TextView mainBestObservationFit;
    private TextView recommendTime;
    private Button weatherDetail;

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

        weatherComment = v.findViewById(R.id.weather_comment);
        currentLocation = v.findViewById(R.id.current_location);
        mainBestObservationFit = v.findViewById(R.id.main_best_observation_fit);
        recommendTime = v.findViewById(R.id.recommend_time);
        weatherDetail = v.findViewById(R.id.weather_detail);

        checkLocationPermission();

        gpsTracker = new GpsTracker(getContext());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        location = getCurrentAddress(latitude, longitude);
        System.out.println("위치 = " + latitude + " " + longitude + " " + location);
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
                            recommendTime.setText(info.getBestTime());
                            areaId = info.getAreaId();

                        } else {
                            Log.e(TAG, "날씨 api 호출 실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<MainInfo> call, Throwable t) {
                        Log.e(TAG, t.getMessage());
                    }
                });

        weatherDetail.setOnClickListener(v1 -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), WeatherActivity2.class);
            LocationDTO locationDTO = new LocationDTO(latitude, longitude, areaId, null, location.split(" ")[2]);
            intent.putExtra("locationDTO", locationDTO);
            startActivityForResult(intent, 104);
        });

        String fileName = "userId"; // 유저 아이디 가져오기
        try {
            FileInputStream fis = getActivity().openFileInput(fileName);
            String line = new BufferedReader(new InputStreamReader(fis)).readLine();
            userId = Long.parseLong(line);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //유저 아이디를 통해 선호 해시태그 목록 가져오기
        Call<List<Long>> call = RetrofitClient.getApiService().getMyHashTagIdList(userId);
        call.enqueue(new Callback<List<Long>>() {
            @Override
            public void onResponse(Call<List<Long>> call, Response<List<Long>> response) {
                if (response.isSuccessful()) {
                    myhashTagIdList = response.body();
                    Filter filter = new Filter(null, myhashTagIdList);
                    //선호 해시태그 목록에 따라 선호 게시물 메인화면에 먼저 가져오기
                    Call<List<MainPost>> call2 = RetrofitClient.getApiService().getMainPosts(filter);
                    call2.enqueue(new Callback<List<MainPost>>() {
                        @Override
                        public void onResponse(Call<List<MainPost>> call, Response<List<MainPost>> response) {
                            if (response.isSuccessful()) {
                                result = response.body();
                                end = count;
                                noMorePost = false;
                                limit = result.size();
                                mainPostList = new ArrayList<>();
                                if (limit < end) { noMorePost = true; }
                                adapter = new MainPost_adapter(result.subList(0, Math.min(end, limit)), getContext());
                                recyclerView.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<MainPost>> call, Throwable t) {
                            Log.d("mainPostList", "인터넷 오류");
                        }
                    });
                    // 밑으로 스크롤시 게시물 목록 업로딩
                    nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())
                            {
                                if (!noMorePost){
                                    progressBar.setVisibility(View.VISIBLE);
                                    end += count;
                                    if (limit < end) { noMorePost = true; }
                                    adapter = new MainPost_adapter(result.subList(0, Math.min(end, limit)), getContext());
                                    recyclerView.setAdapter(adapter);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<Long>> call, Throwable t) {
                Log.d("mainPostIdList", "인터넷 오류");
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

    public void toTheTop() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (linearLayoutManager != null) {
            nestedScrollView.fullScroll(View.FOCUS_UP);
        }
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

    // 위도, 경도를 사용하여 주소로 변환하는 메서드
    public String getCurrentAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return "잘못된 GPS 좌표";
        }

        if (addressList == null || addressList.isEmpty()) {
            return "주소 미발견";
        }
        Address address = addressList.get(0);
        System.out.println("address.getAddressLine(0) = " + address.getAddressLine(0));
        System.out.println("address.getAdminArea() = " + address.getAdminArea());
        System.out.println("address.getLocality() = " + address.getLocality());
        System.out.println("address.getSubLocality() = " + address.getSubLocality());
        System.out.println("address.getFeatureName() = " + address.getFeatureName());
        System.out.println("address.getPostalCode() = " + address.getPostalCode());


        String addressLine = address.getAddressLine(0);
        if (addressLine.startsWith("대한민국")) {
            addressLine = addressLine.substring(5);
        }
        System.out.println("addressLine = " + addressLine);
        String[] s = addressLine.split(" ");
        if (s.length <= 3) {
            return addressLine;
        } else {
            return s[0] + " " + s[1] + " " + s[2];
        }
    }
}