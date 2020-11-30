package com.cse416.backend.model.enums;

public enum CensusCatagories  {
    WHITE_AMERICAN("White"),
    AFRICAN_AMERICAN("African American"),
    AMERICAN_INDIAN("American Indian"),
    ASIAN_AMERICAN("Asian"),
    HAWAIIAN_AMERICAN("Hawaiian"),
    HISPANIC_AMERICAN("Hispanic"),
    OTHER_AMERICAN("Other");

    private final String representation;

    private CensusCatagories(String representation) {
        this.representation = representation;
    }

    public String getStringRepresentation(){
        return representation;
    }
}