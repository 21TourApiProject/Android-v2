package com.starrynight.tourapiproject.starPage.starPageRetrofit;

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
 * className :  StarHashTagAdapter
 * description : 별자리 해시태그 adpater
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2022-12-28
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2022-12-28      jinhyeok      최초생성
 */
public class StarHashTagAdapter  extends RecyclerView.Adapter<StarHashTagAdapter.ViewHolder>{
        ArrayList<StarHashTag> items = new ArrayList<>();
        OnStarHashTagClickListener listener;

        @NotNull
        @Override
        public StarHashTagAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.star__feature_view, parent, false);

            return new StarHashTagAdapter.ViewHolder(view, listener);
        }

        @Override
        public void onBindViewHolder(@NonNull StarHashTagAdapter.ViewHolder viewHolder, int position) {
            StarHashTag item = items.get(position);
            viewHolder.setItem(item);
        }


        @Override
        public int getItemCount() {
            return items.size();
        }

        public void addItem(StarHashTag item) {
            items.add(item);
        }

        public void setItems(ArrayList<StarHashTag> items) {
            this.items = items;
        }

        public StarHashTag getItem(int position) {
            return items.get(position);
        }

        public void setOnItemClickListener(OnStarHashTagClickListener listener) {
            this.listener = listener;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView starhashtagText;

            public ViewHolder(View itemView, final OnStarHashTagClickListener listener) {
                super(itemView);

                starhashtagText = itemView.findViewById(R.id.starhashtagtext);

                itemView.setClickable(true);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getLayoutPosition();

                        if (listener != null) {
                            listener.onItemClick(StarHashTagAdapter.ViewHolder.this, v, position);
                        }
                    }
                });
            }

            public void setItem(StarHashTag item) {
                starhashtagText.setText(item.getHashTagName());
            }
        }
    }
