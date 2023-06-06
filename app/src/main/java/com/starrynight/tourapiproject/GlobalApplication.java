package com.starrynight.tourapiproject;

import android.app.Application;
import android.util.Log;

import com.kakao.sdk.common.KakaoSdk;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class GlobalApplication extends Application {

    private static GlobalApplication instance;

    public static GlobalApplication getGlobalApplicationContext() {
        if (instance == null) {
            throw new IllegalStateException("This Application does not inherit com.kakao.GlobalApplication");
        }

        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Kakao Sdk 초기화
        KakaoSdk.init(this, "2201411c61cc7086fe639ce660ccb21e");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
//
//    private String readKakaokey() {
//        String data = null;
//        InputStream inputStream = getResources().openRawResource(R.raw.kakao);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        int i;
//        try {
//            i = inputStream.read();
//            while (i != -1) {
//                byteArrayOutputStream.write(i);
//                i = inputStream.read();
//            }
//            data = new String(byteArrayOutputStream.toByteArray(), "MS949");
//
//            inputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Log.d("카카오키 뭐임?",data);
//        return data;
//    }
//
//    public class KakaoSDKAdapter extends KakaoAdapter {
//
//        @Override
//        public ISessionConfig getSessionConfig() {
//            return new ISessionConfig() {
//                @Override
//                public AuthType[] getAuthTypes() {
//                    return new AuthType[]{AuthType.KAKAO_LOGIN_ALL};
//                }
//
//                @Override
//                public boolean isUsingWebviewTimer() {
//                    return false;
//                }
//
//                @Override
//                public boolean isSecureMode() {
//                    return false;
//                }
//
//                @Override
//                public ApprovalType getApprovalType() {
//                    return ApprovalType.INDIVIDUAL;
//                }
//
//                @Override
//                public boolean isSaveFormData() {
//                    return true;
//                }
//            };
//        }
//
//        // Application이 가지고 있는 정보를 얻기 위한 인터페이스
//        @Override
//        public IApplicationConfig getApplicationConfig() {
//            return new IApplicationConfig() {
//                @Override
//                public Context getApplicationContext() {
//                    return GlobalApplication.getGlobalApplicationContext();
//                }
//            };
//        }
//    }
}
