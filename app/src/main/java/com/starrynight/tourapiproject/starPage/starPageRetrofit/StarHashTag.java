package com.starrynight.tourapiproject.starPage.starPageRetrofit;

import com.google.gson.annotations.SerializedName;

/**
 * className :  StarHashTag
 * description : 별자리 해시태그
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2022-12-27
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2022-12-27      jinhyeok      최초생성
 */
public class StarHashTag {
    @SerializedName("starHashTagListId")
    private Long starHashTagListId;
    @SerializedName("constellation")
    private Constellation constellation;
    @SerializedName("constId")
    private Long constId;
    @SerializedName("hashTagId")
    private Long hashTagId;
    @SerializedName("hashTagName")
    private String hashTagName;

    public StarHashTag(Long starHashTagListId) {
    }

    public Long getStarHashTagListId() {
        return starHashTagListId;
    }

    public Constellation getConstellation() {
        return constellation;
    }

    public Long getConstId() {
        return constId;
    }

    public Long getHashTagId() {
        return hashTagId;
    }

    public String getHashTagName() {
        return hashTagName;
    }
}
