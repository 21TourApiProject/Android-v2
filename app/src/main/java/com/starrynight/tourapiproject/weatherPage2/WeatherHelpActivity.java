package com.starrynight.tourapiproject.weatherPage2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.starrynight.tourapiproject.R;

public class WeatherHelpActivity extends AppCompatActivity {

    private ImageView help_light_pollution_button; //광공해 버튼
    private ImageView help_temp_button; // 기온 버튼
    private ImageView help_rainfall_probability_button; // 강수확률 버튼
    private ImageView help_humidity_button; // 습도 버튼
    private ImageView help_cloud_button; // 구름량 버튼
    private ImageView help_fine_dust_button; // 미세먼지 버튼
    private ImageView help_wind_speed_button; // 풍속 버튼
    private ImageView help_moon_age_button; // 월령 버튼


    private TextView help_light_pollution_text; //광공해 설명
    private TextView help_temp_text; // 기온 설명
    private TextView help_rainfall_probability_text; // 강수확률 설명
    private TextView help_humidity_text; // 습도 설명
    private TextView help_cloud_text; // 구름량 설명
    private TextView help_fine_dust_text; // 미세먼지 설명
    private TextView help_wind_speed_text; // 풍속 설명
    private TextView help_moon_age_text; // 월령 설명


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_help);

        help_light_pollution_button = findViewById(R.id.help_light_pollution_button);
        help_temp_button = findViewById(R.id.help_temp_button);
        help_rainfall_probability_button = findViewById(R.id.help_rainfall_probability_button);
        help_humidity_button = findViewById(R.id.help_humidity_button);
        help_cloud_button = findViewById(R.id.help_cloud_button);
        help_fine_dust_button = findViewById(R.id.help_fine_dust_button);
        help_wind_speed_button = findViewById(R.id.help_wind_speed_button);
        help_moon_age_button = findViewById(R.id.help_moon_age_button);

        help_light_pollution_text = findViewById(R.id.help_light_pollution_text);
        help_temp_text = findViewById(R.id.help_temp_text);
        help_rainfall_probability_text = findViewById(R.id.help_rainfall_probability_text);
        help_humidity_text = findViewById(R.id.help_humidity_text);
        help_cloud_text = findViewById(R.id.help_cloud_text);
        help_fine_dust_text = findViewById(R.id.help_fine_dust_text);
        help_wind_speed_text = findViewById(R.id.help_wind_speed_text);
        help_moon_age_text = findViewById(R.id.help_moon_age_text);

        help_light_pollution_button.setOnClickListener(view -> {
            if (help_light_pollution_text.getVisibility() == View.GONE) {
                help_light_pollution_button.setRotation(270);
                help_light_pollution_text.setVisibility(View.VISIBLE);
            } else {
                help_light_pollution_button.setRotation(90);
                help_light_pollution_text.setVisibility(View.GONE);
            }
        });

        help_temp_button.setOnClickListener(view -> {
            if (help_temp_text.getVisibility() == View.GONE) {
                help_temp_button.setRotation(270);
                help_temp_text.setVisibility(View.VISIBLE);
            } else {
                help_temp_button.setRotation(90);
                help_temp_text.setVisibility(View.GONE);
            }
        });

        help_rainfall_probability_button.setOnClickListener(view -> {
            if (help_rainfall_probability_text.getVisibility() == View.GONE) {
                help_rainfall_probability_button.setRotation(270);
                help_rainfall_probability_text.setVisibility(View.VISIBLE);
            } else {
                help_rainfall_probability_button.setRotation(90);
                help_rainfall_probability_text.setVisibility(View.GONE);
            }
        });

        help_humidity_button.setOnClickListener(view -> {
            if (help_humidity_text.getVisibility() == View.GONE) {
                help_humidity_button.setRotation(270);
                help_humidity_text.setVisibility(View.VISIBLE);
            } else {
                help_humidity_button.setRotation(90);
                help_humidity_text.setVisibility(View.GONE);
            }
        });

        help_cloud_button.setOnClickListener(view -> {
            if (help_cloud_text.getVisibility() == View.GONE) {
                help_cloud_button.setRotation(270);
                help_cloud_text.setVisibility(View.VISIBLE);
            } else {
                help_cloud_button.setRotation(90);
                help_cloud_text.setVisibility(View.GONE);
            }
        });

        help_fine_dust_button.setOnClickListener(view -> {
            if (help_fine_dust_text.getVisibility() == View.GONE) {
                help_fine_dust_button.setRotation(270);
                help_fine_dust_text.setVisibility(View.VISIBLE);
            } else {
                help_fine_dust_button.setRotation(90);
                help_fine_dust_text.setVisibility(View.GONE);
            }
        });

        help_wind_speed_button.setOnClickListener(view -> {
            if (help_wind_speed_text.getVisibility() == View.GONE) {
                help_wind_speed_button.setRotation(270);
                help_wind_speed_text.setVisibility(View.VISIBLE);
            } else {
                help_wind_speed_button.setRotation(90);
                help_wind_speed_text.setVisibility(View.GONE);
            }
        });

        help_moon_age_button.setOnClickListener(view -> {
            if (help_moon_age_text.getVisibility() == View.GONE) {
                help_moon_age_button.setRotation(270);
                help_moon_age_text.setVisibility(View.VISIBLE);
            } else {
                help_moon_age_button.setRotation(90);
                help_moon_age_text.setVisibility(View.GONE);
            }
        });

        // 뒤로 가기
        ImageView back = findViewById(R.id.weather_help_back);
        back.setOnClickListener(v -> finish());

    }
}