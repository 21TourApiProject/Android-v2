package com.starrynight.tourapiproject.weatherPage;

public class SearchLocationItem {
    String title;
    String subtitle;
    Long areaId; // WEATHER_AREA id
    Long observationId; // WEATHER_OBSERVATION id
    Double latitude;
    Double longitude;
    Long observationalFit;

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Long getAreaId() {
        return areaId;
    }

    public Long getObservationId() {
        return observationId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Long getObservationalFit() {
        return observationalFit;
    }
}
