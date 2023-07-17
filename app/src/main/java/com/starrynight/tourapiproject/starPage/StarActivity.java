package com.starrynight.tourapiproject.starPage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.starPage.constNameRetrofit.ConstellationParams2;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.Constellation;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.OnStarHashTagClickListener;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.StarHashTag;
import com.starrynight.tourapiproject.starPage.starPageRetrofit.StarHashTagAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @className : StarActivity
 * @description : 별자리 상세페이지입니다.
 * @modification : 2022-09-15 (hyeonz) 주석추가
 * @author : hyeonz
 * @date : 2022-09-15
 * @version : 1.0
====개정이력(Modification Information)====
수정일        수정자        수정내용
-----------------------------------------
2022-09-15   hyeonz      주석추가
 */
public class StarActivity extends AppCompatActivity {
    private static final String TAG = "Star page";
    TextView constName,rightNow,hashTag;

    // 별자리 상세정보 뷰
    TextView constMtdTv, constBestMonthTv, constStoryTv, constSummary;
    ImageView constImage,constFeatureImg;
    ImageView story_play_btn;
    Long constId;
    List<StarHashTag> starHashTags;
    StarHashTagAdapter starHashTagAdapter;
    RecyclerView recyclerView;
    LinearLayout hashTagList;

    String intentConstName;
    Constellation constData;

    private TextToSpeechClient ttsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);

        TextToSpeechManager.getInstance().initializeLibrary(getApplicationContext());   //카카오 음성

        // 넘겨준 constId 받기
        Intent intent = getIntent();
        intentConstName = (String) intent.getSerializableExtra("constName");
        Log.d("constName 받아오기", intentConstName);

        constName = findViewById(R.id.detail_const_name);
        constImage = findViewById(R.id.detail_const_image);
        constFeatureImg = findViewById(R.id.const_feature_img);

        constStoryTv = findViewById(R.id.const_story);
        constMtdTv = findViewById(R.id.const_mtd_tv);
        constBestMonthTv = findViewById(R.id.const_best_month_tv);
        constSummary = findViewById(R.id.constSummary);
        hashTagList = findViewById(R.id.starHashTags);

        story_play_btn = findViewById(R.id.story_play_btn);
        rightNow = findViewById(R.id.rightNow);

        ttsClient = new TextToSpeechClient.Builder()
                .setSpeechMode(TextToSpeechClient.NEWTONE_TALK_1)     // 음성합성방식
                .setSpeechSpeed(1.0)            // 발음 속도(0.5~4.0)
                .setSpeechVoice(TextToSpeechClient.VOICE_WOMAN_READ_CALM)  //TTS 음색 모드 설정(여성 차분한 낭독체)
                .setListener(ttsListener)
                .build();

        recyclerView = findViewById(R.id.feature_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        starHashTagAdapter = new StarHashTagAdapter();
        recyclerView.setAdapter(starHashTagAdapter);
        // 별자리 클릭 후 상세 정보 불러오는 api
        Call<Constellation> detailConstCall = RetrofitClient.getApiService().getDetailConst(intentConstName);
        detailConstCall.enqueue(new Callback<Constellation>() {
            @Override
            public void onResponse(Call<Constellation> call, Response<Constellation> response) {
                if (response.isSuccessful()) {

                    constData = response.body();
                    Glide.with(StarActivity.this).load("https://starry-night.s3.ap-northeast-2.amazonaws.com/constDetailImage/b_" + constData.getConstEng() + ".png").fitCenter().into(constImage);
                    constName.setText(constData.getConstName());
                    constStoryTv.setText(constData.getConstStory());
                    constMtdTv.setText(constData.getConstMtd());
                    constBestMonthTv.setText(constData.getConstBestMonth());
                    //매일 볼수 있는 별자리일 경우 배너 추가
                    if(!constBestMonthTv.equals("1월~3월")
                            &&!constBestMonthTv.equals("4월~6월")
                            &&!constBestMonthTv.equals("7월~9월")
                            &&!constBestMonthTv.equals("10월~12월")){
                        Glide.with(StarActivity.this).load("https://starry-night.s3.ap-northeast-2.amazonaws.com/constDetailImage/feature_" + "see_every_day" + ".png").fitCenter().into(constFeatureImg);
                    }
                    constId= constData.getConstId();

                    //별자리 해시태그 특성 이미지 가져오기
                    Call<List<StarHashTag>> hashTagCall = RetrofitClient.getApiService().getStarHashTags(constId);
                    hashTagCall.enqueue(new Callback<List<StarHashTag>>() {
                        @Override
                        public void onResponse(Call<List<StarHashTag>> call, Response<List<StarHashTag>> response) {
                            if (response.isSuccessful()){
                                Log.d("starFeature","별자리 해시태그 이미지 가져오기 성공");
                                starHashTags=response.body();
                                if (starHashTags!=null){
                                    for(StarHashTag sh : starHashTags){
                                        starHashTagAdapter.addItem(sh);
                                        if(sh.getHashTagName().equals("봄")){
                                            Glide.with(StarActivity.this).load("https://starry-night.s3.ap-northeast-2.amazonaws.com/constDetailImage/feature_" + "spring" + ".png").fitCenter().into(constFeatureImg);
                                        }else if(sh.getHashTagName().equals("여름")) {
                                            Glide.with(StarActivity.this).load("https://starry-night.s3.ap-northeast-2.amazonaws.com/constDetailImage/feature_" + "summer" + ".png").fitCenter().into(constFeatureImg);
                                        }else if(sh.getHashTagName().equals("가을")) {
                                            Glide.with(StarActivity.this).load("https://starry-night.s3.ap-northeast-2.amazonaws.com/constDetailImage/feature_" + "fall" + ".png").fitCenter().into(constFeatureImg);
                                        } else if(sh.getHashTagName().equals("겨울")) {
                                            Glide.with(StarActivity.this).load("https://starry-night.s3.ap-northeast-2.amazonaws.com/constDetailImage/feature_" + "winter" + ".png").fitCenter().into(constFeatureImg);
                                        } else if(sh.getHashTagName().equals("황도 12궁")) {
                                            Glide.with(StarActivity.this).load("https://starry-night.s3.ap-northeast-2.amazonaws.com/constDetailImage/feature_" + "ecliptic" + ".png").fitCenter().into(constFeatureImg);
                                        }
                                    }
                                    recyclerView.setAdapter(starHashTagAdapter);
                                    //별자리 해시태그 클릭시 이벤트
                                    starHashTagAdapter.setOnItemClickListener(new OnStarHashTagClickListener() {
                                        @Override
                                        public void onItemClick(StarHashTagAdapter.ViewHolder holder, View view, int position) {
                                            StarHashTag item = starHashTagAdapter.getItem(position);
                                            Intent intent= new Intent(getApplicationContext(),StarSearchActivity.class);
                                            intent.putExtra("starHashTag",item.getHashTagId());
                                            intent.putExtra("starHashTagName",item.getHashTagName());
                                            intent.putExtra("type",2);
                                            Log.d("starHashTag","해시태그 전송"+item.getHashTagId());
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }else{
                                Log.e("starFeature","별자리 해시태그 이미지 가져오기 실패");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<StarHashTag>> call, Throwable t) {
                            Log.e("starFeature","별자리 해시태그 인터넷 오류");
                        }
                    });
                    constSummary.setText(constData.getSummary());

                    ttsClient.setSpeechText(constData.getConstStory());   //뉴톤톡 하고자 하는 문자열을 미리 세팅.
                } else {
                }
            }

            @Override
            public void onFailure(Call<Constellation> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });

        rightNow.setVisibility(View.GONE);
        //지금 볼 수 있는 별자리인지 확인
        Call<List<ConstellationParams2>> toCall =RetrofitClient.getApiService().getTodayConstName();
        toCall.enqueue(new Callback<List<ConstellationParams2>>() {
            @Override
            public void onResponse(Call<List<ConstellationParams2>> call, Response<List<ConstellationParams2>> response) {
                if (response.isSuccessful()){
                    List<ConstellationParams2> cp2s = response.body();
                    for(ConstellationParams2 cp2 : cp2s){
                        String cp2Name = cp2.getConstName();
                        if(constName.getText().equals(cp2Name)){
                            Log.d("starNow","오늘 볼 수 있는 별자리 가져오기 성공."+constName.getText());
                            rightNow.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else{Log.e("starNow","오늘 볼 수 있는 별자리 가져오기 실패.");}
            }

            @Override
            public void onFailure(Call<List<ConstellationParams2>> call, Throwable t) {
                Log.e("starNow","오늘 볼 수 있는 별자리 인터넷 실패.");
            }
        });





        story_play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ttsClient.isPlaying()) {
                    ttsClient.stop();
                } else {
                    ttsClient.play();
                }
            }
        });

        // 뒤로 가기 버튼 이벤트
        ImageView backBtn = findViewById(R.id.detail_star_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private TextToSpeechListener ttsListener = new TextToSpeechListener() {
        @Override
        public void onFinished() {
            int intSentSize = ttsClient.getSentDataSize();      //세션 중에 전송한 데이터 사이즈
            int intRecvSize = ttsClient.getReceivedDataSize();  //세션 중에 전송받은 데이터 사이즈

            final String strInacctiveText = "handleFinished() SentSize : " + intSentSize + "  RecvSize : " + intRecvSize;

            Log.i(TAG, strInacctiveText);
        }

        @Override
        public void onError(int code, String message) {
            Log.e(TAG, "카카오음성오류: " + message);
        }
    };

    public void onDestroy() {
        super.onDestroy();
        TextToSpeechManager.getInstance().finalizeLibrary();
    }

    public void onPause() {
        super.onPause();
        if (ttsClient.isPlaying())
            ttsClient.stop();
    }
}