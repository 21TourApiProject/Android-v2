package com.starrynight.tourapiproject.searchPage.filter;

import com.google.gson.annotations.SerializedName;

public class HashTagItem {
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getIsActive() {
        return isActive;
    }

    public static final int VIEWTYPE_INACTIVE = 0;
    public static final int VIEWTYPE_ACTIVE = 1;

    Long id;
    String category;
    String name;
    int isActive = VIEWTYPE_INACTIVE;   //0은 inactive 1은 active

    public HashTagItem(Long id, String category, String name, int isActive) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.isActive = isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getCategory() {
        return category;
    }
}
