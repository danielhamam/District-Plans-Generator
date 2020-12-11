package com.cse416.backend.model.regions.district;

import com.cse416.backend.model.demographic.*;
import com.cse416.backend.model.enums.CensusCatagories;
import com.cse416.backend.model.regions.district.comparators.*;
import com.cse416.backend.model.regions.precinct.*;
import com.cse416.backend.model.regions.state.*;
import com.cse416.backend.model.regions.county.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.cse416.backend.model.plan.*;

import java.lang.*;
import java.util.*;

import javax.persistence.*;


@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name="Districts")
public class District{

    @Id
    @GeneratedValue
    private Integer districtId;

    private int districtNumber;

    @ManyToOne(targetEntity=State.class, fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="stateId")
    private State state;

    @ManyToOne(targetEntity=Plan.class, fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private Plan plan;

    @JsonIgnore
    @OneToMany(targetEntity=County.class,cascade =  {CascadeType.PERSIST, CascadeType.DETACH}, 
    fetch = FetchType.LAZY,  mappedBy ="district")
    private List<County> counties;

    @Column(name = "numberOfCounties")
    private String numofCounties;

    @JsonIgnore
    @OneToMany(targetEntity=Precinct.class,cascade = {CascadeType.PERSIST, CascadeType.DETACH},
            fetch = FetchType.LAZY, mappedBy ="district")
    private List<Precinct> precincts;

    @Column(name = "numberOfPrecincts")
    private String numofPrecincts;

    @Transient
    private String stateAbbrev;

    @Transient
    @JsonIgnore
    private List <District> neighbors;

    @Transient
    private Demographic demographic;

    @Transient
    private double compactness;

    @Transient
    @JsonIgnore
    private double perimeter;

    @Transient
    @JsonIgnore
    private double area;


    //Neccessary for JPA
    protected District (){}


    public District(int districtNumber, State state, Plan plan){
        this.districtNumber = districtNumber;
        this.state = state;
        this.plan  = plan;
    }


    public District(int districtNumber, State state, Plan plan, List<County> counties, List<Precinct> precincts){
        this.districtNumber = districtNumber;
        this.state = state;
        this.plan  = plan;
        this.counties = counties;
        this.numofCounties = "" + counties.size();
        this.precincts = precincts;
        this.numofCounties = "" + precincts.size();
    }


    public int getDistrictNumber() {
        return districtNumber;
    }

    public void setDistrictNumber(int districtNumber) {
        this.districtNumber = districtNumber;
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

    public Demographic getDemographic() {
        return demographic;
    }

    public void setDemographic(Demographic demographic) {
        this.demographic = demographic;
    }

    public double getCompactness() {
        return compactness;
    }

    public static Comparator getComparatorByCensusCatagories(CensusCatagories censusCatagories)throws Exception{
        switch(censusCatagories) {
            case AFRICAN_AMERICAN:
                return new AfricanAmericanPopulationCompare();
            case AMERICAN_INDIAN:
                return new AmericanIndianVAPCompare();
            case ASIAN_AMERICAN:
                return new AsianVAPCompare();
            case HAWAIIAN_AMERICAN:
                return new NativeHawaiianVAPCompare();
            case OTHER_AMERICAN:
                return new OthersVAPCompare();
            case LATINO_AMERICAN:
                return new LatinoVAPCompare();
            case WHITE_AMERICAN:
                return new WhiteVAPCompare();
            default:
                throw new Exception("CensusCatagories Enum Does Not Exist");
        }
    }


    @Override
    public String toString() {
        return "District{" +
                ", districtNumber=" + districtNumber +
                // ", districtFIPSCode=" + districtFIPSCode +
                ", stateAbbrev='" + stateAbbrev + '\'' +
                ", numofCounties='" + numofCounties + '\'' +
                ", numofPrecincts='" + numofPrecincts + '\'' +
                // ", precincts=" + precincts +
                // ", neighbors=" + neighbors +
                ", perimeter=" + perimeter +
                ", area=" + area +
                ", demographic=" + demographic +
                '}';
    }

}


