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

    private int precinctFIPSCode;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "countyId")
    private County county;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "districtId")
    private District district;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "stateId")
    private State state;

    @Transient
    private Boundary boundary;

    @OneToOne(mappedBy = "precinct", fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    private Demographic demographic;

    @Transient
    @ManyToMany
    @JoinTable(
        name = "PrecinctNeighbors",
        joinColumns = @JoinColumn(name = "precinctID", referencedColumnName = "precinctId"),
        inverseJoinColumns = @JoinColumn(name = "neighborPrecinctID", referencedColumnName = "precinctId")
    )
    private List<Precinct> neighbors;

    //Neccessary for JPA
    protected Precinct (){}

    public Precinct(String precinctName, int precinctFIPSCode, Demographic demographic) {
        this.precinctName = precinctName;
        this.precinctFIPSCode = precinctFIPSCode;
        this.demographic = demographic;

    }

    public Precinct(String precinctName, int precinctFIPSCode, Demographic demographic, List<Precinct> neighbors) {
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

    public List<Precinct>  getNeighbors() {
        return this.neighbors;
    }

    public void setNeighbors(List<Precinct> neighbors) {
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