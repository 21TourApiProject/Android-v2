package com.starrynight.tourapiproject.mainPage.interestArea;

import java.io.Serializable;
import java.util.List;

public class InterestAreaIntent implements Serializable {
    Long userId;
    List<String> interestAreaNameList;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getInterestAreaNameList() {
        return interestAreaNameList;
    }

    public void setInterestAreaNameList(List<String> interestAreaNameList) {
        this.interestAreaNameList = interestAreaNameList;
    }

    public InterestAreaIntent(Long userId, List<String> interestAreaNameList) {
        this.userId = userId;
        this.interestAreaNameList = interestAreaNameList;
    }
}
