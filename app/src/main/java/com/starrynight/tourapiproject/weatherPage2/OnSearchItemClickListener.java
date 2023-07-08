package com.starrynight.tourapiproject.weatherPage2;

import android.view.View;

import com.starrynight.tourapiproject.postItemPage.Search_item_adapter;

public interface OnSearchItemClickListener {
    public void onItemClick(SearchLocationItemAdapter.ViewHolder holder, View view, int position);
}
