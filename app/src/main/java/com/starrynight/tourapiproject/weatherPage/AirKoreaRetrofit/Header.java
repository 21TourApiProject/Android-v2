package com.starrynight.tourapiproject.weatherPage.AirKoreaRetrofit;

import com.google.gson.annotations.SerializedName;

public class Header {

    @SerializedName("resultMsg")
    private String resultMsg;

    @SerializedName("resultCode")
    private String resultCode;

    public String getResultMsg() {
        return resultMsg;
    }

    public String getResultCode() {
        return resultCode;
    }
}
