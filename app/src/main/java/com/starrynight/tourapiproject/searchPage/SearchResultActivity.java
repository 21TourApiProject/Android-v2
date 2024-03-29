package com.starrynight.tourapiproject.searchPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.mapPage.Activities;
import com.starrynight.tourapiproject.mapPage.MapFragment;
import com.starrynight.tourapiproject.searchPage.filter.BottomFilterFragment;
import com.starrynight.tourapiproject.searchPage.filter.FilterType;
import com.starrynight.tourapiproject.searchPage.filter.HashTagItem;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.Filter;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.SearchKey;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.SearchParams1;
import com.starrynight.tourapiproject.weatherPage.WeatherActivity;
import com.starrynight.tourapiproject.weatherPage.WeatherLoadingDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity {
    static final String TAG = "SearchResultActivity";

    WeatherLoadingDialog loadingDialog; // 로딩

    String keyword = "";
    BottomFilterFragment filterFragment;

    List<HashTagItem> hashTagItems = new ArrayList<>();
    List<HashTagItem> areaList = new ArrayList<>();
    List<HashTagItem> themeList = new ArrayList<>();
    List<HashTagItem> peopleList = new ArrayList<>();
    List<HashTagItem> facilityList = new ArrayList<>();
    List<HashTagItem> feeList = new ArrayList<>();

    LinearLayout locationBtn;
    LinearLayout peopleBtn;
    LinearLayout themeBtn;
    LinearLayout facilityBtn;
    LinearLayout feeBtn;

    LinearLayout locationParent;
    LinearLayout peopleParent;
    LinearLayout themeParent;
    LinearLayout facilityParent;
    LinearLayout feeParent;
    TextView locationParentText;
    TextView peopleParentText;
    TextView themeParentText;
    TextView facilityParentText;
    TextView feeParentText;
    ImageView locationParentImg;
    ImageView peopleParentImg;
    ImageView themeParentImg;
    ImageView facilityParentImg;
    ImageView feeParentImg;

    ImageView mapBtn;
    ImageView listBtn;

    FrameLayout fragment;

    int pagenum = 0;

    NestedScrollView scrollView;

    SearchResultTabFragment tabFragment;
    MapFragment mapFragment;

    Activities fromWhere = null;
    String fromHashTagName = null;

    List<Long> areaCodeList = new ArrayList<>();
    List<Long> hashTagIdList = new ArrayList<>();
    List<SearchParams1> observationResult = new ArrayList<>();
    List<SearchParams1> postResult = new ArrayList<>();

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        locationBtn = findViewById(R.id.sr_location_btn);
        peopleBtn = findViewById(R.id.sr_people_btn);
        themeBtn = findViewById(R.id.sr_theme_btn);
        facilityBtn = findViewById(R.id.sr_facility_btn);
        feeBtn = findViewById(R.id.sr_fee_btn);

        locationParent = findViewById(R.id.sr_location_btn);
        peopleParent = findViewById(R.id.sr_people_btn);
        themeParent = findViewById(R.id.sr_theme_btn);
        facilityParent = findViewById(R.id.sr_facility_btn);
        feeParent = findViewById(R.id.sr_fee_btn);
        locationParentText = findViewById(R.id.sr_location_btn_text);
        peopleParentText = findViewById(R.id.sr_people_btn_text);
        themeParentText = findViewById(R.id.sr_theme_btn_text);
        facilityParentText = findViewById(R.id.sr_facility_btn_text);
        feeParentText = findViewById(R.id.sr_fee_btn_text);
        locationParentImg = findViewById(R.id.sr_location_btn_img);
        peopleParentImg = findViewById(R.id.sr_people_btn_img);
        themeParentImg = findViewById(R.id.sr_theme_btn_img);
        facilityParentImg = findViewById(R.id.sr_facility_btn_img);
        feeParentImg = findViewById(R.id.sr_fee_btn_img);

        mapBtn = findViewById(R.id.sr_map_btn);
        listBtn = findViewById(R.id.sr_list_btn);

        fragment = findViewById(R.id.sr_fragment);

        scrollView = findViewById(R.id.sr_scroll_view);

        ImageView back = findViewById(R.id.sr_back_btn);
        back.setOnClickListener(v -> finish());

        androidx.appcompat.widget.SearchView searchView = findViewById(R.id.sr_search_input);

        fragmentManager = getSupportFragmentManager();

        loadingDialog = new WeatherLoadingDialog(SearchResultActivity.this);

        // 게시물 해시태그에서 넘어왔을 때
        Intent intent = getIntent();
        fromWhere = (Activities) intent.getSerializableExtra("FromWhere");

        fromHashTagName = intent.getStringExtra("hashTagName");

        Log.d(TAG, "인텐트 " + fromWhere + " " + fromHashTagName);


        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "검색 작동, 기존내용 초기화");
                keyword = query;
                pagenum = 0;
                observationResult.clear();
                postResult.clear();
                loadingDialog.show();
                mapFragment.initMapView();
                getObservation(0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    this.onQueryTextSubmit("");
                }
                return false;
            }
        });

        getFilterHashtags();


        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getChildAt(0).getBottom() <= (v.getHeight() + scrollY)) {
                    getObservation(++pagenum);
                }
            }
        });
        tabFragment = new SearchResultTabFragment(observationResult, postResult);
        mapFragment = new MapFragment(observationResult);

        fragmentManager.beginTransaction().replace(R.id.sr_fragment, tabFragment).commit();

//        getObservation(0);

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagenum = 0;
                mapFragment.initMapView();
                observationResult.clear();
                getObservation(pagenum);
                mapBtn.setVisibility(View.GONE);
                listBtn.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction().replace(R.id.sr_fragment, mapFragment).commit();
            }
        });

        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagenum = 0;
                observationResult.clear();
                getObservation(pagenum);
                listBtn.setVisibility(View.GONE);
                mapBtn.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction().replace(R.id.sr_fragment, tabFragment).commit();
            }
        });


    }


    private void setFilter() {
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

    public void getObservation(int page) {

        setFilter();

        Filter filter = new Filter(areaCodeList, hashTagIdList);
        SearchKey searchKey = new SearchKey(filter, keyword);
        Call<List<SearchParams1>> call = RetrofitClient.getApiService().getObservationWithFilter(searchKey, page);
        call.enqueue(new Callback<List<SearchParams1>>() {
            @Override
            public void onResponse(Call<List<SearchParams1>> call, Response<List<SearchParams1>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "관측지 검색 성공");
                    observationResult.addAll(response.body());
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

    private void getPosts() {

        setFilter();

        Filter filter = new Filter(areaCodeList, hashTagIdList);
        if (keyword == null) {
            keyword = "";
        }
        SearchKey searchKey = new SearchKey(filter, keyword);
        Call<List<SearchParams1>> call = RetrofitClient.getApiService().getPostWithFilter(searchKey);
        call.enqueue(new Callback<List<SearchParams1>>() {
            @Override
            public void onResponse(Call<List<SearchParams1>> call, Response<List<SearchParams1>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "게시물 검색 성공");
                    loadingDialog.dismiss();
                    postResult.clear();
                    postResult.addAll(response.body());
                    tabFragment.setData(observationResult, postResult, keyword);
                    mapFragment.setData(observationResult);
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

                    for (HashTagItem item : areaList) {
                        if (fromHashTagName != null && fromHashTagName.equals(item.getName())) {
                            item.setIsActive(HashTagItem.VIEWTYPE_ACTIVE);
                        }
                    }


                    Call<List<HashTagItem>> hashtagCall = RetrofitClient.getApiService().getHashTag();
                    hashtagCall.enqueue(new Callback<List<HashTagItem>>() {
                        @Override
                        public void onResponse(Call<List<HashTagItem>> call, Response<List<HashTagItem>> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "해쉬태그 호출 성공");
                                hashTagItems = response.body();

                                for (HashTagItem item : hashTagItems) {
                                    if (fromHashTagName != null && fromHashTagName.equals(item.getName())) {
                                        item.setIsActive(HashTagItem.VIEWTYPE_ACTIVE);
                                    }

                                    switch (item.getCategory()) {
                                        case "THEME":
                                            themeList.add(item);
                                            break;
                                        case "PEOPLE":
                                            peopleList.add(item);
                                            break;
                                        case "FACILITY":
                                            facilityList.add(item);
                                            break;
                                        case "FEE":
                                            feeList.add(item);
                                            break;
                                    }
                                }

                                setFilterOnClick();
                                setParentFilterActive();
                                getObservation(0);

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
            filterFragment.setCancelable(true);
            filterFragment.setDataLists(areaList, peopleList, themeList, facilityList, feeList, keyword);
        }

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterFragment.setFirstTab(FilterType.AREA);
                filterFragment.show(fragmentManager, filterFragment.getTag());
            }
        });

        peopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterFragment.setFirstTab(FilterType.PEOPLE);
                filterFragment.show(fragmentManager, filterFragment.getTag());
            }
        });

        themeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterFragment.setFirstTab(FilterType.THEME);
                filterFragment.show(fragmentManager, filterFragment.getTag());
            }
        });

        facilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterFragment.setFirstTab(FilterType.FACILITY);
                filterFragment.show(fragmentManager, filterFragment.getTag());
            }
        });

        feeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterFragment.setFirstTab(FilterType.FEE);
                filterFragment.show(fragmentManager, filterFragment.getTag());
            }
        });
    }

    public void clearResult() {
        observationResult.clear();
        postResult.clear();
    }

    public void setParentFilterActive() {
        locationParent.setBackgroundResource(R.drawable.search__category_bg);
        peopleParent.setBackgroundResource(R.drawable.search__category_bg);
        themeParent.setBackgroundResource(R.drawable.search__category_bg);
        facilityParent.setBackgroundResource(R.drawable.search__category_bg);
        feeParent.setBackgroundResource(R.drawable.search__category_bg);
        locationParentText.setTextColor(getColor(R.color.white));
        peopleParentText.setTextColor(getColor(R.color.white));
        themeParentText.setTextColor(getColor(R.color.white));
        facilityParentText.setTextColor(getColor(R.color.white));
        feeParentText.setTextColor(getColor(R.color.white));
        locationParentImg.setImageResource(R.drawable.search__filter_down);
        peopleParentImg.setImageResource(R.drawable.search__filter_down);
        themeParentImg.setImageResource(R.drawable.search__filter_down);
        facilityParentImg.setImageResource(R.drawable.search__filter_down);
        feeParentImg.setImageResource(R.drawable.search__filter_down);

        for (HashTagItem item : areaList) {
            if (item.getIsActive() == 1) {
                locationParent.setBackgroundResource(R.drawable.search__category_active_bg);
                locationParentText.setTextColor(getColor(R.color.point_blue));
                locationParentImg.setImageResource(R.drawable.search__filter_down_active);
                break;
            }
        }
        for (HashTagItem item : peopleList) {
            if (item.getIsActive() == 1) {
                peopleParent.setBackgroundResource(R.drawable.search__category_active_bg);
                peopleParentText.setTextColor(getColor(R.color.point_blue));
                peopleParentImg.setImageResource(R.drawable.search__filter_down_active);
                break;
            }
        }
        for (HashTagItem item : themeList) {
            if (item.getIsActive() == 1) {
                themeParent.setBackgroundResource(R.drawable.search__category_active_bg);
                themeParentText.setTextColor(getColor(R.color.point_blue));
                themeParentImg.setImageResource(R.drawable.search__filter_down_active);
                break;
            }
        }
        for (HashTagItem item : facilityList) {
            if (item.getIsActive() == 1) {
                facilityParent.setBackgroundResource(R.drawable.search__category_active_bg);
                facilityParentText.setTextColor(getColor(R.color.point_blue));
                facilityParentImg.setImageResource(R.drawable.search__filter_down_active);
                break;
            }
        }
        for (HashTagItem item : feeList) {
            if (item.getIsActive() == 1) {
                feeParent.setBackgroundResource(R.drawable.search__category_active_bg);
                feeParentText.setTextColor(getColor(R.color.point_blue));
                feeParentImg.setImageResource(R.drawable.search__filter_down_active);
                break;
            }
        }
    }
}