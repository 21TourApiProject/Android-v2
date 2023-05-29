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
import com.starrynight.tourapiproject.weatherPage2.AirKoreaRetrofit.AirKoreaRetrofitClient;
import com.starrynight.tourapiproject.weatherPage2.AirKoreaRetrofit.FineDustData;
import com.starrynight.tourapiproject.weatherPage2.AirKoreaRetrofit.Item;
import com.starrynight.tourapiproject.weatherPage2.OpenWeatherRetrofit.Daily;
import com.starrynight.tourapiproject.weatherPage2.OpenWeatherRetrofit.DetailWeatherData;
import com.starrynight.tourapiproject.weatherPage2.OpenWeatherRetrofit.Hourly;
import com.starrynight.tourapiproject.weatherPage2.OpenWeatherRetrofit.OpenWeatherRetrofitClient;
import com.starrynight.tourapiproject.weatherPage2.weatherRetrofit.DayObservationFit;
import com.starrynight.tourapiproject.weatherPage2.weatherRetrofit.HourObservationFit;
import com.starrynight.tourapiproject.weatherPage2.weatherRetrofit.WeatherRetrofitClient;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity2 extends AppCompatActivity {

    private static final String TAG = "Weather2";

    private static final String OPEN_WEATHER_API_KEY = "7c7ba4d9df15258ce566f6592d875413";
    private static final String OPEN_WEATHER_EXCLUDE = "current,minutely,alerts";
    private static final String OPEN_WEATHER_UNITS = "metric";
    private static final String OPEN_WEATHER_LANG = "kr";

    private static final String AIR_KOREA_API_KEY = "BdxNGWQJQFutFYE6DkjePTmerMbwG2fzioTf6sr69ecOAdLGMH4iiukF8Ex93YotSgkDOHe1VxKNOr8USSN6EQ%3D%3D";
    private static final String AIR_KOREA_RETURN_TYPE = "json";
    private static final String AIR_KOREA_INFORM_CODE = "PM10";


    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatyMdH = new SimpleDateFormat("yyyyMMddHH");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat formatH = new SimpleDateFormat("HH");

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


    private RecyclerView day_Recycler; // 주간 예보 RecyclerView
    private RecyclerView hour_Recycler; // 시간별 예보 RecyclerView
    List<DayObservationFit> dayResult = new ArrayList<>(); // 주간 예보 결과
    List<HourObservationFit> hourResult = new ArrayList<>(); // 시간별 예보 결과

    private TextView best_observation_fit; // 오늘의 최대 관측적합도

    Double latitude; // 위도
    Double longitude; // 경도
    String city; // 도 이름 ex) 서울
    String date; // 오늘 날짜 ex) 230303
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

        weatherHelp = findViewById(R.id.weather_help);


        // 현재 시간(hour) 조회
        hour = formatH.format(new Date(System.currentTimeMillis()));
        detailHour.setText(hour + Const.Weather.DETAIL_HOUR);

        // 메인 페이지에서 위경도, 도시 정보를 받아옴 (메인 페이지 완성 시 주석 해제)
//        Intent mainIntent = getIntent();
//        LocationDTO locationDTO = (LocationDTO) mainIntent.getSerializableExtra("location");
//        latitude = locationDTO.getLatitude(); // 위도
//        longitude = locationDTO.getLongitude(); // 경도
//        city = locationDTO.getCity(); // 도시

        latitude = 37.573386D;
        longitude = 126.910087D;
        city = "서울";

        // 시간별 상세 날씨 정보 호출
        OpenWeatherRetrofitClient.getApiService()
                .geDetailWeatherData(latitude, longitude, OPEN_WEATHER_EXCLUDE, OPEN_WEATHER_API_KEY, OPEN_WEATHER_UNITS, OPEN_WEATHER_LANG)
                .enqueue(new Callback<DetailWeatherData>() {
                    @Override
                    public void onResponse(Call<DetailWeatherData> call, Response<DetailWeatherData> response) {
                        if (response.isSuccessful()) {
                            DetailWeatherData data = response.body();
                            Hourly hourly = data.getHourly().get(0); // 현재 Hour 기준 날씨
                            Daily daily = data.getDaily().get(0); // 현재 Date 기준 날씨

                            unixToDate(hourly.getDt());
                            detail_weather_2.setText(hourly.getWeather().get(0).getDescription());
                            detail_temp_highest.setText(daily.getTemp().getMax());
                            detail_temp_lowest.setText(daily.getTemp().getMin());
                            detail_rainfall_probability.setText(Double.parseDouble(hourly.getPop()) * 100 + Const.Weather.PERCENT);
                            detail_humidity.setText(hourly.getHumidity() + Const.Weather.PERCENT);
                            detail_cloud.setText(hourly.getClouds() + Const.Weather.PERCENT);

                            detail_wind_speed.setText(hourly.getWindSpeed() + Const.Weather.METER_PER_SECOND);
                            detail_moon_age.setText(getMoonPhaseString(Double.valueOf(daily.getMoonPhase())));
                            detail_sunrise.setText(getHHmm(daily.getSunrise()));
                            detail_sunset.setText(getHHmm(daily.getSunset()));
                            detail_moonrise.setText(getHHmm(daily.getMoonrise()));
                            detail_moonset.setText(getHHmm(daily.getMoonset()));
                        } else {
                            Log.e(TAG, "open api 호출 실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailWeatherData> call, Throwable t) {
                        Log.e("연결실패", t.getMessage());
                    }
                });

        // 시간별 미세먼지 정보 호출
        AirKoreaRetrofitClient.getApiService()
                .getFineDustData(AIR_KOREA_API_KEY, AIR_KOREA_RETURN_TYPE, LocalDate.now().toString(), AIR_KOREA_INFORM_CODE)
                .enqueue(new Callback<FineDustData>() {
                    @Override
                    public void onResponse(Call<FineDustData> call, Response<FineDustData> response) {
                        if (response.isSuccessful()) {
                            FineDustData data = response.body();
                            Item item = data.getResponse().getBody().getItems().get(0);

                            Map<String, String> grade = new HashMap<>();
                            String[] split1 = item.getInformGrade().split(Const.Weather.FINE_DUST_SPLIT_1);
                            for (String s1 : split1) {
                                String[] split2 = s1.split(Const.Weather.FINE_DUST_SPLIT_2);
                                grade.put(split2[0], split2[1]);
                            }
                            detail_fine_dust.setText(grade.getOrDefault(city, ""));
                        } else {
                            Log.e(TAG, "air korea 호출 실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<FineDustData> call, Throwable t) {
                        Log.e("연결실패", t.getMessage());
                    }
                });

        //날씨 도움말 페이지 이동
        weatherHelp.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WeatherHelpActivity.class);
            startActivity(intent);
        });

        // 뒤로 가기
        ImageView back = findViewById(R.id.weather_back);
        back.setOnClickListener(v -> finish());


        date = "230301";

        // 시간별 예보
        hour_Recycler = findViewById(R.id.hour_recycler);
        hour_Recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        WeatherRetrofitClient.getApiService()
                .getHourObservationFit(date)
                .enqueue(new Callback<List<HourObservationFit>>() {
                    @Override
                    public void onResponse(Call<List<HourObservationFit>> call, Response<List<HourObservationFit>> response) {
                        if (response.isSuccessful()) {
                            System.out.println("DayObservationFit 서버 연결 성공");
                            hourResult = response.body();
                            HourAdapter hourAdapter = new HourAdapter(hourResult);
                            hour_Recycler.setAdapter(hourAdapter);
                        } else {
                            Log.e(TAG, "HourObservationFit 호출 실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<HourObservationFit>> call, Throwable t) {
                        Log.e("HourObservationFit 서버 연결 실패", t.getMessage());
                    }
                });

        best_observation_fit = findViewById(R.id.best_observation_fit);

        // 주간 예보
        day_Recycler = findViewById(R.id.day_recycler);
        day_Recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        WeatherRetrofitClient.getApiService()
                .getDayObservationFit(date)
                .enqueue(new Callback<List<DayObservationFit>>() {
                    @Override
                    public void onResponse(Call<List<DayObservationFit>> call, Response<List<DayObservationFit>> response) {
                        if (response.isSuccessful()) {
                            System.out.println("DayObservationFit 서버 연결 성공");
                            dayResult = response.body();
                            DayAdapter dayAdapter = new DayAdapter(dayResult);
                            day_Recycler.setAdapter(dayAdapter);
                        } else {
                            Log.e(TAG, "DayObservationFit 호출 실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DayObservationFit>> call, Throwable t) {
                        Log.e("DayObservationFit 서버 연결 실패", t.getMessage());
                    }
                });


    }

    // Unix 타임스탬프를 시간으로 변환하는 메서드
    public void unixToDate(String unixTime) {

        //yyyyMMddHH
        Date unixDate = new Date(Long.parseLong(unixTime) * 1000L);
        formatyMdH.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        String unixToDate = formatyMdH.format(unixDate);
        System.out.println("unixToDate = " + unixToDate);

        //HH
        Date date = new Date(Long.parseLong(unixTime) * 1000L);
        formatH.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        String unixToHour = formatH.format(date);
        System.out.println("unixToHour = " + unixToHour);
    }

    // 월령 수치를 문자열로 바꾸는 메서드
    public String getMoonPhaseString(Double moonPhase) {
        if ((0 <= moonPhase && moonPhase <= 0.06) || (0.94 <= moonPhase && moonPhase <= 1)) {
            return Const.Weather.VERY_GOOD;
        } else if ((0.07 <= moonPhase && moonPhase <= 0.13) || (0.85 <= moonPhase && moonPhase <= 93)) {
            return Const.Weather.GOOD;
        } else if ((0.14 <= moonPhase && moonPhase <= 0.21) || (0.75 <= moonPhase && moonPhase <= 0.84)) {
            return Const.Weather.NORMAL;
        } else if ((0.22 <= moonPhase && moonPhase <= 0.35) || (0.63 <= moonPhase && moonPhase <= 0.74)) {
            return Const.Weather.BAD;
        } else {
            return Const.Weather.VERY_BAD;
        }
    }

    // unix UTC 을 HH:mm 로 바꾸는 메서드
    public String getHHmm(String unixTime) {
        Date unixHourMin = new Date(Long.parseLong(unixTime) * 1000L);
        SimpleDateFormat formatHourMin = new SimpleDateFormat("HH:mm");
        formatHourMin.setTimeZone(java.util.TimeZone.getTimeZone("GMT+9"));
        return formatHourMin.format(unixHourMin);
    }
}