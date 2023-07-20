package com.starrynight.tourapiproject.searchPage.filter;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.searchPage.SearchResultActivity;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.Filter;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.SearchKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BottomFilterFragment extends BottomSheetDialogFragment {
    static final String TAG = "BottomFilterFragment";

    public BottomFilterFragment() {}

//    List<HashTagItem> hashTagItems;
    List<HashTagItem> areaList;
    List<HashTagItem> themeList;
    List<HashTagItem> peopleList;
    List<HashTagItem> facilityList;
    List<HashTagItem> feeList;

    List<Long> areaCodeList = new ArrayList<>();
    List<Long> hashTagIdList = new ArrayList<>();

    String keyword;

    RecyclerView tagRecycler;
    FilterRecyclerAdapter adapter;
    FlexboxLayoutManager layoutManager;

    LinearLayout refreshBtn;
    TextView resultBtn;

    TextView locationBtn;
    TextView peopleBtn;
    TextView themeBtn;
    TextView facilityBtn;
    TextView feeBtn;

    ImageView locationDot;
    ImageView peopleDot;
    ImageView themeDot;
    ImageView facilityDot;
    ImageView feeDot;

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

    FilterType firstTab = FilterType.AREA;

    ImageView closeBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.FilterBottomSheetDialogTheme);
        setCancelable(true);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().setCanceledOnTouchOutside(true);
        return inflater.inflate(R.layout.fragment_bottom_filter, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationBtn = view.findViewById(R.id.filter_location);
        peopleBtn = view.findViewById(R.id.filter_people);
        themeBtn = view.findViewById(R.id.filter_theme);
        facilityBtn = view.findViewById(R.id.filter_facility);
        feeBtn = view.findViewById(R.id.filter_fee);

        locationDot = view.findViewById(R.id.filter_location_dot);
        peopleDot = view.findViewById(R.id.filter_people_dot);
        themeDot = view.findViewById(R.id.filter_theme_dot);
        facilityDot = view.findViewById(R.id.filter_facility_dot);
        feeDot = view.findViewById(R.id.filter_fee_dot);

        tagRecycler = view.findViewById(R.id.filter_tag_recycler);

        closeBtn = view.findViewById(R.id.filter_close);
        refreshBtn = view.findViewById(R.id.filter_refresh);
        resultBtn = view.findViewById(R.id.filter_result_btn);

        locationParent = getActivity().findViewById(R.id.sr_location_btn);
        peopleParent = getActivity().findViewById(R.id.sr_people_btn);
        themeParent = getActivity().findViewById(R.id.sr_theme_btn);
        facilityParent = getActivity().findViewById(R.id.sr_facility_btn);
        feeParent = getActivity().findViewById(R.id.sr_fee_btn);
        locationParentText = getActivity().findViewById(R.id.sr_location_btn_text);
        peopleParentText = getActivity().findViewById(R.id.sr_people_btn_text);
        themeParentText = getActivity().findViewById(R.id.sr_theme_btn_text);
        facilityParentText = getActivity().findViewById(R.id.sr_facility_btn_text);
        feeParentText = getActivity().findViewById(R.id.sr_fee_btn_text);
        locationParentImg = getActivity().findViewById(R.id.sr_location_btn_img);
        peopleParentImg = getActivity().findViewById(R.id.sr_people_btn_img);
        themeParentImg = getActivity().findViewById(R.id.sr_theme_btn_img);
        facilityParentImg = getActivity().findViewById(R.id.sr_facility_btn_img);
        feeParentImg = getActivity().findViewById(R.id.sr_fee_btn_img);

        FilterOnClickItem filterOnClickItem = new FilterOnClickItem() {
            @Override
            public void onClick() {
                getSearchCount();
            }
        };

        adapter = new FilterRecyclerAdapter(getContext(), areaList, peopleList, themeList, facilityList, feeList, filterOnClickItem);

        layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);

        tagRecycler.setLayoutManager(layoutManager);
        tagRecycler.setAdapter(adapter);

        setTabOnClick();
        setFilterActivate();
        switchTab(firstTab);

        closeBtn.setOnClickListener(view1 -> dismiss());

        resultBtn.setOnClickListener(view12 -> {
            ((SearchResultActivity)getActivity()).clearResult();
            ((SearchResultActivity)getActivity()).getObservation(0);
            setParentFilterActive();
            dismiss();
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFilterRefresh();
                setFilterActivate();
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void setFilterRefresh() {
        for (HashTagItem item : areaList) {
            item.setIsActive(HashTagItem.VIEWTYPE_INACTIVE);
        }
        for (HashTagItem item : peopleList) {
            item.setIsActive(HashTagItem.VIEWTYPE_INACTIVE);

        }
        for (HashTagItem item : themeList) {
            item.setIsActive(HashTagItem.VIEWTYPE_INACTIVE);

        }
        for (HashTagItem item : facilityList) {
            item.setIsActive(HashTagItem.VIEWTYPE_INACTIVE);

        }
        for (HashTagItem item : feeList) {
            item.setIsActive(HashTagItem.VIEWTYPE_INACTIVE);

        }
    }

    private void setTabOnClick(){
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchTab(FilterType.AREA);
            }
        });

        peopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchTab(FilterType.PEOPLE);
            }
        });

        themeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchTab(FilterType.THEME);
            }
        });

        facilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchTab(FilterType.FACILITY);
            }
        });

        feeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchTab(FilterType.FEE);
            }
        });
    }

    public void switchTab(FilterType type){
        setFilterActivate();
        locationBtn.setTextColor(getActivity().getColor(R.color.gray_500));
        peopleBtn.setTextColor(getActivity().getColor(R.color.gray_500));
        themeBtn.setTextColor(getActivity().getColor(R.color.gray_500));
        facilityBtn.setTextColor(getActivity().getColor(R.color.gray_500));
        feeBtn.setTextColor(getActivity().getColor(R.color.gray_500));

        switch (type) {
            case AREA:
                adapter.switchTab(FilterType.AREA);
                locationBtn.setTextColor(getActivity().getColor(R.color.white));

                break;
            case PEOPLE:
                adapter.switchTab(FilterType.PEOPLE);
                peopleBtn.setTextColor(getActivity().getColor(R.color.white));
                break;
            case THEME:
                adapter.switchTab(FilterType.THEME);
                themeBtn.setTextColor(getActivity().getColor(R.color.white));
                break;
            case FACILITY:
                adapter.switchTab(FilterType.FACILITY);
                facilityBtn.setTextColor(getActivity().getColor(R.color.white));
                break;
            case FEE:
                adapter.switchTab(FilterType.FEE);
                feeBtn.setTextColor(getActivity().getColor(R.color.white));
                break;
        }
    }

    private void setFilterActivate(){
        locationDot.setVisibility(View.GONE);
        peopleDot.setVisibility(View.GONE);
        themeDot.setVisibility(View.GONE);
        facilityDot.setVisibility(View.GONE);
        feeDot.setVisibility(View.GONE);

        for (HashTagItem item : areaList) {
            if (item.isActive == 1) {
                locationDot.setVisibility(View.VISIBLE);
                break;
            }
        }
        for (HashTagItem item : peopleList) {
            if (item.isActive == 1) {
                peopleDot.setVisibility(View.VISIBLE);
                break;
            }
        }
        for (HashTagItem item : themeList) {
            if (item.isActive == 1) {
                themeDot.setVisibility(View.VISIBLE);
                break;
            }
        }
        for (HashTagItem item : facilityList) {
            if (item.isActive == 1) {
                facilityDot.setVisibility(View.VISIBLE);
                break;
            }
        }
        for (HashTagItem item : feeList) {
            if (item.isActive == 1) {
                feeDot.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void setParentFilterActive() {
        locationParent.setBackgroundResource(R.drawable.search__category_bg);
        peopleParent.setBackgroundResource(R.drawable.search__category_bg);
        themeParent.setBackgroundResource(R.drawable.search__category_bg);
        facilityParent.setBackgroundResource(R.drawable.search__category_bg);
        feeParent.setBackgroundResource(R.drawable.search__category_bg);
        locationParentText.setTextColor(getActivity().getColor(R.color.white));
        peopleParentText.setTextColor(getActivity().getColor(R.color.white));
        themeParentText.setTextColor(getActivity().getColor(R.color.white));
        facilityParentText.setTextColor(getActivity().getColor(R.color.white));
        feeParentText.setTextColor(getActivity().getColor(R.color.white));
        locationParentImg.setImageResource(R.drawable.search__filter_down);
        peopleParentImg.setImageResource(R.drawable.search__filter_down);
        themeParentImg.setImageResource(R.drawable.search__filter_down);
        facilityParentImg.setImageResource(R.drawable.search__filter_down);
        feeParentImg.setImageResource(R.drawable.search__filter_down);

        for (HashTagItem item : areaList) {
            if (item.isActive == 1) {
                locationParent.setBackgroundResource(R.drawable.search__category_active_bg);
                locationParentText.setTextColor(getActivity().getColor(R.color.point_blue));
                locationParentImg.setImageResource(R.drawable.search__filter_down_active);
                break;
            }
        }
        for (HashTagItem item : peopleList) {
            if (item.isActive == 1) {
                peopleParent.setBackgroundResource(R.drawable.search__category_active_bg);
                peopleParentText.setTextColor(getActivity().getColor(R.color.point_blue));
                peopleParentImg.setImageResource(R.drawable.search__filter_down_active);
                break;
            }
        }
        for (HashTagItem item : themeList) {
            if (item.isActive == 1) {
                themeParent.setBackgroundResource(R.drawable.search__category_active_bg);
                themeParentText.setTextColor(getActivity().getColor(R.color.point_blue));
                themeParentImg.setImageResource(R.drawable.search__filter_down_active);
                break;
            }
        }
        for (HashTagItem item : facilityList) {
            if (item.isActive == 1) {
                facilityParent.setBackgroundResource(R.drawable.search__category_active_bg);
                facilityParentText.setTextColor(getActivity().getColor(R.color.point_blue));
                facilityParentImg.setImageResource(R.drawable.search__filter_down_active);
                break;
            }
        }
        for (HashTagItem item : feeList) {
            if (item.isActive == 1) {
                feeParent.setBackgroundResource(R.drawable.search__category_active_bg);
                feeParentText.setTextColor(getActivity().getColor(R.color.point_blue));
                feeParentImg.setImageResource(R.drawable.search__filter_down_active);
                break;
            }
        }
    }

    public void setDataLists(List<HashTagItem> areaList, List<HashTagItem> peopleList, List<HashTagItem> themeList, List<HashTagItem> facilityList, List<HashTagItem> feeList, String keyword){

        this.areaList = areaList;
        this.peopleList = peopleList;
        this.themeList = themeList;
        this.facilityList = facilityList;
        this.feeList = feeList;

        this.keyword = keyword;

    }

    public void setFirstTab(FilterType firstTab){
        this.firstTab = firstTab;

    }

    private void setFilter(){
        hashTagIdList.clear();
        areaCodeList.clear();
        for (HashTagItem h : themeList) {
            if (h.getIsActive() == HashTagItem.VIEWTYPE_ACTIVE) {
                hashTagIdList.add(h.getId());
            }
        }
        for (HashTagItem h : peopleList) {
            if (h.getIsActive() == HashTagItem.VIEWTYPE_ACTIVE) {
                hashTagIdList.add(h.getId());
            }
        }
        for (HashTagItem h : facilityList) {
            if (h.getIsActive() == HashTagItem.VIEWTYPE_ACTIVE) {
                hashTagIdList.add(h.getId());
            }
        }
        for (HashTagItem h : feeList) {
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

    private void getSearchCount() {

        setFilter();

        com.starrynight.tourapiproject.searchPage.searchPageRetrofit.Filter filter = new Filter(areaCodeList, hashTagIdList);
        SearchKey searchKey = new SearchKey(filter, keyword);

        Call<Long> call = RetrofitClient.getApiService().getSearchCountWithFilter(searchKey);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "해쉬태그 호출 성공");
                    Long count = response.body();

                    resultBtn.setText(count+"개의 결과보기");

                } else {
                    Log.d(TAG, "해쉬태그 호출 실패");
                }
            }
            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.d(TAG, "해쉬태그 호출 오류");
            }
        });
    }


}