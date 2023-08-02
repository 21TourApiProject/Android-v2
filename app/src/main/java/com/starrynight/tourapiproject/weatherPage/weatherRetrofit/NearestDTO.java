package com.starrynight.tourapiproject.weatherPage.weatherRetrofit;

public class NearestDTO {
    String sigungu;
    Double latitude;
    Double longitude;

    public NearestDTO(String sigungu, Double latitude, Double longitude) {
        this.sigungu = sigungu;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
