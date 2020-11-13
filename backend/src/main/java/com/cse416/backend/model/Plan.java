package com.cse416.backend.model;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.cse416.backend.model.regions.District;
import com.cse416.backend.model.regions.Precinct;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.geojson.FeatureCollection;

@Entity
@Table(name="Plans")
public class Plan{
    
    @JsonProperty
    @Transient
    private String type;

    @JsonProperty
    @Column(name = "stateID")
    private String stateAbbreviation;

    @JsonProperty
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String planID;

    @JsonProperty
    @Column(name = "numberOfDistricts")
    private int numberOfDistricts;

    @JsonProperty
    @Transient
    private boolean isPlanEnacted;

    @JsonIgnore
    @Transient
    private List<District> districts;

    @JsonIgnore
    private int averageDistrictPopulation;

    @JsonIgnore
    private int averageDistrictCompactness;

    @JsonIgnore
    @Transient
    private File districtFile;

    @JsonProperty
    @Transient
    private FeatureCollection districtsGeoJson;
    // // //https://github.com/opendatalab-de/geojson-jackson

    public Plan(String stateAbbreviation, String type, String planID, int numberOfDistricts,boolean isPlanEnacted) {
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

    public String getPlanID() {
        return planID;
    }

    public void setPlanID(String planID) {
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

    public boolean isisPlanEnacted() {
        return isPlanEnacted;
    }

    public void setisPlanEnacted(boolean isPlanEnacted) {
        this.isPlanEnacted = isPlanEnacted;
    }

    public FeatureCollection getDistrictGeoJson() {
        return districtsGeoJson;
    }

    public void setDistrictGeoJson(FeatureCollection districtFeatureCollection) {
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



