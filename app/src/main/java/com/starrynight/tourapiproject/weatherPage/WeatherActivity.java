package com.starrynight.tourapiproject.weatherPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.common.Const;
import com.starrynight.tourapiproject.observationPage.ObservationsiteActivity;
import com.starrynight.tourapiproject.weatherPage.weatherRetrofit.AreaTimeDTO;
import com.starrynight.tourapiproject.weatherPage.weatherRetrofit.DayObservationalFit;
import com.starrynight.tourapiproject.weatherPage.weatherRetrofit.HourObservationalFit;
import com.starrynight.tourapiproject.weatherPage.weatherRetrofit.WeatherInfo;
import com.starrynight.tourapiproject.weatherPage.weatherRetrofit.WeatherRetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = "Weather";

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat hh = new SimpleDateFormat("HH");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat mm = new SimpleDateFormat("mm");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat MM_dd_EE = new SimpleDateFormat("MM. dd(EE)");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");

    private ImageView lightPollutionLevel;
    private TextView todayComment1;
    private TextView todayComment2;
    private TextView bestObservationalFit;
    private TextView mainEffect;

    private RecyclerView hour_recycler; // 시간별 예보 RecyclerView
    private RecyclerView day_recycler; // 주간 예보 RecyclerView
    List<HourObservationalFit> hourResult = new ArrayList<>(); // 시간별 예보 결과
    List<DayObservationalFit> dayResult = new ArrayList<>(); // 주간 예보 결과

    private TextView detailHour; // 상세 날씨 - 기준 시간
    private TextView detail_weather; // 상세 날씨 - 날씨
    private TextView detail_temp_highest; // 상세 날씨 - 기온 최고
    private TextView detail_temp_lowest; // 상세 날씨 - 기온 최저
    private TextView detail_rainfall_probability; // 상세 날씨 - 강수확률
    private TextView detail_humidity; // 상세 날씨 - 습도
    private TextView detail_cloud; // 상세 날씨 - 구름양
    private TextView detail_fine_dust; // 상세 날씨 - 미세먼지
    private TextView detail_wind_speed; // 상세 날씨 - 풍속
    private TextView detail_moon_age; // 상세 날씨 - 월령
    private TextView detail_sunrise; // 상세 날씨 - 일출
    private TextView detail_sunset; // 상세 날씨 - 일몰
    private TextView detail_moonrise; // 상세 날씨 - 월출
    private TextView detail_moonset; // 상세 날씨 - 월몰


    Double latitude; // 위도
    Double longitude; // 경도
    Long areaId; // WEATHER_AREA id
    Long observationId; // WEATHER_OBSERVATION id
    String hour; // 현재 hour ex) 18
    String min; // 현재 min ex) 10


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //변수, 레이아웃 id와 매칭
        ImageView weatherBack = findViewById(R.id.weather_back);
        ImageView weatherHelp = findViewById(R.id.weather_help);

        TextView todayDate = findViewById(R.id.today_date); // 9.17(토)
        TextView currentPosition = findViewById(R.id.current_position); // 양천읍
        View observatory = findViewById(R.id.observatory);
        TextView observatoryName = findViewById(R.id.observatory_name); // 의정부과학도서관
        ImageView observatory_arrow = findViewById(R.id.observatory_arrow); // 화살표
        lightPollutionLevel = findViewById(R.id.light_pollution_level);

        todayComment1 = findViewById(R.id.today_comment_1);
        todayComment2 = findViewById(R.id.today_comment_2);
        bestObservationalFit = findViewById(R.id.best_observation_fit);
        mainEffect = findViewById(R.id.main_effect);

        hour_recycler = findViewById(R.id.hour_recycler);
        hour_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        day_recycler = findViewById(R.id.day_recycler);
        day_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        detailHour = findViewById(R.id.detail_hour);
        detail_weather = findViewById(R.id.detail_weather);
        detail_temp_highest = findViewById(R.id.detail_temp_highest);
        detail_temp_lowest = findViewById(R.id.detail_temp_lowest);
        detail_rainfall_probability = findViewById(R.id.detail_rainfall_probability);
        detail_humidity = findViewById(R.id.detail_humidity);
        detail_cloud = findViewById(R.id.detail_cloud);
        detail_fine_dust = findViewById(R.id.detail_fine_dust);
        detail_wind_speed = findViewById(R.id.detail_wind_speed);
        detail_moon_age = findViewById(R.id.detail_moon_age);
        detail_sunrise = findViewById(R.id.detail_sunrise);
        detail_sunset = findViewById(R.id.detail_sunset);
        detail_moonrise = findViewById(R.id.detail_moonrise);
        detail_moonset = findViewById(R.id.detail_moonset);

        View weatherMap = findViewById(R.id.weather_map);


        // 현재 시간(hour) 조회
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        hour = hh.format(date);
        min = mm.format(date);
        todayDate.setText(MM_dd_EE.format(date).replace("0", ""));
        detailHour.setText(hour + Const.Weather.DETAIL_HOUR);

        Intent intent = getIntent();
        LocationDTO locationDTO = (LocationDTO) intent.getSerializableExtra("locationDTO");
        Boolean fromObserve = (Boolean) intent.getSerializableExtra("fromObserve"); // 관측지 페이지에서 넘어왔음을 의미
        latitude = locationDTO.getLatitude(); // 위도
        longitude = locationDTO.getLongitude(); // 경도
        areaId = locationDTO.getAreaId();
        observationId = locationDTO.getObservationId();

        if (Objects.nonNull(areaId)) {
            currentPosition.setVisibility(View.VISIBLE);
            currentPosition.setText(locationDTO.getLocation());
        }
        if (Objects.nonNull(observationId)) {
            observatory.setVisibility(View.VISIBLE);
            observatoryName.setText(locationDTO.getLocation());
            observatory.setOnClickListener(v -> {
                Intent observationIntent = new Intent(getApplicationContext(), ObservationsiteActivity.class);
                observationIntent.putExtra("observationId", observationId);
                if (Objects.nonNull(fromObserve) && fromObserve) {
                    observatory_arrow.setVisibility(View.GONE);
                    observationIntent.putExtra("fromWeather", true);
                }
                startActivity(observationIntent);
            });
        }

        AreaTimeDTO areaTimeDTO = new AreaTimeDTO(yyyy_MM_dd.format(date), Integer.valueOf(hour), latitude, longitude);
        if (Objects.nonNull(areaId)) areaTimeDTO.setAreaId(areaId);
        else if (Objects.nonNull(observationId)) areaTimeDTO.setObservationId(observationId);

        WeatherRetrofitClient.getApiService()
                .getWeatherInfo(areaTimeDTO)
                .enqueue(new Callback<WeatherInfo>() {
                    @Override
                    public void onResponse(Call<WeatherInfo> call, Response<WeatherInfo> response) {
                        if (response.isSuccessful()) {
                            WeatherInfo info = response.body();
                            WeatherInfo.DetailWeather detail = info.getDetailWeather();

                            setLightPollutionLevel(info.getLightPollutionLevel());
                            todayComment1.setText(info.getTodayComment1());
                            todayComment2.setText(info.getTodayComment2());
                            bestObservationalFit.setText(Const.Weather.BEST_OBSERVATIONAL_FIT + info.getBestObservationalFit() + Const.Weather.PERCENT);
                            if (info.getBestObservationalFit() < 60) {
                                bestObservationalFit.setBackgroundResource(R.drawable.wt__bad_observational_fit);
                                mainEffect.setBackgroundResource(R.drawable.wt__bad_observational_fit);
                                mainEffect.setText(info.getMainEffect());
                                mainEffect.setVisibility(View.VISIBLE);
                            }

                            hourResult = info.getHourObservationalFitList();
                            HourAdapter hourAdapter = new HourAdapter(hourResult);
                            hour_recycler.setAdapter(hourAdapter);

                            dayResult = info.getDayObservationalFitList();
                            DayAdapter dayAdapter = new DayAdapter(dayResult);
                            day_recycler.setAdapter(dayAdapter);

                            detail_weather.setText(detail.getWeatherText());
                            detail_temp_highest.setText(detail.getTempHighest());
                            detail_temp_lowest.setText(detail.getTempLowest());
                            detail_rainfall_probability.setText(detail.getRainfallProbability());
                            detail_humidity.setText(detail.getHumidity());
                            detail_cloud.setText(detail.getCloud());
                            detail_fine_dust.setText(detail.getFineDust());
                            detail_wind_speed.setText(detail.getWindSpeed());
                            detail_moon_age.setText(detail.getMoonAge());
                            detail_sunrise.setText(detail.getSunrise());
                            detail_sunset.setText(detail.getSunset());
                            detail_moonrise.setText(detail.getMoonrise());
                            detail_moonset.setText(detail.getMoonset());
                        } else {
                            Log.e(TAG, "서버 api 호출 실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherInfo> call, Throwable t) {
                        Log.e("연결실패", t.getMessage());
                    }
                });

//        best_observation_fit = findViewById(R.id.best_observation_fit);


        // 뒤로 가기
        weatherBack.setOnClickListener(v -> finish());

        //날씨 도움말 페이지 이동
        weatherHelp.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), WeatherHelpActivity.class));
        });

        // 실시간 날씨 지도
        weatherMap.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.weather.go.kr/wgis-nuri/html/map.html")));
        });
    }

    private void setLightPollutionLevel(Integer level) {
        if (level == 0) {
            lightPollutionLevel.setBackgroundResource(R.drawable.light_great);
        } else if (level == 1) {
            lightPollutionLevel.setBackgroundResource(R.drawable.light_good);
        } else if (level == 2) {
            lightPollutionLevel.setBackgroundResource(R.drawable.light_common);
        } else if (level == 3) {
            lightPollutionLevel.setBackgroundResource(R.drawable.light_bad);
        } else {
            lightPollutionLevel.setBackgroundResource(R.drawable.light_worst);
        }
    }

    // unix UTC 을 HH:mm 로 바꾸는 메서드
    public String getHHmm(String unixTime) {
        Date unixHourMin = new Date(Long.parseLong(unixTime) * 1000L);
        SimpleDateFormat HHourMin = new SimpleDateFormat("HH:mm");
        HHourMin.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        return HHourMin.format(unixHourMin);
    }
}