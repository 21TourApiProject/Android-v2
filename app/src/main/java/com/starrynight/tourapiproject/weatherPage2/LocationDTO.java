package com.starrynight.tourapiproject.weatherPage2;

import java.io.Serializable;

public class LocationDTO implements Serializable {
    private final Double latitude;
    private final Double longitude;
    private final String city;

    public LocationDTO(Double latitude, Double longitude, String city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getCity() {
        return city;
    }
}
