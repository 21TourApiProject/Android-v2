package com.starrynight.tourapiproject.searchPage.searchPageRetrofit;

import java.util.List;

public class Filter {
    List<Long> areaCodeList;
    List<Long> hashTagIdList;

    public Filter(List<Long> areaCodeList, List<Long> hashTagIdList) {
        this.areaCodeList = areaCodeList;
        this.hashTagIdList = hashTagIdList;
    }

    public List<Long> getAreaCodeList() {
        return areaCodeList;
    }

    public List<Long> getHashTagIdList() {
        return hashTagIdList;
    }
}
