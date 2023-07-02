package com.starrynight.tourapiproject.weatherPage2.weatherRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WeatherRetrofitService {

    @GET("observationFit/day/{date}")
    Call<List<DayObservationalFit>> getDayObservationFit(@Path("date") String date);

    @GET("observationFit/hour/{date}")
    Call<List<HourObservationalFit>> getHourObservationFit(@Path("date") String date);

    @POST("weather/v2/observationalFit/app")
    Call<WeatherInfo> getWeatherInfo(@Body AreaTimeDTO areaTimeDTO);

}
