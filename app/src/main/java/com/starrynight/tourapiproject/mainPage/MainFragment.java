package com.starrynight.tourapiproject.mainPage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.starrynight.tourapiproject.MainActivity;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.alarmPage.AlarmActivity;
import com.starrynight.tourapiproject.alarmPage.subBanner.SubBanner;
import com.starrynight.tourapiproject.mainPage.mainPageRetrofit.ObservationSimpleParams;
import com.starrynight.tourapiproject.mainPage.mainPageRetrofit.PostContentsParams;
import com.starrynight.tourapiproject.mainPage.mainPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.observationPage.ObservationsiteActivity;
import com.starrynight.tourapiproject.postPage.PostActivity;
import com.starrynight.tourapiproject.postWritePage.PostWriteActivity;
import com.starrynight.tourapiproject.starPage.StarActivity;
import com.starrynight.tourapiproject.starPage.StarSearchActivity;
import com.starrynight.tourapiproject.starPage.starItemPage.OnStarItemClickListener;
import com.starrynight.tourapiproject.starPage.starItemPage.StarItem;
import com.starrynight.tourapiproject.starPage.starItemPage.StarViewAdapter;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    ImageView subBanner;
    LinearLayout subBannerLayout;
    ProgressBar progressBar;
    LinearLayout weatherLocationSearch;

    private static final String TAG = "MainFragment";
    private static final String TAG1 = "SubBannerApi";
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

    private RecyclerView starRecycler;
    private ImageButton move_star_btn;
    private StarViewAdapter starViewAdapter;
    private TextView startMonthText;
    private LinearLayout moveReviewBtn;
    private RecyclerView reviewRecycler;
    private RecentReviewAdapter recentReviewAdapter;

    private static final String MAIN_STAR_TEXT = "월에 잘 보여요";

    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        swipeRefreshLayout = v.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        myhashTagIdList = new ArrayList<>();
        nestedScrollView = v.findViewById(R.id.scroll_layout);
        progressBar = v.findViewById(R.id.mainProgressBar);
        weatherLocationSearch = v.findViewById(R.id.weather_location_search);

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

            if(SD == null || !SD.contains("세종") && SGG == null){
                location = "현위치를 불러올 수 없습니다.";
                currentLocation.setText(location);
            } else {
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
        }
        if (location == null) currentLocation.setText("새로고침이 필요합니다.");

        String fileName = "userId"; // 유저 아이디 가져오기
        try {
            FileInputStream fis = getActivity().openFileInput(fileName);
            String line = new BufferedReader(new InputStreamReader(fis)).readLine();
            userId = Long.parseLong(line);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //서브 배너 가져오기
        subBannerLayout = v.findViewById(R.id.subBanner_linear);
        subBanner = (ImageView)v.findViewById(R.id.subBanner);
        Call<SubBanner> subBannerCall = com.starrynight.tourapiproject.myPage.myPageRetrofit.RetrofitClient.getApiService().getLastSubBanner();
        subBannerCall.enqueue(new Callback<SubBanner>() {
            @Override
            public void onResponse(Call<SubBanner> call, Response<SubBanner> response) {
                if(response.isSuccessful()){
                    SubBanner banner = response.body();
                    if(banner.isShow()){ //isShow가 true면 배너가 보일 수 있도록 한다
                        subBannerLayout.setVisibility(View.VISIBLE);
                        Glide.with(getActivity()).load("https://starry-night.s3.ap-northeast-2.amazonaws.com/subBanner/" + banner.getBannerImage()).fitCenter().into(subBanner);
                    }else {
                        subBannerLayout.setVisibility(View.GONE);
                    }
                    subBanner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(banner.getLink()!=null){
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(banner.getLink()));
                                startActivity(intent);
                            }
                        }
                    });

                }else {
                    Log.e(TAG1, "서브 배너 오류");
                }
            }

            @Override
            public void onFailure(Call<SubBanner> call, Throwable t) {
                Log.e(TAG1, t.getMessage());
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

        // 오늘 보기좋은 관측지
        RecyclerView bestFitRecyclerView = v.findViewById(R.id.main_best_fit_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        bestFitRecyclerView.setLayoutManager(linearLayoutManager);
        BestFitObservationAdapter adapter = new BestFitObservationAdapter();
        bestFitRecyclerView.setAdapter(adapter);
        Call<List<ObservationSimpleParams>> call = RetrofitClient.getApiService().getBestFitObservationList();
        call.enqueue(new Callback<List<ObservationSimpleParams>>() {
            @Override
            public void onResponse(Call<List<ObservationSimpleParams>> call, Response<List<ObservationSimpleParams>> response) {
                if (response.isSuccessful()) {
                    List<ObservationSimpleParams> observationSimpleList = response.body();
                    adapter.setItems(observationSimpleList);
                    adapter.notifyDataSetChanged();
                    adapter.setOnItemClicklistener(new OnBestFitObsItemClickListener() {
                        @Override
                        public void onItemClick(BestFitObservationAdapter.ViewHolder holder, View view, int position) {
                            Intent intent = new Intent(getActivity(), ObservationsiteActivity.class);
                            intent.putExtra("observationId", observationSimpleList.get(position).getItemId());
                            startActivity(intent);
                        }
                    });
                } else {
                    Log.d("hot", "요즘 핫한 명소 업로드 실패");
                }
            }

            @Override
            public void onFailure(Call<List<ObservationSimpleParams>> call, Throwable t) {
                Log.d(TAG, "오늘 보기 좋은 관측지 로드 실패");
            }
        });

        // 오늘 볼 수 있는 별자리
        starRecycler = v.findViewById(R.id.main_star_today_recycler);
        move_star_btn = v.findViewById(R.id.main_move_star);
        starViewAdapter = new StarViewAdapter();
        startMonthText = v.findViewById(R.id.main_star_month_txt);
        starRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        starRecycler.setAdapter(starViewAdapter);
        setTodayStarLayout();

        // 최근 관측 후기
        reviewRecycler = v.findViewById(R.id.main_review_recycler);
        moveReviewBtn = v.findViewById(R.id.main_move_review);
        recentReviewAdapter = new RecentReviewAdapter();
        reviewRecycler.setAdapter(recentReviewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        reviewRecycler.setLayoutManager(layoutManager);
        setCurrentReviewLayout();

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

    private void setTodayStarLayout() {
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH) +1;
        startMonthText.setText(month+MAIN_STAR_TEXT);

        // 오늘의 별자리 리스트 불러오는 api
        Call<List<StarItem>> todayConstCall = RetrofitClient.getApiService().getTodayConst();
        todayConstCall.enqueue(new Callback<List<StarItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<StarItem>> call, Response<List<StarItem>> response) {
                if (response.isSuccessful()) {
                    List<StarItem> result = response.body();
                    for (StarItem si : result) {
                        starViewAdapter.addItem(new StarItem(si.getConstId(), si.getConstName(), si.getConstEng()));
                    }
                    starRecycler.setAdapter(starViewAdapter);
                } else {
                    Log.d(TAG, "오늘의 별자리 불러오기 실패");
                }
            }

            @Override
            public void onFailure(Call<List<StarItem>> call, Throwable t) {
                Log.e(TAG, "오늘의 별자리"+t.getMessage());
            }
        });

        // item 클릭 시 해당 아이템 constId 넘겨주기
        starViewAdapter.setOnItemClickListener(new OnStarItemClickListener() {
            @Override
            public void onItemClick(StarViewAdapter.ViewHolder holder, View view, int position) {
                StarItem item = starViewAdapter.getItem(position);
                Intent intent = new Intent(getActivity().getApplicationContext(), StarActivity.class);
                intent.putExtra("constName", item.getConstName());
                Log.d("itemConstName", item.getConstName());
                startActivity(intent);
            }
        });

        move_star_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), StarSearchActivity.class);
                intent.putExtra("starHashTagName",month+"월");
                intent.putExtra("type",3);
                startActivity(intent);
            }
        });
    }

    private void setCurrentReviewLayout() {
        Call<List<PostContentsParams>> call = RetrofitClient.getApiService().getLatestPostWithSize(3);
        call.enqueue(new Callback<List<PostContentsParams>>() {
            @Override
            public void onResponse(Call<List<PostContentsParams>> call, Response<List<PostContentsParams>> response) {
                if (response.isSuccessful()) {
                    List<PostContentsParams> postContentsParamsList = response.body();
                    recentReviewAdapter.setItems(postContentsParamsList);
                    recentReviewAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "최근 관측후기 로드 실패");
                }
            }

            @Override
            public void onFailure(Call<List<PostContentsParams>> call, Throwable t) {
                Log.d(TAG, "최근 관측후기 연결 실패");
            }
        });

        recentReviewAdapter.setOnItemClicklistener(new RecentReviewItemClickListener() {
            @Override
            public void onItemClick(RecentReviewAdapter.ViewHolder holder, View view, int position) {
                PostContentsParams item = recentReviewAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), PostActivity.class);
                intent.putExtra("postId", item.getItemId());
                startActivity(intent);
            }
        });

        moveReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) Objects.requireNonNull(getActivity())).movePost();
            }
        });
    }
}