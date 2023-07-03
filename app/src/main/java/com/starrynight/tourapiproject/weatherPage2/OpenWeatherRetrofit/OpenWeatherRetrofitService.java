package com.starrynight.tourapiproject.weatherPage2.OpenWeatherRetrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherRetrofitService {

    /**
     * current weather, minute forecast for 1 hour, hourly forecast for 48 hours, daily forecast for 7 days and government weather alerts
     * @param lat 위도
     * @param lon 경도
     * @param exclude 제외할 정보
     *                - current (현재 기상 정보)
     *                - minutely (분 단위 기상 정보)
     *                - hourly (시간 단위 기상 정보)
     *                - daily (일 단위 기상 정보)
     *                - alerts (기상 경보 데이터)
     * @param appid API key
     * @param units 단위 (온도, 풍속)
     *              - standard (C, meter/sec)
     *              - metric (F, miles/hour)
     *              - imperial (K, meter/sec)
     * @param lang 언어
     */

    @GET("onecall")
    Call<DetailWeatherData> geDetailWeatherData(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("exclude") String exclude,
            @Query("appid") String appid,
            @Query("units") String units,
            @Query("lang") String lang
    );

}
