package com.starrynight.tourapiproject.mainPage.interestArea.interestAreaRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InterestAreaRetrofitService {

    @GET("interestArea/detail/{regionId}/{regionType}")
    Call<InterestAreaWeatherDTO> getInterestAreaInfo(@Path("regionId") Long regionId, @Path("regionType") Integer regionType);

    @GET("interestArea/{userId}")
    Call<List<InterestAreaDTO>> getAllInterestArea(@Path("userId") Long userId);

    @POST("interestArea")
    Call<Void> addInterestArea(@Body UpdateInterestAreaDTO updateInterestAreaDTO);

    @DELETE("interestArea")
    Call<Void> deleteInterestArea(@Body UpdateInterestAreaDTO updateInterestAreaDTO);
}
