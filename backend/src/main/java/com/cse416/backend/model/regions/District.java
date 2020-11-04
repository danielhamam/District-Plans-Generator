package com.cse416.backend.model.regions;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class District {
    private String districtName;
    private String stateName;
    private int districtNumber;
    private int districtFIPSCode;
    private int stateFIPSCode;
    private int numOfCounties;
    @JsonIgnore private List<County> counties;
    private Boundary boundary;
    private Demographic demographic;

    public District(String name, int FIPSCode, Boundary boundaries, Demographic demographic) {
        this.districtName = name;
        this.districtFIPSCode = FIPSCode;
        this.boundary = boundaries;
        this.demographic = demographic;
    }

    public String getName() {
        return districtName;
    }

    public int getNumber() {
        return districtNumber;
    }

    public int getFIPSCode() {
        return districtFIPSCode;
    }

    public List<County> getCounties() {
        return counties;
    }

    public String getStateName() {
        return stateName;
    }

    public int getStateFIPSCode() {
        return stateFIPSCode;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public Demographic getDemographic() {
        return demographic;
    }

    public int getNumOfCounties() {
        return numOfCounties;
    }

}