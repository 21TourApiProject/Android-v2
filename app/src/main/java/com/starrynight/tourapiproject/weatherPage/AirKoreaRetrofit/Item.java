package com.starrynight.tourapiproject.weatherPage.AirKoreaRetrofit;

import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("informCode")
    private String informCode;

    @SerializedName("informData")
    private String informData;

    @SerializedName("informGrade")
    private String informGrade;

    @SerializedName("dataTime")
    private String dataTime;

    public String getInformCode() {
        return informCode;
    }

    public String getInformData() {
        return informData;
    }

    public String getInformGrade() {
        return informGrade;
    }

    public String getDataTime() {
        return dataTime;
    }
}
