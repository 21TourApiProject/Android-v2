package com.starrynight.tourapiproject.mainPage.interestArea.interestAreaRetrofit;

public class InterestAreaWeatherDTO {

    public Long regionId;
    public String regionName;
    public Integer regionType;
    public String regionImage;
    public String bestDay;
    public Integer bestHour;
    public Integer bestObservationalFit;
    public String weatherReport;
    public Double latitude;
    public Double longitude;

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

    public String getBestDay() {
        return bestDay;
    }

    public void setBestDay(String bestDay) {
        this.bestDay = bestDay;
    }

    public Integer getBestHour() {
        return bestHour;
    }

    public void setBestHour(Integer bestHour) {
        this.bestHour = bestHour;
    }

    public Integer getBestObservationalFit() {
        return bestObservationalFit;
    }

    public void setBestObservationalFit(Integer bestObservationalFit) {
        this.bestObservationalFit = bestObservationalFit;
    }

    public String getWeatherReport() {
        return weatherReport;
    }

    public void setWeatherReport(String weatherReport) {
        this.weatherReport = weatherReport;
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
}
