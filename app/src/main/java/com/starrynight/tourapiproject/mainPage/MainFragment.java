package com.starrynight.tourapiproject.mainPage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.bumptech.glide.Glide;
import com.starrynight.tourapiproject.MainActivity;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.alarmPage.AlarmActivity;
import com.starrynight.tourapiproject.alarmPage.subBanner.SubBanner;
import com.starrynight.tourapiproject.mainPage.interestArea.CustomInterestAreaView;
import com.starrynight.tourapiproject.mainPage.interestArea.InterestAreaIntent;
import com.starrynight.tourapiproject.mainPage.interestArea.InterestAreaPopActivity;
import com.starrynight.tourapiproject.mainPage.interestArea.InterestAreaWeatherActivity;
import com.starrynight.tourapiproject.mainPage.interestArea.interestAreaRetrofit.InterestAreaDTO;
import com.starrynight.tourapiproject.mainPage.interestArea.interestAreaRetrofit.InterestAreaRetrofitClient;
import com.starrynight.tourapiproject.mainPage.mainPageRetrofit.ObservationSimpleParams;
import com.starrynight.tourapiproject.mainPage.mainPageRetrofit.PostContentsParams;
import com.starrynight.tourapiproject.mainPage.mainPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.observationPage.ObservationsiteActivity;
import com.starrynight.tourapiproject.observationPage.RecyclerDecoration;
import com.starrynight.tourapiproject.postPage.PostActivity;
import com.starrynight.tourapiproject.postWritePage.PostWriteActivity;
import com.starrynight.tourapiproject.starPage.StarActivity;
import com.starrynight.tourapiproject.starPage.StarAllActivity;
import com.starrynight.tourapiproject.starPage.starItemPage.OnStarItemClickListener2;
import com.starrynight.tourapiproject.starPage.starItemPage.StarItem;
import com.starrynight.tourapiproject.starPage.starItemPage.StarViewAdpater2;
import com.starrynight.tourapiproject.weatherPage.GpsTracker;
import com.starrynight.tourapiproject.weatherPage.LocationDTO;
import com.starrynight.tourapiproject.weatherPage.WeatherActivity;
import com.starrynight.tourapiproject.weatherPage.WeatherLocationSearchActivity;
import com.starrynight.tourapiproject.weatherPage.weatherRetrofit.MainInfo;
import com.starrynight.tourapiproject.weatherPage.weatherRetrofit.NearestAreaDTO;
import com.starrynight.tourapiproject.weatherPage.weatherRetrofit.WeatherRetrofitClient;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
public class MainFragment extends Fragment implements PullRefreshLayout.OnRefreshListener {

    private MainActivity activityContext;

    private static final String TAG = "MainFragment";
    private static final String TAG1 = "SubBannerApi";
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final String MAIN_STAR_TEXT = "월에 잘 보여요";

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat MM_dd_HH = new SimpleDateFormat("MM'월 'dd'일' HH'시'");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat HH = new SimpleDateFormat("HH");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private Context mContext;
    private Long userId;
    private PullRefreshLayout pullRefreshLayout;
    private ImageView subBanner;
    private LinearLayout subBannerLayout;
    private TextView helloMassage; // 인사말

    private ImageView currentWeatherIcon;
    private TextView currentWeatherError; // 새로고침, 현위치 오류, 날씨 로딩 문구
    private LinearLayout currentWeatherText;
    private TextView currentWeatherComment1Front;
    private TextView currentWeatherComment1Back;
    private TextView currentWeatherComment2;
    private Boolean findLocation; // 현위치 조회 성공 여부
    private LinearLayout weatherReportLayout;

    private RecyclerView starRecycler;
    private ImageButton move_star_btn;
    private StarViewAdpater2 starViewAdapter;
    private TextView startMonthText;
    private LinearLayout moveReviewBtn;
    private RecentReviewAdapter recentReviewAdapter;

    private LinearLayout interestAreaInitLayout; // 관심지역 레이아웃 (0개일 때)
    private LinearLayout addInterestAreaInit; // 관심지역 추가 버튼 (0개일 때)

    private LinearLayout interestAreaLayout; // 관심지역 레이아웃 (1개 이상일 때)
    private TextView editInterestArea; // 관심지역 편집 버튼
    private ImageView addInterestArea; // 관심지역 추가 버튼 (1개 이상일 때)

    List<Long> interestRegionIdList; // 관심지역 id 리스트
    List<Integer> interestRegionTypeList; // 관심지역 type 리스트
    List<String> interestRegionNameList; // 관심지역 이름 리스트
    boolean editMode = false; // 관심지역 편집 상태
    boolean needRefresh = false; // 관심지역 편집취소 클릭 시 새로고침 필요 여부 flag
    boolean needSet = false; // 관심지역 추가 후 재조회 필요 여부 flag
    View[] viewListWithoutInterestArea;
    CustomInterestAreaView interestArea0;
    CustomInterestAreaView interestArea1;
    CustomInterestAreaView interestArea2;

    //도움말
    private LinearLayout help_1;
    private ImageView help_1_button;
    private TextView help_1_text;
    private LinearLayout help_2;
    private ImageView help_2_button;
    private TextView help_2_text;
    private LinearLayout help_3;
    private ImageView help_3_button;
    private TextView help_3_text;
    private LinearLayout help_4;
    private ImageView help_4_button;
    private TextView help_4_text;
    private LinearLayout help_5;
    private ImageView help_5_button;
    private TextView help_5_text;


    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof MainActivity) {
            activityContext = (MainActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        pullRefreshLayout = v.findViewById(R.id.pull_layout);
        pullRefreshLayout.setOnRefreshListener(this);

        helloMassage = v.findViewById(R.id.hello_message);
        currentWeatherIcon = v.findViewById(R.id.main__current_weather_icon);
        currentWeatherError = v.findViewById(R.id.main__current_weather_error);
        currentWeatherText = v.findViewById(R.id.main__current_weather_text);
        currentWeatherComment1Front = v.findViewById(R.id.main__current_weather_comment1_front);
        currentWeatherComment1Back = v.findViewById(R.id.main__current_weather_comment1_back);
        currentWeatherComment2 = v.findViewById(R.id.main__current_weather_comment2);
        weatherReportLayout = v.findViewById(R.id.weather_report_layout);
        LinearLayout weatherLocationSearch = v.findViewById(R.id.weather_location_search);

        // 관심지역 편집 시 투명도 조절을 위한 타 레이아웃 변수
        subBannerLayout = v.findViewById(R.id.subBanner_linear);
        View top_layout = v.findViewById(R.id.top_layout);
        View hello_layout = v.findViewById(R.id.hello_layout);
        View current_layout = v.findViewById(R.id.current_layout);
        View search_layout = v.findViewById(R.id.search_layout);
        View observation_layout = v.findViewById(R.id.observation_layout);
        View star_layout = v.findViewById(R.id.star_layout);
        View night_sky_layout = v.findViewById(R.id.night_sky_layout);
        View tip_layout = v.findViewById(R.id.tip_layout);
        viewListWithoutInterestArea = new View[]{subBannerLayout, top_layout, hello_layout, current_layout, search_layout, observation_layout, star_layout, night_sky_layout, tip_layout};

        // userId 가져오기
        try {
            FileInputStream fis = getActivity().openFileInput("userId");
            String line = new BufferedReader(new InputStreamReader(fis)).readLine();
            userId = Long.parseLong(line);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 환영 메세지
//        RetrofitClient.getApiService().getNickName(userId).enqueue(new Callback<Map<String, String>>() {
//            @Override
//            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
//                if (response.isSuccessful()) {
//                    helloMassage.setText("안녕하세요, " + response.body().get("nickName") + "님!\n별 보러 떠나볼까요?");
//                } else {
//                    Log.e(TAG, "닉네임 조회 오류");
//                }
//            }
//            @Override
//            public void onFailure(Call<Map<String, String>> call, Throwable t) {
//                Log.e(TAG, t.getMessage());
//            }
//        });

        checkLocationPermission();

        GpsTracker gpsTracker = new GpsTracker(getContext());
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

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
            String SD = address.getAdminArea(); // 서울특별시
            String SGG = address.getLocality() == null ? address.getSubLocality() : address.getLocality(); // 서대문구
            Log.d(TAG, "현 위치 = " + SD + ", " + SGG);

            if (SD == null || !SD.contains("세종") && SGG == null) {
                findLocation = false;
                currentWeatherError.setVisibility(View.VISIBLE);
                currentWeatherError.setText("현 위치를 불러올 수 없습니다.");
                currentWeatherText.setVisibility(View.GONE);

            } else {
                findLocation = true;
                currentWeatherError.setVisibility(View.VISIBLE);
                currentWeatherError.setText("현 위치 날씨를 분석하는 중...");
                currentWeatherText.setVisibility(View.GONE);

                Date date = new Date(System.currentTimeMillis());
                NearestAreaDTO nearestAreaDTO = new NearestAreaDTO(SGG, latitude, longitude, yyyy_MM_dd.format(date), Integer.valueOf(HH.format(date)));
                if (SD.contains("세종")) nearestAreaDTO.setSgg("세종");

                WeatherRetrofitClient.getApiService()
                        .getNearestAreaWeatherInfo(nearestAreaDTO)
                        .enqueue(new Callback<MainInfo>() {
                            @Override
                            public void onResponse(Call<MainInfo> call, Response<MainInfo> response) {
                                if (response.isSuccessful()) {
                                    currentWeatherError.setVisibility(View.GONE); // 오류 문구 해제
                                    currentWeatherText.setVisibility(View.VISIBLE);
                                    currentWeatherIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.main__weather_sun));
                                    MainInfo mainInfo = response.body();
                                    currentWeatherComment1Front.setText(MM_dd_HH.format(date));
                                    currentWeatherComment1Back.setText(" " + mainInfo.getLocation() + " 날씨");
                                    currentWeatherComment2.setText(mainInfo.getComment());

                                    // 날씨 요약 레포트 클릭 시 날씨 상세 페이지로 이동
                                    weatherReportLayout.setOnClickListener((view) -> {
                                        if (!editMode) {
                                            Intent intent = new Intent(getActivity().getApplicationContext(), WeatherActivity.class);
                                            LocationDTO locationDTO = new LocationDTO(latitude, longitude, mainInfo.getRegionId(), null, mainInfo.getLocation());
                                            intent.putExtra("locationDTO", locationDTO);
                                            startActivity(intent);
                                        } else {
                                            editModeOut();
                                        }
                                    });
                                } else {
                                    Log.e(TAG, "getNearestAreaWeatherInfo 오류");
                                }
                            }

                            @Override
                            public void onFailure(Call<MainInfo> call, Throwable t) {
                                Log.e(TAG, t.getMessage());
                            }
                        });
            }
        }

        // 위치 허용 동의 후 최초 발생
        if (findLocation == null) {
            currentWeatherError.setVisibility(View.VISIBLE);
            currentWeatherError.setText("새로고침이 필요합니다.");
        }

        // 관심지역 조회
        interestAreaInitLayout = v.findViewById(R.id.interest_area_init_layout);
        addInterestAreaInit = v.findViewById(R.id.addInterestAreaInit);
        interestAreaLayout = v.findViewById(R.id.interest_area_layout);
        interestArea0 = v.findViewById(R.id.interestArea0);
        interestArea1 = v.findViewById(R.id.interestArea1);
        interestArea2 = v.findViewById(R.id.interestArea2);
        addInterestArea = v.findViewById(R.id.addInterestArea);
        resetInterestArea();

        // 관심지역 편집
        editInterestArea = v.findViewById(R.id.editInterestArea);
        editInterestArea.setOnClickListener(view -> {
            if (!editMode) { // 편집 모드 off -> on
                editMode = true;
                needRefresh = false;
                editInterestArea.setText("편집완료");
                Arrays.stream(viewListWithoutInterestArea).forEach(layout -> layout.setAlpha(0.3f)); // 타 영역 투명하게

                if (interestRegionIdList.size() >= 1) {
                    interestArea0.showInterestAreaDelete(true);
                    interestArea0.setInterestAreaDeleteClickListener(() -> {
                        needRefresh = true;
                        InterestAreaRetrofitClient.getApiService()
                                .deleteInterestArea(userId, interestRegionIdList.get(0), interestRegionTypeList.get(0))
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Log.d(TAG, "관심지역 삭제 성공");
                                        interestArea0.setVisibility(View.GONE);
                                        editModeOut();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.e(TAG, "서버 api 호출 실패");
                                    }
                                });
                    });
                }
                if (interestRegionIdList.size() >= 2) {
                    interestArea1.showInterestAreaDelete(true);
                    interestArea1.setInterestAreaDeleteClickListener(() -> {
                        needRefresh = true;
                        InterestAreaRetrofitClient.getApiService()
                                .deleteInterestArea(userId, interestRegionIdList.get(1), interestRegionTypeList.get(1))
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Log.d(TAG, "관심지역 삭제 성공");
                                        interestArea1.setVisibility(View.GONE);
                                        editModeOut();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.e(TAG, "서버 api 호출 실패");
                                    }
                                });
                    });
                }
                if (interestRegionIdList.size() == 3) {
                    interestArea2.showInterestAreaDelete(true);
                    interestArea2.setInterestAreaDeleteClickListener(() -> {
                        needRefresh = true;
                        InterestAreaRetrofitClient.getApiService()
                                .deleteInterestArea(userId, interestRegionIdList.get(2), interestRegionTypeList.get(2))
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Log.d(TAG, "관심지역 삭제 성공");
                                        interestArea2.setVisibility(View.GONE);
                                        editModeOut();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.e(TAG, "서버 api 호출 실패");
                                    }
                                });
                    });
                }
            } else { // 편집 모드 on -> off
                editModeOut();
            }
        });

        // 편집 모드에서 타 영역 클릭시 편집모드 취소됨
        Arrays.stream(viewListWithoutInterestArea).forEach(
                viewItem -> viewItem.setOnClickListener(view -> {
                    if (editMode) editModeOut();
                })
        );

        // 관심지역 클릭
        interestArea0.setOnClickListener((view) -> {
            if (!editMode) {
                Intent intent = new Intent(getActivity().getApplicationContext(), InterestAreaWeatherActivity.class);
                intent.putExtra("regionId", interestRegionIdList.get(0));
                intent.putExtra("regionType", interestRegionTypeList.get(0));
                startActivity(intent);
            } else {
                editModeOut();
            }
        });
        interestArea1.setOnClickListener((view) -> {
            if (!editMode) {
                Intent intent = new Intent(getActivity().getApplicationContext(), InterestAreaWeatherActivity.class);
                intent.putExtra("regionId", interestRegionIdList.get(1));
                intent.putExtra("regionType", interestRegionTypeList.get(1));
                startActivity(intent);
            } else {
                editModeOut();
            }
        });
        interestArea2.setOnClickListener((view) -> {
            if (!editMode) {
                Intent intent = new Intent(getActivity().getApplicationContext(), InterestAreaWeatherActivity.class);
                intent.putExtra("regionId", interestRegionIdList.get(2));
                intent.putExtra("regionType", interestRegionTypeList.get(2));
                startActivity(intent);
            } else {
                editModeOut();
            }
        });

        // 관심지역 추가
        addInterestArea = v.findViewById(R.id.addInterestArea);
        addInterestArea.setOnClickListener(view -> {
            if (!editMode) {
                if (interestRegionIdList.size() >= 3) {
                    //삭제 유도 팝업
                    Intent intent = new Intent(getActivity().getApplicationContext(), InterestAreaPopActivity.class);
                    startActivity(intent);
                } else {
                    // 관심지역 추가 페이지로 이동
                    Intent intent = new Intent(getActivity().getApplicationContext(), WeatherLocationSearchActivity.class);
                    intent.putExtra("interestAreaIntent", new InterestAreaIntent(userId, interestRegionNameList));
                    startActivity(intent);
                    needSet = true;
                }
            } else {
                editModeOut();
            }
        });
        addInterestAreaInit = v.findViewById(R.id.addInterestAreaInit);
        addInterestAreaInit.setOnClickListener(view -> {
            if (!editMode) {
                // 관심지역 추가 페이지로 이동
                Intent intent = new Intent(getActivity().getApplicationContext(), WeatherLocationSearchActivity.class);
                intent.putExtra("interestAreaIntent", new InterestAreaIntent(userId, new ArrayList<>()));
                startActivity(intent);
                needSet = true;
            } else {
                editModeOut();
            }
        });

        setSubbannerLayout(v);

        // 게시물 작성 페이지로 넘어가는 이벤트
        Button postWrite = v.findViewById(R.id.postWrite);
        postWrite.setOnClickListener(view -> {
            if (!editMode) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PostWriteActivity.class);
                startActivity(intent);
            } else {
                editModeOut();
            }
        });

        // 알림 페이지로 넘어가는 이벤트

        Button alarm = v.findViewById(R.id.main_alarm);
        alarm.setOnClickListener(view -> {
            if (!editMode) {
                Intent intent = new Intent(activityContext.getApplicationContext(), AlarmActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            } else {
                editModeOut();
            }
        });

        //위치 검색바 클릭 시 위치 검색 페이지로 이동하는 이벤트
        weatherLocationSearch.setOnClickListener(view -> {
            if (!editMode) {
                Intent intent = new Intent(getActivity().getApplicationContext(), WeatherLocationSearchActivity.class);
                startActivity(intent);
            } else {
                editModeOut();
            }
        });

        // 오늘 보기좋은 관측지
        RecyclerView bestFitRecyclerView = v.findViewById(R.id.main_best_fit_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        bestFitRecyclerView.setLayoutManager(linearLayoutManager);
        BestFitObservationAdapter adapter = new BestFitObservationAdapter();
        RecyclerDecoration obsItemDecorator = new RecyclerDecoration(12, getActivity());
        bestFitRecyclerView.addItemDecoration(obsItemDecorator);
        bestFitRecyclerView.setAdapter(adapter);
        Call<List<ObservationSimpleParams>> call = RetrofitClient.getApiService().getBestFitObservationList();
        call.enqueue(new Callback<List<ObservationSimpleParams>>() {
            @Override
            public void onResponse
                    (Call<List<ObservationSimpleParams>> call, Response<List<ObservationSimpleParams>> response) {
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
        starViewAdapter = new StarViewAdpater2();
        startMonthText = v.findViewById(R.id.main_star_month_txt);
        starRecycler.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerDecoration starItemDecorator = new RecyclerDecoration(12, getActivity());
        starRecycler.addItemDecoration(starItemDecorator);
        starRecycler.setAdapter(starViewAdapter);
        setTodayStarLayout();

        // 최근 관측 후기
        RecyclerView reviewRecycler = v.findViewById(R.id.main_review_recycler);
        moveReviewBtn = v.findViewById(R.id.main_move_review);
        recentReviewAdapter = new RecentReviewAdapter();
        reviewRecycler.setAdapter(recentReviewAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        reviewRecycler.setLayoutManager(layoutManager);
        setCurrentReviewLayout();

        //도움말
        help_1 = v.findViewById(R.id.main_help_1);
        help_1_button = v.findViewById(R.id.main_help_1_button);
        help_1_text = v.findViewById(R.id.main_help_1_text);
        help_2 = v.findViewById(R.id.main_help_2);
        help_2_button = v.findViewById(R.id.main_help_2_button);
        help_2_text = v.findViewById(R.id.main_help_2_text);
        help_3 = v.findViewById(R.id.main_help_3);
        help_3_button = v.findViewById(R.id.main_help_3_button);
        help_3_text = v.findViewById(R.id.main_help_3_text);
        help_4 = v.findViewById(R.id.main_help_4);
        help_4_button = v.findViewById(R.id.main_help_4_button);
        help_4_text = v.findViewById(R.id.main_help_4_text);
        help_5 = v.findViewById(R.id.main_help_5);
        help_5_button = v.findViewById(R.id.main_help_5_button);
        help_5_text = v.findViewById(R.id.main_help_5_text);
        setHelp();

        return v;
    }

    private void setSubbannerLayout(View v) {
        //서브 배너 가져오기
        subBanner = v.findViewById(R.id.subBanner);
        Call<SubBanner> subBannerCall = com.starrynight.tourapiproject.myPage.myPageRetrofit.RetrofitClient.getApiService().getLastSubBanner();
        subBannerCall.enqueue(new Callback<SubBanner>() {
            @Override
            public void onResponse(Call<SubBanner> call, Response<SubBanner> response) {
                if (response.isSuccessful()) {
                    SubBanner banner = response.body();
                    if (banner != null) {
                        if (banner.isShow()) { //isShow가 true면 배너가 보일 수 있도록 한다
                            subBannerLayout.setVisibility(View.VISIBLE);
                            Glide.with(mContext).load("https://starry-night.s3.ap-northeast-2.amazonaws.com/subBanner/" + banner.getBannerImage()).fitCenter().into(subBanner);
                        } else {
                            subBannerLayout.setVisibility(View.GONE);
                        }
                        subBanner.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (banner.getLink() != null) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(banner.getLink()));
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                } else {
                    Log.e(TAG1, "서브 배너 오류");
                }
            }

            @Override
            public void onFailure(Call<SubBanner> call, Throwable t) {
                Log.e(TAG1, t.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().detach(this).commit();
            ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().attach(this).commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().detach(this).commit();
        ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().attach(this).commit();
        pullRefreshLayout.setRefreshing(false);
    }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        }
    }

    private void setTodayStarLayout() {
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH) + 1;
        startMonthText.setText(month + MAIN_STAR_TEXT);

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
                Log.e(TAG, "오늘의 별자리" + t.getMessage());
            }
        });

        // item 클릭 시 해당 아이템 constId 넘겨주기
        starViewAdapter.setOnItemClickListener(new OnStarItemClickListener2() {
            @Override
            public void onItemClick(StarViewAdpater2.ViewHolder holder, View view, int position) {
                StarItem item = starViewAdapter.getItem(position);
                Intent intent = new Intent(activityContext.getApplicationContext(), StarActivity.class);
                intent.putExtra("constName", item.getConstName());
                Log.d("itemConstName", item.getConstName());
                startActivity(intent);
            }
        });

        move_star_btn.setOnClickListener(view -> {
            if (!editMode) {
                Intent intent = new Intent(activityContext.getApplicationContext(), StarAllActivity.class);
                startActivity(intent);
            } else {
                editModeOut();
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

        moveReviewBtn.setOnClickListener(view -> {
            if (!editMode) activityContext.movePost();
            else editModeOut();
        });
    }

    private void setHelp() {

        help_1.setOnClickListener(view -> {
            if (!editMode) {
                if (help_1_text.getVisibility() == View.VISIBLE) {
                    help_1_button.setRotation(180);
                    help_1_text.setVisibility(View.GONE);
                } else {
                    help_1_button.setRotation(0);
                    help_1_text.setVisibility(View.VISIBLE);
                }
            } else {
                editModeOut();
            }
        });
        help_2.setOnClickListener(view -> {
            if (!editMode) {
                if (help_2_text.getVisibility() == View.VISIBLE) {
                    help_2_button.setRotation(180);
                    help_2_text.setVisibility(View.GONE);
                } else {
                    help_2_button.setRotation(0);
                    help_2_text.setVisibility(View.VISIBLE);
                }
            } else {
                editModeOut();
            }
        });
        help_3.setOnClickListener(view -> {
            if (!editMode) {
                if (help_3_text.getVisibility() == View.VISIBLE) {
                    help_3_button.setRotation(180);
                    help_3_text.setVisibility(View.GONE);
                } else {
                    help_3_button.setRotation(0);
                    help_3_text.setVisibility(View.VISIBLE);
                }
            } else {
                editModeOut();
            }
        });
        help_4.setOnClickListener(view -> {
            if (!editMode) {
                if (help_4_text.getVisibility() == View.VISIBLE) {
                    help_4_button.setRotation(180);
                    help_4_text.setVisibility(View.GONE);
                } else {
                    help_4_button.setRotation(0);
                    help_4_text.setVisibility(View.VISIBLE);
                }
            } else {
                editModeOut();
            }
        });
        help_5.setOnClickListener(view -> {
            if (!editMode) {
                if (help_5_text.getVisibility() == View.VISIBLE) {
                    help_5_button.setRotation(180);
                    help_5_text.setVisibility(View.GONE);
                } else {
                    help_5_button.setRotation(0);
                    help_5_text.setVisibility(View.VISIBLE);
                }
            } else {
                editModeOut();
            }
        });
    }

    private void refreshFragment() {
        ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().detach(this).commit();
        ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().attach(this).commit();
    }

    private void editModeOut() {
        editMode = false;
        editInterestArea.setText("편집");
        Arrays.stream(viewListWithoutInterestArea).forEach(layout -> layout.setAlpha(1.0f));

        if (interestRegionIdList.size() >= 1) { // 1, 2, 3
            interestArea0.showInterestAreaDelete(false);
        }
        if (interestRegionIdList.size() >= 2) { // 2, 3
            interestArea1.showInterestAreaDelete(false);
        }
        if (interestRegionIdList.size() == 3) { // 3
            interestArea2.showInterestAreaDelete(false);
        }
        if (needRefresh) refreshFragment();
    }

    private void resetInterestArea() {
        InterestAreaRetrofitClient.getApiService()
                .getAllInterestArea(userId)
                .enqueue(new Callback<List<InterestAreaDTO>>() {
                    @Override
                    public void onResponse(Call<List<InterestAreaDTO>> call, Response<List<InterestAreaDTO>> response) {
                        if (response.isSuccessful()) {
                            List<InterestAreaDTO> interestAreaList = response.body();
                            interestRegionIdList = new ArrayList<>();
                            interestRegionTypeList = new ArrayList<>();
                            interestRegionNameList = new ArrayList<>();

                            interestAreaList.forEach(interestAreaDTO -> {
                                interestRegionIdList.add(interestAreaDTO.getRegionId());
                                interestRegionTypeList.add(interestAreaDTO.getRegionType());
                                interestRegionNameList.add(interestAreaDTO.getRegionName());
                            });

//                            System.out.println("interestRegionIdList = " + interestRegionIdList);
//                            System.out.println("interestRegionNameList = " + interestRegionNameList);

                            if (interestAreaList.size() == 0) { // 0
                                interestAreaInitLayout.setVisibility(View.VISIBLE);
                                interestAreaLayout.setVisibility(View.GONE);
                            }
                            if (interestAreaList.size() >= 1) { // 1, 2, 3
                                interestAreaInitLayout.setVisibility(View.GONE);
                                interestAreaLayout.setVisibility(View.VISIBLE);
                                interestArea0.setVisibility(View.VISIBLE);
                                interestArea0.setInterestAreaName(interestAreaList.get(0).getRegionName());
                                interestArea0.setInterestAreaImage(interestAreaList.get(0).getRegionImage());
                                interestArea0.setInterestAreaObservationalFit(interestAreaList.get(0).getObservationalFit());
                            }
                            if (interestAreaList.size() >= 2) { // 2, 3
                                interestArea1.setVisibility(View.VISIBLE);
                                interestArea1.setInterestAreaName(interestAreaList.get(1).getRegionName());
                                interestArea1.setInterestAreaImage(interestAreaList.get(1).getRegionImage());
                                interestArea1.setInterestAreaObservationalFit(interestAreaList.get(1).getObservationalFit());
                            }
                            if (interestAreaList.size() == 3) { // 3
                                interestArea2.setVisibility(View.VISIBLE);
                                interestArea2.setInterestAreaName(interestAreaList.get(2).getRegionName());
                                interestArea2.setInterestAreaImage(interestAreaList.get(2).getRegionImage());
                                interestArea2.setInterestAreaObservationalFit(interestAreaList.get(2).getObservationalFit());
                            }
                        } else {
                            Log.e(TAG, "서버 api 호출 실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<InterestAreaDTO>> call, Throwable t) {
                        Log.e("연결실패", t.getMessage());
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needSet) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            resetInterestArea();
            needSet = false;
        }
    }
}