package com.starrynight.tourapiproject.weatherPage2.weatherRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WeatherRetrofitService {

    @GET("observationFit/day/{date}")
    Call<List<DayObservationFit>> getDayObservationFit(@Path("date") String date);

    @GET("observationFit/hour/{date}")
    Call<List<HourObservationFit>> getHourObservationFit(@Path("date") String date);

}
