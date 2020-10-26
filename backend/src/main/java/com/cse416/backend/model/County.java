package com.cse416.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class County {
    private String countyName;
    private String districtName;
    private String stateName;
    private int countyFIPSCode;
    private int districtFIPSCode;
    private int stateFIPSCode;
    private Boundary boundary;
    private int numOfPrecincts;
    @JsonIgnore private List<Precinct> precincts;

    public County (String name, int FIPSCode, Boundary boundaries) {
        this.countyName = name;
        this.countyFIPSCode = FIPSCode;
        this.boundary = boundaries;
    }

    public void setState(String name, int FIPSCode) {
        stateName = name;
        stateFIPSCode = FIPSCode;
    }

    public void setDistrict(String name, int FIPSCode) {
        districtName = name;
        districtFIPSCode = FIPSCode;
    }

    public void setPrecincts(List<Precinct> precincts) {
        this.precincts = precincts;
    }

    public String getName() {
        return countyName;
    }

    public int getFIPSCode() {
        return countyFIPSCode;
    }

    public List<Precinct> getPrecincts() {
        return precincts;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public int getNumOfPrecincts() {
        return numOfPrecincts;
    }

    public String getStateName() {
        return stateName;
    }

    public int getStateFIPSCode() {
        return stateFIPSCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public int getDistrictFIPSCode() {
        return districtFIPSCode;
    }

}
