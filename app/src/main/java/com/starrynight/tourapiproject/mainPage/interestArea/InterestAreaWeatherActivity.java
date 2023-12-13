package com.starrynight.tourapiproject.mainPage.interestArea;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.mainPage.interestArea.interestAreaRetrofit.InterestAreaRetrofitClient;
import com.starrynight.tourapiproject.mainPage.interestArea.interestAreaRetrofit.InterestAreaWeatherDTO;
import com.starrynight.tourapiproject.weatherPage.LocationDTO;
import com.starrynight.tourapiproject.weatherPage.WeatherActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InterestAreaWeatherActivity extends AppCompatActivity {

    private static final String TAG = "InterestAreaWeather";

    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat MM_dd_EE = new SimpleDateFormat("MM. dd(EE)");


    private ImageView interest_area_back; // 뒤로가기 버튼
    private TextView interest_area_name; // 지역 또는 관측지 이름
    private TextView interest_area_date; // 날짜, 요일
    private ImageView interest_area_image; // 관측지 이미지
    private TextView interest_area_best_day; // 오늘 또는 내일
    private TextView interest_area_best_hour; // 제일 잘보이는 시간
    private TextView interest_area_best_observational_fit; // 최대 관측적합도
    private TextView interest_area_weather_report; // 날씨 요약 레포트
    private TextView interest_area_detail_weather; // 날씨 자세히 보기 버튼

    Long regionId;
    Integer regionType;
    InterestAreaWeatherDTO interestAreaWeather;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_area_weather);

        interest_area_back = findViewById(R.id.interest_area_back);
        interest_area_name = findViewById(R.id.interest_area_name);
        interest_area_date = findViewById(R.id.interest_area_date);
        interest_area_date.setText(MM_dd_EE.format(new Date(System.currentTimeMillis())));
        interest_area_image = findViewById(R.id.interest_area_image);
        interest_area_best_day = findViewById(R.id.interest_area_best_day);
        interest_area_best_hour = findViewById(R.id.interest_area_best_hour);
        interest_area_best_observational_fit = findViewById(R.id.interest_area_best_observational_fit);
        interest_area_weather_report = findViewById(R.id.interest_area_weather_report);
        interest_area_detail_weather = findViewById(R.id.interest_area_detail_weather);

        regionId = (Long) getIntent().getSerializableExtra("regionId");
        regionType = (Integer) getIntent().getSerializableExtra("regionType");

        System.out.println("regionId = " + regionId);
        System.out.println("regionType = " + regionType);

        InterestAreaRetrofitClient.getApiService()
                .getInterestAreaInfo(regionId, regionType)
                .enqueue(new Callback<InterestAreaWeatherDTO>() {
                    @Override
                    public void onResponse(Call<InterestAreaWeatherDTO> call, Response<InterestAreaWeatherDTO> response) {
                        if (response.isSuccessful()) {
                            interestAreaWeather = response.body();
                            System.out.println("interestAreaWeather = " + interestAreaWeather);
                            interest_area_name.setText(interestAreaWeather.getRegionName());
                            if(Objects.nonNull(interestAreaWeather.getRegionImage())){
                                Glide.with(getApplicationContext()).load(interestAreaWeather.getRegionImage()).into(interest_area_image);
                            }
                            interest_area_best_day.setText(interestAreaWeather.getBestDay());
                            interest_area_best_hour.setText(interestAreaWeather.getBestHour()+"시");
                            interest_area_best_observational_fit.setText(interestAreaWeather.getBestObservationalFit()+"%");
                            if(interestAreaWeather.getBestObservationalFit() < 60){
                                interest_area_best_observational_fit.setTextColor(getColor(R.color.point_red));
                            }
                            interest_area_weather_report.setText(interestAreaWeather.getWeatherReport());

                        } else {
                            Log.e(TAG, "서버 api 호출 실패");
                        }
                    }

                    @Override
                    public void onFailure(Call<InterestAreaWeatherDTO> call, Throwable t) {
                        Log.e("연결실패", t.getMessage());
                    }
                });


        // 상세 날씨 페이지로 이동
        interest_area_detail_weather.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
            LocationDTO locationDTO = new LocationDTO(interestAreaWeather.getLatitude(), interestAreaWeather.getLongitude(),
                    null, null, interestAreaWeather.getRegionName());
            if (regionType == 1) locationDTO.setObservationId(regionId);
            if (regionType == 2) locationDTO.setAreaId(regionId);
            intent.putExtra("locationDTO", locationDTO);
            startActivity(intent);
        });

        // 뒤로 가기
        interest_area_back.setOnClickListener(v -> finish());
    }

}