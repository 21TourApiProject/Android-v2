package com.starrynight.tourapiproject.starPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.Filter;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.SearchKey;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.SearchLoadingDialog;
import com.starrynight.tourapiproject.starPage.constNameRetrofit.ConstellationParams2;
import com.starrynight.tourapiproject.starPage.starItemPage.OnStarItemClickListener;
import com.starrynight.tourapiproject.starPage.starItemPage.OnStarItemClickListener2;
import com.starrynight.tourapiproject.starPage.starItemPage.StarItem;
import com.starrynight.tourapiproject.starPage.starItemPage.StarViewAdapter;
import com.starrynight.tourapiproject.starPage.starItemPage.StarViewAdpater2;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.weatherPage.WeatherLoadingDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @className : StarActivity
 * @description : 별자리 검색 페이지입니다.
 * @modification : 2022-09-15 (hyeonz) 주석추가
 * @author : hyeonz
 * @date : 2022-09-15
 * @version : 1.0
====개정이력(Modification Information)====
수정일        수정자        수정내용
-----------------------------------------
2022-09-15   hyeonz      주석추가
 */
public class StarSearchActivity extends AppCompatActivity {

    androidx.appcompat.widget.SearchView constSearch;
    LinearLayout backBtn,searchWordLayout;
    ImageView deleteBtn;

    List<Long> hashTagIdList;
    List<Long> list = new ArrayList<>(Arrays.asList(1L,2L,3L,4L,5L,6L,7L,8L,9L,10L));
    SearchLoadingDialog dialog;

    List<String> nameList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    String itemClickId;
    TextView resultText;

    String constName;
    Long starHashTag;
    String keyword;
    String hashTagName;
    List<StarItem> searchResult;
    StarViewAdpater2 constAdapter;
    RecyclerView constRecycler;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_search);

        resultText = findViewById(R.id.star_result_text);
        backBtn = findViewById(R.id.star_search_back_btn);
        dialog = new SearchLoadingDialog(StarSearchActivity.this);

        //별자리 검색
        constSearch = findViewById(R.id.edit_search);
        searchWordLayout = findViewById(R.id.search_word_layout);
        deleteBtn = findViewById(R.id.cancel_btn);

        constSearch.setIconifiedByDefault(false);
        constSearch.setQueryHint("궁금한 별자리를 입력해보세요");

        // 검색 결과 별자리 recyclerview
        constRecycler = findViewById(R.id.star_search_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        constRecycler.setLayoutManager(gridLayoutManager);
        constRecycler.addItemDecoration(new StarRecyclerViewWidth(3,30));
        constAdapter = new StarViewAdpater2();
        constRecycler.setAdapter(constAdapter);

        //이전 페이지 해시태그 및 검색어 받아오기
        Intent intent = getIntent();
        int Type = (int) intent.getSerializableExtra("type");
        if(Type==1){
            hashTagName =(String)intent.getSerializableExtra("starName");
            resultText.setText("'"+hashTagName+"'검색결과");
            constSearch.setQuery(hashTagName,false);
            Filter filter = new Filter(null, list );
            SearchKey searchKey = new SearchKey(filter, hashTagName);
            starSearchEveryThing(searchKey);
        }
        if(Type ==2){
            hashTagIdList = new ArrayList<>();
            starHashTag = (long)intent.getSerializableExtra("starHashTag");
            hashTagName = (String)intent.getSerializableExtra("starHashTagName");
            resultText.setText(" ' "+hashTagName+" ' 검색결과");
            constSearch.setQuery(hashTagName,false);
            hashTagIdList.add((long)starHashTag);
            Filter filter = new Filter(null, hashTagIdList);
            SearchKey searchKey = new SearchKey(filter, null);
            starSearchEveryThing(searchKey);
        }
        if(Type == 3){
            hashTagName = (String)intent.getSerializableExtra("starHashTagName");
            resultText.setText(" ' "+hashTagName+" ' 검색결과");
            constSearch.setQuery(hashTagName,false);
            // 오늘 볼 수 있는 별자리 리스트 api
            Call<List<StarItem>> todayConstCall = RetrofitClient.getApiService().getTodayConst();
            todayConstCall.enqueue(new Callback<List<StarItem>>() {
                @Override
                public void onResponse(@NonNull Call<List<StarItem>> call, Response<List<StarItem>> response) {
                    if (response.isSuccessful()) {
                        List<StarItem> result = response.body();
                        for (StarItem si : result) {
                            constAdapter.addItem(new StarItem(si.getConstId(), si.getConstName(), si.getConstEng()));
                        }
                        constRecycler.setAdapter(constAdapter);
                    } else {
                        Log.d("todayConst", "오늘의 별자리 불러오기 실패");
                    }
                }

                @Override
                public void onFailure(Call<List<StarItem>> call, Throwable t) {
                    Log.e("연결실패", t.getMessage());
                }
            });
        }

        //x버튼 클릭시 searchView 비우기
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constSearch.setQuery("",false);
                constSearch.clearFocus();
            }
        });

        // item 클릭 시 해당 아이템 constId 넘겨주기
      constAdapter.setOnItemClickListener(new OnStarItemClickListener2() {
          @Override
          public void onItemClick(StarViewAdpater2.ViewHolder holder, View view, int position) {
              StarItem item = constAdapter.getItem(position);
              Intent intent = new Intent(getApplicationContext(), StarActivity.class);
              intent.putExtra("constName", item.getConstName());
              Log.d("itemConstName", item.getConstName());
              startActivity(intent);
          }
      });

        callAllConstName();
        changeConstSearchText();
        onclickBackBtn();

        constSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        constSearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.d("hasFocus", "0");
                }

            }
        });
    }

    // 별자리 검색 기능
    private void starSearchEveryThing(SearchKey searchKey){
        constAdapter = new StarViewAdpater2();
        Call<List<StarItem>> call = RetrofitClient.getApiService().getConstDataWithFilter(searchKey);
        call.enqueue(new Callback<List<StarItem>>() {
            @Override
            public void onResponse(Call<List<StarItem>> call, Response<List<StarItem>> response) {
                if (response.isSuccessful()){
                    Log.d("searchStar", "별자리 검색 성공");
                    dialog.dismiss();
                    searchResult = response.body();
                    for(StarItem si : searchResult){
                        constAdapter.addItem(si);
                    }
                    constRecycler.setAdapter(constAdapter);
                    constAdapter.setOnItemClickListener(new OnStarItemClickListener2() {
                        @Override
                        public void onItemClick(StarViewAdpater2.ViewHolder holder, View view, int position) {
                            StarItem item = constAdapter.getItem(position);
                            Intent intent = new Intent(getApplicationContext(),StarActivity.class);
                            intent.putExtra("constName",item.getConstName());
                            Log.d("itemConstNameAll", item.getConstName());
                            startActivity(intent);
                        }
                    });
                }
                else {
                    Log.d("searchStar", "별자리 검색 실패");
                }
            }

            @Override
            public void onFailure(Call<List<StarItem>> call, Throwable t) {
                Log.d("searchStar", "별자리 검색 인터넷 오류");
            }
        });
    }

    // 모든 별자리 이름 호출
    public void callAllConstName() {
        Call<List<ConstellationParams2>> constNameCall = RetrofitClient.getApiService().getConstNames();
        constNameCall.enqueue(new Callback<List<ConstellationParams2>>() {
            @Override
            public void onResponse(Call<List<ConstellationParams2>> call, Response<List<ConstellationParams2>> response) {
                if (response.isSuccessful()) {
                    List<ConstellationParams2> result = response.body();

                    for (ConstellationParams2 cp2 : result) {
                        constName = cp2.getConstName();
                        nameList.add(constName);
                    }

                } else {
                    Log.d("constName", "전체 별자리 이름 불러오기 실패");
                }
            }

            @Override
            public void onFailure(Call<List<ConstellationParams2>> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });
    }

    //별자리 텍스트 검색 변화
    public void changeConstSearchText() {
        constSearch.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //검색 버튼 누를 때 호출
                dialog.show();
                resultText.setText("'"+query+"'검색결과");
                Filter filter = new Filter(null, list);
                SearchKey searchKey = new SearchKey(filter, query);
                starSearchEveryThing(searchKey);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    // 별자리 뒤로가기 버튼 클릭 이벤트
    public void onclickBackBtn() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
            constSearch.clearFocus();
            finish();
    }

    private class LoadingAsyncTask extends AsyncTask<String, Long, Boolean> {
        private Context mContext = null;
        private Long mtime;

        public LoadingAsyncTask(Context context, long time) {
            mContext = context.getApplicationContext();
            mtime = time;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                Thread.sleep(mtime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return (true);
        }

        @Override
        protected void onCancelled(Boolean result) {
            dialog.dismiss();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
        }
    }
}