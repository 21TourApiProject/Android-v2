package com.starrynight.tourapiproject.mainPage;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.mainPage.mainPageRetrofit.ObservationSimpleParams;

import java.util.ArrayList;
import java.util.List;

/**
* @className : Post_point_item_Adapter
* @description : 검색 페이지, 게시물 페이지(관련 게시물)에 띄울 게시물 아이템 Adapter 입니다.
* @modification : jinhyeok (2022-08-14) 주석 수정
* @author : 2022-08-14
* @date : jinhyeok
* @version : 1.0
   ====개정이력(Modification Information)====
  수정일        수정자        수정내용
   -----------------------------------------
   jinhyeok      2022-08-14       주석 수정

 */
public class BestFitObservationAdapter extends RecyclerView.Adapter<BestFitObservationAdapter.ViewHolder> {
    List<ObservationSimpleParams> items = new ArrayList<ObservationSimpleParams>();
    private Intent intent;
    OnBestFitObsItemClickListener listener;

    public void addItem(ObservationSimpleParams item) {
        items.add(item);
    }

    public void setItems(List<ObservationSimpleParams> items) {
        this.items = items;
    }

    public ObservationSimpleParams getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, ObservationSimpleParams item) {
        items.set(position, item);
    }

    @NonNull
    @Override
    public BestFitObservationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_best_fit_obs, parent, false);
        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull BestFitObservationAdapter.ViewHolder viewHolder, int position) {
        ObservationSimpleParams item = items.get(position);
        viewHolder.setItem(item);
        viewHolder.imageView.setClipToOutline(true);
        if (item.getThumbnail() != null) {
            Glide.with(viewHolder.itemView.getContext())
                    .load(item.getThumbnail())
                    .into(viewHolder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClicklistener(OnBestFitObsItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView addressTextView;
        TextView fitTextView;
        ImageView imageView;

        public ViewHolder(View itemView, final OnBestFitObsItemClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.fit_obs_name);
            addressTextView = itemView.findViewById(R.id.fit_obs_address);
            fitTextView = itemView.findViewById(R.id.fit_obs_fit);
            imageView = itemView.findViewById(R.id.fit_obs_image);
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(BestFitObservationAdapter.ViewHolder.this, view, position);
                    }
                }
            });

        }

        public void setItem(ObservationSimpleParams item) {
            nameTextView.setText(item.getTitle());
            addressTextView.setText(item.getAddress());
            fitTextView.setText(item.getObserveFit().toString());
        }
    }
}

