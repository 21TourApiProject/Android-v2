package com.starrynight.tourapiproject.weatherPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;

import java.util.ArrayList;

public class SearchLocationItemAdapter extends RecyclerView.Adapter<SearchLocationItemAdapter.ViewHolder> {
    ArrayList<SearchLocationItem> searchItemArrayList;
    OnSearchItemClickListener listener;


    public SearchLocationItemAdapter(ArrayList<SearchLocationItem> searchItemArrayList, WeatherLocationSearchActivity weatherLocationSearchActivity) {
        this.searchItemArrayList = searchItemArrayList;
    }

    public SearchLocationItem getItem(int position) {
        return searchItemArrayList.get(position);
    }

    @NonNull
    @Override
    public SearchLocationItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.weather__location_item, parent, false);
        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(searchItemArrayList.get(position).getTitle());
        holder.subtitle.setText(searchItemArrayList.get(position).getSubtitle());
    }

    public void setOnItemClicklistener(OnSearchItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return searchItemArrayList.size();
    }

    public void filterList(ArrayList<SearchLocationItem> filteredList) {
        searchItemArrayList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView subtitle;
        TextView observationValue;

        public ViewHolder(@NonNull View itemView, final OnSearchItemClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            itemView.setClickable(true);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null) {
                    listener.onItemClick(SearchLocationItemAdapter.ViewHolder.this, v, position);
                }
            });

        }
    }
}
