package com.cse416.backend.model.regions.district;

import com.cse416.backend.model.demographic.*;
import com.cse416.backend.model.regions.precinct.*;
import com.cse416.backend.model.regions.state.*;
import com.cse416.backend.model.regions.county.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.cse416.backend.model.Boundary;
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

    @Transient
    private String districtName;

    private int districtNumber;

    @ManyToOne(targetEntity=State.class, fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="stateId")
    private State state;

    @Transient
    private String stateAbbrev;

    @Column(name = "numberOfCounties")
    private String numofCounties;

    @Column(name = "numberOfPrecincts")
    private String numofPrecincts;

    @Transient
    private Demographic demographic;

    @Transient
    private double compactness;

    @JsonIgnore
    @OneToMany(targetEntity=Precinct.class,cascade = CascadeType.ALL, 
    fetch = FetchType.LAZY, orphanRemoval = true, mappedBy ="district")
    private List<Precinct> precincts;

    @JsonIgnore
    @OneToMany(targetEntity=County.class,cascade = CascadeType.ALL, 
    fetch = FetchType.LAZY, orphanRemoval = true,  mappedBy ="district")
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

    
    @JsonIgnore
    @ManyToOne(targetEntity=Plan.class, fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Plan plan;


   
    //Neccessary for JPA
    protected District (){}

    public District(String districtName, int districtNumber, int districtFIPSCode) {
        this.districtName = districtName;
        this.districtNumber = districtNumber;
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


    @Override
    public String toString() {
        return "District{" +
                "districtName='" + districtName + '\'' +
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

// Class to compare Movies by ratings
class CompactnessCompare implements Comparator<District>
{
    public int compare(District m1, District m2)
    {
        if (m1.getCompactness() < m2.getCompactness()) return -1;
        if (m1.getCompactness() > m2.getCompactness()) return 1;
        else return 0;
    }
}
