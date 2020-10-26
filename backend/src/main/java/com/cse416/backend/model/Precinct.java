package com.cse416.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import javax.naming.spi.StateFactory;

public class Precinct {
    private String precinctName;
    private String countyName;
    private String districtName;
    private String stateName;
    private int precinctFIPSCode;
    private int countyFIPSCode;
    private int districtFIPSCode;
    private int stateFIPSCode;
    private Boundary boundary;
    @JsonIgnore private List<Precinct> neighbors;

    public Precinct(String name, int FIPSCode, Boundary boundary){
        precinctName = name;
        precinctFIPSCode = FIPSCode;
        this.boundary = boundary;
    }

    public void setState(String name, int FIPSCode) {
        stateName = name;
        stateFIPSCode = FIPSCode;
    }

    public void setCounty(String name, int FIPSCode) {
        countyName = name;
        countyFIPSCode = FIPSCode;
    }

    public void setDistrict(String name, int FIPSCode) {
        districtName = name;
        districtFIPSCode = FIPSCode;
    }

    public void setNeighbors(List<Precinct> neighbors) {
        this.neighbors = neighbors;
    }

    public String getName() {
        return precinctName;
    }

    public int getFIPSCode() {
        return precinctFIPSCode;
    }

    public List<Precinct> getNeighbors() {
        return neighbors;
    }

    public String getStateName() {
        return stateName;
    }

    public int getStateFIPSCode() {
        return stateFIPSCode;
    }

    public String getCountyName() {
        return countyName;
    }

    public int getCountyFIPSCode() {
        return countyFIPSCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public int getDistrictFIPSCode() {
        return districtFIPSCode;
    }
    
    public Boundary getBoundary() {
        return boundary;
    }

    public int getNumofNeighbors() {
        return neighbors.size();
    }
}