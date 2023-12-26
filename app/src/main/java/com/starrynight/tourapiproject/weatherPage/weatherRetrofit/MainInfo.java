package com.starrynight.tourapiproject.weatherPage.weatherRetrofit;

public class MainInfo {

    public String location; // ex. 행운동
    public String comment; // ex. 관측적합도 최대 98%로 별 보기 딱 좋네요! 추천 관측 시간은 NN시에요.
    public Long regionId;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getRegionId() {
        return regionId;
    }
}
