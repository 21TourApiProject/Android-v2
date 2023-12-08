package com.starrynight.tourapiproject.mainPage.interestArea;

public class InterestAreaItem {
    String areaName;
    String areaImage;
    String observationalFit;

    public InterestAreaItem(String areaName, String areaImage, String observationalFit) {
        this.areaName = areaName;
        this.areaImage = areaImage;
        this.observationalFit = observationalFit;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaImage() {
        return areaImage;
    }

    public void setAreaImage(String areaImage) {
        this.areaImage = areaImage;
    }

    public String getObservationalFit() {
        return observationalFit;
    }

    public void setObservationalFit(String observationalFit) {
        this.observationalFit = observationalFit;
    }
}
