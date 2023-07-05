package com.starrynight.tourapiproject.searchPage;

import android.view.View;


public interface OnSearchResultItemClickListener {
    public void onItemClick(SearchItemRecyclerAdapter.MyViewHolder holder, View view, int position);
}
