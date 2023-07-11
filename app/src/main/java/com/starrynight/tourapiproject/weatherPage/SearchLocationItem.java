package com.starrynight.tourapiproject.weatherPage;

public class SearchLocationItem {
    String title;
    String subtitle;
    Long locationId; // 날씨 상세 페이지 이동을 위한 id
    String observationValue;

    public SearchLocationItem(String title, String subtitle, Long locationId, String observationValue) {
        this.title = title;
        this.subtitle = subtitle;
        this.locationId = locationId;
        this.observationValue = observationValue;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public Long getLocationId() {
        return locationId;
    }

    public String getObservationValue() {
        return observationValue;
    }
}
