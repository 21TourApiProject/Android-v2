package com.starrynight.tourapiproject.starPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.SearchLoadingDialog;
import com.starrynight.tourapiproject.starPage.starItemPage.OnStarItemClickListener;
import com.starrynight.tourapiproject.starPage.starItemPage.OnStarItemClickListener2;
import com.starrynight.tourapiproject.starPage.starItemPage.StarItem;
import com.starrynight.tourapiproject.starPage.starItemPage.StarViewAdapter;
import com.starrynight.tourapiproject.starPage.starItemPage.StarViewAdpater2;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.weatherPage.GpsTracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @className : StarSelectActivity
 * @description : 별자리 카메라에 띄울 별자리 선택 페이지입니다.
 * @modification : 2022-09-15 (hyeonz) 주석추가
 * @author : jinHyeok
 * @date : 2023-12-11
 * @version : 1.0
====개정이력(Modification Information)====
수정일        수정자        수정내용
-----------------------------------------
2023-12-11   jinHyeok      주석추가
 */
public class SelectStarActivity extends AppCompatActivity {

    RecyclerView selectStarRecyclerView;
    StarViewAdapter selectConstAdapter;
    SearchLoadingDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_star);

        dialog = new SearchLoadingDialog(SelectStarActivity.this);

        // 뒤로가기 버튼 클릭 이벤트
        LinearLayout backBtn = findViewById(R.id.select_star_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //recyclerView 설정
        selectStarRecyclerView = findViewById(R.id.select_star_all_recylcerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        selectStarRecyclerView.setLayoutManager(gridLayoutManager);
        selectStarRecyclerView.addItemDecoration(new StarRecyclerViewWidth(3,30));
        selectConstAdapter = new StarViewAdapter();
        selectStarRecyclerView.setAdapter(selectConstAdapter);
        dialog.show();

        // 전체 별자리 가져오는 이벤트
        Call<List<StarItem>> starItemCall = RetrofitClient.getApiService().getTodayConst();
        starItemCall.enqueue(new Callback<List<StarItem>>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<List<StarItem>> call, Response<List<StarItem>> response) {
                if (response.isSuccessful()) {
                    List<StarItem> result = response.body();
                    for (StarItem si : result) {
                        selectConstAdapter.addItem(new StarItem(si.getConstId(), si.getConstName(), si.getConstEng()));
                    }
                    selectStarRecyclerView.setAdapter(selectConstAdapter);
                    dialog.dismiss();
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<StarItem>> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });
        // item 클릭 시 해당 아이템 constName 넘겨주기
        selectConstAdapter.setOnItemClickListener(new OnStarItemClickListener() {
            @Override
            public void onItemClick(StarViewAdapter.ViewHolder holder, View view, int position) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(SelectStarActivity.this,R.style.DimDialog); // 알림 뒤 화면 dim 처리
                AlertDialog pop = builder.create();
                LayoutInflater layoutInflater = LayoutInflater.from(SelectStarActivity.this);
                View selectView = layoutInflater.inflate(R.layout.custom_select_star_dialog, null);
                pop.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                Display display = getWindowManager().getDefaultDisplay();//dialog 크기 조정
                Point size = new Point();
                display.getSize(size);

                ImageView constImage = selectView.findViewById(R.id.star_alart_image); // dialog 버튼, 이미지 설정
                TextView constName = selectView.findViewById(R.id.star_alert_text);
                StarItem item = selectConstAdapter.getItem(position);
                constName.setText(item.getConstName()+"을(를)\n지금부터 카메라로 찾아볼까요?");
                Glide.with(getApplicationContext())
                        .load("https://starry-night.s3.ap-northeast-2.amazonaws.com/constDetailImage/s_" + item.getConstEng() + ".png")
                        .into(constImage);
                TextView positive = selectView.findViewById(R.id.star_alert_positive);
                TextView negative = selectView.findViewById(R.id.star_alert_negative);
                pop.setView(selectView);
                pop.show();
                WindowManager.LayoutParams params=pop.getWindow().getAttributes();
                params.gravity=Gravity.CENTER_VERTICAL;
                params.width=(int)(size.x*0.9f);
                pop.getWindow().setAttributes(params);

                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pop.dismiss();
                        //GPS 위도, 경도
                        GpsTracker gpsTracker = new GpsTracker(getApplicationContext());
                        double lat = gpsTracker.getLatitude();

                        double lon = gpsTracker.getLongitude();
                        StarItem item = selectConstAdapter.getItem(position);
                        Intent intent = new Intent(getApplicationContext(),StarCameraActivity.class);
                        intent.putExtra("constName", item.getConstName());
                        intent.putExtra("lat",lat);
                        intent.putExtra("lon",lon);
                        startActivity(intent);
                    }
                });

                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pop.dismiss();
                    }
                });

            }
        });

    }
}