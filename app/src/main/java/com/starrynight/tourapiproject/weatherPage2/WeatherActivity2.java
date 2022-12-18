package com.starrynight.tourapiproject.weatherPage2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.common.Const;
import com.starrynight.tourapiproject.weatherPage2.OpenWeatherRetrofit.DetailWeatherData;
import com.starrynight.tourapiproject.weatherPage2.OpenWeatherRetrofit.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity2 extends AppCompatActivity {

    private static final String TAG = "Weather2";
    private static final String API_KEY = "7c7ba4d9df15258ce566f6592d875413";
    private static final String EXCLUDE = "current,minutely";
    private static final String UNITS = "metric";

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

    Double latitude; // 위도
    Double longitude; // 경도

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
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat hh = new SimpleDateFormat("hh"); //yyyy-MM-dd hh:mm:ss
        String getHour = hh.format(date);
        Log.d(TAG, "상세 날씨 기준 시간 : " + getHour);
        detailHour.setText(getHour + Const.Weather.DETAIL_HOUR);

        // 메인 페이지에서 위치 정보를 받아옴
        Intent mainIntent = getIntent();
        LocationDTO locationDTO = (LocationDTO) mainIntent.getSerializableExtra("location");
        latitude = locationDTO.getLatitude(); // 위도
        longitude = locationDTO.getLongitude(); // 경도

        latitude = 37.5734D;
        longitude = 126.9101D;

        // 시간별 상세 날씨 정보 호출
        Call<DetailWeatherData> getHourWeatherData = RetrofitClient.getApiService()
                .geDetailWeatherData(latitude, longitude, EXCLUDE, API_KEY, UNITS);

        getHourWeatherData.enqueue(new Callback<DetailWeatherData>() {
            @Override
            public void onResponse(Call<DetailWeatherData> call, Response<DetailWeatherData> response) {
                if (response.isSuccessful()) {
                    DetailWeatherData data = response.body();
                }
            }

            @Override
            public void onFailure(Call<DetailWeatherData> call, Throwable t) {

            }
        });

        //날씨 도움말 페이지 이동
        weatherHelp.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WeatherHelpActivity.class);
            startActivity(intent);
        });

    }
}