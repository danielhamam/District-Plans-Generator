package com.cse416.backend.model.regions.precinct;

import com.cse416.backend.model.demographic.*;
import com.cse416.backend.model.regions.county.*;
import com.cse416.backend.model.regions.state.*;
import com.cse416.backend.model.regions.district.*;
import com.cse416.backend.model.Boundary;


import java.lang.*;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="Precincts")
public class Precinct {

    @Id
    @GeneratedValue
    private Integer precinctId;
    
    private String precinctName;

    private String precinctFIPSCode;

    @ManyToOne(targetEntity=County.class, fetch = FetchType.LAZY)
    @JoinColumn(name="countyId")
    private County county;

    @ManyToOne(targetEntity=District.class, fetch = FetchType.LAZY)
    @JoinColumn(name="districtId")
    private District district;

    @ManyToOne(targetEntity=State.class, fetch = FetchType.LAZY)
    @JoinColumn(name="stateId")
    private State state;

    @Transient
    private Boundary boundary;

    @Transient
    private Demographic demographic;

    @ManyToMany(targetEntity=Precinct.class,cascade = CascadeType.ALL, 
    fetch = FetchType.LAZY)
    @JoinTable(
        name = "PrecinctNeighbors",
        joinColumns = @JoinColumn(name = "precinctID", referencedColumnName = "precinctId"),
        inverseJoinColumns = @JoinColumn(name = "precinctNeighborID", referencedColumnName = "precinctId")
    )
    private List<Precinct> neighbors;

    //Neccessary for JPA
    protected Precinct (){}

    public Precinct(String precinctName, String precinctFIPSCode, Demographic demographic) {
        this.precinctName = precinctName;
        this.precinctFIPSCode = precinctFIPSCode;
        this.demographic = demographic;

    }

    public Precinct(String precinctName, String precinctFIPSCode, Demographic demographic, List<Precinct> neighbors) {
        this.precinctName = precinctName;
        this.precinctFIPSCode = precinctFIPSCode;
        this.demographic = demographic;
        this.neighbors = neighbors;
    }

    public String getPrecinctName() {
        return precinctName;
    }

    public void setPrecinctName(String precinctName) {
        this.precinctName = precinctName;
    }

    public String getPrecinctFIPSCode() {
        return precinctFIPSCode;
    }

    public void setPrecinctFIPSCode(String precinctFIPSCode) {
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

    public List<Precinct>  getNeighbors() {
        return this.neighbors;
    }

    public void setNeighbors(List<Precinct> neighbors) {
        this.neighbors = neighbors;
    }

    
    @Override
    public String toString() {
        String s = "Precinct{" +
                "precinctName='" + precinctName + '\'' +
                ", precinctFIPSCode=" + precinctFIPSCode +
                ", boundary=" + boundary +
                ", demographic=" + demographic + 
                ", neighbors = [" ;

        for(int i = 0; i < neighbors.size(); i++)
            s  += neighbors.get(i).getPrecinctFIPSCode() + ",";
        
        return s + "]}";
    }
}