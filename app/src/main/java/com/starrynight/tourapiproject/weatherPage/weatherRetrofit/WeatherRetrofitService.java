package com.starrynight.tourapiproject.weatherPage.weatherRetrofit;

import com.starrynight.tourapiproject.weatherPage.SearchLocationItem;

import java.util.List;
import java.util.Map;

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

    @POST("weather/observationalFit/weatherPage")
    Call<WeatherInfo> getWeatherInfo(@Body AreaTimeDTO areaTimeDTO);

    @POST("weather/observationalFit/mainPage")
    Call<MainInfo> getMainInfo(@Body AreaTimeDTO areaTimeDTO);

    @POST("weather/area/nearest")
    Call<Map<String, String>> getNearestArea(@Body NearestDTO nearestDTO);

    @GET("weather/locations")
    Call<List<SearchLocationItem>> getWeatherLocations();
}
