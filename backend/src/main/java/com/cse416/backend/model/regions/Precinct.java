package com.cse416.backend.model.regions;

import com.cse416.backend.model.Demographic;

import java.lang.Integer;

//@Entity
//@Table(name="Precincts")
public class Precinct {

    //@Id
    //@GeneratedValue
    private Integer countyId;

    //@ManyToOne
    private County county;

    //@ManyToOne
    private State state;

    //@ManyToOne
    private District district;

    //@Column(nullable=false)
    private String precinctName;

    //@Column(nullable=false)
    private int precinctFIPSCode;

    private Boundary boundary;

    //@OneToOne
    private Demographic demographic;

    //@JoinTable
    //@OneToMany(targetEntity=Precinct.class)
    private Precinct [] neighbors;

    //Neccessary for JPA
    //protected Precinct (){}
    public Precinct(String precinctName, int precinctFIPSCode, Demographic demographic) {
        this.precinctName = precinctName;
        this.precinctFIPSCode = precinctFIPSCode;
        this.demographic = demographic;

    }

    public Precinct(String precinctName, int precinctFIPSCode, Demographic demographic, Precinct [] neighbors) {
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