package com.starrynight.tourapiproject.searchPage.filter;

import static com.starrynight.tourapiproject.searchPage.filter.HashTagItem.VIEWTYPE_ACTIVE;
import static com.starrynight.tourapiproject.searchPage.filter.HashTagItem.VIEWTYPE_INACTIVE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;

import java.util.ArrayList;
import java.util.List;

public class FilterRecyclerAdapter extends RecyclerView.Adapter<FilterRecyclerAdapter.MyViewHolder>{

    static final String TAG = "FilterRecyclerAdapter";

    Context context;
    List<HashTagItem> list;
    List<HashTagItem> themeList = new ArrayList<>();
    List<HashTagItem> peopleList = new ArrayList<>();
    List<HashTagItem> areaList = new ArrayList<>();
    List<HashTagItem> facilityList = new ArrayList<>();
    List<HashTagItem> feeList = new ArrayList<>();

    FilterOnClickItem mCallback;

    public FilterRecyclerAdapter(Context context, List<HashTagItem> areaList, List<HashTagItem> peopleList , List<HashTagItem> themeList, List<HashTagItem> facilityList, List<HashTagItem> feeList, FilterOnClickItem listner) {
        super();
        this.context = context;

        this.list = areaList;
        this.areaList = areaList;
        this.themeList = themeList;
        this.peopleList = peopleList;
        this.facilityList = facilityList;
        this.feeList = feeList;

        this.mCallback = listner;
    }

    @NonNull
    @Override
    public FilterRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEWTYPE_INACTIVE) {
            View view_inactive = LayoutInflater.from(context).inflate(R.layout.filter__hashtags, parent, false);
            return new MyViewHolder(view_inactive);
        } else {
            View view_active = LayoutInflater.from(context).inflate(R.layout.filter__hashtags_active, parent, false);
            return new MyViewHolder(view_active);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (list.get(position).isActive==VIEWTYPE_INACTIVE) {
            holder.name_inactive.setText(list.get(position).name);
        } else {
            holder.name_active.setText(list.get(position).name);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).isActive;
    }

    public void switchTab(FilterType filterType){
        switch (filterType) {
            case AREA:
                list = areaList;
                break;
            case THEME:
                list = themeList;
                break;
            case PEOPLE:
                list = peopleList;
                break;
            case FACILITY:
                list = facilityList;
                break;
            case FEE:
                list = feeList;
                break;
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name_active;
        TextView name_inactive;

        public MyViewHolder(View itemView) {
            super(itemView);
            name_active = itemView.findViewById(R.id.filter_active_hashTagName);
            name_inactive = itemView.findViewById(R.id.filter_inactive_hashTagName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (list.get(position).isActive == VIEWTYPE_ACTIVE) {
                            list.get(position).setIsActive(VIEWTYPE_INACTIVE);
                        } else {
                            list.get(position).setIsActive(VIEWTYPE_ACTIVE);
                        }

                    }
                    mCallback.onClick();
                    notifyDataSetChanged();
                }
            });

        }
    }

}
