package com.starrynight.tourapiproject.mainPage.interestArea.interestAreaRetrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.starrynight.tourapiproject.common.TaskServer;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InterestAreaRetrofitClient {
    private static final String BASE_URL = TaskServer.serverURL;


    public static InterestAreaRetrofitService getApiService() {
        return getInstance().create(InterestAreaRetrofitService.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
