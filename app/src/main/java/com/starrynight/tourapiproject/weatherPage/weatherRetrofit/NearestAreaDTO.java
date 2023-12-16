package com.starrynight.tourapiproject.weatherPage.weatherRetrofit;

public class NearestAreaDTO {

    String sgg; // 시군군 (서대문구)
    Double latitude;
    Double longitude;
    String date; // 날짜 (2023-05-29)
    Integer hour; // 현재 시간 (18시와의 차이 계산 필요)

    public NearestAreaDTO(String sgg, Double latitude, Double longitude, String date, Integer hour) {
        this.sgg = sgg;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.hour = hour;
    }

    public String getSgg() {
        return sgg;
    }

    public void setSgg(String sgg) {
        this.sgg = sgg;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }
}
