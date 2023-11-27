package com.starrynight.tourapiproject.mainPage.mainPageRetrofit;

import com.google.gson.annotations.SerializedName;

public class ObservationSimpleParams {
    @SerializedName("itemId")
    Long itemId; //관측지id
    @SerializedName("thumbnail")
    String thumbnail; //썸네일
    @SerializedName("title")
    String title; //제목
    @SerializedName("address")
    String address; //주소
    @SerializedName("intro")
    String intro; //짧은 개요
    @SerializedName("longitude")
    Double longitude;  //경도
    @SerializedName("latitude")
    Double latitude; //위도
    @SerializedName("light")
    Double light;   //광공해
    @SerializedName("observeFit")
    Double observeFit;   //관측적합도
    @SerializedName("saved")
    Long saved; //저장횟수

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLight() {
        return light;
    }

    public void setLight(Double light) {
        this.light = light;
    }

    public Double getObserveFit() {
        return observeFit;
    }

    public void setObserveFit(Double observeFit) {
        this.observeFit = observeFit;
    }

    public Long getSaved() {
        return saved;
    }

    public void setSaved(Long saved) {
        this.saved = saved;
    }
}
