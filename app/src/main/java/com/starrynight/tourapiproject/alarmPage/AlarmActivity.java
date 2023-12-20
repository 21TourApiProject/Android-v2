package com.starrynight.tourapiproject.alarmPage;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.myPage.myPageRetrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
* @className : AlarmActivity
* @description : 알림 페이지 입니다.
* @modification : 2022-09-02 (jinhyeok) 주석 수정
* @author : jinhyeok
* @date : 2022-09-02
* @version : 1.0
   ====개정이력(Modification Information)====
  수정일        수정자        수정내용
   -----------------------------------------
   2022-09-02      jinhyeok       주석 수정

 */
public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        List<Alarm> finalAlarmList= new ArrayList<>();
        LinearLayout nothingAlarm = findViewById(R.id.nothingAlarm);
        RecyclerView recyclerView = findViewById(R.id.alarm_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        Long userId=getIntent().getLongExtra("userId",0);

        Call<List<Alarm>> call = RetrofitClient.getApiService().getAllAlarm(userId);
        call.enqueue(new Callback<List<Alarm>>() {
            @Override
            public void onResponse(Call<List<Alarm>> call, Response<List<Alarm>> response) {
                if (response.isSuccessful()) {
                    List<Alarm> alarms = response.body();
                    Log.d("alarm", "알림 업로드 성공");
                    for (int i=0;i<alarms.size();i++){
                        if (alarms.get(i).getAlarmTitle().contains("$")){
                            alarms.get(i).setAlarmTitle(alarms.get(i).getAlarmTitle().substring(1));
                            finalAlarmList.add(alarms.get(i));
                        }
                        else{
                            finalAlarmList.add(alarms.get(i));
                        }
                    }
                    AlarmAdapter adapter = new AlarmAdapter(getApplicationContext(),finalAlarmList);
                    recyclerView.setAdapter(adapter);
                    if(finalAlarmList.isEmpty()){
                        nothingAlarm.setVisibility(View.VISIBLE);
                    }else {
                        nothingAlarm.setVisibility(View.GONE);
                    }
                } else {
                    Log.d("alarm", "알림 업로드 실패");
                }
            }

            @Override
            public void onFailure(Call<List<Alarm>> call, Throwable t) {
                Log.d("alarm", "알림 인터넷 오류");
            }
        });

        LinearLayout back_btn = findViewById(R.id.alarmBack);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}