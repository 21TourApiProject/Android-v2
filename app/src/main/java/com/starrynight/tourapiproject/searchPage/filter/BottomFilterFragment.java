package com.starrynight.tourapiproject.searchPage.filter;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.util.List;
import java.util.Objects;


public class BottomFilterFragment extends BottomSheetDialogFragment {
    static final String TAG = "BottomFilterFragment";

    public BottomFilterFragment() {}

//    List<HashTagItem> hashTagItems;
    List<HashTagItem> areaList;
    List<HashTagItem> themeList;
    List<HashTagItem> peopleList;
    List<HashTagItem> facilityList;
    List<HashTagItem> feeList;

    RecyclerView tagRecycler;
    FilterRecyclerAdapter adapter;
    FlexboxLayoutManager layoutManager;

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



        adapter = new FilterRecyclerAdapter(getContext(), areaList, peopleList, themeList, facilityList, feeList);

        layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);

        tagRecycler.setLayoutManager(layoutManager);
        tagRecycler.setAdapter(adapter);

        setTabOnClick();
        setDotActivate();
        switchTab(firstTab);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });

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
        setDotActivate();
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

    private void setDotActivate(){
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

    public void setDataLists(List<HashTagItem> areaList, List<HashTagItem> peopleList, List<HashTagItem> themeList, List<HashTagItem> facilityList, List<HashTagItem> feeList){

        this.areaList = areaList;
        this.peopleList = peopleList;
        this.themeList = themeList;
        this.facilityList = facilityList;
        this.feeList = feeList;

    }

    public void setFirstTab(FilterType firstTab){
        this.firstTab = firstTab;

    }



}