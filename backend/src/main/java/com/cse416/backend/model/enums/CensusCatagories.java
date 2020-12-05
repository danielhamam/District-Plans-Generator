package com.cse416.backend.model.enums;

public enum CensusCatagories  {
    WHITE_AMERICAN("White", "White"),
    AFRICAN_AMERICAN("Black","Black or African American"),
    AMERICAN_INDIAN("American Indian","American Indian or Alaska Native"),
    ASIAN_AMERICAN("Asian","Asian"),
    HAWAIIAN_AMERICAN("Native Hawaiian", "Native Hawaiian or Other Pacific Islander"),
    LATINO_AMERICAN("Latino", "Hispanic or Latino"),
    OTHER_AMERICAN("Other", "Other race, Non-Hispanic"),
    MULTIPLE_AMERICAN("Multiple", "Two or more race, Non-Hispanic");

    private final String shortenName;
    private final String fullName;

    private CensusCatagories(String shortenName, String fullName) {
        this.shortenName = shortenName;
        this.fullName = fullName;
    }

    public String getFullName(){
        return fullName;
    }

    public String getShortenName(){
        return shortenName;
    }
}


