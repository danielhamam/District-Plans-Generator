package com.cse416.backend.model.demographic;
import com.cse416.backend.model.enums.CensusCatagories;

import java.util.Map;
import javax.persistence.*;
import java.lang.Integer;


@Entity
@Table(name="Demographics")
public class Demographic {

    @Id
    @GeneratedValue
    private Integer demographicId;

    @OneToOne
    @JoinColumn(name = "fk_generalDemographic")
    private CensusGeneralDemographic generalDemographic;

    @OneToOne
    @JoinColumn(name = "fk_votingAgeDemographic")
    private CensusVotingAgeDemographic votingAgeDemographic;

    //Neccessary for JPA
    protected Demographic (){}

    public Demographic(CensusGeneralDemographic generalDemographic, CensusVotingAgeDemographic votingAgeDemographic){
        this.generalDemographic = generalDemographic;
        this.votingAgeDemographic = votingAgeDemographic;
    }

    public Integer getId(){return this.demographicId;}

    public CensusGeneralDemographic getGeneralDemographic(){return this.generalDemographic;}

    public CensusVotingAgeDemographic getVotingAgeDemographic(){return this.votingAgeDemographic;}

    public Integer getGeneralDemographicId(){return this.generalDemographic.getId();}

    public Integer getVotingAgeDemographicId(){return this.votingAgeDemographic.getId();}

    public Long getGeneralDemographicTotalPopulation(){ return this.generalDemographic.getTotalPopulation();}

    public Long getVotingAgeDemographicTotalPopulation(){ return this.generalDemographic.getTotalPopulation();}

    // public String toString(){
    //     return this.generalDemographic.toString() + '\n' + this.votingAgeDemographic.toString();
    // }

    //////////CODE TO BE REMOVED AND PLACED IN THE APPROPRIATE CLASS
    @Transient
    private long totalPopulation;

    @Transient
    private String referenceType;

    @Transient
    private Map<CensusCatagories, Integer> ethnicityPopulation;

    @Transient
    private Map<CensusCatagories, Integer> votingAgePopulation;

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
