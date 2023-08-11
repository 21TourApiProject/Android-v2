package com.starrynight.tourapiproject.weatherPage.weatherRetrofit;

public class AreaTimeDTO {
    String date; // 날짜 (2023-05-29)
    Integer hour; // 현재 시간 (18시와의 차이 계산 필요)
    Double lat; // 위도
    Double lon; // 경도
    String address; // 주소 (서울시 관악구 행운동)
    Long areaId; // WEATHER_AREA id
    Long observationId; // WEATHER_OBSERVATION id

    public AreaTimeDTO(String date, Integer hour, Double lat, Double lon) {
        this.date = date;
        this.hour = hour;
        this.lat = lat;
        this.lon = lon;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public void setObservationId(Long observationId) {
        this.observationId = observationId;
    }
}
