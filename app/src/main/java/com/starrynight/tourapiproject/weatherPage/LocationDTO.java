package com.starrynight.tourapiproject.weatherPage;

import java.io.Serializable;

public class LocationDTO implements Serializable {
    private final Double latitude;
    private final Double longitude;
    private Long areaId; // WEATHER_AREA id
    private Long observationId; // WEATHER_OBSERVATION id
    private final String location; // 읍면동 또는 관측지

    public LocationDTO(Double latitude, Double longitude, Long areaId, Long observationId, String location) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.areaId = areaId;
        this.observationId = observationId;
        this.location = location;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Long getObservationId() {
        return observationId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public String getLocation() {
        return location;
    }

    public void setAreaId(Long areaId){
        this.areaId = areaId;
    }

    public void setObservationId(Long observationId) {
        this.observationId = observationId;
    }
}
