package com.starrynight.tourapiproject.mainPage.interestArea;

import com.starrynight.tourapiproject.weatherPage.SearchLocationItem;

import java.io.Serializable;

public class InterestAreaAddIntent implements Serializable {
    Long userId;
    SearchLocationItem searchLocationItem;

    public InterestAreaAddIntent(Long userId, SearchLocationItem searchLocationItem) {
        this.userId = userId;
        this.searchLocationItem = searchLocationItem;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public SearchLocationItem getSearchLocationItem() {
        return searchLocationItem;
    }

    public void setSearchLocationItem(SearchLocationItem searchLocationItem) {
        this.searchLocationItem = searchLocationItem;
    }
}
