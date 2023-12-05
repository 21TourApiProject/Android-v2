package com.starrynight.tourapiproject.myPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.sdk.user.UserApiClient;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.myPage.myPageRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.signUpPage.SignUpActivity;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @className : LogoutPopActivity.java
 * @description : 로그아웃 팝업 페이지 입니다.
 * @modification : 2022-09-07(sein) 수정
 * @author : sein
 * @date : 2022-09-07
 * @version : 1.0

    ====개정이력(Modification Information)====
        수정일        수정자        수정내용
    -----------------------------------------
      2022-09-07     sein        주석 생성

 */
public class LogoutPopActivity extends AppCompatActivity {

    private static final String TAG = "Logout";
    private static final String FCMTAG = "FcmToken";
    Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.activity_logout_pop);

        Intent intent = getIntent();
        userId = (Long) intent.getSerializableExtra("userId"); //전 페이지에서 받아온 사용자 id
    }

    //로그아웃
    public void logout(View v) {

        Call<Boolean> call = RetrofitClient.getApiService().checkIsKakao(userId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                Log.d(TAG, "카카오 가입자인지 확인");
                Boolean isKakao = response.body();
                if (isKakao != null) {
                    if (isKakao) {
                        UserApiClient.getInstance().logout(error->{
                            if(error!=null){
                                Log.e("KakaoLogout","로그아웃 실패 : ",error);
                                File dir = getFilesDir();
                                File file = new File(dir, "userId");
                                boolean deleted = file.delete();
                                Intent intent = new Intent(LogoutPopActivity.this, SignUpActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else{
                                Log.i("KakaoLogout","로그아웃 완료");
                                File dir = getFilesDir();
                                File file = new File(dir, "userId");
                                boolean deleted = file.delete();
                                //fcm 토큰 삭제 관련 코드
                                SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE); //임시 메모리에 있는 데이터들 삭제
                                SharedPreferences.Editor editor=pref.edit();
                                editor.clear();
                                editor.apply();
                                Call<Void> deleteCall = RetrofitClient.getApiService().deleteFcmToken(userId); //fcm delete api 호출
                                deleteCall.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if(response.isSuccessful()){
                                            Log.d(FCMTAG,"유저 fcmToken 삭제 성공");
                                        }else{
                                            Log.d(FCMTAG,"유저 fcmToken 삭제 실패");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.e(FCMTAG,"유저 fcmToken 삭제 인터넷 연결 오류");
                                    }
                                });
                                Intent intent = new Intent(LogoutPopActivity.this, SignUpActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                            return null;
                        });
                    } else {
                        File dir = getFilesDir();
                        File file = new File(dir, "userId");
                        boolean deleted = file.delete();
                        SharedPreferences pref=getSharedPreferences("pref",MODE_PRIVATE);
                        SharedPreferences.Editor editor=pref.edit();
                        editor.clear();
                        editor.apply();
                        Call<Void> deleteCall2 = RetrofitClient.getApiService().deleteFcmToken(userId); //fcm delete api 호출
                        deleteCall2.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful()){
                                    Log.d(FCMTAG,"유저 fcmToken 삭제 성공");
                                }else{
                                    Log.d(FCMTAG,"유저 fcmToken 삭제 실패");
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e(FCMTAG,"유저 fcmToken 삭제 인터넷 연결 오류");
                            }
                        });
                        Intent intent = new Intent(LogoutPopActivity.this, SignUpActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else {
                    Log.e(TAG, "사용자 타입설정 안됨");
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e(TAG, "연결실패");
            }
        });


    }

    //팝업 닫기
    public void closeLogout(View v) {
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);
        finish();
    }
}
