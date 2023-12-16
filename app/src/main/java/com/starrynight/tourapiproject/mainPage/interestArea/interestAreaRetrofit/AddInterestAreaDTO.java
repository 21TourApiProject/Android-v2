package com.starrynight.tourapiproject.mainPage.interestArea.interestAreaRetrofit;

public class AddInterestAreaDTO {

    public Long userId;
    public Long regionId;
    public String regionName;
    public Integer regionType;

    public AddInterestAreaDTO() {
    }

    public AddInterestAreaDTO(Long userId, Long regionId, Integer regionType) {
        this.userId = userId;
        this.regionId = regionId;
        this.regionType = regionType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Integer getRegionType() {
        return regionType;
    }

    public void setRegionType(Integer regionType) {
        this.regionType = regionType;
    }
}
