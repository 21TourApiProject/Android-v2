package com.starrynight.tourapiproject.weatherPage2.AirKoreaRetrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AirKoreaRetrofitService {

    /**
     * 대기질 예보통보 조회
     * @param serviceKey 서비스키
     * @param returnType 데이터 표출 방식
     *                   - xml
     *                   - json
     * @param searchDate 조회 날짜 (2023-02-19)
     * @param informCode 통보코드
     *                   - PM10
     *                   - PM25
     *                   - P3
     */
    @GET("getMinuDustFrcstDspth")
    Call<FineDustData> getFineDustData(
            @Query("serviceKey") String serviceKey,
            @Query("returnType") String returnType,
            @Query("searchDate") String searchDate,
            @Query("informCode") String informCode
    );

}














