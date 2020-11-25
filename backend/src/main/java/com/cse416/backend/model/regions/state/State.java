package com.cse416.backend.model.regions.state;

import com.cse416.backend.model.plan.*;
import com.cse416.backend.model.regions.precinct.*;
import com.cse416.backend.model.Boundary;
import com.cse416.backend.model.demographic.*;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.FeatureCollection;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import javax.persistence.*;
import java.lang.Integer;

// @Entity
// @Table(name="States")
public class State {

    // @Column(length=25)
    private String stateName;

    // @Id
    // @Column(name="stateId", nullable=false, length=2)
    private String stateAbbreviation;

    // @Column(nullable=false)
    private int stateFIPSCode;

    private int totalPopulation;

    // @Transient
    private Plan enactedPlan;

    // @Transient
    private int numOfPrecincts;

    // @Transient
    private int numOfCounties;

    // @Transient
    private int numOfDistricts;

    // @Transient
    // @OneToMany(targetEntity=Precinct.class)
    @JsonIgnore
    private Precinct [] statePrecincts;

    // @Transient
    @JsonIgnore
    private Boundary boundary;

    // @Transient
    @JsonIgnore
    private File precinctsFile;

    // @Transient
    @JsonIgnore
    private File algorithmPrecinctsFile;

    @JsonIgnore
    private String algorithmPrecinctsJson;

    // @Transient
    @JsonIgnore
    private FeatureCollection precinctsGeoJson;

    // @Transient
    @JsonIgnore
    private FeatureCollection stateGeoJson;

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
        String algorithmPrecinctsFilePath = "src/main/resources/system/states/" +
                stateAbbreviation.toLowerCase() + "/AlgorithmPrecincts.json";
//        String boundaryFilePath = "src/main/resources/system/states/" +
//                stateAbbreviation.toLowerCase() + "/StateBoundaries.json";
//        this.stateFile = new File(new File(boundaryFilePath).getAbsolutePath());
        try{
            this.precinctsFile = new File(new File(precinctFilePath).getAbsolutePath());
            this.algorithmPrecinctsFile = new File(new File(algorithmPrecinctsFilePath).getAbsolutePath());
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

    public Precinct[] getStatePrecincts() {
        return statePrecincts;
    }

    public void setStatePrecincts(Precinct[] statePrecincts) {
        this.statePrecincts = statePrecincts;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
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




    // public Map<String, Object>  getClientInitialData(){
    //     Map<String, Object> clientState = new HashMap<>();
    //     clientState.put("stateName", this.stateName);
    //     clientState.put("stateAbbreviation", this.stateAbbreviation);
    //     clientState.put("stateFIPSCode", this.stateFIPSCode);
    //     clientState.put("numOfPrecincts", 10);
    //     clientState.put("numOfCounties", 10);
    //     clientState.put("enactedPlan", this.enactedPlan.getClientInitialData());
    //     clientState.put("demographic", this.demographic);
    //     return clientState;
    // }

    @Override
    public String toString() {
        return "State{" +
                "stateName='" + stateName + '\'' +
                ", stateAbbreviation='" + stateAbbreviation + '\'' +
                ", stateFIPSCode=" + stateFIPSCode +
                ", enactedPlan=" + enactedPlan +
                ", statePrecincts=" + Arrays.toString(statePrecincts) +
                ", boundary=" + boundary +
                '}';
    }
}