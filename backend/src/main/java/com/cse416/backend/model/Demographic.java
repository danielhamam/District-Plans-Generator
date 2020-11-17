package com.cse416.backend.model;
import com.cse416.backend.model.enums.CensusCatagories;

import java.util.Map;
import javax.persistence.*;
import java.lang.Integer;


//@Entity
//@Table(name="Demographics")
public class Demographic {

    //@Id
    //@GeneratedValue
    private Integer demographicId;

    //@Transient
    private long totalPopulation;

    //@Transient
    private String referenceType;

    private Map<CensusCatagories, Integer> ethnicityPopulation;

    private Map<CensusCatagories, Integer> votingAgePopulation;

    //Neccessary for JPA
    protected Demographic (){}

    public Demographic(long totalPopulation){
        this.totalPopulation = totalPopulation;

    }

    public Demographic(long totalPopulation, Map<CensusCatagories, Integer> ethnicityPopulation, Map<CensusCatagories, Integer> votingAgePopulation) {
        this.totalPopulation = totalPopulation;
        this.ethnicityPopulation = ethnicityPopulation;
        this.votingAgePopulation = votingAgePopulation;
    }

    public Demographic(long totalPopulation, String referenceType, Map<CensusCatagories, Integer> ethnicityPopulation, Map<CensusCatagories, Integer> votingAgePopulation) {
        this.totalPopulation = totalPopulation;
        this.referenceType = referenceType;
        this.ethnicityPopulation = ethnicityPopulation;
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

    public Map<CensusCatagories, Integer> getEthnicityPopulation() {
        return ethnicityPopulation;
    }

    public void setEthnicityPopulation(Map<CensusCatagories, Integer> ethnicityPopulation) {
        this.ethnicityPopulation = ethnicityPopulation;
    }

    public Map<CensusCatagories, Integer> getVotingAgePopulation() {
        return votingAgePopulation;
    }

    public void setVotingAgePopulation(Map<CensusCatagories, Integer> votingAgePopulation) {
        this.votingAgePopulation = votingAgePopulation;
    }

    @Override
    public String toString() {
        return "Demographic{" +
                "totalPopulation=" + totalPopulation +
                ", referenceType='" + referenceType + '\'' +
                ", ethnicityPopulation=" + ethnicityPopulation +
                ", votingAgePopulation=" + votingAgePopulation +
                '}';
    }
}
