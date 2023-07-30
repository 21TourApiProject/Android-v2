package com.starrynight.tourapiproject.searchPage;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.MyPostSearchParams;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.SearchParams1;

import java.util.ArrayList;
import java.util.List;

public class ResultViewPagerAdapter extends FragmentStateAdapter {

    private ArrayList<SearchResultListFragment> items= new ArrayList<>();;

    public ResultViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle , List<SearchParams1> observationList, List<SearchParams1> postList) {
        super(fragmentManager, lifecycle);

        items = new ArrayList<>();
        items.add(new SearchResultListFragment(observationList));
        items.add(new SearchResultListFragment(postList));
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(List<SearchParams1> observationList, List<SearchParams1> postList, String keyword) {
        items.get(0).setData(observationList,keyword);
        items.get(1).setData(postList,keyword);
    }
}
