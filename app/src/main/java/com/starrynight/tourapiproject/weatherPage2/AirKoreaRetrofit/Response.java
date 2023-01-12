package com.starrynight.tourapiproject.weatherPage2.AirKoreaRetrofit;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("body")
    private Body body;

    @SerializedName("header")
    private Header header;

    public Body getBody() {
        return body;
    }

    public Header getHeader() {
        return header;
    }
}
