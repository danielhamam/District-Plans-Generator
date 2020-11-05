package com.cse416.backend.model;
import java.util.List;

import com.cse416.backend.model.regions.District;
import com.cse416.backend.model.regions.Precinct;


public class Plan{
    String name;
    String stateAbbrev;
    String planID;
    int numOfDistricts;
    int numOfCounties;
    int numofPrecincts;
    List<District> districts;
    List<Precinct> precinct;
    int averageDistrictPopulation;
    int averageDistrictCompactness;
    boolean isEnactedPlan;
    int year;

    public Plan(String name, String stateAbbrev, String planID, int numOfDistricts, int numOfCounties, int numofPrecincts,boolean isEnactedPlan, int year) {
        this.name = name;
        this.stateAbbrev = stateAbbrev;
        this.planID = planID;
        this.numOfDistricts = numOfDistricts;
        this.numOfCounties = numOfCounties;
        this.numofPrecincts = numofPrecincts;
        this.isEnactedPlan = isEnactedPlan;
        this.year = year;
    }
    
    public Plan(String name, String stateAbbrev, String planID, int numOfDistricts, int numOfCounties, int numofPrecincts, List<District> districts, List<Precinct> precinct, int averageDistrictPopulation, int averageDistrictCompactness, boolean isEnactedPlan, int year) {
        this.name = name;
        this.stateAbbrev = stateAbbrev;
        this.planID = planID;
        this.numOfDistricts = numOfDistricts;
        this.numOfCounties = numOfCounties;
        this.numofPrecincts = numofPrecincts;
        this.districts = districts;
        this.precinct = precinct;
        this.averageDistrictPopulation = averageDistrictPopulation;
        this.averageDistrictCompactness = averageDistrictCompactness;
        this.isEnactedPlan = isEnactedPlan;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Precinct> getPrecinct() {
        return precinct;
    }

    public void setPrecinct(List<Precinct> precinct) {
        this.precinct = precinct;
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

    @Override
    public String toString() {
        return "Plan{" +
                "name='" + name + '\'' +
                ", stateAbbrev='" + stateAbbrev + '\'' +
                ", planID='" + planID + '\'' +
                ", numOfDistricts=" + numOfDistricts +
                ", numOfCounties=" + numOfCounties +
                ", numofPrecincts=" + numofPrecincts +
                ", districts=" + districts +
                ", precinct=" + precinct +
                ", averageDistrictPopulation=" + averageDistrictPopulation +
                ", averageDistrictCompactness=" + averageDistrictCompactness +
                ", isEnactedPlan=" + isEnactedPlan +
                ", year=" + year +
                '}';
    }
}



