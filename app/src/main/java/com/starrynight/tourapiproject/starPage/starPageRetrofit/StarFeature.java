package com.starrynight.tourapiproject.starPage.starPageRetrofit;

import com.google.gson.annotations.SerializedName;

/**
 * className :  StarFeature
 * description : TODO 예시 클래스 입니다.
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2023-07-20
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2023-07-20      jinhyeok      최초생성
 */
public class StarFeature {
    @SerializedName("starFeatureId")
    private Long starFeatureId;
    @SerializedName("starFeatureName")
    private String starFeatureName;

    public Long getStarFeatureId() {
        return starFeatureId;
    }

    public void setStarFeatureId(Long starFeatureId) {
        this.starFeatureId = starFeatureId;
    }

    public String getStarFeatureName() {
        return starFeatureName;
    }

    public void setStarFeatureName(String starFeatureName) {
        this.starFeatureName = starFeatureName;
    }
}
