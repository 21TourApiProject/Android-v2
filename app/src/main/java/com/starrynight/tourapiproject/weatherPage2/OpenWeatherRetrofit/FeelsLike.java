package com.starrynight.tourapiproject.weatherPage2.OpenWeatherRetrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeelsLike {

    @SerializedName("day")
    @Expose
    private String day;

    public String getDay() {
        return day;
    }

    //    @SerializedName("day")
//    private String day;
//
//    @SerializedName("night")
//    private String night;
//
//    @SerializedName("eve")
//    private String eve;
//
//    @SerializedName("morn")
//    private String morn;
//
//    public String getDay() {
//        return day;
//    }
//
//    public String getNight() {
//        return night;
//    }
//
//    public String getEve() {
//        return eve;
//    }
//
//    public String getMorn() {
//        return morn;
//    }
}
