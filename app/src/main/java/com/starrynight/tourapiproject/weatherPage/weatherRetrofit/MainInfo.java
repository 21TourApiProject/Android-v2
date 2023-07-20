package com.starrynight.tourapiproject.weatherPage.weatherRetrofit;

public class MainInfo {

    public String comment;
    public String bestObservationalFit;
    public String bestTime;
    public String mainEffect;
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

    public String getMainEffect() {
        return mainEffect;
    }

    public Long getAreaId() {
        return areaId;
    }
}
