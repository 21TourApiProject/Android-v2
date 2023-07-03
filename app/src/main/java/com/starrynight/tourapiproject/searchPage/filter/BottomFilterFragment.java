package com.starrynight.tourapiproject.searchPage.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.starrynight.tourapiproject.R;

import java.util.List;


public class BottomFilterFragment extends BottomSheetDialogFragment {
    static final String TAG = "BottomFilterFragment";

    public BottomFilterFragment() {}

//    List<HashTagItem> hashTagItems;
    List<HashTagItem> areaList;
    List<HashTagItem> themeList;
    List<HashTagItem> peopleList;
    List<HashTagItem> transportList;

    RecyclerView tagRecycler;
    FilterRecyclerAdapter adapter;
    FlexboxLayoutManager layoutManager;

    TextView locationBtn;
    TextView transportBtn;
    TextView peopleBtn;
    TextView themeBtn;

    ImageView locationDot;
    ImageView transportDot;
    ImageView peopleDot;
    ImageView themeDot;

    FilterType firstTab = FilterType.AREA;

    ImageView closeBtn;

    public static BottomFilterFragment newInstance(String param1, String param2) {
        BottomFilterFragment fragment = new BottomFilterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_filter, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationBtn = view.findViewById(R.id.filter_location);
        transportBtn = view.findViewById(R.id.filter_transport);
        peopleBtn = view.findViewById(R.id.filter_people);
        themeBtn = view.findViewById(R.id.filter_theme);

        locationDot = view.findViewById(R.id.filter_location_dot);
        peopleDot = view.findViewById(R.id.filter_people_dot);
        transportDot = view.findViewById(R.id.filter_transport_dot);
        themeDot = view.findViewById(R.id.filter_theme_dot);

        tagRecycler = view.findViewById(R.id.filter_tag_recycler);

        closeBtn = view.findViewById(R.id.filter_close);



        adapter = new FilterRecyclerAdapter(getContext(), areaList, transportList, peopleList, themeList);

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
                getActivity().onBackPressed();
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

        transportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchTab(FilterType.TRANSPORT);
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
    }

    public void switchTab(FilterType type){
        setDotActivate();
        locationBtn.setTextColor(getActivity().getColor(R.color.gray_500));
        transportBtn.setTextColor(getActivity().getColor(R.color.gray_500));
        peopleBtn.setTextColor(getActivity().getColor(R.color.gray_500));
        themeBtn.setTextColor(getActivity().getColor(R.color.gray_500));

        switch (type) {
            case AREA:
                adapter.switchTab(FilterType.AREA);
                locationBtn.setTextColor(getActivity().getColor(R.color.white));
                break;
            case TRANSPORT:
                adapter.switchTab(FilterType.TRANSPORT);
                setDotActivate();
                transportBtn.setTextColor(getActivity().getColor(R.color.white));
                break;
            case PEOPLE:
                adapter.switchTab(FilterType.PEOPLE);
                peopleBtn.setTextColor(getActivity().getColor(R.color.white));
                break;
            case THEME:
                adapter.switchTab(FilterType.THEME);
                themeBtn.setTextColor(getActivity().getColor(R.color.white));
                break;
        }
    }

    private void setDotActivate(){
        locationDot.setVisibility(View.GONE);
        transportDot.setVisibility(View.GONE);
        peopleDot.setVisibility(View.GONE);
        themeDot.setVisibility(View.GONE);

        for (HashTagItem item : areaList) {
            if (item.isActive == 1) {
                locationDot.setVisibility(View.VISIBLE);
                break;
            }
        }
        for (HashTagItem item : transportList) {
            if (item.isActive == 1) {
                transportDot.setVisibility(View.VISIBLE);
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
    }

    public void setDataLists(List<HashTagItem> areaList, List<HashTagItem> transportList, List<HashTagItem> peopleList, List<HashTagItem> themeList){

        this.areaList = areaList;
        this.transportList = transportList;
        this.peopleList = peopleList;
        this.themeList = themeList;

    }

    public void setFirstTab(FilterType firstTab){
        this.firstTab = firstTab;

    }
}