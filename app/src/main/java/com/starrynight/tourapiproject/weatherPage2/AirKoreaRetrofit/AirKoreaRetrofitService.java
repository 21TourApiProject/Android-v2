package com.starrynight.tourapiproject.weatherPage2.AirKoreaRetrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AirKoreaRetrofitService {

    @GET("getMinuDustFrcstDspth")
    Call<FineDustData> getFineDustData(
            @Query("serviceKey") String serviceKey,
            @Query("returnType") String returnType,
            @Query("searchDate") String searchDate,
            @Query("informCode") String informCode
    );

}














