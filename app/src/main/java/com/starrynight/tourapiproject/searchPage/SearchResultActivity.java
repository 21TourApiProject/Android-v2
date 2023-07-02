package com.starrynight.tourapiproject.searchPage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.searchPage.filter.BottomFilterFragment;
import com.starrynight.tourapiproject.searchPage.filter.FilterType;
import com.starrynight.tourapiproject.searchPage.filter.HashTagItem;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity {
    static final String TAG = "SearchResultActivity";

    String keyword = null;
    BottomFilterFragment filterFragment;

    List<HashTagItem> hashTagItems;
    List<HashTagItem> areaList;
    List<HashTagItem> themeList = new ArrayList<>();
    List<HashTagItem> peopleList = new ArrayList<>();
    List<HashTagItem> transportList = new ArrayList<>();

    LinearLayout locationBtn;
    LinearLayout transportBtn;
    LinearLayout peopleBtn;
    LinearLayout themeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        locationBtn = findViewById(R.id.sr_location_btn);
        transportBtn = findViewById(R.id.sr_transport_btn);
        peopleBtn = findViewById(R.id.sr_people_btn);
        themeBtn = findViewById(R.id.sr_theme_btn);

        ImageView back = findViewById(R.id.sr_back_btn);
        back.setOnClickListener(v -> finish());

        androidx.appcompat.widget.SearchView searchView = findViewById(R.id.sr_search_input);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                keyword = query;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });


        getFilterHashtags();

    }

    private void getFilterHashtags() {
        Call<List<HashTagItem>> areacall = RetrofitClient.getApiService().getAreaFilter();
        areacall.enqueue(new Callback<List<HashTagItem>>() {
            @Override
            public void onResponse(Call<List<HashTagItem>> call, Response<List<HashTagItem>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "지역필터 호출 성공");
                    areaList = response.body();

                    Call<List<HashTagItem>> hashtagCall = RetrofitClient.getApiService().getHashTag();
                    hashtagCall.enqueue(new Callback<List<HashTagItem>>() {
                        @Override
                        public void onResponse(Call<List<HashTagItem>> call, Response<List<HashTagItem>> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "해쉬태그 호출 성공");
                                hashTagItems = response.body();

                                for (HashTagItem item : hashTagItems) {
                                    switch (item.getCategory()) {
                                        case "THEME":
                                            themeList.add(item);
                                            break;
                                        case "TRANSPORT":
                                            transportList.add(item);
                                            break;
                                        case "PEOPLE":
                                            peopleList.add(item);
                                            break;
                                    }
                                }

                                setFilterOnClick();

                            } else {
                                Log.d(TAG, "해쉬태그 호출 실패");
                            }
                        }
                        @Override
                        public void onFailure(Call<List<HashTagItem>> call, Throwable t) {
                            Log.d(TAG, "해쉬태그 호출 오류");
                        }
                    });
                } else {
                    Log.d(TAG, "지역필터 호출 실패");
                }
            }
            @Override
            public void onFailure(Call<List<HashTagItem>> call, Throwable t) {
                Log.d(TAG, "지역필터 호출 오류");
            }
        });
    }

    private void setFilterOnClick() {
        if (filterFragment == null) {
            filterFragment = new BottomFilterFragment();
            filterFragment.setDataLists(areaList, transportList, peopleList, themeList);
        }

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterFragment.setFirstTab(FilterType.AREA);
                filterFragment.show(getSupportFragmentManager(), filterFragment.getTag());
            }
        });

        transportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterFragment.setFirstTab(FilterType.TRANSPORT);
                filterFragment.show(getSupportFragmentManager(), filterFragment.getTag());
            }
        });

        peopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterFragment.setFirstTab(FilterType.PEOPLE);
                filterFragment.show(getSupportFragmentManager(), filterFragment.getTag());
            }
        });

        themeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterFragment.setFirstTab(FilterType.THEME);
                filterFragment.show(getSupportFragmentManager(), filterFragment.getTag());
            }
        });
    }
}