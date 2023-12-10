package com.starrynight.tourapiproject.mainPage.interestArea;

public class InterestAreaDTO {

    public Long regionId;
    public String regionName;
    public Integer regionType;
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

    public String getObservationalFit() {
        return observationalFit;
    }

    public void setObservationalFit(String observationalFit) {
        this.observationalFit = observationalFit;
    }
}
