package com.starrynight.tourapiproject.starPage;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.MainActivity;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.SearchFragment;
import com.starrynight.tourapiproject.searchPage.SearchResultFragment;
import com.starrynight.tourapiproject.starPage.StarActivity;
import com.starrynight.tourapiproject.starPage.starItemPage.OnStarItemClickListener;
import com.starrynight.tourapiproject.starPage.starItemPage.OnStarItemClickListener2;
import com.starrynight.tourapiproject.starPage.starItemPage.StarItem;
import com.starrynight.tourapiproject.starPage.starItemPage.StarViewAdapter;
import com.starrynight.tourapiproject.starPage.starItemPage.StarViewAdpater2;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.GridItemDecoration;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.RetrofitClient;

import org.w3c.dom.Text;

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

    String[] hashtagList={"황도 12궁", "길잡이별", "봄","여름","가을","겨울"};
    RecyclerView recyclerView;
    StarViewAdpater2 constAdapter;
    StarViewAdapter constTodayAdapter;

    LinearLayout starFilterItem;
    RecyclerView constTodayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_all);

        //검색바 설정
        androidx.appcompat.widget.SearchView searchView = findViewById(R.id.starSearch);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("궁금한 별자리를 검색해보세요!");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getApplicationContext(),StarSearchActivity.class);
                intent.putExtra("keyword",query);
                intent.putExtra("type",1);
                Log.d("keyword", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // 뒤로가기 버튼 클릭 이벤트
        ImageView backBtn = findViewById(R.id.all_star_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //recyclerView 설정
        recyclerView = findViewById(R.id.all_const_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new StarRecyclerViewWidth(0,40));
        constAdapter = new StarViewAdpater2();
        recyclerView.setAdapter(constAdapter);

        // 이름, 이미지를 불러오기 위한 get api
        Call<List<StarItem>> starItemCall = RetrofitClient.getApiService().getConstellation();
        starItemCall.enqueue(new Callback<List<StarItem>>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<List<StarItem>> call, Response<List<StarItem>> response) {
                if (response.isSuccessful()) {
                    List<StarItem> result = response.body();
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

        // item 클릭 시 해당 아이템 constId 넘겨주기
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

        constTodayList = findViewById(R.id.today_cel_recycler);
        constTodayList.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        constTodayList.addItemDecoration(new StarRecyclerViewWidth(20,0));
        constTodayAdapter = new StarViewAdapter();
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

        TextView[] hashList= new TextView[6];
        //검색 해시태그 목록
        starFilterItem = findViewById(R.id.selectStarFilterItem);
        starFilterItem.removeAllViews(); //초기화
        for (int i = 0; i < hashList.length; i++) {
                String hashTagName= hashtagList[i];
                hashList[i] = new TextView(getApplicationContext());
                hashList[i].setText(" " + hashtagList[i] + " >");
                hashList[i].setTextSize(16);
                hashList[i].setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                hashList[i].setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.hashtag_background));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int index = i+23;
            hashList[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(getApplicationContext(),StarSearchActivity.class);
                        intent.putExtra("starHashTag",index);
                        intent.putExtra("starHashTagName",hashTagName);
                        intent.putExtra("type",2);
                        Log.d("starHashTag","해시태그 전송"+index);
                        startActivity(intent);
                    }
                });
                params.rightMargin = 20;
                hashList[i].setLayoutParams(params);
                starFilterItem.addView(hashList[i]);
                starFilterItem.setDividerPadding(5);
        }
    }
}