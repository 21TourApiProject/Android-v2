package com.starrynight.tourapiproject.starPage.starPageRetrofit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * className :  StarFeatureAdapter
 * description : TODO 예시 클래스 입니다.
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2023-07-20
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2023-07-20      jinhyeok      최초생성
 */
public class StarFeatureAdapter extends RecyclerView.Adapter<StarFeatureAdapter.ViewHolder> {
    ArrayList<StarFeature> items = new ArrayList<>();
    OnStarFeatureClickListener listener;

    @NotNull
    @Override
    public StarFeatureAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.star__feature_view, parent, false);

        return new StarFeatureAdapter.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StarFeatureAdapter.ViewHolder viewHolder, int position) {
        StarFeature item = items.get(position);
        viewHolder.setItem(item);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(StarFeature item) {
        items.add(item);
    }

    public void setItems(ArrayList<StarFeature> items) {
        this.items = items;
    }

    public StarFeature getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnStarFeatureClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView starFeatureText;

        public ViewHolder(View itemView, final OnStarFeatureClickListener listener) {
            super(itemView);

            starFeatureText = itemView.findViewById(R.id.starhashtagtext);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();

                    if (listener != null) {
                        listener.onItemClick(StarFeatureAdapter.ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(StarFeature item) {
            starFeatureText.setText(item.getStarFeatureName());
        }
    }
}
