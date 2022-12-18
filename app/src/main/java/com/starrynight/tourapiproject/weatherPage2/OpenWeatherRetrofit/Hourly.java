package com.starrynight.tourapiproject.weatherPage2.OpenWeatherRetrofit;

import com.google.gson.annotations.SerializedName;
import com.starrynight.tourapiproject.weatherPage.wtMetModel.Weather;

import java.util.List;

public class Hourly {
    @SerializedName("dt")
    private String dt;

    @SerializedName("temp")
    private String temp;

    @SerializedName("feels_like")
    private String feelsLike;

    @SerializedName("pressure")
    private String pressure;

    @SerializedName("humidity")
    private String humidity;

    @SerializedName("dew_point")
    private String dewPoint;

    @SerializedName("uvi")
    private String uvi;

    @SerializedName("clouds")
    private String clouds;

    @SerializedName("visibility")
    private String visibility;

    @SerializedName("wind_speed")
    private String windSpeed;

    @SerializedName("wind_deg")
    private String windDeg;

    @SerializedName("wind_gust")
    private String windGust;

    @SerializedName("weather")
    private List<Weather> weather;

    @SerializedName("pop")
    private String pop;

    public String getDt() {
        return dt;
    }

    public String getTemp() {
        return temp;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getDewPoint() {
        return dewPoint;
    }

    public String getUvi() {
        return uvi;
    }

    public String getClouds() {
        return clouds;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public String getWindDeg() {
        return windDeg;
    }

    public String getWindGust() {
        return windGust;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public String getPop() {
        return pop;
    }
}
