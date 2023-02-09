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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
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
import com.starrynight.tourapiproject.weatherPage2.WeatherActivity2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
    ImageView relateImage1;
    ImageView relateImage2;
    ImageView relateImage3;

    private ViewPager2 obs_slider;
    private LinearLayout obs_indicator;
    private TextView imageSource_txt;
    private String[] obs_images;
    private List<String> imageSources;

    private ViewPager2 course_slider;
    private LinearLayout course_circle_indicator;
    private LinearLayout course_txt_indicator;

    private FrameLayout relateImageFrame;

    private ObservationFeeAdapter observationFeeAdapter;
    private List<ObserveFee> obs_fee_list;
    private String[] relatefilename = new String[5];


    private BalloonObject balloonObject = new BalloonObject();   //mapfragment bundle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //관측지 게시글 이미지 설정
        setContentView(R.layout.activity_observationsite);
        relateImage1 = findViewById(R.id.relateImage);
        relateImage2 = findViewById(R.id.relateImage2);
        relateImage3 = findViewById(R.id.relateImage3);
        relateImageFrame = findViewById(R.id.relateImageFrame);

        Intent intent = getIntent();

        Long observationId = (Long) intent.getSerializableExtra("observationId"); //전 페이지에서 받아온 contentId

        postId = (Long) intent.getSerializableExtra("postId");


        Call<Observation> call1 = RetrofitClient.getApiService().getObservation(observationId);
        call1.enqueue(new Callback<Observation>() {
            @Override
            public void onResponse(Call<Observation> call, Response<Observation> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "관측지 호출 성공");
                    observation = response.body();

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

                    LinearLayout to_light_btn = findViewById(R.id.obs_move_light);
                    to_light_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), WeatherActivity2.class);
                            startActivity(intent);
                        }
                    });

                    if (observation.getNature()) {
                        //자연관측지일 경우 레이아웃 구성
                        RelativeLayout nature_layout = findViewById(R.id.obs_fornature_layout);
                        nature_layout.setVisibility(View.VISIBLE);
                        LinearLayout operating_layout = findViewById(R.id.obs_foroperating_layout);
                        operating_layout.setVisibility(View.GONE);

                        //전화번호, 홈페이지 설정
                        setInquiry(true);
                        setReserve(true);

                        TextView nature_parking = findViewById(R.id.obs_nature_parking_txt);
                        nature_parking.setText(observation.getParking());
                        TextView nature_address = findViewById(R.id.obs_nature_address_txt);
                        nature_address.setText(observation.getAddress());
                        TextView nature_guide = findViewById(R.id.obs_nature_guide_txt);
                        nature_guide.setText(Html.fromHtml(observation.getGuide(), HtmlCompat.FROM_HTML_MODE_LEGACY));

                    } else {
                        //운영관측지일 경우 레이아웃 구성

                        //홈페이지, 전화 연결
                        setInquiry(false);
                        setReserve(false);

                        TextView address = findViewById(R.id.obs_address_txt);
                        address.setText(observation.getAddress());
                        TextView operatinghour = findViewById(R.id.obs_operatinghour_txt);
                        operatinghour.setText(Html.fromHtml(observation.getOperatingHour(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                        TextView closedday = findViewById(R.id.obs_closedday_txt);
                        closedday.setText(observation.getClosedDay());

                        //이용요금 어댑터 설정
                        initFeeAdapter(observationId);

                        TextView guide = findViewById(R.id.obs_guide_txt);
                        guide.setText(Html.fromHtml(observation.getGuide(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                        TextView parking = findViewById(R.id.obs_parking_txt);
                        parking.setText(observation.getParking());

                    }


                    //해쉬태그 리사이클러 설정
                    initHashtagRecycler();

                    Call<List<String>> call2 = RetrofitClient.getApiService().getObserveHashTags(observationId);
                    call2.enqueue(new Callback<List<String>>() {

                        @Override
                        public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "관측지 해쉬태그 호출 성공");
                                observeHashTags = response.body();
                                balloonObject.setHashtags(observeHashTags); //지도에 넣을 bundle
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

                    //지도버튼 설정
                    setKakaomap();

                    //코스설정
                    setCourse();


                } else {
                    Log.e(TAG, "관측지 호출 실패");
                }
            }

            @Override
            public void onFailure(Call<Observation> call, Throwable t) {
                Log.e(TAG, "연결실패" + t.getMessage());
            }

        });

        //찜버튼 설정
        LinearLayout save_btn = findViewById(R.id.obs_save_btn);
        ImageView save_img = findViewById(R.id.obs_save_img);

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

        //이미 찜한건지 확인
        Call<Boolean> call0 = com.starrynight.tourapiproject.myPage.myPageRetrofit.RetrofitClient.getApiService().isThereMyWish(userId, observationId, 0);
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
                    Call<Void> call = com.starrynight.tourapiproject.myPage.myPageRetrofit.RetrofitClient.getApiService().createMyWish(userId, observationId, 0);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                //버튼 디자인 바뀌게 구현하기
                                isWish = true;
                                save_img.setImageResource(R.drawable.bookmark);
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
                    Call<Void> call = com.starrynight.tourapiproject.myPage.myPageRetrofit.RetrofitClient.getApiService().deleteMyWish(userId, observationId, 0);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                isWish = false;
                                save_img.setImageResource(R.drawable.bookmark_non);
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
        ImageButton back_btn = findViewById(R.id.obs_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView postwrite_btn = findViewById(R.id.writePost_btn);
        postwrite_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), PostWriteActivity.class);
                startActivity(intent1);
            }
        });

        //게시물 이미지 가져오기
        Call<List<PostImage>> call = com.starrynight.tourapiproject.postPage.postRetrofit.RetrofitClient.getApiService().getRelatePostImageList(observationId);
        call.enqueue(new Callback<List<PostImage>>() {
            @Override
            public void onResponse(Call<List<PostImage>> call, Response<List<PostImage>> response) {
                if (response.isSuccessful()) {
                    Log.d("relatePostImage", "관련 게시물 이미지 업로드");
                    List<PostImage> relateImageList = response.body();
                    for (int i = 0; i < relateImageList.size(); i++) {
                        relatefilename[i] = relateImageList.get(i).getImageName();
                        if (relateImageList.size() > 4) {
                            break;
                        }
                    }
                    if (relatefilename[0] != null) {
                        Glide.with(getApplicationContext())
                                .load("https://starry-night.s3.ap-northeast-2.amazonaws.com/postImage/" + relatefilename[0])
                                .into(relateImage1);
                    }
                    if (relatefilename[1] != null) {
                        relateImage2.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext())
                                .load("https://starry-night.s3.ap-northeast-2.amazonaws.com/postImage/" + relatefilename[1])
                                .into(relateImage2);
                    }
                    if (relatefilename[2] != null) {
                        relateImageFrame.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext())
                                .load("https://starry-night.s3.ap-northeast-2.amazonaws.com/postImage/" + relatefilename[2])
                                .into(relateImage3);
                    }
                    relateImage1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!relateImageList.isEmpty()) {
                                Intent intent01 = new Intent(getApplicationContext(), PostActivity.class);
                                intent01.putExtra("postId", relateImageList.get(0).getPostId());
                                startActivity(intent01);
                            }
                        }
                    });
                    relateImage2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent2 = new Intent(getApplicationContext(), PostActivity.class);
                            intent2.putExtra("postId", relateImageList.get(1).getPostId());
                            startActivity(intent2);
                        }
                    });
                    relateImage3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent2 = new Intent(getApplicationContext(), MoreObservationActivity.class);
                            intent2.putExtra("observationId", observationId);
                            startActivity(intent2);
                        }
                    });
                } else {
                    Log.d("relatePostImage", "관련 게시물 이미지 업로드 실패");
                }
            }

            @Override
            public void onFailure(Call<List<PostImage>> call, Throwable t) {
                Log.d("relatePostImage", "관련 게시물 이미지 업로드 인터넷 오류");
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
        recyclerHashTagAdapter = new RecyclerHashTagAdapter();
        hashTagsrecyclerView.setAdapter(recyclerHashTagAdapter);
    }

    private void initFeeAdapter(Long observationId) {
        //이용요금 레이아웃 어댑터 연결
        observationFeeAdapter = new ObservationFeeAdapter();
        RecyclerView feeView = findViewById(R.id.obs_entrancefee_layout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        feeView.setLayoutManager(linearLayoutManager);
        ObservationFeeDecoration spaceDecoration = new ObservationFeeDecoration(8);
        feeView.addItemDecoration(spaceDecoration);
        feeView.setAdapter(observationFeeAdapter);

        // 이용요금 더보기 버튼 설정
        LinearLayout moreFeeBtn = findViewById(R.id.obs_fee_more_btn);
        ImageView moreFeeImg = findViewById(R.id.obs_fee_more_img);
        TextView moreFeeTxt = findViewById(R.id.obs_fee_more_txt);

        moreFeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (observationFeeAdapter.getItemCount()<=3) {
                    //더보기 접혀있을 때

                }else{
                    //더보기 열여있을 때

                }
            }
        });

        Call<List<ObserveFee>> call4 = RetrofitClient.getApiService().getObserveFeeList(observationId);
        call4.enqueue(new Callback<List<ObserveFee>>() {

            @Override
            public void onResponse(Call<List<ObserveFee>> call, Response<List<ObserveFee>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "관측지 이용요금 호출 성공");
                    obs_fee_list = response.body();

                    for (ObserveFee p : obs_fee_list) {
                        ObservationFeeItem item = new ObservationFeeItem();
                        item.setEntranceFee(p.getEntranceFee());
                        item.setFeeName(p.getFeeName());

                        observationFeeAdapter.addItem(item);
                    }

                    observationFeeAdapter.notifyDataSetChanged();

                } else {
                    Log.e(TAG, "관측지 이용요금 호출 실패");
                }
            }

            @Override
            public void onFailure(Call<List<ObserveFee>> call, Throwable t) {
                Log.e(TAG, "연결실패" + t.getMessage());
            }
        });

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

    //코스관련 텍스트, relative view 설정
    private void setCourse(){

        ImageView course_img = findViewById(R.id.obs_course_image);
        Call<List<String>> call6 = RetrofitClient.getApiService().getCourseNameList(observation.getObservationId());
        call6.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    //코스 인디케이터에 넣을 코스 이름 받아오기
                    List<String> course_name_list = response.body();
                    setCourseTxt(course_name_list);

                    Call<List<CourseTouristPoint>> call5 = RetrofitClient.getApiService().getCourseTouristPointList(observation.getObservationId());
                    call5.enqueue(new Callback<List<CourseTouristPoint>>() {
                        @Override
                        public void onResponse(Call<List<CourseTouristPoint>> call, Response<List<CourseTouristPoint>> response) {
                            if (response.isSuccessful()) {
                                //코스 viewpager에 적용할 관광지 정보 가져오기
                                if (response != null) {
                                    Log.d(TAG, "관측지 코스 관광지 호출 성공");
                                    List<CourseTouristPoint> touristPointList = response.body();

                                    setCourseRecycler(touristPointList);
                                }

                            } else {
                                Log.e(TAG, "관측지 코스 호출 실패");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<CourseTouristPoint>> call, Throwable t) {
                            Log.e(TAG, "관측지 코스 연결실패");
                        }
                    });
                } else {
                    Log.e(TAG, "관측지 코스이름 호출 실패");
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e(TAG, "관측지 코스 이름 연결 실패");
            }
        });
    }

    //코스 리사이클러 설정
    private void setCourseRecycler(List<CourseTouristPoint> courseTouristPoints){
        ObservationCourseAdapter observationCourseAdapter = new ObservationCourseAdapter(ObservationsiteActivity.this);

        CourseDividerDecorator dividerItemDecoration = new CourseDividerDecorator(mContext.getDrawable(R.drawable.observation__course_divider));
        RecyclerView courseRecyclerView = findViewById(R.id.obs_course_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        courseRecyclerView.setLayoutManager(linearLayoutManager);
        courseRecyclerView.addItemDecoration(dividerItemDecoration);
        courseRecyclerView.setAdapter(observationCourseAdapter);


        for (int i = 0; i < courseTouristPoints.size(); i++) {
            CourseTouristPoint p = courseTouristPoints.get(i);
            String name = Integer.toString(i+1);
            name=name + ". "+ p.getTitle();

            ObservationCourseItem item = new ObservationCourseItem();
            item.setCategory(p.getCat3Name());
            item.setAddress(p.getAddr());
            item.setImage(p.getFirstImage());
            item.setName(name);
            item.setOverview(p.getOverview());

            observationCourseAdapter.addItem(item);
        }
        observationCourseAdapter.notifyDataSetChanged();
    }

    //코스 텍스트 설정
    private void setCourseTxt(List<String> courseNameList){
        TextView course_text = findViewById(R.id.obs_course_text);

        String course = "";
        for (int i = 0; i < courseNameList.size(); i++) {
            course += courseNameList.get(i);
            if (i < courseNameList.size() - 1) {
                course += " > ";
            }
        }
        course_text.setText(course);
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
                        Intent homepageurl = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+observation.getLink()));
                        startActivity(homepageurl);
                    } catch (Exception e) {
                        Log.e(TAG, "홈페이지 이동 불가");
                    }
                }
            });
        }
    }

    //카카오맵 버튼 설정
    private void setKakaomap(){
        TextView map_btn = findViewById(R.id.obs_location_btn);
        String url = "kakaomap://look?p=" + observation.getLatitude() + "," + observation.getLongitude();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    String url2 = "market://details?id=net.daum.android.map";
                    Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url2));
                    startActivity(intent2);
                }

            }
        });
    }

    private void setReserve(boolean is_nature){
        TextView reserve_btn = findViewById(R.id.obs_reserve);
        if(is_nature){
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
                        Intent homepageurl = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+observation.getLink()));
                        startActivity(homepageurl);
                    } catch (Exception e) {
                        Log.e(TAG, "홈페이지 이동 불가");
                    }
                }
            });
        }


    }

}