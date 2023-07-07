package com.starrynight.tourapiproject.weatherPage2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.weatherPage2.weatherRetrofit.WeatherRetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherLocationSearchActivity extends AppCompatActivity {

    private static final String TAG = "WeatherLocationSearch";

    private EditText locationSearch;
    private RecyclerView locationResult;
    private LinearLayoutManager layoutManager;

    ArrayList<SearchLocationItem> searchItemArrayList, filteredList;
    SearchLocationItemAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_location_search);

        ImageView weatherLocationSearchFinish = findViewById(R.id.weatherLocationSearch_finish);
        locationSearch = findViewById(R.id.location_search);
        locationResult = findViewById(R.id.location_result);
        locationResult.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
        searchItemArrayList = new ArrayList<>();
        filteredList = new ArrayList<>();

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
        searchAdapter = new SearchLocationItemAdapter(searchItemArrayList, this);
        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        locationResult.setLayoutManager(layoutManager);
        locationResult.setAdapter(searchAdapter);

        WeatherRetrofitClient.getApiService().getWeatherLocations().enqueue(new Callback<List<SearchLocationItem>>() {
            @Override
            public void onResponse(Call<List<SearchLocationItem>> call, Response<List<SearchLocationItem>> response) {
                if (response.isSuccessful()) {
                    List<SearchLocationItem> locationList = response.body();
                    for (int i = 0; i < locationList.size() - 1; i++) {
                        searchItemArrayList.add(new SearchLocationItem(locationList.get(i).getTitle(), locationList.get(i).getSubtitle(), locationList.get(i).getLocationId(), locationList.get(i).getObservationValue()));
                    }
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