package com.cse416.backend.model;
import java.util.List;

import com.cse416.backend.model.regions.District;
import com.cse416.backend.model.regions.Precinct;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Plan{
    @JsonProperty("type")
    private String planName;
    private String stateAbbrev;
    private String planID;
    private int numOfDistricts;
    private int numOfCounties;
    private int numofPrecincts;
    private boolean isEnactedPlan;
    private int year;
    @JsonIgnore
    private List<District> districts;
    @JsonIgnore
    private int averageDistrictPopulation;
    @JsonIgnore
    private int averageDistrictCompactness;

    public Plan(String planName, String stateAbbrev, String planID, int numOfDistricts, int numOfCounties, int numofPrecincts,boolean isEnactedPlan, int year) {
        this.planName = planName;
        this.stateAbbrev = stateAbbrev;
        this.planID = planID;
        this.numOfDistricts = numOfDistricts;
        this.numOfCounties = numOfCounties;
        this.numofPrecincts = numofPrecincts;
        this.isEnactedPlan = isEnactedPlan;
        this.year = year;
    }
    public Plan(String planName, String stateAbbrev, String planID, int numOfDistricts, int numOfCounties, int numofPrecincts, boolean isEnactedPlan, int year, List<District> districts){
        this.planName = planName;
        this.stateAbbrev = stateAbbrev;
        this.planID = planID;
        this.numOfDistricts = numOfDistricts;
        this.numOfCounties = numOfCounties;
        this.numofPrecincts = numofPrecincts;
        this.isEnactedPlan = isEnactedPlan;
        this.districts = districts;
        this.year = year;
    }
    

    public Plan(String planName, String stateAbbrev, String planID, int numOfDistricts, int numOfCounties, int numofPrecincts, List<District> districts, int averageDistrictPopulation, int averageDistrictCompactness, boolean isEnactedPlan, int year) {
        this.planName = planName;
        this.stateAbbrev = stateAbbrev;
        this.planID = planID;
        this.numOfDistricts = numOfDistricts;
        this.numOfCounties = numOfCounties;
        this.numofPrecincts = numofPrecincts;
        this.districts = districts;
        this.averageDistrictPopulation = averageDistrictPopulation;
        this.averageDistrictCompactness = averageDistrictCompactness;
        this.isEnactedPlan = isEnactedPlan;
        this.year = year;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getStateAbbrev() {
        return stateAbbrev;
    }

    public void setStateAbbrev(String stateAbbrev) {
        this.stateAbbrev = stateAbbrev;
    }

    public String getPlanID() {
        return planID;
    }

    public void setPlanID(String planID) {
        this.planID = planID;
    }

    public int getNumOfDistricts() {
        return numOfDistricts;
    }

    public void setNumOfDistricts(int numOfDistricts) {
        this.numOfDistricts = numOfDistricts;
    }

    public int getNumOfCounties() {
        return numOfCounties;
    }

    public void setNumOfCounties(int numOfCounties) {
        this.numOfCounties = numOfCounties;
    }

    public int getNumofPrecincts() {
        return numofPrecincts;
    }

    public void setNumofPrecincts(int numofPrecincts) {
        this.numofPrecincts = numofPrecincts;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public int getAverageDistrictPopulation() {
        return averageDistrictPopulation;
    }

    public void setAverageDistrictPopulation(int averageDistrictPopulation) {
        this.averageDistrictPopulation = averageDistrictPopulation;
    }

    public int getAverageDistrictCompactness() {
        return averageDistrictCompactness;
    }

    public void setAverageDistrictCompactness(int averageDistrictCompactness) {
        this.averageDistrictCompactness = averageDistrictCompactness;
    }

    public boolean isisEnactedPlan() {
        return isEnactedPlan;
    }

    public void setisEnactedPlan(boolean isEnactedPlan) {
        this.isEnactedPlan = isEnactedPlan;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @JsonIgnore
    public Map<String, Object> getClientInitialData(){
        Map<String, Object> clientPlan = new HashMap<>();
        clientPlan.put("type", this.planName);
        clientPlan.put("planID", this.planID);
        clientPlan.put("numOfDistricts", this.numOfDistricts);
        clientPlan.put("numOfCounties", this.numOfCounties);
        List <Object> clientDistrict = new ArrayList<>();
        districts.forEach(district -> clientDistrict.add(district.getClientInitialData()));
        clientPlan.put("districts", clientDistrict);
        return clientPlan;

    }

    // @JsonIgnore
    // public List<District> getClientDistricts(){
    //     Map<String, Object> clientPlan = new HashMap<>();
    //     clientPlan.put("planName", this.planName);
    //     clientPlan.put("planID", this.planID);
    //     clientPlan.put("numOfDistricts", this.numOfDistricts);
    //     clientPlan.put("numOfCounties", this.numOfCounties);
    //     List <Object> clientDistrict = new ArrayList<>();
    //     districts.forEach(district -> clientDistrict.add(district.getClientInitialData()));
    //     clientPlan.put("districts", clientDistrict);
    //     return clientPlan;
       
    // }

    @Override
    public String toString() {
        return "Plan{" +
                "planName='" + planName + '\'' +
                ", stateAbbrev='" + stateAbbrev + '\'' +
                ", planID='" + planID + '\'' +
                ", numOfDistricts=" + numOfDistricts +
                ", numOfCounties=" + numOfCounties +
                ", numofPrecincts=" + numofPrecincts +
                ", districts=" + districts +
                ", averageDistrictPopulation=" + averageDistrictPopulation +
                ", averageDistrictCompactness=" + averageDistrictCompactness +
                ", isEnactedPlan=" + isEnactedPlan +
                ", year=" + year +
                '}';
    }
}



