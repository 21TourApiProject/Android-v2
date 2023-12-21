package com.starrynight.tourapiproject.mainPage.mainPageRetrofit;

import com.starrynight.tourapiproject.starPage.starItemPage.StarItem;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

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

    @GET("constellation/todayConst")
    Call<List<StarItem>> getTodayConst();

    @GET("posts/{size}")
    Call<List<PostContentsParams>> getLatestPostWithSize(@Path("size") int size);

    @GET("user/{userId}/nickName")
    Call<Map<String, String>> getNickName(@Path("userId") Long userId);
}
