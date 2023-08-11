package com.starrynight.tourapiproject.weatherPage.AirKoreaRetrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Body {
    @SerializedName("items")
    private List<Item> items;

    @SerializedName("totalCount")
    private Integer totalCount;

    @SerializedName("pageNo")
    private Integer pageNo;

    @SerializedName("numOfRows")
    private Integer numOfRows;

    public List<Item> getItems() {
        return items;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public Integer getNumOfRows() {
        return numOfRows;
    }
}
