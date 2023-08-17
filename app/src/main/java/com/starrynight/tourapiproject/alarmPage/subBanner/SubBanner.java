package com.starrynight.tourapiproject.alarmPage.subBanner;

import com.google.gson.annotations.SerializedName;

/**
 * className :  SubBanner
 * description : TODO 예시 클래스 입니다.
 * modification : 2022.08.01(박진혁) methodA수정
 * author : jinhyeok
 * date : 2023-08-15
 * version : 1.0
 * ====개정이력(Modification Information)====
 * 수정일        수정자        수정내용
 * -----------------------------------------
 * 2023-08-15      jinhyeok      최초생성
 */
public class SubBanner {
    @SerializedName("subBannerId")
    private Long subBannerId;
    @SerializedName("bannerImage")
    private String bannerImage;
    @SerializedName("link")
    private String link;
    @SerializedName("isShow")
    private Boolean isShow;

    public SubBanner(Long subBannerId, String bannerImage, String link, boolean isShow) {
        this.subBannerId = subBannerId;
        this.bannerImage = bannerImage;
        this.link = link;
        this.isShow = isShow;
    }

    public Long getSubBannerId() {
        return subBannerId;
    }

    public void setSubBannerId(Long subBannerId) {
        this.subBannerId = subBannerId;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
