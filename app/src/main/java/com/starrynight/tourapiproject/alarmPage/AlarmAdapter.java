package com.starrynight.tourapiproject.alarmPage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.myPage.AlarmSettingActivity;
import com.starrynight.tourapiproject.myPage.NoticeDetailActivity;
import com.starrynight.tourapiproject.myPage.SettingActivity;
import com.starrynight.tourapiproject.myPage.notice.Notice;
import com.starrynight.tourapiproject.postPage.PostActivity;
import com.starrynight.tourapiproject.searchPage.SearchResultActivity;

import java.util.ArrayList;
import java.util.List;

/**
* @className : AlarmAdapter
* @description : 알림 클래스 adapter 입니다.
* @modification : 2022-09-02 (jinhyeok) 주석 수정
* @author : jinhyeok
* @date : 2022-09-02
* @version : 1.0
   ====개정이력(Modification Information)====
  수정일        수정자        수정내용
   -----------------------------------------
   2022-09-02      jinhyeok       주석 수정

 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {
    List<Alarm> items = new ArrayList<Alarm>();
    Context context;
    OnAlarmClickListener listener;

    public AlarmAdapter(Context context, List<Alarm> items) {
        this.context = context;
        this.items = items;
    }

    public void addItem(Alarm item) {
        items.add(item);
    }

    public void setItems(ArrayList<Alarm> items) {
        this.items = items;
    }

    public Alarm getItem(int position) {
        return items.get(position);
    }


    public void setItem(int position, Alarm item) {
        items.set(position, item);
    }

    @NonNull
    @Override
    public AlarmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.custom_alarm_item, parent, false);
        return new AlarmAdapter.ViewHolder(itemView, listener);
    }


    @Override
    public void onBindViewHolder(@NonNull AlarmAdapter.ViewHolder viewHolder, int position) {
        Alarm item = items.get(position);
        viewHolder.setItem(item);
        viewHolder.alarmLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getIsNotice().equals("notice")){
                    Intent intent = new Intent(viewHolder.itemView.getContext(), NoticeDetailActivity.class);
                    intent.putExtra("noticeId",item.getItemId());
                    viewHolder.itemView.getContext().startActivity(intent);
                }
                if (item.getIsNotice().equals("comment")){
                    Intent intent = new Intent(viewHolder.itemView.getContext(), PostActivity.class);
                    intent.putExtra("postId",item.getItemId());
                    viewHolder.itemView.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClicklistener(OnAlarmClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout alarmLayout;
        TextView alarmtitle;
        TextView alarmdate;
        Button alarmBtn;
        ImageView star;

        public ViewHolder(View itemView, final OnAlarmClickListener listener) {
            super(itemView);
            alarmLayout = itemView.findViewById(R.id.alarmLayout);
            alarmtitle = itemView.findViewById(R.id.alarm_title);
            alarmdate = itemView.findViewById(R.id.alarm_date);
            alarmBtn = itemView.findViewById(R.id.scroll_btn);
            star = itemView.findViewById(R.id.alarm_star);

        }

        public void setItem(Alarm item) {
            alarmtitle.setText(item.getAlarmTitle());
            alarmdate.setText(item.getAlarmDate());
            if(item.getIsNotice().equals("notice")){
                star.setImageResource(R.drawable.alarm__star_non);
            }else{
                star.setImageResource(R.drawable.main__alarm);
            }
        }
    }

}
