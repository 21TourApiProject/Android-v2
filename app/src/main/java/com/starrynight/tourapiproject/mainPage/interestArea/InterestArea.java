package com.starrynight.tourapiproject.mainPage.interestArea;

public class InterestArea {

    public Long interestAreaId;
    public Long userId;
    public Long regionId;
    public String regionName;
    public Integer regionType;
    public Double observationalFit;

    public Long getInterestAreaId() {
        return interestAreaId;
    }

    public void setInterestAreaId(Long interestAreaId) {
        this.interestAreaId = interestAreaId;
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

    public Double getObservationalFit() {
        return observationalFit;
    }

    public void setObservationalFit(Double observationalFit) {
        this.observationalFit = observationalFit;
    }
}
