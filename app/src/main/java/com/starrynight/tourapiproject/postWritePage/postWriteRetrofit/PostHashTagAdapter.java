package com.starrynight.tourapiproject.postWritePage.postWriteRetrofit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.alarmPage.AlarmAdapter;
import com.starrynight.tourapiproject.searchPage.filter.HashTagItem;

import java.util.ArrayList;
import java.util.List;

import static com.starrynight.tourapiproject.searchPage.filter.HashTagItem.VIEWTYPE_ACTIVE;
import static com.starrynight.tourapiproject.searchPage.filter.HashTagItem.VIEWTYPE_INACTIVE;

/**
 * className :  PostHashTagAdapter
 * description : TODO 예시 클래스 입니다.
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2023-07-12
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2023-07-12      jinhyeok      최초생성
 */
public class PostHashTagAdapter extends RecyclerView.Adapter<PostHashTagAdapter.ViewHolder> {
    ArrayList<HashTagItem> items = new ArrayList<>();

    public void addItem(HashTagItem item) {
        items.add(item);
    }

    public void setItems(ArrayList<HashTagItem> items) {
        this.items = items;
    }

    public void removeItem(int position) {
        items.remove(position);
    }

    public HashTagItem getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, HashTagItem item) {
        items.set(position, item);
    }

    @NonNull
    @Override
    public PostHashTagAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.custom_postwrite__hashtags_active, parent, false);
        return new PostHashTagAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHashTagAdapter.ViewHolder viewHolder, int position) {
        HashTagItem item = items.get(position);
        viewHolder.setItem(item);
        if(item.getIsActive()==VIEWTYPE_ACTIVE){
            viewHolder.active_postHashTagName.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(),R.color.gray_700));
            viewHolder.active_postHashTagName.setBackground(ContextCompat.getDrawable(viewHolder.itemView.getContext(),R.drawable.selectmyhashtag_hashtag));
        }
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getIsActive() == VIEWTYPE_INACTIVE) {
                    viewHolder.active_postHashTagName.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(),R.color.gray_700));
                    viewHolder.active_postHashTagName.setBackground(ContextCompat.getDrawable(viewHolder.itemView.getContext(),R.drawable.selectmyhashtag_hashtag));
                    item.setIsActive(VIEWTYPE_ACTIVE);
                } else {
                    viewHolder.active_postHashTagName.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(),R.color.gray_300));
                    viewHolder.active_postHashTagName.setBackground(ContextCompat.getDrawable(viewHolder.itemView.getContext(),R.drawable.selectmyhashtag_hashtag_non));
                    item.setIsActive(VIEWTYPE_INACTIVE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView active_postHashTagName;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            active_postHashTagName = itemView.findViewById(R.id.postwrite_hashtags_activeText);
            linearLayout = itemView.findViewById(R.id.hashtag_linearLayout);
        }
        public void setItem(HashTagItem item) {
            active_postHashTagName.setText(item.getName());
        }
    }

}
