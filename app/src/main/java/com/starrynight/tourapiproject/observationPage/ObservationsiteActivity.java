package com.starrynight.tourapiproject.observationPage;

import static com.starrynight.tourapiproject.MainActivity.mContext;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.mapPage.BalloonObject;
import com.starrynight.tourapiproject.observationPage.course.CourseDividerDecorator;
import com.starrynight.tourapiproject.observationPage.course.ObservationCourseAdapter;
import com.starrynight.tourapiproject.observationPage.course.ObservationCourseItem;
import com.starrynight.tourapiproject.observationPage.fee.ExpandableHeightGridView;
import com.starrynight.tourapiproject.observationPage.fee.ObservationFeeAdapter;
import com.starrynight.tourapiproject.observationPage.fee.ObservationFeeDecoration;
import com.starrynight.tourapiproject.observationPage.fee.ObservationFeeItem;
import com.starrynight.tourapiproject.observationPage.observationPageRetrofit.CourseTouristPoint;
import com.starrynight.tourapiproject.observationPage.observationPageRetrofit.Observation;
import com.starrynight.tourapiproject.observationPage.observationPageRetrofit.ObserveFee;
import com.starrynight.tourapiproject.observationPage.observationPageRetrofit.ObserveImageInfo;
import com.starrynight.tourapiproject.observationPage.observationPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.postPage.PostActivity;
import com.starrynight.tourapiproject.postPage.postRetrofit.PostImage;
import com.starrynight.tourapiproject.postWritePage.PostWriteActivity;
import com.starrynight.tourapiproject.searchPage.ResultViewPagerAdapter;
import com.starrynight.tourapiproject.weatherPage.LocationDTO;
import com.starrynight.tourapiproject.weatherPage.WeatherActivity;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
* @className : ObservationsiteActivity.java
* @description : 관측지페이지
* @modification : gyul chyoung (2022-08-30) 주석추가
* @author : 2022-08-30
* @date : gyul chyoung
* @version : 1.0
     ====개정이력(Modification Information)====
  수정일        수정자        수정내용    -----------------------------------------
   gyul chyoung       2022-08-30       주석추가
 */

public class ObservationsiteActivity extends AppCompatActivity {
    private Long userId;
    private Long postId;
    private Boolean isWish;

    private static final String TAG = "observation page";
    private Observation observation;
    private List<String> observeHashTags;
    private RecyclerHashTagAdapter recyclerHashTagAdapter;
    TextView outline;
    TextView link;

    LinearLayout to_light_btn;

    Long count_tmp;

    private ViewPager2 obs_slider;
    private LinearLayout obs_indicator;
    private TextView imageSource_txt;
    private String[] obs_images;
    private List<String> imageSources;

    private boolean isFeeClosed =true;


    ViewPager2 observeViewPager;
    TabLayout tabLayout;
    ObserveViewPagerAdapter observeViewPagerAdapter;


    private BalloonObject balloonObject = new BalloonObject();   //mapfragment bundle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //관측지 게시글 이미지 설정
        setContentView(R.layout.activity_observationsite);
        to_light_btn = findViewById(R.id.obs_move_light);

        Intent intent = getIntent();

        Long observationId = (Long) intent.getSerializableExtra("observationId"); //전 페이지에서 받아온 contentId
        Boolean fromWeather = (Boolean) intent.getSerializableExtra("fromWeather");//날씨에서 왔는지 확인

        if (fromWeather!=null && fromWeather) {
            to_light_btn.setVisibility(View.GONE);
        }
        postId = (Long) intent.getSerializableExtra("postId");

        observeViewPager = findViewById(R.id.observation_view_pager);
        tabLayout = findViewById(R.id.obs_tab_layout);
        observeViewPager.setSaveEnabled(false);


        Call<Observation> call1 = RetrofitClient.getApiService().getObservation(observationId);
        call1.enqueue(new Callback<Observation>() {
            @Override
            public void onResponse(Call<Observation> call, Response<Observation> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "관측지 호출 성공");
                    observation = response.body();
                    if (observation.getSaved() == null) {
                        observation.setSaved();
                    }
                    setViewPager();

                    Call<List<ObserveImageInfo>> call3 = RetrofitClient.getApiService().getObserveImageInfo(observationId);
                    call3.enqueue(new Callback<List<ObserveImageInfo>>() {
                        @Override
                        public void onResponse(Call<List<ObserveImageInfo>> call, Response<List<ObserveImageInfo>> response) {
                            if (response.isSuccessful()) {
                                if (!response.body().isEmpty()) {
                                    Log.d(TAG, "관측지 이미지 호출 성공");
                                    List<ObserveImageInfo> observeImageInfos = response.body();
                                    List<String> imageList = new ArrayList<>();
                                    imageSources = new ArrayList<>();

                                    for (ObserveImageInfo info : observeImageInfos) {
                                        imageList.add(info.getImage());
                                        imageSources.add(info.getImageSource());
                                    }
                                    obs_images = imageList.toArray(new String[imageList.size()]);
                                    balloonObject.setImage(imageList.get(0));   //map위한 bundle

                                    obs_slider = findViewById(R.id.obs_Img_slider);
                                    obs_indicator = findViewById(R.id.obs_Img_indicator);
                                    obs_slider.setAdapter(new ObserveImageSliderAdapter(ObservationsiteActivity.this, obs_images));
                                    obs_slider.setOffscreenPageLimit(10);

                                    obs_slider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                        @Override
                                        public void onPageSelected(int position) {
                                            super.onPageSelected(position);
                                            setObserveCurrentIndicator(position);
                                        }
                                    });
                                    imageSource_txt = findViewById(R.id.obs_image_source);
                                    setupObserveIndicators(obs_images.length);
                                }
                            } else {
                                Log.e(TAG, "관측지 이미지 호출 실패");
                            }

                        }

                        @Override
                        public void onFailure(Call<List<ObserveImageInfo>> call, Throwable t) {
                            Log.e(TAG, "관측지 이미지 연결결 실패");
                        }
                    });

                    //관측지 타입과 무관한 정보 설정
                    TextView name = findViewById(R.id.obs_name_txt);
                    name.setText(observation.getObservationName());
                    outline = findViewById(R.id.obs_outline_txt);
                    outline.setText(observation.getOutline());
                    setOutlineButton(); //개요 더보기 설정
                    setLightIcon(observation.getLight());

                    to_light_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
                            LocationDTO locationDTO = new LocationDTO(observation.getLatitude(), observation.getLongitude(), null, observationId, observation.getObservationName());
                            intent.putExtra("locationDTO", locationDTO);
                            intent.putExtra("fromObserve", true);
                            startActivityForResult(intent, 104);
                        }
                    });

                    if (observation.getNature()) {

                        //전화번호, 홈페이지 설정
                        setInquiry(true);
                        setReserve(true);

                    } else {
                        //운영관측지일 경우 레이아웃 구성

                        //홈페이지, 전화 연결
                        setInquiry(false);
                        setReserve(false);
                    }
                    // 후기, 정보 탭 설정

                    //해쉬태그 리사이클러 설정
                    initHashtagRecycler();

                    Call<List<String>> call2 = RetrofitClient.getApiService().getObserveHashTags(observationId);
                    call2.enqueue(new Callback<List<String>>() {

                        @Override
                        public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "관측지 해쉬태그 호출 성공");
                                observeHashTags = response.body();
                                for (String p : observeHashTags) {
                                    RecyclerHashTagItem item = new RecyclerHashTagItem();
                                    item.setHashtagName(p);
                                    recyclerHashTagAdapter.addItem(item);
                                }
                                recyclerHashTagAdapter.notifyDataSetChanged();

                            } else {
                                Log.e(TAG, "관측지 해쉬태그 호출 실패");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<String>> call, Throwable t) {
                            Log.e(TAG, "연결실패" + t.getMessage());

                        }
                    });

                    //찜버튼 설정
                    setSaveBtn();

                } else {
                    Log.e(TAG, "관측지 호출 실패");
                }
            }

            @Override
            public void onFailure(Call<Observation> call, Throwable t) {
                Log.e(TAG, "연결실패" + t.getMessage());
            }

        });

        //앱 내부 저장소의 userId 데이터 읽기
        String fileName = "userId";
        try {
            FileInputStream fis = this.openFileInput(fileName);
            String line = new BufferedReader(new InputStreamReader(fis)).readLine();
            userId = Long.parseLong(line);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ImageButton back_btn = findViewById(R.id.obs_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private void setSaveBtn(){
        //찜버튼 설정
        LinearLayout save_btn = findViewById(R.id.obs_save_btn);
        ImageView save_img = findViewById(R.id.obs_save_img);
        TextView save_count = findViewById(R.id.obs_save_count);
        save_count.setText(observation.getSaved().toString());
        count_tmp = observation.getSaved();

        //이미 찜한건지 확인
        Call<Boolean> call0 = com.starrynight.tourapiproject.myPage.myPageRetrofit.RetrofitClient.getApiService().isThereMyWish(userId, observation.getObservationId(), 0);
        call0.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        isWish = true;
                        save_img.setImageResource(R.drawable.bookmark);
                    } else {
                        isWish = false;
                    }
                } else {
                    Log.d("isWish", "내 찜 조회하기 실패");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });

        // 짐버튼 클릭 설정정
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isWish) { //찜 안한 상태일때
                    Call<Void> call = com.starrynight.tourapiproject.myPage.myPageRetrofit.RetrofitClient.getApiService().createMyWish(userId, observation.getObservationId(), 0);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                //버튼 디자인 바뀌게 구현하기
                                isWish = true;
                                save_img.setImageResource(R.drawable.bookmark);
                                count_tmp++;
                                save_count.setText(count_tmp.toString());
                                Toast.makeText(getApplicationContext(), "나의 여행버킷리스트에 저장되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("isWish", "관광지 찜 실패");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("연결실패", t.getMessage());
                        }
                    });
                } else {
                    Call<Void> call = com.starrynight.tourapiproject.myPage.myPageRetrofit.RetrofitClient.getApiService().deleteMyWish(userId, observation.getObservationId(), 0);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                isWish = false;
                                save_img.setImageResource(R.drawable.bookmark_non);
                                count_tmp--;
                                save_count.setText(count_tmp.toString());
                                Toast.makeText(getApplicationContext(), "나의 여행버킷리스트에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("isWishDelete", "관광지 찜 삭제 실패");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("연결실패", t.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void initHashtagRecycler() {
        //해쉬태그 리사이클러 초기화
        RecyclerDecoration hashtagDecoration = new RecyclerDecoration(16);
        RecyclerView hashTagsrecyclerView = findViewById(R.id.obs_hashtags_layout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        hashTagsrecyclerView.setLayoutManager(linearLayoutManager);
        hashTagsrecyclerView.addItemDecoration(hashtagDecoration);
        recyclerHashTagAdapter = new RecyclerHashTagAdapter();
        hashTagsrecyclerView.setAdapter(recyclerHashTagAdapter);
    }

    private void setOutlineButton() {
        TextView outline_btn = findViewById(R.id.obs_outline_btn);
        Layout l = outline.getLayout();

        //개요가 4줄 이상일 경우만 자세히 보기 버튼 생성
        if (l != null) {
            int lines = l.getLineCount();
            if (lines > 0)
                if (l.getEllipsisCount(lines - 1) > 0) {
                    outline_btn.setVisibility(View.VISIBLE);
                    Log.d(TAG, "텍스트 줄넘침");
                }
        }

        //개요 더보기 버튼 클릭시 아래로 늘리기
        outline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outline.getEllipsize() != null) {
                    outline_btn.setText("접기");
                    outline.setEllipsize(null);
                    outline.setMaxLines(Integer.MAX_VALUE);
                }else{
                    outline_btn.setText("더보기");
                    outline.setEllipsize(TextUtils.TruncateAt.END);
                    outline.setMaxLines(3);
                }
            }
        });
    }

    private void setupObserveIndicators(int count) {
        //이비지 슬라이더 인디케이터 설정
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(15, 0, 15, 0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.observe__indicator_inactive));
            indicators[i].setLayoutParams(params);
            obs_indicator.addView(indicators[i]);
        }
        setObserveCurrentIndicator(0);
    }

    private void setObserveCurrentIndicator(int position) {
        imageSource_txt.setText(imageSources.get(position));
        int childCount = obs_indicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) obs_indicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.observe__indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.observe__indicator_inactive
                ));
            }
        }
    }


    //광공해 아이콘 설정
    private void setLightIcon(Double light){
        ImageView light_icon = findViewById(R.id.obs_light_icon);
        if (light <= 1)
            light_icon.setImageResource(R.drawable.light_great);
        else if (light > 1 && light <= 15)
            light_icon.setImageResource(R.drawable.light_good);
        else if (light > 15 && light <= 45)
            light_icon.setImageResource(R.drawable.light_common);
        else if (light > 45 && light <= 80)
            light_icon.setImageResource(R.drawable.light_bad);
        else if (light > 80)
            light_icon.setImageResource(R.drawable.light_worst);
    }

    //문의사항(전화,홈페이지)설정
    private void setInquiry(boolean is_nature){
        if (is_nature) {
            LinearLayout inquiry_layout = findViewById(R.id.obs_inquiry);
            inquiry_layout.setAlpha((float) 0.1);
        }else{

            //전화연결 설정
            LinearLayout call_layout = findViewById(R.id.obs_call);
            call_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        String tmp = observation.getPhoneNumber();
                        String phone = tmp.replaceAll("[-]", "");
                        Log.d(TAG, "전화번호" + phone);
                        Intent call_url = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                        startActivity(call_url);
                    } catch (Exception e) {
                        Log.e(TAG, "전화연결 불가");
                    }

                }
            });
            //홈페이지 설정정
            LinearLayout homepage_ayout = findViewById(R.id.obs_homepage);
            homepage_ayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Log.i(TAG,"Connect to " + observation.getLink());
                        Intent homepageurl = new Intent(Intent.ACTION_VIEW, Uri.parse(observation.getLink()));
                        startActivity(homepageurl);
                    } catch (Exception e) {
                        Log.e(TAG, "홈페이지 이동 불가");
                    }
                }
            });
        }
    }

    private void setReserve(boolean is_nature){
        TextView reserve_btn = findViewById(R.id.obs_reserve);
        if(is_nature || observation.getReserve()==null){
            //홈페이지 없는 경우
            reserve_btn.setText("입장 예약이 불가능해요.");
            reserve_btn.setBackground(getDrawable(R.drawable.observation__reserve_none));
            reserve_btn.setTextColor(getColor(R.color.gray_600));

        }else{
            //하단 예약버튼 설정 - 홈페이지 있는 경우
            reserve_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Log.i(TAG,"Connect to " + observation.getReserve());
                        Intent homepageurl = new Intent(Intent.ACTION_VIEW, Uri.parse(observation.getReserve()));
                        startActivity(homepageurl);
                    } catch (Exception e) {
                        Log.e(TAG, "홈페이지 이동 불가");
                    }
                }
            });
        }
    }

    private void setViewPager() {
        observeViewPagerAdapter = new ObserveViewPagerAdapter(getSupportFragmentManager(),getLifecycle(),observation);
        observeViewPager.setAdapter(observeViewPagerAdapter);
        final List<String> tabElement = Arrays.asList("정보","후기");

        //tabLyout와 viewPager 연결
        new TabLayoutMediator(tabLayout, observeViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                tab.setText(tabElement.get(position));
            }
        }).attach();
    }

}