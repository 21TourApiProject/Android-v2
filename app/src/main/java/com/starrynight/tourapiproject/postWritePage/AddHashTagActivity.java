package com.starrynight.tourapiproject.postWritePage;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.postWritePage.postWriteRetrofit.PostHashTagAdapter;
import com.starrynight.tourapiproject.postWritePage.postWriteRetrofit.PostHashTagParams;
import com.starrynight.tourapiproject.searchPage.filter.HashTagItem;
import com.starrynight.tourapiproject.searchPage.searchPageRetrofit.RetrofitClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.starrynight.tourapiproject.searchPage.filter.HashTagItem.VIEWTYPE_ACTIVE;

/**
* @className : AddHashTagActivity
* @description : 게시물 작성 페이지의 해시태그 추가 페이지 입니다.
* @modification : 2022-09-02 (jinhyeok) 주석 수정
* @author : jinhyeok
* @date : 2022-09-02
* @version : 1.0
   ====개정이력(Modification Information)====
  수정일        수정자        수정내용
   -----------------------------------------
   2022-09-02      jinhyeok       주석 수정

 */
public class AddHashTagActivity extends AppCompatActivity {
    List<PostHashTagParams> postHashTagParams = new ArrayList<>();
    List<PostHashTagParams> postHashTagParams2 = new ArrayList<>();
    List<HashTagItem> hashTaglist;
    RecyclerView areaRecyclerView, themeRecyclerView,peopleRecyclerView,facilityRecyclerView, feeRecyclerView;
    ArrayList<String> hashtag=new ArrayList<>();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hash_tag);


        Intent intent = getIntent();
        postHashTagParams2 = (List<PostHashTagParams>) intent.getSerializableExtra("hashTagParams");

        if (postHashTagParams2 != null) {
            for (int i = 0; i < postHashTagParams2.size(); i++) {
                if (postHashTagParams2.get(i).getHashTagName() != null) {
                    hashtag.add(postHashTagParams2.get(i).getHashTagName());
                }
            }
        }


        PostHashTagAdapter themeAdapter =new PostHashTagAdapter();
        PostHashTagAdapter facilityAdapter =new PostHashTagAdapter();
        PostHashTagAdapter peopleAdapter =new PostHashTagAdapter();
        PostHashTagAdapter feeAdapter =new PostHashTagAdapter();
        PostHashTagAdapter localAdapter =new PostHashTagAdapter();
        FlexboxLayoutManager flexboxLayoutManager;
        FlexboxLayoutManager flexboxLayoutManager1;
        FlexboxLayoutManager flexboxLayoutManager2;
        FlexboxLayoutManager flexboxLayoutManager3;

        flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager1 = new FlexboxLayoutManager(this);
        flexboxLayoutManager1.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager1.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager2 = new FlexboxLayoutManager(this);
        flexboxLayoutManager2.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager2.setJustifyContent(JustifyContent.FLEX_START);
        flexboxLayoutManager3 = new FlexboxLayoutManager(this);
        flexboxLayoutManager3.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager3.setJustifyContent(JustifyContent.FLEX_START);
        themeRecyclerView= findViewById(R.id.themePostHashTag);
        peopleRecyclerView=findViewById(R.id.whoPostHashTag);
        facilityRecyclerView=findViewById(R.id.facilityPostHashtag);
        feeRecyclerView=findViewById(R.id.feePostHashTag);
        areaRecyclerView = findViewById(R.id.localPostHashTag);

        //해시태그 리스트 불러오기
        Call<List<HashTagItem>> hashTagCall = RetrofitClient.getApiService().getHashTag();
        hashTagCall.enqueue(new Callback<List<HashTagItem>>() {
            @Override
            public void onResponse(Call<List<HashTagItem>> call, Response<List<HashTagItem>> response) {
                if(response.isSuccessful()){
                    Log.d("postHashTag", "모든 해쉬태그 호출 성공");
                    hashTaglist=response.body();
                    //이전에 이미 클릭해놓은 해시태그가 있는 경우 확인
                    if(hashtag!=null){
                        for(String h : hashtag){
                            for(HashTagItem item : hashTaglist){
                                if(item.getName().equals(h)){
                                    item.setIsActive(1);
                                }
                            }
                        }
                    }
                    for(HashTagItem item: hashTaglist){
                        switch (item.getCategory()){
                           // case "LOCAL":
                             //   areaList.add(item);
                              //  break;
                            case "THEME":
                                themeAdapter.addItem(item);
                                break;
                            case "PEOPLE":
                                peopleAdapter.addItem(item);
                                break;
                            case "FACILITY":
                                facilityAdapter.addItem(item);
                                break;
                            case "FEE":
                                feeAdapter.addItem(item);
                                break;
                        }
                    }

                    themeRecyclerView.setLayoutManager(flexboxLayoutManager);
                    themeRecyclerView.setAdapter(themeAdapter);
                    peopleRecyclerView.setLayoutManager(flexboxLayoutManager1);
                    peopleRecyclerView.setAdapter(peopleAdapter);
                    facilityRecyclerView.setLayoutManager(flexboxLayoutManager2);
                    facilityRecyclerView.setAdapter(facilityAdapter);
                    feeRecyclerView.setLayoutManager(flexboxLayoutManager3);
                    feeRecyclerView.setAdapter(feeAdapter);
                }else{
                    Log.e("postHashTag", "모든 해쉬태그 호출 실패");
                }
            }

            @Override
            public void onFailure(Call<List<HashTagItem>> call, Throwable t) {
                Log.e("postHashTag", "모든 해쉬태그 호출 인터넷 실패");
            }
        });





        final List<String> finallist = new ArrayList<>(); //메인 해시태그 리스트

        //완료 버튼 클릭 이벤트
        TextView plusHashTag = findViewById(R.id.finish_add_hashTag);
        plusHashTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<themeAdapter.getItemCount();i++){
                    if(themeAdapter.getItem(i).getIsActive()==VIEWTYPE_ACTIVE){
                        PostHashTagParams postHashTagParam = new PostHashTagParams();
                        postHashTagParam.setHashTagName(themeAdapter.getItem(i).getName());
                        postHashTagParams.add(postHashTagParam);
                        finallist.add(themeAdapter.getItem(i).getName());
                    }
                }
                for(int i=0;i<facilityAdapter.getItemCount();i++){
                    if(facilityAdapter.getItem(i).getIsActive()==VIEWTYPE_ACTIVE){
                        PostHashTagParams postHashTagParam = new PostHashTagParams();
                        postHashTagParam.setHashTagName(facilityAdapter.getItem(i).getName());
                        postHashTagParams.add(postHashTagParam);
                        finallist.add(facilityAdapter.getItem(i).getName());
                    }
                }
                for(int i=0;i<feeAdapter.getItemCount();i++){
                    if(feeAdapter.getItem(i).getIsActive()==VIEWTYPE_ACTIVE){
                        PostHashTagParams postHashTagParam = new PostHashTagParams();
                        postHashTagParam.setHashTagName(feeAdapter.getItem(i).getName());
                        postHashTagParams.add(postHashTagParam);
                        finallist.add(feeAdapter.getItem(i).getName());
                    }
                }
                for(int i=0;i<peopleAdapter.getItemCount();i++){
                    if(peopleAdapter.getItem(i).getIsActive()==VIEWTYPE_ACTIVE){
                        PostHashTagParams postHashTagParam = new PostHashTagParams();
                        postHashTagParam.setHashTagName(peopleAdapter.getItem(i).getName());
                        postHashTagParams.add(postHashTagParam);
                        finallist.add(peopleAdapter.getItem(i).getName());
                    }
                }
                intent.putExtra("postHashTagParams", (Serializable) postHashTagParams);
                intent.putExtra("hashTagList", (Serializable) finallist);
                setResult(3, intent);
                finish();
            }
        });

        //뒤로 가기 버튼
        ImageView back = findViewById(R.id.addHashTag_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //recyclerview 간격
    public static class RecyclerViewDecoration extends RecyclerView.ItemDecoration {

        private final int divRight;

        public RecyclerViewDecoration(int divRight) {

            this.divRight = divRight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.right = divRight;
        }
    }
}