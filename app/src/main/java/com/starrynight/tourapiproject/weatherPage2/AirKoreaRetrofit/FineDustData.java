package com.starrynight.tourapiproject.weatherPage2.AirKoreaRetrofit;

import com.google.gson.annotations.SerializedName;

public class FineDustData {

    @SerializedName("response")
    private Response response;

    public Response getResponse() {
        return response;
    }
}
