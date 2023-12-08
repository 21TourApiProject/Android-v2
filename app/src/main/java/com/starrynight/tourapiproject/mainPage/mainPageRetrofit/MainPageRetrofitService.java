package com.starrynight.tourapiproject.mainPage.mainPageRetrofit;

import com.starrynight.tourapiproject.mainPage.interestArea.InterestArea;
import com.starrynight.tourapiproject.mainPage.interestArea.InterestAreaDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
* @className : MainPageRetrofitService.java
* @description : 메인페이지 관련 레트로핏 주소설정
* @modification : gyul chyoung (2022-08-30) 내용
* @author : 2022-08-30
* @date : gyul chyoung
* @version : 1.0
     ====개정이력(Modification Information)====
  수정일        수정자        수정내용    -----------------------------------------
   gyul chyoung       2022-08-30       최초생성
 */

public interface MainPageRetrofitService {

    @GET("observations/simple")
    Call<List<ObservationSimpleParams>> getBestFitObservationList();

    @GET("interestArea/{userId}")
    Call<List<InterestArea>> getAllInterestArea(@Path("userId") Long userId);

    @POST("interestArea")
    Call<Void> addInterestArea(@Body InterestAreaDTO interestAreaDTO);

    @DELETE("interestArea")
    Call<Void> deleteInterestArea(@Body InterestAreaDTO interestAreaDTO);

}
