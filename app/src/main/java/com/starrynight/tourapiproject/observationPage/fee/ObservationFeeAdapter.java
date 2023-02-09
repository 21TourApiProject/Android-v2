package com.starrynight.tourapiproject.observationPage.fee;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
* @className : GridFeeAdapter.java
* @description : 관측지 요금 리사이클러 어댑터
* @modification : gyul chyoung (2022-08-30) 주석추가
* @author : 2022-08-30
* @date : gyul chyoung
* @version : 1.0
     ====개정이력(Modification Information)====
  수정일        수정자        수정내용    -----------------------------------------
   gyul chyoung       2022-08-30       주석추가
    gyul chyoung        2022-01-05      그리드 어댑터로 수정
    gyul chyoung        2022-01-26      다시 리사이클러 어댑터로 수정
 */

public class ObservationFeeAdapter extends RecyclerView.Adapter<ObservationFeeAdapter.ViewHolder> {

    private ArrayList<ObservationFeeItem> listFee = new ArrayList<>();  //실제로 표시되는 정보
    private ArrayList<ObservationFeeItem> listFee2 = new ArrayList<>(); //3개 이상 더보기 분리용

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fee_name;
        TextView entrance_fee;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            fee_name = (TextView) itemView.findViewById(R.id.feename_txt);
            entrance_fee = (TextView) itemView.findViewById(R.id.entrancefee_txt);
        }

        void onBind(ObservationFeeItem item) {
            fee_name.setText(Html.fromHtml(item.getFeeName(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            entrance_fee.setText(item.getEntranceFee());
            if (item.getEntranceFee() == null) {
                entrance_fee.setVisibility(View.GONE);
            }
        }
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.observe_fee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(listFee.get(position));
    }

    @Override
    public int getItemCount() {
        return listFee.size();
    }

    public void addItem(ObservationFeeItem item) {
        if (listFee.size() < 3) {
            listFee.add(item);
        } else {
            listFee2.add(item);
        }

    }

    //이용요금 더보기 클릭 시
    public void showMoreFee(){
        for (ObservationFeeItem item : listFee2) {
            listFee.add(item);
        }
    }

    //이용요금 접기 클릭 시
    public void closeMoreFee(){
        if (listFee.size() > 3) {
            for (int i = 3; i < listFee.size(); i++) {
                listFee.remove(i);
            }
        }

    }
}

