package com.cse416.backend.model;
import java.util.List;

import com.cse416.backend.model.enums.CensusCatagories;
import com.cse416.backend.model.enums.ClientCompactness;
import com.cse416.backend.model.enums.JobStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

public class Job{
    private String stateAbbrev;
    private String jobID;
    private int seawulfJobID;
    private String jobName;
    private int numOfDistricts;
    private int numDistrictingPlan;
    private ClientCompactness clientCompactness;
    private double populationDifference;
    private CensusCatagories focusedMinority;
    private JobStatus status;
    private String jobSummary;
    private List <Plan> allDistrictingPlan;
    private List <Plan> otherDistrictingPlan;
    private Plan averageDistrictPlan;
    private Plan extremeDistrictPlan;
    private Plan randomDistrictPlan;
    private int averagePlanPopulation;
    private int averagePlanCompactness;

    public Job(String jobName, String stateAbbrev, String jobID, int seawulfJobID, int numOfDistricts, int numDistrictingPlan, double populationDifference, ClientCompactness clientCompactness, CensusCatagories focusedMinority, JobStatus status) {
        this.stateAbbrev = stateAbbrev;
        this.jobID = jobID;
        this.seawulfJobID = seawulfJobID;
        this.jobName = jobName;
        this.numOfDistricts = numOfDistricts;
        this.numDistrictingPlan = numDistrictingPlan;
        this.clientCompactness = clientCompactness;
        this.populationDifference = populationDifference;
        this.focusedMinority = focusedMinority;
        this.status = status;
    }

    public Job(String stateAbbrev, String jobID, int seawulfJobID, String jobName, int numOfDistricts, int numDistrictingPlan, ClientCompactness clientCompactness, long populationDifference, CensusCatagories focusedMinority, JobStatus status, String jobSummary, List<Plan> allDistrictingPlan, List<Plan> otherDistrictingPlan, Plan averageDistrictPlan, Plan extremeDistrictPlan, Plan randomDistrictPlan, int averagePlanPopulation, int averagePlanCompactness) {
        this.stateAbbrev = stateAbbrev;
        this.jobID = jobID;
        this.seawulfJobID = seawulfJobID;
        this.jobName = jobName;
        this.numOfDistricts = numOfDistricts;
        this.numDistrictingPlan = numDistrictingPlan;
        this.clientCompactness = clientCompactness;
        this.populationDifference = populationDifference;
        this.focusedMinority = focusedMinority;
        this.status = status;
        this.jobSummary = jobSummary;
        this.allDistrictingPlan = allDistrictingPlan;
        this.otherDistrictingPlan = otherDistrictingPlan;
        this.averageDistrictPlan = averageDistrictPlan;
        this.extremeDistrictPlan = extremeDistrictPlan;
        this.randomDistrictPlan = randomDistrictPlan;
        this.averagePlanPopulation = averagePlanPopulation;
        this.averagePlanCompactness = averagePlanCompactness;
    }


    public String getStateAbbrev() {
        return stateAbbrev;
    }

    public void setStateAbbrev(String stateAbbrev) {
        this.stateAbbrev = stateAbbrev;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public int getSeawulfJobID() {
        return seawulfJobID;
    }

    public void setSeawulfJobID(int seawulfJobID) {
        this.seawulfJobID = seawulfJobID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getNumOfDistricts() {
        return numOfDistricts;
    }

    public void setNumOfDistricts(int numOfDistricts) {
        this.numOfDistricts = numOfDistricts;
    }

    public int getNumDistrictingPlan() {
        return numDistrictingPlan;
    }

    public void setNumDistrictingPlan(int numDistrictingPlan) {
        this.numDistrictingPlan = numDistrictingPlan;
    }

    public ClientCompactness getClientCompactness() {
        return clientCompactness;
    }

    public void setClientCompactness(ClientCompactness clientCompactness) {
        this.clientCompactness = clientCompactness;
    }

    public double getPopulationDifference() {
        return populationDifference;
    }

    public void setPopulationDifference(double populationDifference) {
        this.populationDifference = populationDifference;
    }

    public CensusCatagories getFocusedMinority() {
        return focusedMinority;
    }

    public void setFocusedMinority(CensusCatagories focusedMinority) {
        this.focusedMinority = focusedMinority;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getJobSummary() {
        return jobSummary;
    }

    public void setJobSummary(String jobSummary) {
        this.jobSummary = jobSummary;
    }

    public List<Plan> getAllDistrictingPlan() {
        return allDistrictingPlan;
    }

    public void setAllDistrictingPlan(List<Plan> allDistrictingPlan) {
        this.allDistrictingPlan = allDistrictingPlan;
    }

    public List<Plan> getOtherDistrictingPlan() {
        return otherDistrictingPlan;
    }

    public void setOtherDistrictingPlan(List<Plan> otherDistrictingPlan) {
        this.otherDistrictingPlan = otherDistrictingPlan;
    }

    public Plan getAverageDistrictPlan() {
        return averageDistrictPlan;
    }

    public void setAverageDistrictPlan(Plan averageDistrictPlan) {
        this.averageDistrictPlan = averageDistrictPlan;
    }

    public Plan getExtremeDistrictPlan() {
        return extremeDistrictPlan;
    }

    public void setExtremeDistrictPlan(Plan extremeDistrictPlan) {
        this.extremeDistrictPlan = extremeDistrictPlan;
    }

    public Plan getRandomDistrictPlan() {
        return randomDistrictPlan;
    }

    public void setRandomDistrictPlan(Plan randomDistrictPlan) {
        this.randomDistrictPlan = randomDistrictPlan;
    }

    public int getAveragePlanPopulation() {
        return averagePlanPopulation;
    }

    public void setAveragePlanPopulation(int averagePlanPopulation) {
        this.averagePlanPopulation = averagePlanPopulation;
    }

    public int getAveragePlanCompactness() {
        return averagePlanCompactness;
    }

    public void setAveragePlanCompactness(int averagePlanCompactness) {
        this.averagePlanCompactness = averagePlanCompactness;
    }

    public Map<String, Object>  getClientInitialData(){
        Map<String, Object> clientJob = new HashMap<>();
        clientJob.put("stateAbbrev", this.stateAbbrev);
        clientJob.put("jobID", this.jobID);
        clientJob.put("jobName", this.jobName);
        clientJob.put("numOfDistricts", this.numOfDistricts);
        clientJob.put("numDistrictingPlan", this.numDistrictingPlan);
        clientJob.put("clientCompactness", this.clientCompactness);
        clientJob.put("populationDifference", this.populationDifference);
        clientJob.put("focusedMinority", this.focusedMinority);
        clientJob.put("status", this.status);
        return clientJob;
    }

    @Override
    public String toString() {
        return "Job{" +
                "stateAbbrev='" + stateAbbrev + '\'' +
                ", jobID='" + jobID + '\'' +
                ", seawulfJobID=" + seawulfJobID +
                ", jobName='" + jobName + '\'' +
                ", numOfDistricts=" + numOfDistricts +
                ", numDistrictingPlan=" + numDistrictingPlan +
                ", clientCompactness=" + clientCompactness +
                ", populationDifference=" + populationDifference +
                ", focusedMinority=" + focusedMinority +
                ", status=" + status +
                ", jobSummary='" + jobSummary + '\'' +
                ", allDistrictingPlan=" + allDistrictingPlan +
                ", otherDistrictingPlan=" + otherDistrictingPlan +
                ", averageDistrictPlan=" + averageDistrictPlan +
                ", extremeDistrictPlan=" + extremeDistrictPlan +
                ", randomDistrictPlan=" + randomDistrictPlan +
                ", averagePlanPopulation=" + averagePlanPopulation +
                ", averagePlanCompactness=" + averagePlanCompactness +
                '}';
    }
}

