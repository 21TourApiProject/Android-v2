package com.starrynight.tourapiproject.weatherPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;

import java.util.ArrayList;

public class SearchLocationItemAdapter extends RecyclerView.Adapter<SearchLocationItemAdapter.ViewHolder> {
    ArrayList<SearchLocationItem> items;
    OnSearchItemClickListener listener;

    public SearchLocationItemAdapter(ArrayList<SearchLocationItem> items) {
        this.items = items;
    }

    public SearchLocationItem getItem(int position) {
        return items.get(position);
    }

    // ViewHolder 객체를 생성하고 초기화
    @NonNull
    @Override
    public SearchLocationItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.weather__location_item, parent, false);
        return new ViewHolder(itemView, listener);
    }

    // 데이터를 가져와 ViewHolder 안의 내용을 채워줌
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        SearchLocationItem item = items.get(position);
        viewHolder.setItem(item);
    }

    public void setOnItemClickListener(OnSearchItemClickListener listener) {
        this.listener = listener;
    }


    // 총 데이터의 갯수를 반환
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void filterList(ArrayList<SearchLocationItem> filteredList) {
        items = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView subtitle;
        TextView observationalFit;
        ImageView observationalStar;

        public ViewHolder(@NonNull View itemView, final OnSearchItemClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            observationalFit = itemView.findViewById(R.id.observationalFit);
            observationalStar = itemView.findViewById(R.id.observationalStar);
            itemView.setClickable(true);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null) {
                    listener.onItemClick(SearchLocationItemAdapter.ViewHolder.this, v, position);
                }
            });

        }

        public void setItem(SearchLocationItem item) {
            title.setText(item.getTitle());
            subtitle.setText(item.getSubtitle());
            if (item.getObservationalFit() != null) { // 관측지
                observationalStar.setAlpha(1.0f);
                observationalFit.setText("~" + item.getObservationalFit() + "%");
            } else { // 지역
                observationalStar.setAlpha(0.0f);
                observationalFit.setText("");
            }
        }
    }
}
