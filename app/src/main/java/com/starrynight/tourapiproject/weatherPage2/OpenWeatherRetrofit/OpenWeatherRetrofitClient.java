package com.starrynight.tourapiproject.weatherPage2.OpenWeatherRetrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.starrynight.tourapiproject.common.TaskServer;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherRetrofitClient {

    private static final String BASE_URL = TaskServer.openWeatherURL;


    public static OpenWeatherRetrofitService getApiService() {
        return getInstance().create(OpenWeatherRetrofitService.class);
    }

    private static Retrofit getInstance() {
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}