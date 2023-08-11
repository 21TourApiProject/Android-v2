package com.starrynight.tourapiproject.searchPage.searchPageRetrofit;

import com.google.gson.annotations.SerializedName;

public class HashTag {
    @SerializedName("hashTagId")
    Long hashTagId;
    @SerializedName("category")
    String category;
    @SerializedName("hashTagName")
    String hashTagName;

}
