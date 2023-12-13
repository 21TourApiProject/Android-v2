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
import com.starrynight.tourapiproject.mainPage.mainPageRetrofit.PostContentsParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* @className : RecentReviewAdapter
* @description : 메인페이지의 관측후기 리사이클러뷰 어댑터
* @modification : gyulchyoung 생성
* @author : 2022-08-14
* @date : jinhyeok
* @version : 1.0
   ====개정이력(Modification Information)====
  수정일        수정자        수정내용
   -----------------------------------------
   gyulchyoung      2023-12-13       생성

 */
public class RecentReviewAdapter extends RecyclerView.Adapter<RecentReviewAdapter.ViewHolder> {
    List<PostContentsParams> items = new ArrayList<PostContentsParams>();
    private Intent intent;
    RecentReviewItemClickListener listener;

    public void addItem(PostContentsParams item) {
        items.add(item);
    }

    public void setItems(List<PostContentsParams> items) {
        this.items = items;
    }

    public PostContentsParams getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, PostContentsParams item) {
        items.set(position, item);
    }

    @NonNull
    @Override
    public RecentReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_main_review, parent, false);
        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentReviewAdapter.ViewHolder viewHolder, int position) {
        PostContentsParams item = items.get(position);
        viewHolder.setItem(item);
        viewHolder.imageView.setClipToOutline(true);

        if (item.getThumbnail() != null) {
//            Glide.with(viewHolder.itemView.getContext())
//                    .load(item.getThumbnail())
//                    .into(viewHolder.imageView);
            String imageName = item.getThumbnail();
            Glide.with(viewHolder.itemView.getContext()).load("https://starry-night.s3.ap-northeast-2.amazonaws.com/postImage/" + imageName).into(viewHolder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClicklistener(RecentReviewItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationTextVIew;
        TextView titleTextView;
        TextView contentsTextView;
        ImageView imageView;

        public ViewHolder(View itemView, final RecentReviewItemClickListener listener) {
            super(itemView);
            locationTextVIew = itemView.findViewById(R.id.main__review_location);
            titleTextView = itemView.findViewById(R.id.main__review_title);
            contentsTextView = itemView.findViewById(R.id.main__review_contents);
            imageView = itemView.findViewById(R.id.main__review_image);
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(RecentReviewAdapter.ViewHolder.this, view, position);
                    }
                }
            });

        }

        public void setItem(PostContentsParams item) {
            if (item.getObservationCustomName() != null && !item.getObservationCustomName().isEmpty()) {
                locationTextVIew.setText(item.getObservationCustomName());
            } else {
                locationTextVIew.setText(item.getObservationName());
            }
            titleTextView.setText(item.getTitle());
            contentsTextView.setText(item.getContents());
        }
    }
}

