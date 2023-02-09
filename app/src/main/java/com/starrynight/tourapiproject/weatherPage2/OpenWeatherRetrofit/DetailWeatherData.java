package com.starrynight.tourapiproject.weatherPage2.OpenWeatherRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailWeatherData {

    @SerializedName("lat")
    private String lat;

    @SerializedName("lon")
    private String lon;

    @SerializedName("timezone")
    private String timezone;

    @SerializedName("timezone_offset")
    private String timezoneOffset;

    @SerializedName("hourly")
    private List<Hourly> hourly;

    @SerializedName("daily")
    private List<Daily> daily;

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getTimezoneOffset() {
        return timezoneOffset;
    }

    public List<Hourly> getHourly() {
        return hourly;
    }

    public List<Daily> getDaily() {
        return daily;
    }
}
