package com.starrynight.tourapiproject.mainPage.interestArea.interestAreaRetrofit;

public class InterestAreaDTO {

    public Long regionId;
    public String regionName;
    public Integer regionType;
    public String regionImage;
    public String observationalFit;

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

    public String getRegionImage() {
        return regionImage;
    }

    public void setRegionImage(String regionImage) {
        this.regionImage = regionImage;
    }

    public String getObservationalFit() {
        return observationalFit;
    }

    public void setObservationalFit(String observationalFit) {
        this.observationalFit = observationalFit;
    }
}
