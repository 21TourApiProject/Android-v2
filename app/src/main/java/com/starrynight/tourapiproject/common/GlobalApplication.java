package com.starrynight.tourapiproject.common;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;
import com.starrynight.tourapiproject.BuildConfig;

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
        KakaoSdk.init(this, BuildConfig.KAKAO_SDK_KEY);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}
