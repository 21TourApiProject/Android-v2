package com.starrynight.tourapiproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.starrynight.tourapiproject.alarmPage.AlarmActivity;
import com.starrynight.tourapiproject.myPage.myPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.starPage.TonightSkyFragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author : jinhyeok
 * @version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2022-09-02      jinhyeok       주석 수정
 * @className : MainActivity
 * @description : 메인 페이지 입니다.
 * @modification : 2022-09-02 (jinhyeok) 주석 수정
 * @date : 2022-09-02
 */
public class MainActivity extends AppCompatActivity {

    public static Context mContext;
    private static final String TAG = "FcmToken";

    MainFragment mainFragment;
    SearchFragment searchFragment;
    TonightSkyFragment tonightSkyFragment;
    PersonFragment personFragment;
    ReviewFragment reviewFragment;
    View bottom;
    BottomNavigationView bottomNavigationView;
    private long backKeyPressTime = 0;
    String[] WRITE_PERMISSION = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] READ_PERMISSION = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    String[] INTERNET_PERMISSION = new String[]{Manifest.permission.INTERNET};
    String[] PERMISSION = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.POST_NOTIFICATIONS
    };
    int PERMISSIONS_REQUEST_CODE = 100;
    Long userId;

    Fragment map, searchResult, filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mainFragment = new MainFragment();
        searchFragment = new SearchFragment();
        tonightSkyFragment = new TonightSkyFragment();
        personFragment = new PersonFragment();
        reviewFragment = new ReviewFragment();

        //권한 설정
        int permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permission3 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
        int permission4 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int permission5 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
//        int permission6 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS);

        Log.d("test", "onClick: location clicked");
        if (permission == PackageManager.PERMISSION_GRANTED
                && permission2 == PackageManager.PERMISSION_GRANTED
                && permission3 == PackageManager.PERMISSION_GRANTED
                && permission4 == PackageManager.PERMISSION_GRANTED
                && permission5 == PackageManager.PERMISSION_GRANTED
//                &&permission6 == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("MyTag", "읽기,쓰기,인터넷 권한이 있습니다.");

        } else {
            Log.d("test", "permission denied");
//            Toast.makeText(getApplicationContext(), "쓰기권한이 없습니다.", Toast.LENGTH_SHORT).show();
//            ActivityCompat.requestPermissions(MainActivity.this, WRITE_PERMISSION, PERMISSIONS_REQUEST_CODE);
//            ActivityCompat.requestPermissions(MainActivity.this, READ_PERMISSION, PERMISSIONS_REQUEST_CODE);
//            ActivityCompat.requestPermissions(MainActivity.this, INTERNET_PERMISSION, PERMISSIONS_REQUEST_CODE);
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSION, PERMISSIONS_REQUEST_CODE);
        }

        String fileName = "userId"; // 유저 아이디 가져오기
        try {
            FileInputStream fis = openFileInput(fileName);
            String line = new BufferedReader(new InputStreamReader(fis)).readLine();
            userId = Long.parseLong(line);
            fis.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        getToken();
        //메인 페이지 초기화 상태
        getSupportFragmentManager().beginTransaction().replace(R.id.main_view, mainFragment).commitAllowingStateLoss();

        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        bottom = findViewById(R.id.bottom_nav_view);
        //바텀 네비게이션 버튼 클릭 시 이벤트
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_main:
                        if (mainFragment == null) {
                            mainFragment = new MainFragment();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_view, mainFragment).commitAllowingStateLoss();
                        return true;
                    case R.id.navigation_observation:
                        if (searchFragment == null) {
                            searchFragment = new SearchFragment();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_view, searchFragment).commitAllowingStateLoss();
                        return true;

                    case R.id.navigation_star:
                        if (tonightSkyFragment == null) {
                            tonightSkyFragment = new TonightSkyFragment();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_view, tonightSkyFragment).commitAllowingStateLoss();
                        return true;

                    case R.id.navigation_review:
                        if (reviewFragment == null) {
                            reviewFragment = new ReviewFragment();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_view, reviewFragment).commitAllowingStateLoss();
                        return true;

                    case R.id.navigation_person:
                        if (personFragment == null) {
                            personFragment = new PersonFragment();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_view, personFragment).commitAllowingStateLoss();
                        return true;
                }
                return false;
            }
        });

        if(getIntent()!=null&&getIntent().getStringExtra("type")!=null){
            if(getIntent().getStringExtra("type").equals("alarm")){
                Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        }

    }

    //뒤로가기 버튼 클릭 시
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_view);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (bottomNavigationView.getSelectedItemId() == R.id.navigation_star) {
            if (tonightSkyFragment != null)
                getSupportFragmentManager().beginTransaction().hide(tonightSkyFragment).commit();
            bottomNavigationView.setSelectedItemId(R.id.navigation_main);
            if (mainFragment != null)
                getSupportFragmentManager().beginTransaction().show(mainFragment).commit();
        } else
            if (bottomNavigationView.getSelectedItemId() == R.id.navigation_main) {
            if (System.currentTimeMillis() > backKeyPressTime + 2000) {
                backKeyPressTime = System.currentTimeMillis();
                Toast.makeText(this, "한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressTime + 2000) {
                finishAffinity();
                System.runFinalization();
                System.exit(0);
//                finish();
            }
        } else if (bottomNavigationView.getSelectedItemId() == R.id.navigation_person || bottomNavigationView.getSelectedItemId() == R.id.navigation_observation|| bottomNavigationView.getSelectedItemId() == R.id.navigation_review) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_main);
        } else {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            } else {
                super.onBackPressed();
//                finish();
            }
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_view, fragment).commitAllowingStateLoss();
    }

    public void getToken(){
        //토큰값을 받아옵니다.
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "getToken 실행 안됨");
                            return;
                        }
                        if(task.getResult()!=null){
                            String token = task.getResult();
                            SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
                            SharedPreferences.Editor editor=pref.edit();
                            editor.putString("token",token);
                            editor.commit();
                            Call<Boolean> call = RetrofitClient.getApiService().updateFcmToken(userId,token);
                            call.enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    if(response.isSuccessful()){
                                        Boolean result = response.body();
                                        if(result){
                                            Log.d(TAG, "기존 토큰값과 동일합니다.");
                                        }else {
                                            Log.d(TAG, "유저 토큰값 업데이트 합니다.\n new token:"+token);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {
                                    Log.e(TAG, "fcm 토큰 인터넷 오류");
                                }
                            });
                        }
                    }
                });
    }

}