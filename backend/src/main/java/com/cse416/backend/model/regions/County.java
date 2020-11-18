package com.cse416.backend.model.regions;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import javax.persistence.*;
import java.lang.Integer;


@Entity
@Table(name="Counties")
public class County {

    @Id
    @GeneratedValue
    private Integer countyId;

    @Column(nullable=false, length=2)
    private String stateId;

    @Column(nullable=false)
    private String countyName;

    private long totalPopulation;

    @Transient
    private String districtName;

    @Transient
    private String stateName;

    @Column(nullable=false)
    private int countyFIPSCode;

    @Transient
    private int districtFIPSCode;

    @Transient
    private int stateFIPSCode;

    @Transient
    private Boundary boundary;

    //@ManyToOne
    @Transient
    private District district;

    //@ManyToOne
    @Transient
    private State state;

    @Column(name = "numberOfPrecincts")
    private int numOfPrecincts;

    @Transient
    // @JoinTable
    // @OneToMany(targetEntity=Precinct.class)
    @JsonIgnore 
    private List<Precinct> precincts;

    //Neccessary for JPA
    protected County (){}

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
