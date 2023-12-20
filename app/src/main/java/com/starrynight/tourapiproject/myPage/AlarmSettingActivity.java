package com.starrynight.tourapiproject.myPage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.starrynight.tourapiproject.R;

public class AlarmSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch switchComment = findViewById(R.id.switch_comment);
        SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        boolean isCommentDenied = pref.getBoolean("isCommentDenied",true);
        switchComment.setChecked(isCommentDenied);
        Log.d("isDenied","수신여부: "+isCommentDenied);
        switchComment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                editor.putBoolean("isCommentDenied", isChecked);
                editor.apply();
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch switchNotice = findViewById(R.id.switch_notice);
        boolean isNoticeDenied = pref.getBoolean("isNoticeDenied",true);
        switchNotice.setChecked(isNoticeDenied);
        Log.d("isNoticeDenied","수신여부: "+isNoticeDenied);
        switchNotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                editor.putBoolean("isNoticeDenied", isChecked);
                editor.apply();
            }
        });

        LinearLayout alarmSettingBack = findViewById(R.id.alarm_setting_back);
        alarmSettingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}