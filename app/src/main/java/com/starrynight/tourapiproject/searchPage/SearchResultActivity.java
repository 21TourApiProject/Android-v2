package com.starrynight.tourapiproject.searchPage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.searchPage.filter.BottomFilterFragment;
import com.starrynight.tourapiproject.searchPage.filter.FilterType;
import com.starrynight.tourapiproject.searchPage.filter.HashTagItem;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.Filter;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.SearchKey;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.SearchParams1;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity {
    static final String TAG = "SearchResultActivity";

    String keyword = null;
    BottomFilterFragment filterFragment;

    List<HashTagItem> hashTagItems = new ArrayList<>();
    List<HashTagItem> areaList = new ArrayList<>();
    List<HashTagItem> themeList = new ArrayList<>();
    List<HashTagItem> peopleList = new ArrayList<>();
    List<HashTagItem> transportList = new ArrayList<>();

    LinearLayout locationBtn;
    LinearLayout transportBtn;
    LinearLayout peopleBtn;
    LinearLayout themeBtn;

    ViewPager2 resultViewPager;
    TabLayout tabLayout;
    ResultViewPagerAdapter resultViewPagerAdapter;

    List<Long> areaCodeList = new ArrayList<>();
    List<Long> hashTagIdList = new ArrayList<>();
    List<SearchParams1> observationResult;
    List<SearchParams1> postResult;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("create실행중");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        locationBtn = findViewById(R.id.sr_location_btn);
        transportBtn = findViewById(R.id.sr_transport_btn);
        peopleBtn = findViewById(R.id.sr_people_btn);
        themeBtn = findViewById(R.id.sr_theme_btn);

        resultViewPager = findViewById(R.id.sr_result_view_pager);
        tabLayout = findViewById(R.id.sr_tab_layout);

        ImageView back = findViewById(R.id.sr_back_btn);
        back.setOnClickListener(v -> finish());

        androidx.appcompat.widget.SearchView searchView = findViewById(R.id.sr_search_input);

        fragmentManager = getSupportFragmentManager();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                keyword = query;
                getObservation();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        getFilterHashtags();

    }

    @Override
    protected void onResume() {
        System.out.println("resume실행중");
        super.onResume();
        getObservation();
    }

    private void setViewPager() {
        resultViewPagerAdapter = new ResultViewPagerAdapter(getSupportFragmentManager(),getLifecycle(),observationResult,postResult);
        resultViewPager.setAdapter(resultViewPagerAdapter);
        final List<String> tabElement = Arrays.asList("관측지","게시글");

        //tabLyout와 viewPager 연결
        new TabLayoutMediator(tabLayout, resultViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
//                LinearLayout tabview = new LinearLayout(SearchResultActivity.this);
//                textView.setText(tabElement.get(position));
//                tab.setCustomView(textView);
                tab.setText(tabElement.get(position));
            }

        }).attach();
    }

    private void setFilter(){
        hashTagIdList.clear();
        areaCodeList.clear();
        for (HashTagItem h : hashTagItems) {
            if (h.getIsActive() == HashTagItem.VIEWTYPE_ACTIVE) {
                hashTagIdList.add(h.getId());
            }
        }
        for (HashTagItem h : areaList) {
            if (h.getIsActive() == HashTagItem.VIEWTYPE_ACTIVE) {
                areaCodeList.add(h.getId());
            }
        }
    }

    private void getObservation(){

        setFilter();

        Filter filter = new Filter(areaCodeList, hashTagIdList);
        SearchKey searchKey = new SearchKey(filter, keyword);
        Call<List<SearchParams1>> call = RetrofitClient.getApiService().getObservationWithFilter(searchKey);
        call.enqueue(new Callback<List<SearchParams1>>() {
            @Override
            public void onResponse(Call<List<SearchParams1>> call, Response<List<SearchParams1>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "관측지 검색 성공");
                    observationResult = response.body();
                    getPosts();

                } else {
                    Log.e(TAG, "관측지 검색 실패");
                }
            }

            @Override
            public void onFailure(Call<List<SearchParams1>> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });
    }

    private void getPosts(){

        setFilter();

        Filter filter = new Filter(areaCodeList, hashTagIdList);
        SearchKey searchKey = new SearchKey(filter, keyword);
        Call<List<SearchParams1>> call = RetrofitClient.getApiService().getPostWithFilter(searchKey);
        call.enqueue(new Callback<List<SearchParams1>>() {
            @Override
            public void onResponse(Call<List<SearchParams1>> call, Response<List<SearchParams1>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "게시물 검색 성공");
                    postResult = response.body();
                    setViewPager();

                } else {
                    Log.e(TAG, "게시물 검색 실패");
                }
            }

            @Override
            public void onFailure(Call<List<SearchParams1>> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });
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
                                getObservation();

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