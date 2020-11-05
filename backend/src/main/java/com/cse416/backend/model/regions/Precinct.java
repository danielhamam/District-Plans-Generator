package com.cse416.backend.model.regions;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import javax.naming.spi.StateFactory;

public class Precinct {
    String precinctName;
    int precinctFIPSCode;
    Boundary boundary;
    Demographic demographic;
    Precinct [] neighbors;

    
    public Precinct(String precinctName, int precinctFIPSCode, Boundary boundary, Demographic demographic, Precinct [] neighbors) {
        this.precinctName = precinctName;
        this.precinctFIPSCode = precinctFIPSCode;
        this.boundary = boundary;
        this.demographic = demographic;
        this.neighbors = neighbors;
    }

    public String getPrecinctName() {
        return precinctName;
    }

    public void setPrecinctName(String precinctName) {
        this.precinctName = precinctName;
    }

    public int getPrecinctFIPSCode() {
        return precinctFIPSCode;
    }

    public void setPrecinctFIPSCode(int precinctFIPSCode) {
        this.precinctFIPSCode = precinctFIPSCode;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }

    public Demographic getDemographic() {
        return this.demographic;
    }

    public void setDemographic(Demographic demographic) {
        this.demographic = demographic;
    }

    public Precinct [] getNeighbors() {
        return this.neighbors;
    }

    public void setNeighbors(Precinct [] neighbors) {
        this.neighbors = neighbors;
    }

    @Override
    public String toString() {
        return "Precinct{" +
                "precinctName='" + precinctName + '\'' +
                ", precinctFIPSCode=" + precinctFIPSCode +
                ", boundary=" + boundary +
                ", demographic=" + demographic +
                ", neighbors=" + neighbors +
                '}';
    }
}