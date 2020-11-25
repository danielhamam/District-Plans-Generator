package com.cse416.backend.model.regions.district;

import com.cse416.backend.model.demographic.*;
import com.cse416.backend.model.regions.precinct.*;
import com.cse416.backend.model.regions.state.*;
import com.cse416.backend.model.regions.county.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.cse416.backend.model.Boundary;

import java.lang.*;
import java.util.*;

import javax.persistence.*;



@Entity
@Table(name="Districts")
public class District {

    @Id
    @GeneratedValue
    private Integer districtId;

    @Transient
    private String districtName;


    private int districtNumber;

    //Note : DistrictNumber and District FIPSCode are the same
    // @Transient
    // private int districtFIPSCode;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "stateId")
    private State state;

    @Transient
    private String stateAbbrev;

    @Column(name = "numberOfCounties")
    private String numofCounties;

    @Column(name = "numberOfPrecincts")
    private String numofPrecincts;

    @OneToOne(mappedBy = "district", fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    private Demographic demographic;

    @Transient
    private Boundary boundary;

    @JsonIgnore
    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    private List<Precinct> precincts;

    @JsonIgnore
    @OneToMany(mappedBy = "state", fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    private List<County> counties;

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
        // this.districtFIPSCode = districtFIPSCode;
    }

    public District(String districtName, int districtNumber, int districtFIPSCode, Demographic demographic) {
        this.districtName = districtName;
        this.districtNumber = districtNumber;
        // this.districtFIPSCode = districtFIPSCode;
        this.demographic = demographic;
    }

    public District(String districtName, int districtNumber, int districtFIPSCode, Demographic demographic, Boundary boundaries) {
        this.districtName = districtName;
        this.districtNumber = districtNumber;
        // this.districtFIPSCode = districtFIPSCode;
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

    // public int getDistrictFIPSCode() {
    //     return districtFIPSCode;
    // }

    // public void setDistrictFIPSCode(int districtFIPSCode) {
    //     this.districtFIPSCode = districtFIPSCode;
    // }

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
        // clientDistrict.put("districtFIPSCode", this.districtFIPSCode);
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
                // ", districtFIPSCode=" + districtFIPSCode +
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