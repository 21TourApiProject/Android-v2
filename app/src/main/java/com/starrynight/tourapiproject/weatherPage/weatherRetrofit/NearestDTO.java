package com.starrynight.tourapiproject.weatherPage.weatherRetrofit;

public class NearestDTO {
    String sgg;
    Double latitude;
    Double longitude;

    public NearestDTO(String sgg, Double latitude, Double longitude) {
        this.sgg = sgg;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setSgg(String sgg) {
        this.sgg = sgg;
    }
}
