package com.starrynight.tourapiproject.weatherPage.weatherRetrofit;

public class MainInfo {

    public String comment;
    public String bestObservationalFit;
    public String bestTime;
    public Long areaId;

    public String getComment() {
        return comment;
    }

    public String getBestObservationalFit() {
        return bestObservationalFit;
    }

    public String getBestTime() {
        return bestTime;
    }

    public Long getAreaId() {
        return areaId;
    }
}
