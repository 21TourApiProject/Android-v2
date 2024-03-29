package com.starrynight.tourapiproject.weatherPage;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.mainPage.interestArea.InterestAreaAddIntent;
import com.starrynight.tourapiproject.mainPage.interestArea.InterestAreaAddPopActivity;
import com.starrynight.tourapiproject.mainPage.interestArea.InterestAreaIntent;
import com.starrynight.tourapiproject.weatherPage.weatherRetrofit.WeatherRetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherLocationSearchActivity extends AppCompatActivity {

    private static final String TAG = "WeatherLocationSearch";

    Long userId;
    List<String> interestAreaNameList;
    Integer count;

    private EditText locationSearch;
    private RecyclerView locationResult;
    private LinearLayoutManager layoutManager;
    private TextView weatherLocationTitle;

    ArrayList<SearchLocationItem> searchItemArrayList, filteredList;
    SearchLocationItemAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_location_search);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == 1) {
                count++;
            }
        });

        if (getIntent().getSerializableExtra("interestAreaIntent") != null) {
            InterestAreaIntent interestAreaIntent = (InterestAreaIntent) getIntent().getSerializableExtra("interestAreaIntent");
            userId = interestAreaIntent.getUserId();
            interestAreaNameList = interestAreaIntent.getInterestAreaNameList();
            count = interestAreaNameList.size();

            weatherLocationTitle = findViewById(R.id.weatherLocationTitle);
            weatherLocationTitle.setText("관심지역 추가");
        }

        ImageView weatherLocationSearchFinish = findViewById(R.id.weatherLocationSearch_finish);
        locationSearch = findViewById(R.id.location_search);
        locationResult = findViewById(R.id.location_result);
        locationResult.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
        searchItemArrayList = new ArrayList<>();
        filteredList = new ArrayList<>();

        locationSearch.requestFocus(); // 키보드 바로 올라오게
        locationSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchLocation(locationSearch.getText().toString());
            }
        });

        //검색어 입력했을 때 나오는 관측지 리스트
        searchAdapter = new SearchLocationItemAdapter(searchItemArrayList);
        searchAdapter.setOnItemClickListener((holder, view, position) -> {
            SearchLocationItem item = searchAdapter.getItem(position);
            if (userId != null) { // 관심지역 추가
                if (count >= 3) {
                    Toast.makeText(getApplicationContext(), "관심지역은 최대 3개까지 등록할 수 있습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), InterestAreaAddPopActivity.class);
                    intent.putExtra("interestAreaAddIntent", new InterestAreaAddIntent(userId, item));
                    launcher.launch(intent);
                }
            } else { // 날씨 페이지
                Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
                LocationDTO locationDTO = new LocationDTO(item.getLatitude(), item.getLongitude(), null, null, item.getTitle());
                if (Objects.nonNull(item.getAreaId())) locationDTO.setAreaId(item.areaId);
                if (Objects.nonNull(item.getObservationId()))
                    locationDTO.setObservationId(item.observationId);
                intent.putExtra("locationDTO", locationDTO);
                startActivity(intent);
            }
        });
        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        locationResult.setLayoutManager(layoutManager);
        locationResult.setAdapter(searchAdapter);

        WeatherRetrofitClient.getApiService().getWeatherLocations().enqueue(new Callback<List<SearchLocationItem>>() {
            @Override
            public void onResponse(Call<List<SearchLocationItem>> call, Response<List<SearchLocationItem>> response) {
                if (response.isSuccessful()) {
                    searchItemArrayList.addAll(response.body());
                    if (interestAreaNameList != null) {
                        searchItemArrayList.removeIf(item -> interestAreaNameList.contains(item.getTitle()));
                    }
                    searchAdapter.filterList(searchItemArrayList);
                } else {
                    Log.d(TAG, "날씨 location 리스트 업로드 실패");
                }
            }

            @Override
            public void onFailure(Call<List<SearchLocationItem>> call, Throwable t) {
                Log.d(TAG, "날씨 location 리스트 업로드 인터넷 실패");
            }
        });

        weatherLocationSearchFinish.setOnClickListener(v -> finish()); // 창 닫기
    }

    private void searchLocation(String searchText) {

        filteredList.clear();
        for (int i = 0; i < searchItemArrayList.size(); i++) {
            if (searchItemArrayList.get(i).getTitle().contains(searchText) || searchItemArrayList.get(i).getSubtitle().contains(searchText)) {
                filteredList.add(searchItemArrayList.get(i));
            }
        }
        searchAdapter.filterList(filteredList);
    }
}