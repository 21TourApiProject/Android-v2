package com.starrynight.tourapiproject.postWritePage;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.observationPage.observationPageRetrofit.Observation;
import com.starrynight.tourapiproject.observationPage.observationPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.postItemPage.OnSearchItemClickListener;
import com.starrynight.tourapiproject.postItemPage.Search_item;
import com.starrynight.tourapiproject.postItemPage.Search_item_adapter;
import com.starrynight.tourapiproject.postItemPage.Search_item_adapter2;
import com.starrynight.tourapiproject.postWritePage.postWriteRetrofit.PostHashTagParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
* @className : SearchObservingPointActivity
* @description : 게시물 작성시 관측지 기입 페이지 입니다.
* @modification : 2022-09-02 (jinhyeok) 주석 수정
* @author : jinhyeok
* @date : 2022-09-02
* @version : 1.0
   ====개정이력(Modification Information)====
  수정일        수정자        수정내용
   -----------------------------------------
   2022-09-02      jinhyeok       주석 수정

 */
public class SearchObservingPointActivity extends AppCompatActivity {
    EditText findObservePoint;
    String observePoint;
    ArrayList<Search_item> searchitemArrayList, filteredList;
    Search_item_adapter search_item_adapter,near_item_adapter;
    Search_item_adapter2 add_item_adapter;
    LinearLayoutManager layoutManager,nearLayoutManager,addLayoutManager;
    RecyclerView searchObservationRecyclerview;
    RecyclerView addRecyclerView;
    RecyclerView nearRecyclerView;
    LinearLayout addLinearLayout, nearLinearLayout,searchLinearLayout, noResultLinearLayout,optionRegistLinearLayout;
    TextView optionText;
    EditText editText;
    AddAreaFragment addAreaFragment;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_observing_point);

        addRecyclerView=findViewById(R.id.addRecyclerview);
        nearRecyclerView=findViewById(R.id.nearRecyclerview);
        addLinearLayout=findViewById(R.id.addPositionLinearLayout);
        nearLinearLayout=findViewById(R.id.nearPositionLinearLayout);
        searchLinearLayout=findViewById(R.id.searchLinearLayout);
        noResultLinearLayout =findViewById(R.id.noResultLayout);
        optionRegistLinearLayout =findViewById(R.id.optionRegist);
        searchObservationRecyclerview = findViewById(R.id.searchObservationRecyclerView);
        searchObservationRecyclerview.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
        findObservePoint = findViewById(R.id.findObservePoint);
        searchitemArrayList = new ArrayList<>();
        filteredList = new ArrayList<>();

        optionText = findViewById(R.id.optionText);

        //검색어 입력했을 때 나오는 관측지 리스트
        search_item_adapter = new Search_item_adapter(searchitemArrayList, this);
        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        searchObservationRecyclerview.setLayoutManager(layoutManager);
        searchObservationRecyclerview.setAdapter(search_item_adapter);

        //가까운 관측지 리스트
        //near_item_adapter=new Search_item_adapter(searchitemArrayList,this);
        //nearLayoutManager= new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        //nearRecyclerView.setLayoutManager(nearLayoutManager);
        //nearRecyclerView.setAdapter(near_item_adapter);

        //추가한 위치
        //add_item_adapter = new Search_item_adapter2(searchitemArrayList,this);
        //addLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        //addRecyclerView.setLayoutManager(addLayoutManager);
        //addRecyclerView.setAdapter(add_item_adapter);

        Call<List<Observation>> call = RetrofitClient.getApiService().getAllObservation();
        call.enqueue(new Callback<List<Observation>>() {
            @Override
            public void onResponse(Call<List<Observation>> call, Response<List<Observation>> response) {
                if (response.isSuccessful()) {
                    Log.d("observation", "관측지 리스트 업로드");
                    List<Observation> observationList = response.body();
                    for (int i = 0; i < observationList.size() - 1; i++) {
                        searchitemArrayList.add(new Search_item(observationList.get(i).getObservationName(), observationList.get(i).getAddress()));
                    }
                    search_item_adapter.filterList(searchitemArrayList);
                } else {
                    Log.d("observation", "관측지 리스트 업로드 실패");
                }
            }


            @Override
            public void onFailure(Call<List<Observation>> call, Throwable t) {
                Log.d("observation", "관측지 리스트 업로드 인터넷 실패");
            }
        });
        search_item_adapter.notifyDataSetChanged();

        findObservePoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = findObservePoint.getText().toString();
                searchFilter(text);
            }
        });

        search_item_adapter.setOnItemClicklistener(new OnSearchItemClickListener() {
            @Override
            public void onItemClick(Search_item_adapter.ViewHolder holder, View view, int position) {
                Search_item item = search_item_adapter.getItem(position);
                observePoint = item.getItemName();
                Intent intent = new Intent();
                intent.putExtra("observationName", observePoint);
                setResult(2, intent);
                finish();
            }
        });

        fragmentManager = getSupportFragmentManager();

        //임의 관측지 클릭 시
        optionRegistLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observePoint = ((EditText) (findViewById(R.id.findObservePoint))).getText().toString();
                addAreaFragment =new AddAreaFragment();
                addAreaFragment.setCancelable(true);
                addAreaFragment.show(fragmentManager,addAreaFragment.getTag());

            }
        });

        //관측지 돋보기 클릭 버튼
        Button addObservePoint = findViewById(R.id.addObservePoint);
        addObservePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLinearLayout.setVisibility(View.VISIBLE);
            }
        });
        //뒤로가기 버튼
        ImageView back_btn = findViewById(R.id.search__back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //키보드 입력시 변화
        editText = findViewById(R.id.findObservePoint);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!editText.getText().toString().equals("")) {
                        observePoint = ((EditText) (findViewById(R.id.findObservePoint))).getText().toString();
                    }
                } else {
                    return false;
                }
                return true;
            }
        });

    }

    public void searchFilter(String searchText) {
        filteredList.clear();
        for (int i = 0; i < searchitemArrayList.size(); i++) {
            if (searchitemArrayList.get(i).getItemName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(searchitemArrayList.get(i));
                noResultLinearLayout.setVisibility(View.GONE);
            }
        }
        if (filteredList.size() == 0) {
            {
                noResultLinearLayout.setVisibility(View.VISIBLE);
                String option =((EditText) (findViewById(R.id.findObservePoint))).getText().toString();
                optionText.setText("'"+option+"'에 대한\n검색 결과가 없어요");
            }
        }
        search_item_adapter.filterList(filteredList);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==206){
            if(resultCode==6){
                Log.d("postArea", "지역 해시태그 데이터 로드");
                String optionObName = (String) data.getSerializableExtra("optionObservationName");
                List<String> areaList = (List<String>) data.getSerializableExtra("areaList");
                List<PostHashTagParams> postAreaParams = (List<PostHashTagParams>) data.getSerializableExtra("postAreaParams");
                Intent intent =new Intent();
                intent.putExtra("postAreaParams", (Serializable) postAreaParams);
                intent.putExtra("areaList", (Serializable) areaList);
                intent.putExtra("optionObservationName",(Serializable) observePoint);
                setResult(2,intent);
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}