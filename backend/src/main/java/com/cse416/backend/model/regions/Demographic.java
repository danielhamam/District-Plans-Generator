package com.cse416.backend.model.regions;
import com.cse416.backend.model.enums.CensusCatagories;
import java.lang.reflect.Array;
import java.util.HashMap;

public class Demographic {
    private long totalPopulation;
    private String referenceType;
    private HashMap<CensusCatagories, Array> ethnicityPopulation;
    private HashMap<CensusCatagories, Array> agePopulation;
    private HashMap<CensusCatagories, Array> votingAgePopulation;

    public Demographic(long totalPopulation){
        this.totalPopulation = totalPopulation;

    }

    public Demographic(long totalPopulation, String referenceType, HashMap<CensusCatagories, Array> ethnicityPopulation, HashMap<CensusCatagories, Array> agePopulation, HashMap<CensusCatagories, Array> votingAgePopulation) {
        this.totalPopulation = totalPopulation;
        this.referenceType = referenceType;
        this.ethnicityPopulation = ethnicityPopulation;
        this.agePopulation = agePopulation;
        this.votingAgePopulation = votingAgePopulation;
    }

    public long getTotalPopulation() {
        return totalPopulation;
    }

    public void setTotalPopulation(long totalPopulation) {
        this.totalPopulation = totalPopulation;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public HashMap<CensusCatagories, Array> getEthnicityPopulation() {
        return ethnicityPopulation;
    }

    public void setEthnicityPopulation(HashMap<CensusCatagories, Array> ethnicityPopulation) {
        this.ethnicityPopulation = ethnicityPopulation;
    }

    public HashMap<CensusCatagories, Array> getAgePopulation() {
        return agePopulation;
    }

    public void setAgePopulation(HashMap<CensusCatagories, Array> agePopulation) {
        this.agePopulation = agePopulation;
    }

    public HashMap<CensusCatagories, Array> getVotingAgePopulation() {
        return votingAgePopulation;
    }

    public void setVotingAgePopulation(HashMap<CensusCatagories, Array> votingAgePopulation) {
        this.votingAgePopulation = votingAgePopulation;
    }

    @Override
    public String toString() {
        return "Demographic{" +
                "totalPopulation=" + totalPopulation +
                ", referenceType='" + referenceType + '\'' +
                ", ethnicityPopulation=" + ethnicityPopulation +
                ", agePopulation=" + agePopulation +
                ", votingAgePopulation=" + votingAgePopulation +
                '}';
    }
}
