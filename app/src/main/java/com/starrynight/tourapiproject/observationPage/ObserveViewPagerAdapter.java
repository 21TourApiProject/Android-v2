package com.starrynight.tourapiproject.observationPage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.starrynight.tourapiproject.observationPage.observationPageRetrofit.Observation;
import com.starrynight.tourapiproject.searchPage.SearchResultListFragment;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.SearchParams1;

import java.util.ArrayList;
import java.util.List;

public class ObserveViewPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> items= new ArrayList<>();;

    public ObserveViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle , Observation observation) {
        super(fragmentManager, lifecycle);

        items.add(new ObserveInfoFragment(observation));
        items.add(new ObserveReviewFragment(observation));
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

//    public void setData(Observation observation) {
//        items.get(0).setData(observation);
//        items.get(1).setData(observation);
//    }
}
