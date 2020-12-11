package com.cse416.backend.model.plan;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.cse416.backend.model.regions.district.District;
import com.cse416.backend.model.regions.precinct.Precinct;
import com.cse416.backend.model.regions.state.State;
import com.cse416.backend.model.job.*;

import java.lang.*;
import java.util.*;
import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.geojson.FeatureCollection;
import javax.persistence.*;

@Entity
@Table(name="Plans")
public class Plan{
    
   
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="planId")
    @JsonProperty
    private Integer planID;

    @ManyToOne(targetEntity=Job.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="jobId")
    @JsonIgnore
    private Job job;

    @JsonProperty
    private int numberOfDistricts;

    @OneToMany(targetEntity=District.class,cascade = CascadeType.ALL,
    fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinTable(
        name = "PlanDistricts",
        joinColumns = @JoinColumn(name = "planId"),
        inverseJoinColumns = @JoinColumn(name = "districtId")
    )
    @JsonIgnore
    private List<District> districts;

    @JsonProperty
    private double averageDistrictPopulation;

    @JsonProperty
    private double averageDistrictCompactness;

    @Transient
    @JsonProperty
    private boolean isPlanEnacted;

    @Transient
    @JsonProperty
    private String type;

    @Transient
    @JsonProperty
    private String stateAbbreviation;

    @Transient
    @JsonIgnore
    private File districtFile;

    @JsonProperty
    @Transient
    private FeatureCollection districtsGeoJson;
    // // //https://github.com/opendatalab-de/geojson-jackson

    @JsonIgnore
    @Transient
    private int x;

    @JsonIgnore
    @Transient
    private int y;


    //Neccessary for JPA
    protected Plan (){}

    public Plan(String stateAbbreviation, String type, Integer planID, int numberOfDistricts,boolean isPlanEnacted) {
        this.stateAbbreviation = stateAbbreviation;
        this.type = type;
        this.planID = planID;
        this.numberOfDistricts = numberOfDistricts;
        this.isPlanEnacted = isPlanEnacted;
        String filePath = "src/main/resources/system/states/" +
                stateAbbreviation.toLowerCase() + "/" + this.type + "Districts.json";
        this.districtFile = new File(new File(filePath).getAbsolutePath());
        try{
            this.districtsGeoJson = createDistrictFeatureCollection();
        }
        catch(IOException error){
            error.printStackTrace();
        }
    }


    public Plan(Job job, int numberOfDistricts, double averageDistrictPopulation, double averageDistrictCompactness, String type){
        this.job = job;
        this.numberOfDistricts = numberOfDistricts;
        this.averageDistrictPopulation = averageDistrictPopulation;
        this.averageDistrictCompactness = averageDistrictCompactness;
        this.type = type;
    }

    public FeatureCollection createDistrictFeatureCollection()throws IOException{
        return new ObjectMapper().readValue(this.districtFile, FeatureCollection.class);
    }
    
    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }

    public String getstateAbbreviation() {
        return stateAbbreviation;
    }

    public void setstateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public Integer getPlanID() {
        return planID;
    }

    public void setPlanID(Integer planID) {
        this.planID = planID;
    }

    public int getNumberOfDistricts() {
        return numberOfDistricts;
    }

    public void setNumberOfDistricts(int numberOfDistricts) {
        this.numberOfDistricts = numberOfDistricts;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public double getAverageDistrictPopulation() {
        return averageDistrictPopulation;
    }

    public void setAverageDistrictPopulation(int averageDistrictPopulation) {
        this.averageDistrictPopulation = averageDistrictPopulation;
    }

    public double getAverageDistrictCompactness() {
        return averageDistrictCompactness;
    }

    public void setAverageDistrictCompactness(int averageDistrictCompactness) {
        this.averageDistrictCompactness = averageDistrictCompactness;
    }

    public boolean isisPlanEnacted() {
        return isPlanEnacted;
    }

    public void setisPlanEnacted(boolean isPlanEnacted) {
        this.isPlanEnacted = isPlanEnacted;
    }

    public FeatureCollection getDistrictsGeoJson() {
        return districtsGeoJson;
    }

    public void setDistrictsGeoJson(FeatureCollection districtFeatureCollection) {
        this.districtsGeoJson = districtFeatureCollection;
    }

    // @JsonIgnore
    // public Map<String, Object> getClientInitialData(){
    //     Map<String, Object> clientPlan = new HashMap<>();
    //     clientPlan.put("type", this.type);
    //     clientPlan.put("planID", this.planID);
    //     clientPlan.put("numOfDistricts", this.numOfDistricts);
    //     clientPlan.put("numOfCounties", this.numOfCounties);
    //     List <Object> clientDistrict = new ArrayList<>();
    //     districts.forEach(district -> clientDistrict.add(district.getClientInitialData()));
    //     clientPlan.put("districts", clientDistrict);
    //     return clientPlan;
    // }

    // @JsonIgnore
    // public List<District> getClientDistricts(){
    //     Map<String, Object> clientPlan = new HashMap<>();
    //     clientPlan.put("type", this.type);
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
                ", stateAbbreviation='" + stateAbbreviation + '\'' +
                "type='" + type + '\'' +
                ", planID='" + planID + '\'' +
                ", numberOfDistricts=" + numberOfDistricts +
                ", districts=" + districts +
                ", averageDistrictPopulation=" + averageDistrictPopulation +
                ", averageDistrictCompactness=" + averageDistrictCompactness +
                ", isPlanEnacted=" + isPlanEnacted +
                '}';
    }
}



