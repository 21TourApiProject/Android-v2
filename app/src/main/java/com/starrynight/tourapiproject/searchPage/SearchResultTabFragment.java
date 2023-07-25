package com.starrynight.tourapiproject.searchPage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.SearchParams1;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SearchResultTabFragment extends Fragment {

    ViewPager2 resultViewPager;
    TabLayout tabLayout;
    ResultViewPagerAdapter resultViewPagerAdapter;

    String keyword;


    List<SearchParams1> observationResult = new ArrayList<>();
    List<SearchParams1> postResult = new ArrayList<>();

    public SearchResultTabFragment() {
        // Required empty public constructor
    }

    public SearchResultTabFragment(List<SearchParams1> observationResult, List<SearchParams1> postResult) {
        this.observationResult = observationResult;
        this.postResult = postResult;
//        resultViewPagerAdapter.setData(observationResult,postResult);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_search_result_tab, container, false);

        resultViewPager = v.findViewById(R.id.sr_result_view_pager);
        tabLayout = v.findViewById(R.id.sr_tab_layout);
        resultViewPager.setSaveEnabled(false);
        setViewPager();
        return v;
    }

    private void setViewPager() {
        resultViewPagerAdapter = new ResultViewPagerAdapter(getChildFragmentManager(),getLifecycle(),observationResult,postResult);
        resultViewPager.setAdapter(resultViewPagerAdapter);
        final List<String> tabElement = Arrays.asList("관측지","게시글");

        //tabLyout와 viewPager 연결
        new TabLayoutMediator(tabLayout, resultViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                tab.setText(tabElement.get(position));
            }

        }).attach();
    }

    public void setData(List<SearchParams1> observationResult, List<SearchParams1> postResult, String keyword) {
        this.observationResult = observationResult;
        this.postResult = postResult;
        this.keyword = keyword;
        resultViewPagerAdapter.setData(observationResult,postResult,keyword);
    }
}