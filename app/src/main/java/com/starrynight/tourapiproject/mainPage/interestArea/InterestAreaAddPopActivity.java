package com.starrynight.tourapiproject.mainPage.interestArea;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.mainPage.interestArea.interestAreaRetrofit.AddInterestAreaDTO;
import com.starrynight.tourapiproject.mainPage.interestArea.interestAreaRetrofit.InterestAreaRetrofitClient;
import com.starrynight.tourapiproject.weatherPage.SearchLocationItem;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InterestAreaAddPopActivity extends AppCompatActivity {

    private static final String TAG = "InterestAreaAddPop";

    Long userId;
    SearchLocationItem item;
    TextView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_interest_area_add_pop);
        TextView regionName = findViewById(R.id.interest_area_add_region_name);
        close = findViewById(R.id.interest_area_add_close);

        if (getIntent().getSerializableExtra("interestAreaAddIntent") != null) {
            InterestAreaAddIntent intent = (InterestAreaAddIntent) getIntent().getSerializableExtra("interestAreaAddIntent");
            userId = intent.getUserId();
            item = intent.getSearchLocationItem();
            regionName.setText("'" + item.getTitle() + "'을 관심지역으로 등록할까요?");
        }

        close.setOnClickListener(v -> {
            AddInterestAreaDTO addInterestAreaDTO = new AddInterestAreaDTO();
            addInterestAreaDTO.setUserId(userId);
            addInterestAreaDTO.setRegionName(item.getTitle());

            if (Objects.nonNull(item.getObservationId())) {
                addInterestAreaDTO.setRegionType(1);
                addInterestAreaDTO.setRegionId(item.getObservationId());
            }
            if (Objects.nonNull(item.getAreaId())) {
                addInterestAreaDTO.setRegionType(2);
                addInterestAreaDTO.setRegionId(item.getAreaId());
            }

            InterestAreaRetrofitClient.getApiService()
                    .addInterestArea(addInterestAreaDTO)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "관심 지역 추가 성공");
                                Toast.makeText(getApplicationContext(), "관심지역이 추가되었어요", Toast.LENGTH_SHORT).show();
                                setResult(1);
                                finish();
                            } else {
                                Log.e(TAG, "서버 api 호출 실패");
                                setResult(0);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("연결실패", t.getMessage());
                            setResult(0);
                            finish();
                        }
                    });
        });
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
    }
}
