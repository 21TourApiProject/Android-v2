package com.starrynight.tourapiproject.touristPointPage.touristPointRetrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.starrynight.tourapiproject.common.TaskServer;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @className : RetrofitClient.java
 * @description : 관광지 관련 레트로핏 설정 입니다.
 * @modification : 2022-09-07(sein) 수정
 * @author : sein
 * @date : 2022-09-07
 * @version : 1.0

    ====개정이력(Modification Information)====
        수정일        수정자        수정내용
    -----------------------------------------
      2022-09-07     sein        주석 생성

 */
public class RetrofitClient {
    private static final String BASE_URL = TaskServer.serverURL;


    public static TouristPointPageRetrofitService getApiService() {
        return getInstance().create(TouristPointPageRetrofitService.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
