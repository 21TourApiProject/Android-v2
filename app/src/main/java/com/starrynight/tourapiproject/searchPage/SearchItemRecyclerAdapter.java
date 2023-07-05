package com.starrynight.tourapiproject.searchPage;

import static com.starrynight.tourapiproject.searchPage.filter.HashTagItem.VIEWTYPE_ACTIVE;
import static com.starrynight.tourapiproject.searchPage.filter.HashTagItem.VIEWTYPE_INACTIVE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.searchPage.filter.FilterType;
import com.starrynight.tourapiproject.searchPage.filter.HashTagItem;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.SearchParams1;
import com.starrynight.tourapiproject.touristPointPage.HashTagAdapter2;

import java.util.ArrayList;
import java.util.List;

public class SearchItemRecyclerAdapter extends RecyclerView.Adapter<SearchItemRecyclerAdapter.MyViewHolder>{

    static final String TAG = "FilterRecyclerAdapter";

    OnSearchResultItemClickListener listener;
    Context context;
    List<SearchParams1> list = new ArrayList<>();

    public SearchItemRecyclerAdapter(Context context,List<SearchParams1> list) {
        super();
        this.context = context;

        this.list = list;
    }

    public void setOnSearchResultItemClickListener(OnSearchResultItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchItemRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.search__recycler_item, parent, false);
        return new MyViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SearchParams1 item = list.get(position);
        if (item.getThumbnail() != null)
            Glide.with(context).load(item.getThumbnail()).into(holder.imageView);
        holder.name.setText(item.getTitle());
        holder.savedNum.setText(item.getSaved().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public SearchParams1 getItem(int position) {
        return list.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        ImageView savedImg;
        TextView name;
        TextView savedNum;

        public MyViewHolder(View itemView , final OnSearchResultItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sr_item_image);
            name = itemView.findViewById(R.id.sr_item_name);
            savedNum = itemView.findViewById(R.id.sr_item_save_text);
            savedImg = itemView.findViewById(R.id.sr_item_save_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (listener != null) {
                            listener.onItemClick(SearchItemRecyclerAdapter.MyViewHolder.this, view, position);
                        }
                    }
                    notifyDataSetChanged();
                }
            });

        }
    }

}
