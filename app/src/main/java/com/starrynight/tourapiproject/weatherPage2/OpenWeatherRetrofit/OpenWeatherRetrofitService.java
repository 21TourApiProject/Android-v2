package com.starrynight.tourapiproject.weatherPage2.OpenWeatherRetrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherRetrofitService {
    @GET("onecall")
    Call<DetailWeatherData> geDetailWeatherData(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("exclude") String exclude,
            @Query("appid") String appid,
            @Query("units") String units
    );
}
