package com.starrynight.tourapiproject.searchPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.observationPage.ObservationsiteActivity;
import com.starrynight.tourapiproject.postPage.PostActivity;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.SearchParams1;

import java.util.ArrayList;
import java.util.List;

public class SearchResultListFragment extends Fragment {

    RecyclerView recyclerView;
    SearchItemRecyclerAdapter adapter;
    GridLayoutManager layoutManager;
    LinearLayout noResultLayout;
    TextView noResultText;

    String keyword;


    List<SearchParams1> list = new ArrayList<>();

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
        noResultText = v.findViewById(R.id.srl_no_result_text);

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
                if (item.getContentType()==null) {
                    Intent intent = new Intent(getContext(), PostActivity.class);
                    intent.putExtra("postId", item.getItemId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), ObservationsiteActivity.class);
                    intent.putExtra("observationId", item.getItemId());
                    startActivity(intent);
                }

            }
        });
        if (list.isEmpty()) {
            noResultLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noResultLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        return v;
    }

    public void setData(List<SearchParams1> list, String keyword) {
        this.list  = list;
        this.keyword = keyword;
        if (noResultLayout != null) {
            if (list.isEmpty()) {
                noResultText.setText("'"+keyword+"'에 대한\n 검색 결과가 없어요");
                noResultLayout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                if (list.size() > 0) {
                }
                noResultLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
        }
    }
}