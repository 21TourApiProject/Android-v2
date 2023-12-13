package com.starrynight.tourapiproject.mainPage.mainPageRetrofit;

import com.google.gson.annotations.SerializedName;

public class PostContentsParams {

    @SerializedName("itemId")
    private Long itemId;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("title")
    private String title;
    @SerializedName("observationId")
    private Long observationId;
    @SerializedName("observationName")
    private String observationName;
    @SerializedName("observationCustomName")
    private String observationCustomName;
    @SerializedName("contents")
    private String contents;

    public Long getItemId() {
        return itemId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public Long getObservationId() {
        return observationId;
    }

    public String getObservationName() {
        return observationName;
    }

    public String getObservationCustomName() {
        return observationCustomName;
    }

    public String getContents() {
        return contents;
    }
}
