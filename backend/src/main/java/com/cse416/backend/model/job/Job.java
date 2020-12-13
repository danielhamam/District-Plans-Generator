package com.cse416.backend.model.job;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.cse416.backend.dao.services.CountyDAOService;
import com.cse416.backend.dao.services.DemographicDAOService;
import com.cse416.backend.dao.services.JobDAOService;
import com.cse416.backend.dao.services.PrecinctDAOService;
import com.cse416.backend.model.demographic.CensusEthnicity;
import com.cse416.backend.model.enums.CensusCatagories;
import com.cse416.backend.model.enums.ClientCompactness;
import com.cse416.backend.model.enums.JobStatus;
import com.cse416.backend.model.job.boxnwhisker.BoxWhisker;
import com.cse416.backend.model.job.boxnwhisker.BoxWhiskerPlot;
import com.cse416.backend.model.job.summary.Summary;
import com.cse416.backend.model.regions.district.District;
import com.cse416.backend.model.regions.state.State;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.cse416.backend.model.plan.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.geojson.FeatureCollection;
import org.springframework.beans.factory.annotation.Autowired;
import com.cse416.backend.model.regions.district.comparators.*;



import javax.persistence.*;

import java.lang.*;
import java.util.*;

@Entity
@Table(name = "Jobs")
public class Job{

    @Id
    @GeneratedValue
    @Column(name = "jobId")
    @JsonProperty("jobID")
    private Integer generatedId;

    @Column(nullable=true)
    @JsonProperty("jobName")
    private String jobName;

    @Column(name = "compactness")
    @JsonProperty("compactness")
    private ClientCompactness clientCompactness;

    @JsonProperty("populationDifference")
    private double populationDifference;

    @Column(name = "numberOfPlans")
    @JsonProperty("plansAmount")
    private int numDistrictingPlan;

    @Column(name = "numberOfDistricts")
    @JsonProperty("districtsAmount")
    private int numOfDistricts;

    @Column(name = "jobStatus")
    @JsonProperty("status")
    private JobStatus status;

    @Transient
    @JsonProperty("minorityAnalyzed")
    private List<CensusCatagories> minorityAnalyzedEnumration;

    @Transient
    @JsonProperty("districtPlans")
    private List<Plan> clientDistrictingPlans;

    @ManyToMany(targetEntity=CensusEthnicity.class, fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinTable(
            name = "JobMinorityGroups",
            joinColumns = @JoinColumn(name = "jobId"),
            inverseJoinColumns = @JoinColumn(name = "censusEthnicityId")
    )
    @JsonIgnore
    private List<CensusEthnicity> minorityAnalyzed;

    @ManyToOne(targetEntity=State.class, fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinColumn(name="stateId")
    @JsonIgnore
    private State state;

    @Transient
    @JsonIgnore
    private Plan averageDistrictPlan;

    @Transient
    @JsonIgnore
    private Plan extremeDistrictPlan;

    @Transient
    @JsonIgnore
    private Plan randomDistrictPlan;


    @Transient
    @JsonIgnore
    private int stateFIPSCode;

    @Transient
    @JsonIgnore
    private int averagePlanPopulation;

    @Transient
    @JsonIgnore
    private int averagePlanCompactness;

    @JsonIgnore
    @Column(name="seawulfJobID", unique=true)
    private String seawulfJobID;

    @Transient
    @JsonIgnore
    private Summary summary;
    
    @JsonIgnore
    @OneToMany(targetEntity=Plan.class,cascade = CascadeType.ALL,
    fetch = FetchType.LAZY, orphanRemoval = true, mappedBy ="job")
    private List <Plan> allPlans;

    @JsonIgnore
    @OneToOne(targetEntity=BoxWhisker.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "boxWhiskerId")
    private BoxWhisker boxWhisker;

    protected Job (){}

    public Job (@JsonProperty("jobName")String jobName,
                @JsonProperty("plansAmount")int numDistrictingPlan,
                @JsonProperty("populationDifference")double populationDifference,
                @JsonProperty("compactness")ClientCompactness clientCompactness,
                @JsonProperty("minorityAnalyzed") List<CensusCatagories> minorityAnalyzedEnumration){
        this.jobName = jobName;
        this.seawulfJobID = "0";
        this.numDistrictingPlan = numDistrictingPlan;
        this.clientCompactness = clientCompactness;
        this.populationDifference = populationDifference;
        this.minorityAnalyzedEnumration = minorityAnalyzedEnumration;
        this.status = JobStatus.PENDING;

    }

    private FeatureCollection createPrecinctFile(String name)throws IOException{
        String precinctFilePath = "src/main/resources/system/states/" +
                state.getStateAbbreviation().toLowerCase() + "/" + name + "District.json";
        String precinctFilePathAbsolutePath = new File(precinctFilePath).getAbsolutePath();
        File file = new File(precinctFilePathAbsolutePath);
        return new ObjectMapper().readValue(file, FeatureCollection.class);
    }

    public void initializeJobsFiles(){
       if(status == JobStatus.FINISHED){
           this.averageDistrictPlan = new Plan(state.getStateAbbreviation(),"Average",
                   state.getNumOfDistricts(),false);
           this.extremeDistrictPlan = new Plan(state.getStateAbbreviation(),"Extreme",
                   state.getNumOfDistricts(),false);
           this.randomDistrictPlan = new Plan(state.getStateAbbreviation(),"Random",
                   state.getNumOfDistricts(),false);


       }
    }


//    public Job (@JsonProperty("jobName")String jobName,
//                @JsonProperty("districtsAmount")int numOfDistricts,
//                @JsonProperty("plansAmount")int numDistrictingPlan,
//                @JsonProperty("populationDifference")double populationDifference,
//                @JsonProperty("compactness")ClientCompactness clientCompactness,
//                @JsonProperty("minorityAnalyzed") List<CensusCatagories> minorityAnalyzedEnumration){
//        //TODO: Format the information to be consistant with frontend and database
//        System.out.println("Job spring");
//        this.jobName = jobName;
//        this.seawulfJobID = "0";
//        this.numDistrictingPlan = numDistrictingPlan;
//        this.clientCompactness = clientCompactness;
//        this.populationDifference = populationDifference;
//        this.minorityAnalyzedEnumration = minorityAnalyzedEnumration;
//        this.status = JobStatus.PENDING;
//        this.numOfDistricts = numOfDistricts;
//        // this.clientStatus = status.getStringRepresentation();
////        for (CensusCatagories censusCatagories : minorityAnalyzed) {
////            this.clientMinorityAnalyzed.add(censusCatagories.getStringRepresentation());
////        }
//    }


    public Integer getJobID() {
        return generatedId;
    }

    public void setJobID(Integer jobID) {
        this.generatedId = jobID;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        this.numOfDistricts = state.getNumOfDistricts();
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getSeawulfJobID() {
        return seawulfJobID;
    }

    public void setSeawulfJobID(String seawulfJobID) {
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


    public int getNumDistrictingPlan() {
        return numDistrictingPlan;
    }

    public void setNumDistrictingPlan(int numDistrictingPlan) {
        this.numDistrictingPlan = numDistrictingPlan;
    }

    public List<CensusCatagories> getMinorityAnalyzedEnumration(){
        return this.minorityAnalyzedEnumration;
    }

    public void setMinorityAnalyzed(List<CensusEthnicity> minorityAnalyzed){
        this.minorityAnalyzed = minorityAnalyzed;
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

    public List<Plan> getAllPlans() {
        return allPlans;
    }

    public void setAllPlans(List<Plan> allPlans) {
        this.allPlans = allPlans;
    }

    public BoxWhisker getBoxWhisker() {
        return boxWhisker;
    }

    public void setBoxWhisker(BoxWhisker boxWhisker) {
        this.boxWhisker = boxWhisker;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }


    public void setAverageDistrictPlan(Plan averageDistrictPlan) {
        this.averageDistrictPlan = averageDistrictPlan;

//        FileWriter averageDistricts = new FileWriter(new File(jobDirectory).getAbsolutePath() + "/" + "AverageDistricts.json");
//        mapper.writeValue(averageDistricts, null);

    }

    public void setExtremeDistrictPlan(Plan extremeDistrictPlan) {
        this.extremeDistrictPlan = extremeDistrictPlan;

//        FileWriter extremeDistricts = new FileWriter(new File(jobDirectory).getAbsolutePath() + "/" + "ExtremeDistricts.json");
//        mapper.writeValue(extremeDistricts, null);
    }

    public void setRandomDistrictPlan(Plan randomDistrictPlan) {
        this.randomDistrictPlan = randomDistrictPlan;

//        FileWriter randomDistricts = new FileWriter(new File(jobDirectory).getAbsolutePath() + "/" + "RandomDistricts.json");
//        mapper.writeValue(randomDistricts, null);
    }

    public Plan getAverageDistrictPlan() {
        return averageDistrictPlan;
    }

    public Plan getExtremeDistrictPlan() {
        return extremeDistrictPlan;
    }

    public Plan getRandomDistrictPlan() {
        return randomDistrictPlan;
    }

    @JsonIgnore
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

    @JsonIgnore
    public List<CensusEthnicity> getMinorityAnalyzedCensusEthnicity() {
        return minorityAnalyzed;
    }

    public void setMinorityAnalyzedEnumration(List<CensusCatagories> minorityAnalyzedEnumration) {
        this.minorityAnalyzedEnumration = minorityAnalyzedEnumration;
    }

    @JsonIgnore
    public ArrayList getClientPlans(){
        ArrayList <Plan> list = new ArrayList<>();
        list.add(averageDistrictPlan);
        list.add(extremeDistrictPlan);
        list.add(randomDistrictPlan);
        return list;
    }

    @Override
    public String toString() {
        return "Job{" +
                ", jobName='" + jobName + '\'' +
                ", state='" + state.getStateAbbreviation() + " Object" + '\'' +
                ", jobID=" + generatedId +
                ", seawulfJobID=" + seawulfJobID +
                ", status=" + status +
                ", clientCompactness=" + clientCompactness +
                ", populationDifference=" + populationDifference +
                ", numOfDistricts=" + numOfDistricts +
                ", numDistrictingPlan=" + numDistrictingPlan +
                ", jobSummary='" + summary + '\'' +
                ", allPlans=" + allPlans +
                ", averageDistrictPlan=" + averageDistrictPlan +
                ", extremeDistrictPlan=" + extremeDistrictPlan +
                ", randomDistrictPlan=" + randomDistrictPlan +
                ", averagePlanPopulation=" + averagePlanPopulation +
                ", averagePlanCompactness=" + averagePlanCompactness +
                ", minorityAnalyzedEnumaration=" + minorityAnalyzedEnumration +
                ", minorityAnalyzed=" + minorityAnalyzed +
                ", boxWhisker=" + boxWhisker +
                '}';
    }
}

