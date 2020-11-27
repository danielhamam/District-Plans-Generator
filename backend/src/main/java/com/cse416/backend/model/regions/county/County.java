package com.cse416.backend.model.regions.county;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.cse416.backend.model.regions.precinct.*;
import com.cse416.backend.model.regions.district.*;
import com.cse416.backend.model.regions.state.*;
import com.cse416.backend.model.demographic.*;


import com.cse416.backend.model.Boundary;

import java.lang.*;
import java.util.*;
import javax.persistence.*;



@Entity
@Table(name="Counties")
public class County {

    @Id
    @GeneratedValue
    private Integer countyId;

    private String countyName;

    private Integer countyFIPSCode;
    
    @Transient
    private Boundary boundary;

    @ManyToOne(targetEntity=District.class, fetch = FetchType.LAZY)
    @JoinColumn(name="districtId")
    private District district;

    @ManyToOne(targetEntity=State.class, fetch = FetchType.LAZY)
    @JoinColumn(name="stateId")
    private State state;

    @Column(name = "numberOfPrecincts")
    private int numOfPrecincts;

    @JsonIgnore 
    @OneToMany(targetEntity=Precinct.class,cascade = CascadeType.ALL, 
    fetch = FetchType.LAZY, orphanRemoval = true,  mappedBy ="county")
    private List<Precinct> precincts;

    @Transient
    private Demographic demographic;


    //Neccessary for JPA
    protected County (){}

    public County (String name, int FIPSCode, Boundary boundaries) {
        this.countyName = name;
        this.countyFIPSCode = FIPSCode;
        this.boundary = boundaries;
    }

    // public void setState(String name, int FIPSCode) {
    //     stateName = name;
    //     stateFIPSCode = FIPSCode;
    // }

    // public void setDistrict(String name, int FIPSCode) {
    //     districtName = name;
    //     districtFIPSCode = FIPSCode;
    // }

    // public void setPrecincts(List<Precinct> precincts) {
    //     this.precincts = precincts;
    // }

    public int getId(){ return countyId;}

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

    // public String getStateName() {
    //     return state.getStateName();
    // }

    // public int getStateFIPSCode() {
    //     return state.getStateFIPSCode();
    // }

    // public String getDistrictName() {
    //     return district.getDistrictName();
    // }

    // public int getDistrictFIPSCode() {
    //     return district.getDistrictNumber();
    // }

    public String toString(){
        return "County Name " + getName() + " FIPSCode : " + getFIPSCode();
    }

}
