package com.starrynight.tourapiproject.weatherPage2.OpenWeatherRetrofit;

import com.google.gson.annotations.SerializedName;

public class Temp {

    @SerializedName("day")
    private String day;

    @SerializedName("min")
    private String min;

    @SerializedName("max")
    private String max;

    public String getDay() {
        return day;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    //    @SerializedName("day")
//    private String day;
//
//    @SerializedName("min")
//    private String min;
//
//    @SerializedName("max")
//    private String max;
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
//    public String getMin() {
//        return min;
//    }
//
//    public String getMax() {
//        return max;
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