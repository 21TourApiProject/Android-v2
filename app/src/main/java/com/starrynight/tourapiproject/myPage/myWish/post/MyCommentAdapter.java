package com.starrynight.tourapiproject.myPage.myWish.post;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
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
import com.starrynight.tourapiproject.touristPointPage.HashTagAdapter2;

import java.util.ArrayList;
import java.util.List;

/**
 * className :  MyCommentAdapter
 * description : TODO 예시 클래스 입니다.
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2023-07-16
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2023-07-16      jinhyeok      최초생성
 */
public class MyCommentAdapter extends RecyclerView.Adapter<MyCommentAdapter.ViewHolder> {
    private static List<MyComment> items;
    private OnMyCommentItemClickListener listener;
    private Context context;

    public MyCommentAdapter(List<MyComment> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public MyCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.custom_my_wish_comment_item, viewGroup, false);

        return new MyCommentAdapter.ViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCommentAdapter.ViewHolder viewHolder, int position) {
        MyComment item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void addItem(MyComment item) {
        items.add(item);
    }

    public void setItems(ArrayList<MyComment> items) {
        this.items = items;
    }

    public MyComment getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, MyComment item) {
        items.set(position, item);
    }

    public void setOnMyCommentItemClickListener(OnMyCommentItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView myCommentImage;
        TextView myCommentPostTitle;
        TextView myComment;
        TextView myCommentYearDate;

        public ViewHolder(View itemView, final OnMyCommentItemClickListener listener) {
            super(itemView);

            myCommentImage = itemView.findViewById(R.id.commentImage);
            myCommentPostTitle = itemView.findViewById(R.id.comment_post_text);
            myComment = itemView.findViewById(R.id.commentText);
            myCommentYearDate = itemView.findViewById(R.id.comment_date);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClick(MyCommentAdapter.ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(MyComment item) {
            //게시물 썸네일 가져오기
            if (item.getThumbnail() != null) {
                Glide.with(context).load("https://starry-night.s3.ap-northeast-2.amazonaws.com/postImage/" + item.getThumbnail()).into(myCommentImage);
                myCommentImage.setClipToOutline(true);
            }
            myCommentPostTitle.setText(item.getPostTitle());
            myComment.setText(item.getComment());
            myCommentYearDate.setText(item.getYearDate());
        }
    }
}
