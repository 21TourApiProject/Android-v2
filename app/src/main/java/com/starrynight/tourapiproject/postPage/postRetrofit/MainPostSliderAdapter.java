package com.starrynight.tourapiproject.postPage.postRetrofit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.postPage.PostActivity;

import java.util.ArrayList;

/**
 * className :  MainPostSliderAdapter
 * description : TODO 예시 클래스 입니다.
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2023-07-24
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2023-07-24      jinhyeok      최초생성
 */
public class MainPostSliderAdapter extends RecyclerView.Adapter<MainPostSliderAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<String> sliderImage;
    private Long postId;
    MainPostImageSliderItemClickListener listener;

    public MainPostSliderAdapter(Context context, ArrayList<String> sliderImage,Long postId) {
        this.context = context;
        this.sliderImage = sliderImage;
        this.postId=postId;
    }

    @NonNull
    @Override
    public MainPostSliderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_main_image, parent, false);
        return new MainPostSliderAdapter.MyViewHolder(view, listener);

    }

    @Override
    public void onBindViewHolder(@NonNull MainPostSliderAdapter.MyViewHolder holder, int position) {
        holder.bindSliderImage(sliderImage.get(position));
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PostActivity.class);
                intent.putExtra("postId", postId);
                ((Activity) context).startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sliderImage.size();
    }

    public void OnItemClicklistener(MainPostImageSliderItemClickListener listener) {
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;

        public MyViewHolder(@NonNull View itemView, final MainPostImageSliderItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.custom_main_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onItemClick(MainPostSliderAdapter.MyViewHolder.this, v, position);
                    }
                }
            });
        }

        public void bindSliderImage(String imageURL) {
            imageURL=imageURL.replace("+","%2B");
            Glide.with(context)
                    .load(imageURL)
                    .into(mImageView);

        }
    }
}
