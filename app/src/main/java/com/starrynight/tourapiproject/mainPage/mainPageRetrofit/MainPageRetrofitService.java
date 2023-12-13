package com.starrynight.tourapiproject.mainPage.mainPageRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author : 2022-08-30
 * @version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용    -----------------------------------------
 * gyul chyoung       2022-08-30       최초생성
 * @className : MainPageRetrofitService.java
 * @description : 메인페이지 관련 레트로핏 주소설정
 * @modification : gyul chyoung (2022-08-30) 내용
 * @date : gyul chyoung
 */

public interface MainPageRetrofitService {

    @GET("observations/simple")
    Call<List<ObservationSimpleParams>> getBestFitObservationList();

}
