package com.cse416.backend.model;
import java.util.List;

import com.cse416.backend.model.enums.CensusCatagories;
import com.cse416.backend.model.enums.ClientCompactness;
import com.cse416.backend.model.enums.JobStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

import java.util.HashMap;
import java.util.Map;
import java.lang.Integer;
 
@Entity
@Table(name = "Jobs")
public class Job{

    @Id
    @GeneratedValue
    @Column(name = "jobId")
    private Integer generatedId;

    @JsonProperty("jobName")
    @Column(nullable=true)
    private String jobName;

    @JsonProperty
    @Transient
    private List<CensusCatagories> minorityAnalyzed;

    @JsonProperty("compactness")
    @Column(name = "compactness")
    private ClientCompactness clientCompactness;

    @JsonProperty("populationDifference")
    private double populationDifference;

    @JsonProperty("plansAmount")
    @Column(name = "numberOfPlans")
    private int numDistrictingPlan;

    @JsonProperty("districtsAmount")
    @Column(name = "numberOfDistricts")
    private int numOfDistricts;

    @JsonProperty
    @Column(name = "jobStatus", nullable=false)
    private JobStatus status;

    @JsonProperty
    @Transient
    private String jobID;

    @JsonIgnore
    @Transient
    private Plan averageDistrictPlan;

    @JsonIgnore
    @Transient
    private Plan extremeDistrictPlan;

    @JsonIgnore
    @Transient
    private Plan randomDistrictPlan;

    @JsonIgnore
    @Column(name = "stateId", nullable=false, length=2)
    private String stateAbbrev;

    @JsonIgnore
    @Transient
    private int stateFIPSCode;

    @JsonIgnore
    @Transient
    private int averagePlanPopulation;

    @JsonIgnore
    @Transient
    private int averagePlanCompactness;

    @JsonIgnore
    @Transient
    private int seawulfJobID;

    @JsonIgnore
    @Transient
    private String jobSummary;

    @JsonIgnore
    @Transient
    private List <Plan> allDistrictingPlan;

    @JsonIgnore
    @Transient
    private List <Plan> otherDistrictingPlan;

    @JsonIgnore
    @Transient
    private BoxWhisker boxWhisker;
    
    //Neccessary for JPA
    protected Job (){}

    public Job (@JsonProperty("jobName")String jobName, 
                @JsonProperty("districtsAmount")int numOfDistricts, 
                @JsonProperty("plansAmount")int numDistrictingPlan, 
                @JsonProperty("populationDifference")double populationDifference, 
                @JsonProperty("compactness")ClientCompactness clientCompactness, 
                @JsonProperty("minorityAnalyzed") List<CensusCatagories> minorityAnalyzed){
        this.jobName = jobName;
        this.numOfDistricts = numOfDistricts;
        this.numDistrictingPlan = numDistrictingPlan;
        this.clientCompactness = clientCompactness;
        this.populationDifference = populationDifference;
        this.minorityAnalyzed = minorityAnalyzed;
        this.status = JobStatus.PENDING;
    }
    
    public Job(String stateAbbrev, String jobName, String jobID, int seawulfJobID, int numOfDistricts,
               int numDistrictingPlan, double populationDifference, ClientCompactness clientCompactness,
               List<CensusCatagories> minorityAnalyzed, JobStatus status, BoxWhisker boxWhisker){
        this.stateAbbrev = stateAbbrev;
        this.jobName = jobName;
        this.jobID = jobID;
        this.seawulfJobID = seawulfJobID;
        this.numOfDistricts = numOfDistricts;
        this.numDistrictingPlan = numDistrictingPlan;
        this.clientCompactness = clientCompactness;
        this.populationDifference = populationDifference;
        this.minorityAnalyzed = minorityAnalyzed;
        this.status = status;
        this.boxWhisker = boxWhisker;
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

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
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

    public List<CensusCatagories> getminorityAnalyzed() {
        return minorityAnalyzed;
    }

    public void setminorityAnalyzed(List<CensusCatagories> minorityAnalyzed) {
        this.minorityAnalyzed = minorityAnalyzed;
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


    public Plan getPlanByID(String planID){
        if(planID.equals(averageDistrictPlan.getPlanID())){
            return averageDistrictPlan;
        }
        else if(planID.equals(extremeDistrictPlan.getPlanID())) {
            return extremeDistrictPlan;
        }
        else if(planID.equals(randomDistrictPlan.getPlanID())) {
            return randomDistrictPlan;
        }
        else{

        }
        return null;

    }

    // @JsonIgnore
    // public Map<String, Object> getClientInitialData(){
    //     Map<String, Object> clientJob = new HashMap<>();
    //     clientJob.put("stateAbbrev", this.stateAbbrev);
    //     clientJob.put("jobID", this.jobID);
    //     clientJob.put("jobName", this.jobName);
    //     clientJob.put("numOfDistricts", this.numOfDistricts);
    //     clientJob.put("numDistrictingPlan", this.numDistrictingPlan);
    //     clientJob.put("clientCompactness", this.clientCompactness);
    //     clientJob.put("populationDifference", this.populationDifference);
    //     clientJob.put("minorityAnalyzed", this.minorityAnalyzed);
    //     clientJob.put("status", this.status);
    //     return clientJob;
    // }

    @JsonIgnore
    public Map<String, Object> getClientPlans(){
        Map<String, Object> clientJob = new HashMap<>();
        clientJob.put("averageDistrictPlan", this.averageDistrictPlan);
        clientJob.put("extremeDistrictPlan", this.extremeDistrictPlan);
        clientJob.put("randomDistrictPlan", this.randomDistrictPlan);
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
                ", minorityAnalyzed=" + minorityAnalyzed +
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

