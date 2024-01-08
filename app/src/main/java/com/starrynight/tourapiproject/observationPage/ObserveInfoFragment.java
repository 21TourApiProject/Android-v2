package com.starrynight.tourapiproject.observationPage;

import static com.starrynight.tourapiproject.MainActivity.mContext;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.observationPage.course.CourseDividerDecorator;
import com.starrynight.tourapiproject.observationPage.course.ObservationCourseAdapter;
import com.starrynight.tourapiproject.observationPage.course.ObservationCourseItem;
import com.starrynight.tourapiproject.observationPage.fee.ObservationFeeAdapter;
import com.starrynight.tourapiproject.observationPage.fee.ObservationFeeDecoration;
import com.starrynight.tourapiproject.observationPage.fee.ObservationFeeItem;
import com.starrynight.tourapiproject.observationPage.observationPageRetrofit.CourseTouristPoint;
import com.starrynight.tourapiproject.observationPage.observationPageRetrofit.Observation;
import com.starrynight.tourapiproject.observationPage.observationPageRetrofit.ObserveFee;
import com.starrynight.tourapiproject.observationPage.observationPageRetrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ObserveInfoFragment extends Fragment {
    private static final String TAG = "observationInfo fragment";

    private Observation observation;
    Long observationId;

    RelativeLayout nature_layout;
    LinearLayout operating_layout;
    TextView nature_parking;
    TextView nature_address;
    TextView nature_guide;
    TextView address;
    TextView operatinghour;
    TextView closedday;
    TextView guide;
    TextView parking;

    TextView course_text;
    ImageView course_img;
    LinearLayout course;
    RecyclerView courseRecyclerView;

    LinearLayout moreFeeBtn;
    ImageView moreFeeImg;
    TextView moreFeeTxt;
    RecyclerView feeView;

    TextView map_btn;
    TextView nature_map_btn;

    private ObservationFeeAdapter observationFeeAdapter;
    private List<ObserveFee> obs_fee_list;


    public ObserveInfoFragment() {
        // Required empty public constructor
    }

    public ObserveInfoFragment(Observation observation) {
        this.observation = observation;
        this.observationId = observation.getObservationId();
    }


    public static ObserveInfoFragment newInstance(String param1, String param2) {
        ObserveInfoFragment fragment = new ObserveInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_observe_info, container, false);
        nature_layout = v.findViewById(R.id.obs_fornature_layout);
        operating_layout = v.findViewById(R.id.obs_foroperating_layout);
        nature_parking = v.findViewById(R.id.obs_nature_parking_txt);
        nature_address = v.findViewById(R.id.obs_nature_address_txt);
        nature_guide = v.findViewById(R.id.obs_nature_guide_txt);

        address = v.findViewById(R.id.obs_address_txt);
        operatinghour = v.findViewById(R.id.obs_operatinghour_txt);
        closedday = v.findViewById(R.id.obs_closedday_txt);
        guide = v.findViewById(R.id.obs_guide_txt);
        parking = v.findViewById(R.id.obs_parking_txt);

        course_text = v.findViewById(R.id.obs_course_text);
        course_img = v.findViewById(R.id.obs_course_image);
        course = v.findViewById(R.id.obs_course);
        courseRecyclerView = v.findViewById(R.id.obs_course_recycler);

        moreFeeBtn = v.findViewById(R.id.obs_fee_more_btn);
        moreFeeImg = v.findViewById(R.id.obs_fee_more_img);
        moreFeeTxt = v.findViewById(R.id.obs_fee_more_txt);
        feeView = v.findViewById(R.id.obs_entrancefee_layout);

        map_btn = v.findViewById(R.id.obs_location_btn);
        nature_map_btn = v.findViewById(R.id.obs_nature_location_btn);

        if (observation != null) {
            setInfos();
            //지도버튼 설정
            setKakaomap();

            //코스설정
            setCourse();
        }



        return v;
    }

    private void setInfos() {
        if (observation.getNature()) {
            //자연관측지일 경우 레이아웃 구성
            nature_layout.setVisibility(View.VISIBLE);
            operating_layout.setVisibility(View.GONE);
            nature_parking.setText(observation.getParking());
            nature_address.setText(observation.getAddress());
            nature_guide.setText(Html.fromHtml(observation.getGuide(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        } else {
            //운영관측지일 경우 레이아웃 구성

            address.setText(observation.getAddress());
            operatinghour.setText(Html.fromHtml(observation.getOperatingHour(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            closedday.setText(observation.getClosedDay());

            //이용요금 어댑터 설정
            initFeeAdapter(observationId);

            guide.setText(Html.fromHtml(observation.getGuide(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            parking.setText(observation.getParking());

        }
    }

    private void initFeeAdapter(Long observationId) {
        //이용요금 레이아웃 어댑터 연결
        observationFeeAdapter = new ObservationFeeAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        feeView.setLayoutManager(linearLayoutManager);
        ObservationFeeDecoration spaceDecoration = new ObservationFeeDecoration(8);
        feeView.addItemDecoration(spaceDecoration);
        feeView.setAdapter(observationFeeAdapter);

        // 이용요금 더보기 버튼 설정


        moreFeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (observationFeeAdapter.getItemCount() <= 3) {
                    observationFeeAdapter.showMoreFee();
                    moreFeeImg.setImageResource(R.drawable.observation__fee_close);
                    moreFeeTxt.setText("접기");
                }else{
                    observationFeeAdapter.closeMoreFee();
                    moreFeeImg.setImageResource(R.drawable.observation__fee_more);
                    moreFeeTxt.setText("더보기");
                }
                observationFeeAdapter.notifyDataSetChanged();
            }
        });


        Call<List<ObserveFee>> call4 = RetrofitClient.getApiService().getObserveFeeList(observationId);
        call4.enqueue(new Callback<List<ObserveFee>>() {

            @Override
            public void onResponse(Call<List<ObserveFee>> call, Response<List<ObserveFee>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "관측지 이용요금 호출 성공");
                    obs_fee_list = response.body();

                    if (obs_fee_list.size()<=3) {
                        moreFeeBtn.setVisibility(View.GONE);
                    }

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

    //코스관련 텍스트, relative view 설정
    private void setCourse(){

        try{
            observation.getCourseOrder();
        } catch (NullPointerException e){
            course.setVisibility(View.INVISIBLE);
            return;
        }

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
        ObservationCourseAdapter observationCourseAdapter = new ObservationCourseAdapter(getActivity());

        CourseDividerDecorator dividerItemDecoration = new CourseDividerDecorator(mContext.getDrawable(R.drawable.observation__course_divider));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
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

        String course = "";
        for (int i = 0; i < courseNameList.size(); i++) {
            course += courseNameList.get(i);
            if (i < courseNameList.size() - 1) {
                course += " > ";
            }
        }
        course_text.setText(course);
    }

    //카카오맵 버튼 설정
    private void setKakaomap(){
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

        nature_map_btn.setOnClickListener(new View.OnClickListener() {
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
}