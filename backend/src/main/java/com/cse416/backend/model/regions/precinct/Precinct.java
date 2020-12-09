package com.cse416.backend.model.regions.precinct;

import com.cse416.backend.model.demographic.*;
import com.cse416.backend.model.regions.county.*;
import com.cse416.backend.model.regions.state.*;
import com.cse416.backend.model.regions.district.*;
import com.cse416.backend.model.Boundary;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


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

    @ManyToOne(targetEntity=County.class, fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="countyId")
    private County county;

    @ManyToOne(targetEntity=District.class, fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="districtId")
    private District district;

    @ManyToOne(targetEntity=State.class, fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="stateId")
    private State state;

    @ManyToMany(targetEntity=Precinct.class,fetch = FetchType.LAZY)
    @JoinTable(
        name = "PrecinctNeighbors",
        joinColumns = @JoinColumn(name = "precinctID", referencedColumnName = "precinctId"),
        inverseJoinColumns = @JoinColumn(name = "precinctNeighborID", referencedColumnName = "precinctId")
    )
    private List<Precinct> neighbors;

    @Transient
    @JsonIgnore
    private Demographic demographic;

    //Neccessary for JPA
    protected Precinct (){}

    public Precinct(String precinctName, String precinctFIPSCode, Demographic demographic) {
        this.precinctName = precinctName;
        this.precinctFIPSCode = precinctFIPSCode;

    }

    public Precinct(String precinctName, String precinctFIPSCode, Demographic demographic, List<Precinct> neighbors) {
        this.precinctName = precinctName;
        this.precinctFIPSCode = precinctFIPSCode;
        this.neighbors = neighbors;
    }

    public Integer getPrecinctId() {
        return precinctId;
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



    public List<Precinct>  getNeighbors() {
        return this.neighbors;
    }

    public void setNeighbors(List<Precinct> neighbors) {
        this.neighbors = neighbors;
    }

    public Demographic getDemographic() {
        return demographic;
    }

    public void setDemographic(Demographic demographic) {
        this.demographic = demographic;
    }

    @Override
    public String toString() {
        String s = "Precinct{" +
                "precinctName='" + precinctName + '\'' +
                ", precinctFIPSCode=" + precinctFIPSCode +
                ", demographic=" + demographic +
                ", neighbors = [" ;

        for(int i = 0; i < neighbors.size(); i++)
            s  += neighbors.get(i).getPrecinctFIPSCode() + ",";
        
        return s + "]}";
    }

    public String printPrecintNameFIPS() {
        String s = "Precinct{" +
                "precinctName='" + precinctName + '\'' +
                ", precinctFIPSCode=" + precinctFIPSCode + "]}\n";
        return s;
    }
}