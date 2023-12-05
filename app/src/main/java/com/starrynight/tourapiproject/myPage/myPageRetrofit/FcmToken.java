package com.starrynight.tourapiproject.myPage.myPageRetrofit;

import com.google.gson.annotations.SerializedName;

/**
 * className :  FcmToken
 * description : TODO 예시 클래스 입니다.
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2023-12-03
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2023-12-03      jinhyeok      최초생성
 */
public class FcmToken {

    @SerializedName("fcmId")
    private Long fcmId;
    @SerializedName("user")
    private User user;
    @SerializedName("userId")
    private Long userId;
    @SerializedName("fcmToken")
    private String fcmToken;

    public FcmToken(Long fcmId) {
    }
}
