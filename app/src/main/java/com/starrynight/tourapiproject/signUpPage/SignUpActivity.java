package com.starrynight.tourapiproject.signUpPage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.Gender;
import com.kakao.sdk.user.model.Profile;
import com.starrynight.tourapiproject.MainActivity;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.signUpPage.signUpRetrofit.KakaoUserParams;
import com.starrynight.tourapiproject.signUpPage.signUpRetrofit.RetrofitClient;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author : sein
 * @version : 1.0
 * <p>
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2022-09-07     sein        주석 생성
 * @className : SignUpActivity.java
 * @description : 회원가입 페이지 입니다.
 * @modification : 2022-09-07(sein) 수정
 * @date : 2022-09-07
 */
public class SignUpActivity extends AppCompatActivity {

    private static final String TAG0 = "SignUpActivity";
    private static final String TAG2 = "KakaoLoginApi";
    String[] WRITE_PERMISSION = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String[] READ_PERMISSION = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    String[] INTERNET_PERMISSION = new String[]{Manifest.permission.INTERNET};
    int PERMISSIONS_REQUEST_CODE = 100;
    KakaoUserParams kakaoUserParams = new KakaoUserParams();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //로그인 버튼
        Button logIn = findViewById(R.id.logIn);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((EditText) (findViewById(R.id.logInEmail))).getText().toString();
                String password = ((EditText) (findViewById(R.id.logInPwd))).getText().toString();
                //로그인을 위한 get api
                Call<Long> call = RetrofitClient.getApiService().logIn(email, password);
                call.enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                        if (response.isSuccessful()) {
                            Long result = response.body();
                            if (result == -1L) {
                                Toast.makeText(getApplicationContext(), "로그인 정보가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                            } else if (result == -2L) {
                                Toast.makeText(getApplicationContext(), "카카오 회원은 아래의 카카오 로그인을 이용해주세요.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG0, "로그인 성공");

                                //앱 내부 저장소에 userId란 이름으로 사용자 id 저장
                                String fileName = "userId";
                                String userId = result.toString();
                                Log.d(TAG0, "userId " + userId);
                                try {
                                    FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                                    fos.write(userId.getBytes());
                                    fos.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                //메인 페이지로 이동
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                                //권한 설정
                                int permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                                int permission3 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);//denied면 -1

                                Log.d("test", "onClick: location clicked");
                                if (permission == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED && permission3 == PackageManager.PERMISSION_GRANTED) {
                                    Log.d("MyTag", "읽기,쓰기,인터넷 권한이 있습니다.");

                                } else if (permission == PackageManager.PERMISSION_DENIED) {
                                    Log.d("test", "permission denied");
                                    Toast.makeText(getApplicationContext(), "쓰기권한이 없습니다.", Toast.LENGTH_SHORT).show();
                                    ActivityCompat.requestPermissions(SignUpActivity.this, WRITE_PERMISSION, PERMISSIONS_REQUEST_CODE);
                                    ActivityCompat.requestPermissions(SignUpActivity.this, READ_PERMISSION, PERMISSIONS_REQUEST_CODE);
                                    ActivityCompat.requestPermissions(SignUpActivity.this, INTERNET_PERMISSION, PERMISSIONS_REQUEST_CODE);
                                }
                                finish();

                            }
                        } else {
                            Log.e(TAG0, response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {
                        Log.e("연결실패", t.getMessage());
                    }
                });
            }
        });

        //이메일 찾기 버튼
        TextView findEmail = findViewById(R.id.findEmail);
        findEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, FindEmailActivity.class);
                startActivity(intent);
            }
        });

        //비밀번호 찾기 버튼
        TextView findPwd = findViewById(R.id.findPwd);
        findPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });


        //일반 회원가입
        Button generalSignUp = findViewById(R.id.generalSignUp);
        generalSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, GeneralSingUpActivity.class);
                startActivity(intent);
            }
        });

        //카카오 회원가입

        ImageButton kakaoSignUp = findViewById(R.id.kakaoSignUp);

        kakaoSignUp.setOnClickListener(view -> {
            if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(SignUpActivity.this)) {
                // 카카오톡 있음->카카오톡으로 로그인
                Log.d("KakaoLogin", "onClick: 카카오톡 있음");

                kakaologin();
            } else {
                //카카오 계정으로 로그인
                Log.d("KakaoLogin", "카카오 계정으로 로그인");
                //카카오 로그인 창 뜸
                kakaoAccountLogin();
//                KakaoLoginAsyncTask task = new KakaoLoginAsyncTask(SignUpActivity.this);
//                task.execute();
            }
        });
    }

    private void kakaologin() {
        // 카카오톡으로 로그인
        UserApiClient.getInstance().loginWithKakaoTalk(SignUpActivity.this, (oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG2, "카카오톡 로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG2, "카카오톡 로그인 성공");
                getKakaoInfo();
            }
            return null;
        });
    }

    private void kakaoAccountLogin() {
        // 카카오계정으로 로그인
        UserApiClient.getInstance().loginWithKakaoAccount(SignUpActivity.this, (oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG2, "카카오계정 로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG2, "카카오계정 로그인 성공(토큰)");
                getKakaoInfo();
            }
            return null;
        });
    }

    private void getKakaoInfo() {
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e(TAG2, "사용자 정보 요청 실패", meError);
            } else {
                Log.i(TAG2,"사용자 정보요청 성공");

                String id = String.valueOf(user.getId());
                Account kakaoAccount = user.getKakaoAccount();
                if (kakaoAccount != null) {

                    // 이메일
                    String email = kakaoAccount.getEmail();
                    if (email != null) {
                        kakaoUserParams.setEmail(email);
                    } else {
                        Log.d(TAG2, "onSuccess:email null ");
                    }
                    if (kakaoAccount.getAgeRange() != null) {
                        kakaoUserParams.setAgeRange(kakaoAccount.getAgeRange().toString());
                    }
                    if (kakaoAccount.getBirthday() != null) {
                        kakaoUserParams.setBirthDay(kakaoAccount.getBirthday());
                    }
                    if (kakaoAccount.getGender() != null) {
                        if (kakaoAccount.getGender() == Gender.FEMALE)
                            kakaoUserParams.setSex(false);
                        else if (kakaoAccount.getGender() == Gender.MALE)
                            kakaoUserParams.setSex(true);
                    }

                    Profile profile = kakaoAccount.getProfile();
                    if (profile == null) {
                        Log.d(TAG2, "onSuccess:profile null ");
                    } else {
                        kakaoUserParams.setProfileImage(profile.getProfileImageUrl());
                        kakaoUserParams.setNickName(profile.getNickname());
                    }

                    // 프로필
                    Profile _profile = kakaoAccount.getProfile();

                    if (_profile != null) {

                        kakaoUserParams.setNickName(profile.getNickname());
                        kakaoUserParams.setProfileImage(profile.getProfileImageUrl());


                    } else if (kakaoAccount.getProfileNeedsAgreement() == true) {
                        // 동의 요청 후 프로필 정보 획득 가능
                        kakaoUserParams.setNickName(profile.getNickname());
                        kakaoUserParams.setProfileImage(profile.getProfileImageUrl());
                    } else {
                        // 프로필 획득 불가
                        Log.d(TAG2, "profile disagree");
                    }

                    signinWithKakaodata();
                }
            }
            return null;
        });
    }

    private void signinWithKakaodata() {
        Call<Boolean> call = RetrofitClient.getApiService().checkDuplicateEmail(kakaoUserParams.getEmail());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Boolean result = response.body();
                    if (result) {
                        Log.d(TAG2, "회원가입 진행");
                        Intent intent = new Intent(SignUpActivity.this, KakaoPhoneAuthActivity.class);
                        intent.putExtra("userParams", kakaoUserParams);
                        startActivity(intent);
                    } else if (!result) {
                        Log.d(TAG2, "회원가입 미진행, 이미가입된 이메일");
                        Call<Long> call2 = RetrofitClient.getApiService().kakaoLogIn(kakaoUserParams.getEmail());
                        call2.enqueue(new Callback<Long>() {
                            @Override
                            public void onResponse(Call<Long> call, Response<Long> response) {
                                //앱 내부 저장소에 userId란 이름으로 사용자 id 저장
                                String fileName = "userId";
                                String userId = response.body().toString();
                                Log.d(TAG0, "userId " + userId);
                                try {
                                    FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                                    fos.write(userId.getBytes());
                                    fos.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                //메인 페이지로 이동
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                                //권한 설정
                                int permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                                int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                                int permission3 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);//denied면 -1

                                Log.d("test", "onClick: location clicked");
                                if (permission == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED && permission3 == PackageManager.PERMISSION_GRANTED) {
                                    Log.d("MyTag", "읽기,쓰기,인터넷 권한이 있습니다.");

                                } else if (permission == PackageManager.PERMISSION_DENIED) {
                                    Log.d("test", "permission denied");
                                    Toast.makeText(getApplicationContext(), "쓰기권한이 없습니다.", Toast.LENGTH_SHORT).show();
                                    ActivityCompat.requestPermissions(SignUpActivity.this, WRITE_PERMISSION, PERMISSIONS_REQUEST_CODE);
                                    ActivityCompat.requestPermissions(SignUpActivity.this, READ_PERMISSION, PERMISSIONS_REQUEST_CODE);
                                    ActivityCompat.requestPermissions(SignUpActivity.this, INTERNET_PERMISSION, PERMISSIONS_REQUEST_CODE);
                                }
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Long> call, Throwable t) {
                                Log.e(TAG0, "카카오 로그인 사용자 없음");
                            }
                        });

                    }
                } else {
                    Log.e(TAG2, "이메일 중복 체크 실패");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });
    }


//    private class KakaoLoginAsyncTask extends AsyncTask<String, Long, Boolean> {
//        private Context mContext = null;
//        ProgressBar progressBar = new ProgressBar(SignUpActivity.this, null, R.attr.progressBarStyle);
//        RelativeLayout.LayoutParams p;
//
//        {
//            p = new RelativeLayout.LayoutParams(300, 300);
//            p.addRule(RelativeLayout.CENTER_IN_PARENT);
//        }
//
//        ConstraintLayout layout;
//
//        {
//            layout = findViewById(R.id.activity_login);
//            layout.addView(progressBar,p);
//            progressBar.setVisibility(View.GONE);
//        }
//
//        public KakaoLoginAsyncTask(Context context) {
//            mContext = context.getApplicationContext();
//        }
//
//        @Override
//        protected void onPreExecute() {
//            progressBar.setVisibility(View.VISIBLE);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Boolean doInBackground(String... strings) {
//
//            while (!Session.getCurrentSession().checkAndImplicitOpen()) {
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            return (Session.getCurrentSession().checkAndImplicitOpen());
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            progressBar.setVisibility(View.GONE);
//            sessionCallback.requestMe();
//        }
//    }

}