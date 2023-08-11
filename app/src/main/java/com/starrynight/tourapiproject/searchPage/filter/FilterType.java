package com.starrynight.tourapiproject.searchPage.filter;

public enum FilterType {
    THEME(1),
    TRANSPORT(2),
    PEOPLE(3),
    AREA(4),
    FACILITY(5),
    FEE(6);

    private final int value;

    FilterType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
