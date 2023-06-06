package com.starrynight.tourapiproject.starPage.starItemPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.starrynight.tourapiproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * className :  StarViewAdpater2
 * description :별자리 페이지의 Adapter입니다.
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2022-12-04
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2022-12-04      jinhyeok      최초생성
 */
public class StarViewAdpater2 extends RecyclerView.Adapter<StarViewAdpater2.ViewHolder> {
    ArrayList<StarItem> items = new ArrayList<>();
    OnStarItemClickListener2 listener;

    @NotNull
    @Override
    public StarViewAdpater2.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.star__custom_grid_layout_non, parent, false);

        return new StarViewAdpater2.ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull StarViewAdpater2.ViewHolder viewHolder, int position) {
        StarItem item = items.get(position);
        viewHolder.setItem(item);
        Glide.with(viewHolder.itemView.getContext())
                .load("https://starry-night.s3.ap-northeast-2.amazonaws.com/constDetailImage/s_" + item.getConstEng() + ".png")
                .into(viewHolder.constImage);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(StarItem item) {
        items.add(item);
    }

    public void setItems(ArrayList<StarItem> items) {
        this.items = items;
    }

    public StarItem getItem(int position) {
        return items.get(position);
    }

    public void setOnItemClickListener(OnStarItemClickListener2 listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView constName;
        ImageView constImage;

        public ViewHolder(View itemView, final OnStarItemClickListener2 listener) {
            super(itemView);

            constImage = itemView.findViewById(R.id.const_image);
            constName = itemView.findViewById(R.id.const_name);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();

                    if (listener != null) {
                        listener.onItemClick(StarViewAdpater2.ViewHolder.this, v, position);
                    }
                }
            });
        }

        public void setItem(StarItem item) {
            constName.setText(item.getConstName());
        }
    }
}
