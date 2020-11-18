package com.cse416.backend.model.regions;

import com.cse416.backend.model.Demographic;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.persistence.*;
import java.lang.Integer;


@Entity
@Table(name="Districts")
public class District {

    @Id
    @GeneratedValue
    private Integer districtId;

    @Transient
    private String districtName;


    private int districtNumber;

    @Transient
    private int districtFIPSCode;

    @Transient
    // @ManyToOne
    private String stateAbbrev;

    private String numofCounties;

    private String numofPrecincts;

    @Transient
    // @OneToOne
    private Demographic demographic;

    @Transient
    private Boundary boundary;

    @Transient
    // @JoinTable
    // @OneToMany(targetEntity=Precinct.class)
    @JsonIgnore
    private List<Precinct> precincts;

    @Transient
    @JsonIgnore
    private List <District> neighbors;

    @Transient
    @JsonIgnore
    private double perimeter;

   @Transient
    @JsonIgnore
    private double area;
   
    //Neccessary for JPA
    protected District (){}

    public District(String districtName, int districtNumber, int districtFIPSCode) {
        this.districtName = districtName;
        this.districtNumber = districtNumber;
        this.districtFIPSCode = districtFIPSCode;
    }

    public District(String districtName, int districtNumber, int districtFIPSCode, Demographic demographic) {
        this.districtName = districtName;
        this.districtNumber = districtNumber;
        this.districtFIPSCode = districtFIPSCode;
        this.demographic = demographic;
    }

    public District(String districtName, int districtNumber, int districtFIPSCode, Demographic demographic, Boundary boundaries) {
        this.districtName = districtName;
        this.districtNumber = districtNumber;
        this.districtFIPSCode = districtFIPSCode;
        this.boundary = boundaries;
        this.demographic = demographic;
    }


    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public int getDistrictNumber() {
        return districtNumber;
    }

    public void setDistrictNumber(int districtNumber) {
        this.districtNumber = districtNumber;
    }

    public int getDistrictFIPSCode() {
        return districtFIPSCode;
    }

    public void setDistrictFIPSCode(int districtFIPSCode) {
        this.districtFIPSCode = districtFIPSCode;
    }

    public String getStateAbbrev() {
        return stateAbbrev;
    }

    public void setStateAbbrev(String stateAbbrev) {
        this.stateAbbrev = stateAbbrev;
    }

    public String getNumofCounties() {
        return numofCounties;
    }

    public void setNumofCounties(String numofCounties) {
        this.numofCounties = numofCounties;
    }

    public String getNumofPrecincts() {
        return numofPrecincts;
    }

    public void setNumofPrecincts(String numofPrecincts) {
        this.numofPrecincts = numofPrecincts;
    }

    public List<Precinct> getPrecincts() {
        return precincts;
    }

    public void setPrecincts(List<Precinct> precincts) {
        this.precincts = precincts;
    }

    public List<District> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<District> neighbors) {
        this.neighbors = neighbors;
    }

    public double getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(double perimeter) {
        this.perimeter = perimeter;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }

    public Demographic getDemographic() {
        return demographic;
    }

    public void setDemographic(Demographic demographic) {
        this.demographic = demographic;
    }

    public Map<String, Object> getClientInitialData(){
        Map<String, Object> clientDistrict = new HashMap<>();
        clientDistrict.put("districtName", this.districtName);
        clientDistrict.put("districtNumber", this.districtNumber);
        clientDistrict.put("districtFIPSCode", this.districtFIPSCode);
        clientDistrict.put("numofCounties", this.numofCounties);
        clientDistrict.put("numofCounties", this.numofPrecincts);
        clientDistrict.put("demographic", this.demographic);
        clientDistrict.put("boundary", this.boundary);
        return clientDistrict;
    }


    @Override
    public String toString() {
        return "District{" +
                "districtName='" + districtName + '\'' +
                ", districtNumber=" + districtNumber +
                ", districtFIPSCode=" + districtFIPSCode +
                ", stateAbbrev='" + stateAbbrev + '\'' +
                ", numofCounties='" + numofCounties + '\'' +
                ", numofPrecincts='" + numofPrecincts + '\'' +
                ", precincts=" + precincts +
                ", neighbors=" + neighbors +
                ", perimeter=" + perimeter +
                ", area=" + area +
                ", boundary=" + boundary +
                ", demographic=" + demographic +
                '}';
    }



}