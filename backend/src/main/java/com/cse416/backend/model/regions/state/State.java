package com.cse416.backend.model.regions.state;

import com.cse416.backend.model.plan.*;
import com.cse416.backend.model.regions.precinct.*;
import com.cse416.backend.model.regions.county.*;
import com.cse416.backend.model.regions.district.*;
import com.cse416.backend.model.job.*;


import com.cse416.backend.model.Boundary;
import com.cse416.backend.model.demographic.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.FeatureCollection;

import java.io.IOException;

import java.lang.*;
import java.util.*;
import java.io.File;
import javax.persistence.*;


@Entity
@Table(name="States")
public class State {

    @Id
    @Column(name="stateId", nullable=false, length=2)
    private String stateAbbreviation;

    @Column(length=25)
    private String stateName;

    private int stateFIPSCode;

    @Transient
    private int totalPopulation;

    @Transient
    private Plan enactedPlan;

    @Column(name="numberOfPrecincts")
    private int numOfPrecincts;

    @Column(name="numberOfCounties")
    private int numOfCounties;

    @Column(name="numberOfDistricts")
    private int numOfDistricts;

    @JsonIgnore
    @OneToMany(mappedBy = "state", fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    private List<Precinct> statePrecincts;


    @JsonIgnore
    @OneToMany(mappedBy = "state", fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    private List<County> stateCounties;


    @JsonIgnore
    @OneToMany(mappedBy = "state", fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    private List<District> stateDistricts;

    @JsonIgnore
    @OneToMany(mappedBy = "state", fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    private List<Job> stateJobs;

    @Transient
    @JsonIgnore
    private File precinctsFile;

    @Transient
    @JsonIgnore
    private File algorithmPrecinctsFile;

    @JsonIgnore
    private String algorithmPrecinctsJson;

    @Transient
    @JsonIgnore
    private FeatureCollection precinctsGeoJson;

    @Transient
    @JsonIgnore
    private FeatureCollection stateGeoJson;

    @JsonIgnore
    @OneToOne(mappedBy = "state", fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    private Demographic demographic;

    //Neccessary for JPA
    protected State (){}

    public State(String stateName, String stateAbbreviation, int stateFIPSCode, int totalPopulation, int numOfCounties, int numOfDistricts, int numOfPrecincts){
        this.stateName = stateName;
        this.stateAbbreviation = stateAbbreviation;
        this.stateFIPSCode = stateFIPSCode;
        this.totalPopulation = totalPopulation;
        this.numOfPrecincts = numOfPrecincts;
        this.numOfCounties = numOfCounties;
        this.numOfDistricts = numOfDistricts;
        this.enactedPlan = new Plan(stateAbbreviation,"Enacted","0",57,true);
        String precinctFilePath = "src/main/resources/system/states/" +
                stateAbbreviation.toLowerCase() + "/Precincts.json";
        String precinctFilePathAbsolutePath = new File(precinctFilePath).getAbsolutePath();
        this.precinctsFile = new File(precinctFilePathAbsolutePath);
        this.enactedPlan = new Plan(stateAbbreviation,"Enacted","0",57,true);
        
        try{
            this.precinctsFile = new File(new File(precinctFilePath).getAbsolutePath());
            // this.algorithmPrecinctsFile = new File(new File(algorithmPrecinctsFilePath).getAbsolutePath());
            this.precinctsGeoJson = createPrecinctsFeatureCollection();
        }
        catch(IOException error){
            error.printStackTrace();
        }
    }

    private FeatureCollection createPrecinctsFeatureCollection()throws IOException {
        return new ObjectMapper().readValue(this.precinctsFile, FeatureCollection.class);
    }

    private String createAlgorithmPrecinctsString()throws IOException {
        return "";
    }
    
    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public int getStateFIPSCode() {
        return stateFIPSCode;
    }

    public void setStateFIPSCode(int stateFIPSCode) {
        this.stateFIPSCode = stateFIPSCode;
    }

    public Plan getEnactedPlan() {
        return enactedPlan;
    }

    public void setEnactedPlan(Plan enactedPlan) {
        this.enactedPlan = enactedPlan;
    }

    public List<Precinct> getStatePrecincts() {
        return statePrecincts;
    }

    public void setStatePrecincts(List<Precinct> statePrecincts) {
        this.statePrecincts = statePrecincts;
    }


    public int getTotalPopulation() {
        return totalPopulation;
    }

    public int getNumOfCounties() {
        return numOfCounties;
    }

    public int getNumOfDistricts() {
        return numOfDistricts;
    }

    public int getNumOfPrecincts() {
        return numOfPrecincts;
    }

    @JsonIgnore
    public Map getClientPrecinctsGeoJson() {
        Map <String, Object> map = new HashMap<>();
        map.put("precinctsGeoJson", precinctsGeoJson);
        return map;
    }

    @JsonIgnore
    public Map getAlgorithmJson() {
        Map <String, Object> map = new HashMap<>();
        map.put("stateName", stateName);
        map.put("nameAbbrev", stateAbbreviation);
        map.put("stateFIPSCode", stateFIPSCode);
        map.put("totalPopulation", totalPopulation);
        map.put("precincts", algorithmPrecinctsJson);
        return map;
    }


    @Override
    public String toString() {
        return "State{" +
                "stateName='" + stateName + '\'' +
                ", stateAbbreviation='" + stateAbbreviation + '\'' +
                ", stateFIPSCode=" + stateFIPSCode +
                ", enactedPlan=" + enactedPlan +
                ", statePrecincts=" + statePrecincts.toString() +
                '}';
    }
}