package com.starrynight.tourapiproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.starrynight.tourapiproject.signUpPage.SignUpActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "RemoteConfig";
    Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Handler handler = new Handler();


        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults); // 값을 받아오지 못했을 경우 대비하여 디폴트값 정의
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        String installed_version ="";
                        String firebase_version ="0.0.0";
                        boolean versionPass = true;

                        if (task.isSuccessful()) {
                            installed_version = BuildConfig.VERSION_NAME;
                            firebase_version = mFirebaseRemoteConfig.getString("tour_api_project_v_1_3");
                            if(installed_version.equals(firebase_version)){
                                versionPass = true;
                            }else{
                                versionPass = false;
                            }
                            if (!versionPass) {
                                Log.d("version_check_success ", installed_version +" | "+ firebase_version);
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle("업데이트 안내");
                                builder.setMessage("이전 버전을 사용하고 계시군요!\n보다 좋은 서비스 경험을 위해 업데이트를 부탁드려요.");
                                builder.setPositiveButton("업데이트",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                String url = "https://play.google.com/store/apps/details?id=com.starrynight.tourapiproject";
                                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                startActivity(i);
                                                System.runFinalization();
                                                System.exit(0);
                                            }
                                        });
                                builder.setNegativeButton("나가기",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                System.runFinalization();
                                                System.exit(0);
                                            }
                                        });
                                builder.show();
                            }
                            else{
                                Log.d("version_check_success ", installed_version +" | "+ firebase_version);
                                if (getLogin()) {
                                    Log.d("Login", "유저 정보 있음" + userId);
                                    handler.postDelayed(() -> {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.putExtra("userId", userId);
                                        startActivity(intent);
                                        finish();
                                    }, 2000);
                                } else {
                                    handler.postDelayed(() -> {
                                        Log.d("Login", "유저 정보 없음");
                                        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }, 2000);
                                }
                            }
                        } else {
                            Log.d(TAG, "Config params updated failed");
                        }
                    }
                });






    }

    public boolean getLogin() {
        String fileName = "userId";
        try {
            FileInputStream fis = openFileInput(fileName);
            String line = new BufferedReader(new InputStreamReader(fis)).readLine();
            userId = Long.parseLong(line);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userId != null;
    }
}