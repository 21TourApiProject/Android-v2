package com.starrynight.tourapiproject.weatherPage2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.common.Const;
import com.starrynight.tourapiproject.weatherPage2.weatherRetrofit.AreaTimeDTO;
import com.starrynight.tourapiproject.weatherPage2.weatherRetrofit.DayObservationalFit;
import com.starrynight.tourapiproject.weatherPage2.weatherRetrofit.HourObservationalFit;
import com.starrynight.tourapiproject.weatherPage2.weatherRetrofit.WeatherInfo;
import com.starrynight.tourapiproject.weatherPage2.weatherRetrofit.WeatherRetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity2 extends AppCompatActivity {

    private static final String TAG = "Weather2";

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat YYYY_MM_DD_HH = new SimpleDateFormat("yyyyMMddHH");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat HH = new SimpleDateFormat("HH");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

    private TextView detailHour; //상세 날씨 - 기준 시간
    private TextView detail_weather_2; //상세 날씨 - 날씨
    private TextView detail_temp_highest; //상세 날씨 - 기온 최고
    private TextView detail_temp_lowest; //상세 날씨 - 기온 최저
    private TextView detail_rainfall_probability; //상세 날씨 - 강수확률
    private TextView detail_humidity; //상세 날씨 - 습도
    private TextView detail_cloud; //상세 날씨 - 구름양
    private TextView detail_fine_dust; //상세 날씨 - 미세먼지
    private TextView detail_wind_speed; //상세 날씨 - 풍속
    private TextView detail_moon_age; //상세 날씨 - 월령
    private TextView detail_sunrise; //상세 날씨 - 일출
    private TextView detail_sunset; //상세 날씨 - 일몰
    private TextView detail_moonrise; //상세 날씨 - 월출
    private TextView detail_moonset; //상세 날씨 - 월몰

    private ImageView weatherHelp; // 날씨 도움말 페이지

    private RecyclerView hour_Recycler; // 시간별 예보 RecyclerView
    private RecyclerView day_Recycler; // 주간 예보 RecyclerView
    List<HourObservationalFit> hourResult = new ArrayList<>(); // 시간별 예보 결과
    List<DayObservationalFit> dayResult = new ArrayList<>(); // 주간 예보 결과


    private TextView best_observation_fit; // 오늘의 최대 관측적합도

    Double latitude; // 위도
    Double longitude; // 경도
    Long areaId; // WEATHER_AREA id
    Long observationId; // WEATHER_OBSERVATION id
    String hour; // 현재 시간 ex) 18


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather2);

        //변수, 레이아웃 id와 매칭
        detailHour = findViewById(R.id.detail_hour);
        detail_weather_2 = findViewById(R.id.detail_weather_2);
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

        hour_Recycler = findViewById(R.id.hour_recycler);
        hour_Recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        day_Recycler = findViewById(R.id.day_recycler);
        day_Recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        weatherHelp = findViewById(R.id.weather_help);


        // 현재 시간(hour) 조회
        hour = HH.format(new Date(System.currentTimeMillis()));
        detailHour.setText(hour + Const.Weather.DETAIL_HOUR);

        // 메인 페이지에서 위경도, 지역 또는 관측지 정보를 받아옴 (메인 페이지 완성 시 주석 해제)
//        Intent mainIntent = getIntent();
//        LocationDTO locationDTO = (LocationDTO) mainIntent.getSerializableExtra("location");
//        latitude = locationDTO.getLatitude(); // 위도
//        longitude = locationDTO.getLongitude(); // 경도
//        areaId = locationDTO.getAreaId();
//        observationId = locationDTO.getObservationId();

        latitude = 37.573386D;
        longitude = 126.910087D;
        areaId = 1L;
        observationId = 1L;

        AreaTimeDTO areaTimeDTO = new AreaTimeDTO(YYYY_MM_DD.format(new Date(System.currentTimeMillis())), Integer.valueOf(hour), latitude, longitude);
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

                            hourResult = info.getHourObservationalFitList();
                            HourAdapter hourAdapter = new HourAdapter(hourResult);
                            hour_Recycler.setAdapter(hourAdapter);

                            dayResult = info.getDayObservationalFitList();
                            DayAdapter dayAdapter = new DayAdapter(dayResult);
                            day_Recycler.setAdapter(dayAdapter);

                            detail_weather_2.setText(detail.getWeatherText());
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

        //날씨 도움말 페이지 이동
        weatherHelp.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WeatherHelpActivity.class);
            startActivity(intent);
        });

        // 뒤로 가기
        ImageView back = findViewById(R.id.weather_back);
        back.setOnClickListener(v -> finish());

    }

    // unix UTC 을 HH:mm 로 바꾸는 메서드
    public String getHHmm(String unixTime) {
        Date unixHourMin = new Date(Long.parseLong(unixTime) * 1000L);
        SimpleDateFormat HHourMin = new SimpleDateFormat("HH:mm");
        HHourMin.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        return HHourMin.format(unixHourMin);
    }
}