package com.starrynight.tourapiproject.observationPage.course;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.myPage.myWish.MyWish;
import com.starrynight.tourapiproject.observationPage.OutlinePopActivity;
import com.starrynight.tourapiproject.observationPage.RecyclerHashTagItem;
import com.starrynight.tourapiproject.observationPage.observationPageRetrofit.CourseTouristPoint;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
* @className : RecyclerCourseAdapter.java
* @description : 관측지 코스 recycler adapter
* @modification : gyul chyoung (2022-08-30) 주석추가
* @author : 2022-08-30
* @date : gyul chyoung
* @version : 1.0
     ====개정이력(Modification Information)====
  수정일        수정자        수정내용    -----------------------------------------
    gyul chyoung       2023-01-12       최초생성
 */

public class ObservationCourseAdapter extends RecyclerView.Adapter<ObservationCourseAdapter.ViewHolder> {

    private static final String TAG = "course adapter";
    private List<ObservationCourseItem> listTouristPoint = new ArrayList<>();
    private Context context;

    public ObservationCourseAdapter(Context context) {
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView tp_img;
        private TextView name_txt;
        private TextView tp_type_txt;
        private TextView overview_txt;
        private TextView address_txt;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tp_img = itemView.findViewById(R.id.course_tp_img);
            name_txt = itemView.findViewById(R.id.course_tpname_txt);
            tp_type_txt = itemView.findViewById(R.id.course_tptype_txt);
            overview_txt = itemView.findViewById(R.id.course_outline_txt);
            address_txt = itemView.findViewById(R.id.course_tpaddress_txt);
        }

        void onBind(ObservationCourseItem item) {
            name_txt.setText(item.getName());
            tp_type_txt.setText(item.getCategory());
            overview_txt.setText(item.getOverview());
            address_txt.setText(item.getAddress());
            bindImage(item.getImage());
        }

        private void setOutlineButton(int position) {
            TextView outline_btn = itemView.findViewById(R.id.course_outline_btn);

            ViewTreeObserver vto = overview_txt.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Layout l = overview_txt.getLayout();
                    if (l != null) {
                        int lines = l.getLineCount();
                        if (lines > 0)
                            if (l.getEllipsisCount(lines - 1) > 0) {
                                outline_btn.setVisibility(View.VISIBLE);
                                Log.d(TAG, "텍스트 줄넘침");
                            }
                    }
                }
            });

            //개요 더보기 버튼 클릭시 팝업띄움
            outline_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (overview_txt.getEllipsize() != null) {
                        outline_btn.setText("접기");
                        overview_txt.setEllipsize(null);
                        overview_txt.setMaxLines(Integer.MAX_VALUE);
                    }else{
                        outline_btn.setText("더보기");
                        overview_txt.setEllipsize(TextUtils.TruncateAt.END);
                        overview_txt.setMaxLines(3);
                    }
                }
            });
        }

        public void bindImage(String imageURL) {
            Glide.with(context)
                    .load(imageURL)
                    .into(tp_img);

        }

    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.observation__course_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.setOutlineButton(position);
        holder.onBind(listTouristPoint.get(position));
    }

    @Override
    public int getItemCount() {
        return listTouristPoint.size();
    }

    public void addItem(ObservationCourseItem item) {
        listTouristPoint.add(item);
    }

}
