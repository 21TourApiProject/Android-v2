package com.starrynight.tourapiproject.weatherPage2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.starrynight.tourapiproject.R;

public class WeatherHelpActivity extends AppCompatActivity {

    private ImageView help_1_button; // 별 쉽게 관찰하기
    private ImageView help_2_button; // 별 사진 제대로 찍기
    private ImageView help_3_button; // 별 관측 준비물
    private ImageView help_4_button; // 별 관측 에티켓
    private ImageView help_5_button; // 은하수를 보는 법

    private TextView help_1_text;
    private TextView help_2_text;
    private TextView help_3_text;
    private TextView help_4_text;
    private TextView help_5_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_help);

        help_1_button = findViewById(R.id.help_1_button);
        help_2_button = findViewById(R.id.help_2_button);
        help_3_button = findViewById(R.id.help_3_button);
        help_4_button = findViewById(R.id.help_4_button);
        help_5_button = findViewById(R.id.help_5_button);

        help_1_text = findViewById(R.id.help_1_text);
        help_2_text = findViewById(R.id.help_2_text);
        help_3_text = findViewById(R.id.help_3_text);
        help_4_text = findViewById(R.id.help_4_text);
        help_5_text = findViewById(R.id.help_5_text);

        help_1_button.setOnClickListener(view -> {
            if (help_1_text.getVisibility() == View.GONE) {
                help_1_button.setRotation(180);
                help_1_text.setVisibility(View.VISIBLE);
            } else {
                help_1_button.setRotation(0);
                help_1_text.setVisibility(View.GONE);
            }
        });

        help_2_button.setOnClickListener(view -> {
            if (help_2_text.getVisibility() == View.GONE) {
                help_2_button.setRotation(180);
                help_2_text.setVisibility(View.VISIBLE);
            } else {
                help_2_button.setRotation(0);
                help_2_text.setVisibility(View.GONE);
            }
        });

        help_3_button.setOnClickListener(view -> {
            if (help_3_text.getVisibility() == View.GONE) {
                help_3_button.setRotation(180);
                help_3_text.setVisibility(View.VISIBLE);
            } else {
                help_3_button.setRotation(0);
                help_3_text.setVisibility(View.GONE);
            }
        });

        help_4_button.setOnClickListener(view -> {
            if (help_4_text.getVisibility() == View.GONE) {
                help_4_button.setRotation(180);
                help_4_text.setVisibility(View.VISIBLE);
            } else {
                help_4_button.setRotation(0);
                help_4_text.setVisibility(View.GONE);
            }
        });

        help_5_button.setOnClickListener(view -> {
            if (help_5_text.getVisibility() == View.GONE) {
                help_5_button.setRotation(180);
                help_5_text.setVisibility(View.VISIBLE);
            } else {
                help_5_button.setRotation(0);
                help_5_text.setVisibility(View.GONE);
            }
        });

        // 뒤로 가기
        ImageView back = findViewById(R.id.weather_help_back);
        back.setOnClickListener(v -> finish());

    }
}