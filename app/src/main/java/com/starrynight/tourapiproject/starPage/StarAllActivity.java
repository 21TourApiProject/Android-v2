package com.starrynight.tourapiproject.starPage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.observationPage.RecyclerDecoration;
import com.starrynight.tourapiproject.starPage.starItemPage.OnStarItemClickListener;
import com.starrynight.tourapiproject.starPage.starItemPage.OnStarItemClickListener2;
import com.starrynight.tourapiproject.starPage.starItemPage.StarItem;
import com.starrynight.tourapiproject.starPage.starItemPage.StarViewAdapter;
import com.starrynight.tourapiproject.starPage.starItemPage.StarViewAdpater2;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.OnStarFeatureClickListener;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.StarFeature;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.StarFeatureAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @className : StarAllActivity
 * @description : 모든 천체 보기 페이지입니다.
 * @modification : 2022-09-15 (hyeonz) 주석추가
 * @author : hyeonz
 * @date : 2022-09-15
 * @version : 1.0
====개정이력(Modification Information)====
수정일        수정자        수정내용
-----------------------------------------
2022-09-15   hyeonz      주석추가
 */
public class StarAllActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    StarViewAdpater2 constAdapter;
    StarViewAdapter constTodayAdapter;
    StarFeatureAdapter starFeatureAdapter;
    TextView monthText,starNumber;
    LinearLayout moreStarLinearLayout;
    LinearLayout starFilterItem;
    RecyclerView constTodayList,starHashTagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_all);

        //검색바 설정
        EditText searchView = findViewById(R.id.starSearch);
        searchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!searchView.getText().toString().equals("")) {
                        String star = ((EditText) (findViewById(R.id.starSearch))).getText().toString();
                        Intent intent = new Intent(getApplicationContext(),StarSearchActivity.class);
                        intent.putExtra("starName", star);
                        intent.putExtra("type",1);
                        startActivity(intent);
                    }
                } else {
                    return false;
                }
                return true;
            }
        });

        // 뒤로가기 버튼 클릭 이벤트
        LinearLayout backBtn = findViewById(R.id.all_star_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //n월 달 텍스트 가져오기
        monthText = findViewById(R.id.allMonthStarText);
        long now = System.currentTimeMillis();//현재시간 가져오기
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M");
        String month = simpleDateFormat.format(date);
        monthText.setText(month);

        //더보기 클릭
        moreStarLinearLayout = findViewById(R.id.moreStar);
        moreStarLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),StarSearchActivity.class);
                intent.putExtra("starHashTagName",month+"월");
                intent.putExtra("type",3);
                startActivity(intent);
            }
        });

        //recyclerView 설정
        recyclerView = findViewById(R.id.all_const_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new StarRecyclerViewWidth(3,30));
        constAdapter = new StarViewAdpater2();
        recyclerView.setAdapter(constAdapter);

        starNumber=findViewById(R.id.star_number_text);

        // 전체 별자리 가져오는 이벤트
        Call<List<StarItem>> starItemCall = RetrofitClient.getApiService().getConstellation();
        starItemCall.enqueue(new Callback<List<StarItem>>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<List<StarItem>> call, Response<List<StarItem>> response) {
                if (response.isSuccessful()) {
                    List<StarItem> result = response.body();
                    starNumber.setText(String.valueOf(result.size()));
                    for (StarItem si : result) {
                        constAdapter.addItem(new StarItem(si.getConstId(), si.getConstName(), si.getConstEng()));
                    }
                    recyclerView.setAdapter(constAdapter);
                } else {
                }
            }

            @Override
            public void onFailure(Call<List<StarItem>> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });


        // item 클릭 시 해당 아이템 constName 넘겨주기
        constAdapter.setOnItemClickListener(new OnStarItemClickListener2() {
            @Override
            public void onItemClick(StarViewAdpater2.ViewHolder holder, View view, int position) {
                StarItem item = constAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), StarActivity.class);
                intent.putExtra("constName", item.getConstName());
                Log.d("itemConstNameAll", item.getConstName());
                startActivity(intent);
            }
        });

        constTodayList = findViewById(R.id.today_cel_recyclerview);
        constTodayList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        constTodayList.addItemDecoration(new StarRecyclerViewWidth(20,0));
        constTodayAdapter = new StarViewAdapter();
        RecyclerDecoration starItemDecorator = new RecyclerDecoration(8, getApplicationContext());
        constTodayList.addItemDecoration(starItemDecorator);
        constTodayList.setAdapter(constTodayAdapter);

        // 오늘 볼 수 있는 별자리 리스트 api
        Call<List<StarItem>> todayConstCall = RetrofitClient.getApiService().getTodayConst();
        todayConstCall.enqueue(new Callback<List<StarItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<StarItem>> call, Response<List<StarItem>> response) {
                if (response.isSuccessful()) {
                    List<StarItem> result = response.body();
                    for (StarItem si : result) {
                        constTodayAdapter.addItem(new StarItem(si.getConstId(), si.getConstName(), si.getConstEng()));
                    }
                    constTodayList.setAdapter(constTodayAdapter);
                } else {
                    Log.d("todayConst", "오늘의 별자리 불러오기 실패");
                }
            }

            @Override
            public void onFailure(Call<List<StarItem>> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });

        // item 클릭 시 해당 아이템 constId 넘겨주기
        constTodayAdapter.setOnItemClickListener(new OnStarItemClickListener() {
            @Override
            public void onItemClick(StarViewAdapter.ViewHolder holder, View view, int position) {
                StarItem item = constTodayAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), StarActivity.class);
                intent.putExtra("constName", item.getConstName());
                Log.d("itemConstNameAll", item.getConstName());
                startActivity(intent);
            }
        });

        //검색 해시태그 목록
        starFilterItem = findViewById(R.id.selectStarFilterItem);
        starHashTagList =findViewById(R.id.starHashtagRecyclerView);
        starHashTagList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        starFeatureAdapter = new StarFeatureAdapter();

        Call<List<StarFeature>> starFeatureCall = RetrofitClient.getApiService().getAllStarFeature();
        starFeatureCall.enqueue(new Callback<List<StarFeature>>() {
            @Override
            public void onResponse(Call<List<StarFeature>> call, Response<List<StarFeature>> response) {
                if(response.isSuccessful()){
                    List<StarFeature> result = response.body();
                    for(StarFeature starFeature: result){
                        starFeatureAdapter.addItem(starFeature);
                    }
                    starHashTagList.setAdapter(starFeatureAdapter);
                }else{
                    Log.d("starHashtag", "별자리 해시태그 불러오기 실패");
                }
            }

            @Override
            public void onFailure(Call<List<StarFeature>> call, Throwable t) {
                Log.e("starHashtag", "별자리 해시태그 불러오기 인터넷 연결 실패");
            }
        });

        //해시태그 클릭 시 이벤트
        starFeatureAdapter.setOnItemClickListener(new OnStarFeatureClickListener() {
            @Override
            public void onItemClick(StarFeatureAdapter.ViewHolder holder, View view, int position) {
                StarFeature item = starFeatureAdapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), StarSearchActivity.class);
                intent.putExtra("starHashTagName", item.getStarFeatureName());
                intent.putExtra("starHashTag",item.getStarFeatureId());
                intent.putExtra("type",2);
                startActivity(intent);
            }
        });
    }
}