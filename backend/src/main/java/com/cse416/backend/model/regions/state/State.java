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
import javax.swing.plaf.synth.SynthTextAreaUI;


@Entity
@Table(name="States")
public class State {

    @Id
    @Column(name="stateId", nullable = false, unique = true)
    private String stateAbbreviation;

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

    @OneToMany(targetEntity=Precinct.class,cascade = CascadeType.ALL, 
    fetch = FetchType.LAZY, orphanRemoval = true, mappedBy ="state")
    @JsonIgnore
    private List<Precinct> statePrecincts;

    @OneToMany(targetEntity=County.class,cascade = CascadeType.ALL, 
    fetch = FetchType.LAZY, orphanRemoval = true, mappedBy ="state")
    @JsonIgnore
    private List<County> stateCounties;


    @OneToMany(targetEntity=District.class,cascade = CascadeType.ALL, 
    fetch = FetchType.LAZY, orphanRemoval = true, mappedBy ="state")
    @JsonIgnore
    private List<District> stateDistricts;

    @OneToMany(targetEntity=Job.class,cascade = CascadeType.ALL, 
    fetch = FetchType.LAZY, orphanRemoval = true, mappedBy ="state")
    @JsonIgnore
    private List<Job> stateJobs;

    @Transient
    @JsonIgnore
    private File precinctsFile;

    @Transient
    @JsonIgnore
    private File algorithmPrecinctsFile;

    @Transient
    @JsonIgnore
    private String algorithmPrecinctsJson;

    @Transient
    @JsonIgnore
    private FeatureCollection precinctsGeoJson;

    @Transient
    @JsonIgnore
    private FeatureCollection stateGeoJson;

    @Transient
    @JsonIgnore
    private Map algorithmPrecinctsMap;

    //Neccessary for JPA
    protected State (){
    }

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

    public void setInitialFiles(){
        if(stateAbbreviation == null){
            throw new NullPointerException("State abbreviation doesn't exist ");
        }
        try{
            enactedPlan = new Plan(stateAbbreviation,"Enacted","0",57,true);
            String precinctFilePath = "src/main/resources/system/states/" +
                    stateAbbreviation.toLowerCase() + "/Precincts.json";
            String precinctFilePathAbsolutePath = new File(precinctFilePath).getAbsolutePath();
            precinctsFile = new File(precinctFilePathAbsolutePath);
            precinctsGeoJson = createPrecinctsFeatureCollection();

        }
        catch(IOException error){
            error.printStackTrace();
        }
    }

    public void setAlgorithmPrecinctMap(){
        if(statePrecincts == null){
            throw new NullPointerException("State abbreviation doesn't exist ");
        }
        if(algorithmPrecinctsMap != null){
           return;
        }
        System.out.println("setAlgorithmPrecinctMap");
        Map <String,Object> precinctsMap = new HashMap<>();
        Map <String,Object> temp=null;
//        for(Precinct precinct: statePrecincts){
//            Map <String,Object> precinctMap = new HashMap<>();
//            precinctMap.put("neighbors", precinct.getNeighbors());
//            precinctsMap.put(precinct.getPrecinctFIPSCode(),precinctMap);
//        }
        Precinct precinct =  statePrecincts.get(0);
//        System.out.println(precinct);
//        List <Precinct> neigbors =  precinct.getNeighbors();
//        Precinct castedPrecintct = precinct;
//        System.out.println("========");
//        System.out.println("========");
//        System.out.println("========");
//        System.out.println("========");
//        System.out.println("========");
//        System.out.println(neigbors);
//        System.out.println(precinct.getClass());
//        System.out.println(precinct);
//        System.out.println(castedPrecintct);
//        System.out.println(castedPrecintct.getNeighbors().get(0));
//
        System.out.println(precinct.getNeighbors().get(0));



//        List ne = (List)statePrecincts.get(0).getNeighbors();
//        System.out.println(ne);
//        algorithmPrecinctsMap = precinctsMap;
//        System.out.println(algorithmPrecinctsMap.get(temp));
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
        map.put("precincts", algorithmPrecinctsMap);
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