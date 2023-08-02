package com.starrynight.tourapiproject.postItemPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.observationPage.ObservationsiteActivity;
import com.starrynight.tourapiproject.postPage.postRetrofit.PostHashTag;

import java.util.ArrayList;
/**
* @className : PostWriteHashTagItemAdapter
* @description : 해시태그 추가 페이지에 필요하 해시태그 아이템 Adapter 입니다.
* @modification : jinhyeok (2022-08-14) 주석 수정
* @author : 2022-08-14
* @date : jinhyeok
* @version : 1.0
   ====개정이력(Modification Information)====
  수정일        수정자        수정내용
   -----------------------------------------
   jinhyeok      2022-08-14       주석 수정

 */
public class MainPostHashTagItemAdapter extends RecyclerView.Adapter<MainPostHashTagItemAdapter.ViewHolder> {
    ArrayList<PostHashTagItem> items = new ArrayList<PostHashTagItem>();
    OnPostWriteHashTagItemAdapter listener;

    public void addItem(PostHashTagItem item){
        items.add(item);
    }
    public void setItems(ArrayList<PostHashTagItem>items){
        this.items = items;
    }
    public void removeItem(int position){ items.remove(position); }

    public PostHashTagItem getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, PostHashTagItem item){
        items.set(position,item);
    }

    @NonNull
    @Override
    public MainPostHashTagItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =  LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.hashtags_full2, parent, false);
        return new MainPostHashTagItemAdapter.ViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainPostHashTagItemAdapter.ViewHolder viewHolder, int position){
        if (position != 0) {
            viewHolder.observationpin.setVisibility(View.GONE);
            viewHolder.postHashTagName.setPadding(4,0,15,0);
            PostHashTagItem item = items.get(position);
            viewHolder.setItem(item);
        } else {
            PostHashTagItem item0 = items.get(0);
            viewHolder.setItem(item0);
            viewHolder.postHashTagName.setText(item0.getHashTagname());
            viewHolder.postHashTagName.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(),R.color.point_blue));
            viewHolder.observationpin.setVisibility(View.VISIBLE);
            if(viewHolder.observationId==null){
                viewHolder.observationBtn.setVisibility(View.GONE);
                viewHolder.postHashTagName.setPadding(0,0,15,0);
            }else{
                viewHolder.observationBtn.setVisibility(View.VISIBLE);
            }

            viewHolder.postHashTagName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item0.getObservationId() != null) {
                        Intent intent1 = new Intent(viewHolder.itemView.getContext(), ObservationsiteActivity.class);
                        intent1.putExtra("observationId", item0.ObservationId);
                        viewHolder.itemView.getContext().startActivity(intent1);
                    }
                }
            });
        }

    }

    public void  setOnItemClicklistener(OnPostWriteHashTagItemAdapter listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView postHashTagName;
        Long observationId;
        ImageView observationpin,observationBtn;

        public ViewHolder(View itemView, final OnPostWriteHashTagItemAdapter listener){
            super(itemView);
            postHashTagName =itemView.findViewById(R.id.hashtags_name);
            observationpin = itemView.findViewById(R.id.mainpost_pin);
            observationBtn =itemView.findViewById(R.id.mainPostObservationBtn);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null){
                        listener.onItemClick(MainPostHashTagItemAdapter.ViewHolder.this, v, position);
                    }
                }
            });
        }

        @SuppressLint("SetTextI18n")
        public void setItem(PostHashTagItem item){
            postHashTagName.setText("#"+item.getHashTagname());
            observationId = item.getObservationId();
        }
    }
}
