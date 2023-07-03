package com.starrynight.tourapiproject.weatherPage2;

import java.io.Serializable;

public class LocationDTO implements Serializable {
    private final Double latitude;
    private final Double longitude;
    private final String city;
    private final Long areaId; // WEATHER_AREA id
    private final Long observationId; // WEATHER_OBSERVATION id

    public LocationDTO(Double latitude, Double longitude, String city, Long areaId, Long observationId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.areaId = areaId;
        this.observationId = observationId;
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

    public Long getObservationId() {
        return observationId;
    }

    public Long getAreaId() {
        return areaId;
    }
}
