package com.starrynight.tourapiproject.weatherPage.OpenWeatherRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Daily {

    @SerializedName("dt")
    private String dt;

    @SerializedName("sunrise")
    private String sunrise;

    @SerializedName("sunset")
    private String sunset;

    @SerializedName("moonrise")
    private String moonrise;

    @SerializedName("moonset")
    private String moonset;

    @SerializedName("moon_phase")
    private String moonPhase;

    @SerializedName("temp")
    private Temp temp;

    @SerializedName("feels_like")
    private FeelsLike feelsLike;

    @SerializedName("pressure")
    private String pressure;

    @SerializedName("humidity")
    private String humidity;

    @SerializedName("dew_point")
    private String dewPoint;

    @SerializedName("wind_speed")
    private String windSpeed;

    @SerializedName("wind_deg")
    private String windDeg;

    @SerializedName("wind_gust")
    private String windGust;

    @SerializedName("weather")
    private List<Weather> weather;

    @SerializedName("clouds")
    private String clouds;

    @SerializedName("pop")
    private String pop;

    @SerializedName("rain")
    private String rain;

    @SerializedName("snow")
    private String snow;

    @SerializedName("uvi")
    private String uvi;

    public String getDt() {
        return dt;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public String getMoonrise() {
        return moonrise;
    }

    public String getMoonset() {
        return moonset;
    }

    public String getMoonPhase() {
        return moonPhase;
    }

    public Temp getTemp() {
        return temp;
    }

    public FeelsLike getFeelsLike() {
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

    public String getClouds() {
        return clouds;
    }

    public String getPop() {
        return pop;
    }
}
