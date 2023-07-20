package com.starrynight.tourapiproject.searchPage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.observationPage.ObservationsiteActivity;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.SearchParams1;

import java.util.List;

public class SearchResultListFragment extends Fragment {

    RecyclerView recyclerView;
    SearchItemRecyclerAdapter adapter;
    GridLayoutManager layoutManager;
    LinearLayout noResultLayout;


    List<SearchParams1> list;

    public SearchResultListFragment() {}

    public SearchResultListFragment(List<SearchParams1> list) {
        this.list  = list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_search_result_list, container, false);

        noResultLayout = v.findViewById(R.id.srf_no_result);

        recyclerView = v.findViewById(R.id.srf_recycler);
        recyclerView.addItemDecoration(new SearchItemDecoration(20));
        adapter = new SearchItemRecyclerAdapter(getContext(), list);

        layoutManager = new GridLayoutManager(getContext(),2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnSearchResultItemClickListener(new OnSearchResultItemClickListener() {
            @Override
            public void onItemClick(SearchItemRecyclerAdapter.MyViewHolder holder, View view, int position) {
                SearchParams1 item = adapter.getItem(position);
                Intent intent = new Intent(getContext(), ObservationsiteActivity.class);
                intent.putExtra("observationId", item.getItemId());
                startActivity(intent);
            }
        });
        if (list.isEmpty()) {
            noResultLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        return v;
    }

    public void setData(List<SearchParams1> list) {
        this.list  = list;
        if (noResultLayout != null) {
            if (list.isEmpty()) {
                noResultLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                noResultLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
        }
    }
}