package com.starrynight.tourapiproject.signUpPage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.starrynight.tourapiproject.MainActivity;
import com.starrynight.tourapiproject.R;
import com.starrynight.tourapiproject.signUpPage.signUpRetrofit.RetrofitClient;
import com.starrynight.tourapiproject.signUpPage.signUpRetrofit.UserParams;

import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

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
 * @className : PhoneAuthActivity.java
 * @description :  회원가입 시 전화번호를 인증하는 페이지입니다.
 * @modification : 2022-09-07(sein) 수정
 * @date : 2022-09-07
 */
public class PhoneAuthActivity extends AppCompatActivity implements
        View.OnClickListener {
    private static final String TAG = "PhoneAuth";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private FirebaseAuth mAuth;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private EditText mobilePhoneNumber;
    private TextView phoneGuide; //전화번호 칸 바로 밑에 글칸
    private EditText authCode;
    private TextView sendAuth, resendAuth, authText;

    String testPhoneNum = "+16505553333";

    UserParams userParams;
    private Boolean isSend = false;

    String phoneNumber;
    private Boolean isPhoneEmpty = true; //전화번호이 비어있는지
    private Boolean isNotPhone = false; //올바른 전화번호 형식이 아닌지
    private Boolean isPhoneDuplicate = true; //전화번호이 중복인지

    //    Button phoneAgree;
    private Boolean isPhoneAgree = true;

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
        setContentView(R.layout.activity_phone_auth);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        Intent intent = getIntent();
        userParams = (UserParams) intent.getSerializableExtra("userParams");

        mobilePhoneNumber = findViewById(R.id.mobilePhoneNumber); //전화번호
        phoneGuide = findViewById(R.id.phoneGuide);
        authCode = findViewById(R.id.authCode); //인증코드
        sendAuth = findViewById(R.id.sendAuth); //처음 문자요청
        authText = findViewById(R.id.authText);//인증번호 전송 했습니다. 텍스트
        resendAuth = findViewById(R.id.resendAuth); //재 문자요청
        Button verify = findViewById(R.id.verify); //인증요청

        sendAuth.setOnClickListener(this);
        resendAuth.setOnClickListener(this);
        verify.setOnClickListener(this);

//        TextView pass = findViewById(R.id.pass);
//        pass.setVisibility(View.GONE);

        //뒤로 가기
        LinearLayout authBack = findViewById(R.id.authBack);
        authBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //휴대폰 정보 수집 동의
//        phoneAgree = findViewById(R.id.phoneAgree);
//        phoneAgree.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isPhoneAgree){
//                    phoneAgree.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.signup_agree_non));
//                    isPhoneAgree = false;
//                } else{
//                    phoneAgree.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.signup_agree));
//                    isPhoneAgree = true;
//                }
//            }
//        });


        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);

                mVerificationInProgress = false;
                //signInWithPhoneAuthCredential(credential); (주의)이거 살리면 오류남
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mobilePhoneNumber.setError("전화번호 형식이 올바르지 않습니다.");

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };


        //전화번호 칸에 글씨가 입력됨에 따라 실시간으로 phoneGuide 뜨게
        mobilePhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력란에 변화가 있을 때
                showPhoneGuide(s);
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때
                showPhoneGuide(arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
    }

    private void showPhoneGuide(CharSequence s) {
        String text = s.toString();

        if (text.isEmpty()) {
            phoneGuide.setText("전화번호을 입력해주세요.");
            isPhoneEmpty = true;
        } else if (!isCorrectPhoneRule(text)) {
            phoneGuide.setText("전화번호 형식이 올바르지 않습니다.");
            isPhoneEmpty = false;
            isNotPhone = true;
        } else {
            phoneGuide.setText("");

            //전화번호이 중복인지 아닌지를 위한 get api
            Call<Boolean> call = RetrofitClient.getApiService().checkDuplicateMobilePhoneNumber(text);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        Boolean result = response.body();
                        if (result) {
                            phoneGuide.setText("사용가능한 전화번호입니다.");
                            phoneNumber = text;
                            isPhoneEmpty = false;
                            isNotPhone = false;
                            isPhoneDuplicate = false;
                        } else if (!result) {
                            phoneGuide.setText("이미 가입된 전화번호입니다.");
                            isPhoneEmpty = false;
                            isNotPhone = false;
                            isPhoneDuplicate = true;
                        }
                    } else {
                        Log.e(TAG, "중복 체크 실패");
                        phoneGuide.setText("오류가 발생했습니다. 다시 시도해주세요.");
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.e("연결실패", t.getMessage());
                    phoneGuide.setText("오류가 발생했습니다. 다시 시도해주세요.");
                }
            });
        }
    }

    //전화번호가 11자리가 아니면 형식 틀린것으로 간주
    private boolean isCorrectPhoneRule(String text) {
        return text.length() == 11;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mVerificationInProgress) {
            startPhoneNumberVerification(mobilePhoneNumber.getText().toString());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(120L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(120L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "인증 성공"); //인증 성공하면

                        //회원가입을 위한 post api
                        if (isPhoneAgree)
                            userParams.setMobilePhoneNumber(mobilePhoneNumber.getText().toString());
                        else
                            userParams.setMobilePhoneNumber(null);

                        RetrofitClient.getApiService().signUp(userParams).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.isSuccessful()) {
                                    Log.d(TAG, "회원가입 성공");
                                    signOut();

                                    //앱 내부 저장소에 userId란 이름으로 사용자 id 저장
                                    String fileName = "userId";
                                    String userId = response.body();
                                    try {
                                        FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                                        fos.write(userId.getBytes());
                                        fos.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //Fcm 토큰 생성
                                    Call<Void> fcmCall = com.starrynight.tourapiproject.myPage.myPageRetrofit.RetrofitClient.getApiService().createFcmToken(Long.parseLong(userId));
                                    fcmCall.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            if(response.isSuccessful()){
                                                Log.d(TAG,"fcm 토큰 생성 성공");
                                            }else{
                                                Log.e(TAG,"fcm 토큰 생성 실패");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Log.e(TAG,"fcm 토큰 생성 인터넷 오류");
                                        }
                                    });

                                    Intent intent = new Intent(PhoneAuthActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); //액티비티 스택제거
                                    startActivity(intent);
                                } else {
                                    Log.e(TAG, "회원가입 실패");
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.e("연결실패", t.getMessage());
                            }
                        });
                    } else {
                        Log.w(TAG, "인증 실패", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(getApplicationContext(), "올바르지 않은 인증번호입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
    }

    //국제 번호 붙여주는 함수
    private String changePhoneNumber(String phoneNumber) {
        return "+82" + phoneNumber.substring(1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendAuth:
                if (isPhoneEmpty) {
                    Toast.makeText(getApplicationContext(), "전화번호을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                } else if (isNotPhone) {
                    Toast.makeText(getApplicationContext(), "전화번호 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                    break;
                } else if (isPhoneDuplicate) {
                    Toast.makeText(getApplicationContext(), "이미 가입된 전화번호입니다.", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    isSend = true;
//                    Toast.makeText(getApplicationContext(), "해당 번호로 인증 문자가 발송되었습니다.", Toast.LENGTH_SHORT).show();
                    startPhoneNumberVerification(changePhoneNumber(mobilePhoneNumber.getText().toString()));
                    sendAuth.setVisibility(View.GONE);
                    resendAuth.setVisibility(View.VISIBLE);
                    phoneGuide.setVisibility(View.GONE);
                    authText.setVisibility(View.VISIBLE);

                    break;
                }

            case R.id.verify:
                String code = authCode.getText().toString();

                if (TextUtils.isEmpty(code)) {
                    authCode.setError("인증번호를 입력해주세요.");
                    return;
                }
                if (!isSend) {
                    authCode.setError("인증 요청을 먼저 해주세요.");
                    return;
                }
                Toast.makeText(getApplicationContext(), "잠시만 기다려주세요...", Toast.LENGTH_SHORT).show();
                verifyPhoneNumberWithCode(mVerificationId, code);
                break;

            case R.id.resendAuth:
                if (isPhoneEmpty) {
                    Toast.makeText(getApplicationContext(), "전화번호을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                } else if (isNotPhone) {
                    Toast.makeText(getApplicationContext(), "전화번호 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                    break;
                } else if (isPhoneDuplicate) {
                    Toast.makeText(getApplicationContext(), "이미 가입된 전화번호입니다.", Toast.LENGTH_SHORT).show();
                    break;
                } else {
//                    Toast.makeText(getApplicationContext(), "해당 번호로 인증 문자가 재발송되었습니다.", Toast.LENGTH_SHORT).show();
                    resendVerificationCode(changePhoneNumber(mobilePhoneNumber.getText().toString()), mResendToken);
                    break;
                }
        }
    }
}