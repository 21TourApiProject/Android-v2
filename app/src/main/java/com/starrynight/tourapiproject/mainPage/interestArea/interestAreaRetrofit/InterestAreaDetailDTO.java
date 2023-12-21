package com.starrynight.tourapiproject.mainPage.interestArea.interestAreaRetrofit;

public class InterestAreaDetailDTO {

    public Long regionId;
    public String regionName;
    public Integer regionType;
    public String regionImage;
    public Double latitude;
    public Double longitude;
    public InterestAreaDetailWeatherInfo interestAreaDetailWeatherInfo;

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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public InterestAreaDetailWeatherInfo getInterestAreaDetailWeatherInfo() {
        return interestAreaDetailWeatherInfo;
    }

    public void setInterestAreaDetailWeatherInfo(InterestAreaDetailWeatherInfo interestAreaDetailWeatherInfo) {
        this.interestAreaDetailWeatherInfo = interestAreaDetailWeatherInfo;
    }
}
