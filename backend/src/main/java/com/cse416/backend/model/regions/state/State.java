package com.cse416.backend.model.regions.state;

import com.cse416.backend.model.demographic.Demographic;
import com.cse416.backend.model.plan.*;
import com.cse416.backend.model.regions.precinct.*;
import com.cse416.backend.model.regions.county.*;
import com.cse416.backend.model.regions.district.*;
import com.cse416.backend.model.job.*;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.geojson.FeatureCollection;

import java.io.IOException;

import java.lang.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.File;
import javax.persistence.*;


@Entity
@Table(name="States")
public class State {

    @Id
    @Column(name="stateId", nullable = false, unique = true)
    private String stateAbbreviation;

    private String stateName;

    private int stateFIPSCode;

    @Transient
    private long totalPopulation;

    @Transient
    private Plan enactedPlan;

    @Column(name="numberOfPrecincts")
    private int numOfPrecincts;

    @Column(name="numberOfCounties")
    private int numOfCounties;

    @Column(name="numberOfDistricts")
    private int numOfDistricts;

    @Transient
    private Demographic demographic;

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
    private JsonNode algorithmPrecinctsJson;

    @Transient
    @JsonIgnore
    private File precinctsCoordinatesFile;

    @Transient
    @JsonIgnore
    private JsonNode precinctsCoordinatesJson;

    @Transient
    @JsonIgnore
    private FeatureCollection precinctsGeoJson;

    @Transient
    @JsonIgnore
    private FeatureCollection stateGeoJson;

    @Transient
    @JsonIgnore
    private ObjectMapper mapper = new ObjectMapper();


    //Neccessary for JPA
    protected State (){
    }

//    public State(String stateName, String stateAbbreviation, int stateFIPSCode, int totalPopulation, int numOfCounties, int numOfDistricts, int numOfPrecincts){
//        this.stateName = stateName;
//        this.stateAbbreviation = stateAbbreviation;
//        this.stateFIPSCode = stateFIPSCode;
//        this.totalPopulation = totalPopulation;
//        this.numOfPrecincts = numOfPrecincts;
//        this.numOfCounties = numOfCounties;
//        this.numOfDistricts = numOfDistricts;
//        this.enactedPlan = new Plan(stateAbbreviation,"Enacted",0,57,true);
//        String precinctFilePath = "src/main/resources/system/states/" +
//                stateAbbreviation.toLowerCase() + "/Precincts.json";
//        String precinctFilePathAbsolutePath = new File(precinctFilePath).getAbsolutePath();
//        this.precinctsFile = new File(precinctFilePathAbsolutePath);
//        this.enactedPlan = new Plan(stateAbbreviation,"Enacted",0,57,true);
//
//        try{
//            this.precinctsFile = new File(new File(precinctFilePath).getAbsolutePath());
//            // this.algorithmPrecinctsFile = new File(new File(algorithmPrecinctsFilePath).getAbsolutePath());
//            this.precinctsGeoJson = new ObjectMapper().readValue(this.precinctsFile, FeatureCollection.class);
//        }
//        catch(IOException error){
//            error.printStackTrace();
//        }
//    }

    private void createPrecinctFile()throws IOException{
        String precinctFilePath = "src/main/resources/system/states/" +
                stateAbbreviation.toLowerCase() + "/Precincts.json";
        String precinctFilePathAbsolutePath = new File(precinctFilePath).getAbsolutePath();
        precinctsFile = new File(precinctFilePathAbsolutePath);
        precinctsGeoJson = mapper.readValue(precinctsFile, FeatureCollection.class);
    }

    private void createAlgorithmPrecinctFile()throws IOException{
        String algoPrecinctFilePath = "src/main/resources/system/states/" +
                stateAbbreviation.toLowerCase() + "/AlgorithmPrecincts.json";
        String algoPrecinctFilePathAbsolutePath = new File(algoPrecinctFilePath).getAbsolutePath();
        algorithmPrecinctsFile = new File(algoPrecinctFilePathAbsolutePath);
        algorithmPrecinctsJson = mapper.readTree(algorithmPrecinctsFile);
    }

    private void createPrecinctsCoordinateFile()throws IOException{
        String precinctsCoordinatesPath = "src/main/resources/system/states/" +
                stateAbbreviation.toLowerCase() + "/PrecinctsCoordinates.json";
        String precinctsCoordinatesAbsolutePath = new File(precinctsCoordinatesPath).getAbsolutePath();
        precinctsCoordinatesFile = new File(precinctsCoordinatesAbsolutePath);
    }


    public void initializeSystemFiles(){
        if(stateAbbreviation == null){
            throw new NullPointerException("State abbreviation doesn't exist ");
        }
        try{
            enactedPlan = new Plan(stateAbbreviation,"Enacted",0,57,true);
            createPrecinctFile();
            createAlgorithmPrecinctFile();
        }
        catch(IOException error){
            error.printStackTrace();
        }
    }

    public File getDemographicHeatMap(String censusEthnicity){
        String filename = "other.json";
        switch(censusEthnicity) {
            case "White":
                filename = "white.json";
                break;
            case "Black or African American":
                filename = "black.json";
                break;
            case "American Indian or Alaska Native":
                filename = "american_indian.json";
                break;
            case "Asian":
                filename = "asian.json";
                break;
            case "Native Hawaiian or Other Pacific Islander":
                filename = "native_hawaiian.json";
                break;
            case "Hispanic":
                filename = "hispanic.json";
                break;
        }
        String heatmapFilePath = "src/main/resources/system/states/" +
                stateAbbreviation.toLowerCase() + "/heatmap/" + filename;
        String heatmapFileAbsolutePath = new File(heatmapFilePath).getAbsolutePath();
        return new File(heatmapFileAbsolutePath);
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

    public void setTotalPopulation(long totalPopulation){ this.totalPopulation = totalPopulation; }

    public long getTotalPopulation() {
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

    public Demographic getDemographic() {
        return demographic;
    }

    public void setDemographic(Demographic demographic) {
        this.demographic = demographic;
    }

    public void setTotalPopulation(int totalPopulation) {
        this.totalPopulation = totalPopulation;
    }

    public JsonNode getPrecinctsCoordinatesJson() {
        return precinctsCoordinatesJson;
    }

    public FeatureCollection getPrecinctsGeoJson() {
        return precinctsGeoJson;
    }

    @JsonIgnore
    public Precinct getPrecinctByFIPSCode(String FIPS){
        return statePrecincts.stream()
        .filter(p-> p.getPrecinctFIPSCode().equals(FIPS))
        .findFirst()
        .orElseThrow(NoSuchElementException::new);
    }

    @JsonIgnore
    public Map getClientPrecinctsGeoJson() {
        Map <String, Object> map = new HashMap<>();
        map.put("precinctsGeoJson", precinctsGeoJson);
        return map;
    }

    public File getAlgorithmPrecinctsFile() {
        return algorithmPrecinctsFile;
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