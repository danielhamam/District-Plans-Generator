package com.cse416.backend.model.demographic;
import com.cse416.backend.model.demographic.CensusEthnicity;
import com.cse416.backend.model.enums.CensusCatagories;
import com.cse416.backend.model.regions.precinct.*;
import com.cse416.backend.model.regions.state.*;
import com.cse416.backend.model.regions.district.*;
import com.cse416.backend.model.regions.county.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


import javax.persistence.*;

import java.lang.*;
import java.util.*;


@Entity
@Table(name="Demographics")
public class Demographic {

    @Id
    @GeneratedValue
    @JsonProperty("demographicId")
    private Integer demographicId;

    @Column(name="total")
    private Long totalPopulation;

    @Column(name="white")
    private Long whitePopulation;

    @Column(name="hispanic")
    private Long hispanicPopulation;

    @Column(name="americanIndian")
    private Long americanIndianPopulation;

    @Column(name="nativeHawaiian")
    private Long nativeHawaiianPopulation;

    @Column(name="black")
    private Long africanAmericanPopulation;

    @Column(name="asian")
    private Long asianPopulation;

    @Column(name="otherRace")
    private Long otherRacePopulation;

    @Column(name="multipleRace")
    private Long multipleRacePopulation;

    @Column(name="totalVAP")
    private Long totalVAPPopulation;

    @Column(name="whiteVAP")
    private Long whiteVAPPopulation;

    @Column(name="hispanicVAP")
    private Long hispanicVAPPopulation;

    @Column(name="americanIndianVAP")
    private Long americanIndianVAPPopulation;

    @Column(name="nativeHawaiianVAP")
    private Long nativeHawaiianVAPPopulation;

    @Column(name="blackVAP")
    private Long africanAmericanVAPPopulation;

    @Column(name="asianVAP")
    private Long asianVAPPopulation;

    @Column(name="otherRaceVAP")
    private Long otherRaceVAPPopulation;

    @Column(name="multipleRaceVAP")
    private Long multipleRaceVAPPopulation;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "districtId")
    @JsonIgnore
    private District district;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "stateId")
    @JsonIgnore
    private State state;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "countyId")
    @JsonIgnore
    private County county;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "precinctId")
    @JsonIgnore
    private Precinct precinct;

    public Demographic(){}
    
    //Voting Age Demographic Setter
    public void setVotingAgeDemographic(Long totalVAPPopulation, Long whiteVAPPopulation, Long hispanicVAPPopulation,  Long americanIndianVAPPopulation, Long nativeHawaiianVAPPopulation, Long africanAmericanVAPPopulation, Long asianVAPPopulation, Long otherRaceVAPPopulation, Long multipleRaceVAPPopulation){
        this.totalVAPPopulation = totalVAPPopulation;
        this.whiteVAPPopulation = whiteVAPPopulation;
        this.hispanicVAPPopulation = hispanicVAPPopulation;
        this.americanIndianVAPPopulation = americanIndianVAPPopulation;
        this.nativeHawaiianVAPPopulation = nativeHawaiianVAPPopulation;
        this.africanAmericanVAPPopulation = africanAmericanVAPPopulation;
        this.asianVAPPopulation = asianVAPPopulation;
        this.otherRaceVAPPopulation = otherRaceVAPPopulation;
        this.multipleRaceVAPPopulation = multipleRaceVAPPopulation;
    }

    //General Demographic Setter
    public void setGeneralDemographic(Long totalPopulation, Long whitePopulation, Long hispanicPopulation,  Long americanIndianPopulation, Long nativeHawaiianPopulation, Long africanAmericanPopulation, Long asianPopulation, Long otherRacePopulation, Long multipleRacePopulation){
        this.totalPopulation = totalPopulation;
        this.whitePopulation = whitePopulation;
        this.hispanicPopulation = hispanicPopulation;
        this.americanIndianPopulation = americanIndianPopulation;
        this.nativeHawaiianPopulation = nativeHawaiianPopulation;
        this.africanAmericanPopulation = africanAmericanPopulation;
        this.asianPopulation = asianPopulation;
        this.otherRacePopulation = otherRacePopulation;
        this.multipleRacePopulation = multipleRacePopulation;
    }

    public Integer getId(){return this.demographicId;}

    //This object is only set if the Demographic object
    //Belongs to a state object
    public State getState(){return this.state;}

    //This object is only set if the Demographic object
    //Belongs to a district object
    public District getDistrict(){return this.district;}

    //This object is only set if the Demographic object
    //Belongs to a county object
    public County getCounty(){return this.county;}

    //This object is only set if the Demographic object
    //Belongs to a precinct object
    public Precinct getPrecinct(){return this.precinct;}

    //General Demographic Getters

    public Long getTotalPopulation(){return this.totalPopulation;}

    public Long getWhitePopulation(){return this.whitePopulation;}

    public Long getHispanicPopulation(){return this.hispanicPopulation;}

    public Long getAmericanIndianPopulation(){return this.americanIndianPopulation;}

    public Long getNativeHawaiianPopulation(){return this.nativeHawaiianPopulation;}

    public Long getAfricanAmericanPopulation(){return this.africanAmericanPopulation;}

    public Long getAsianPopulation(){return this.asianPopulation;}

    public Long getOtherRacePopulation(){return this.otherRacePopulation;}

    public Long getMultipleRacePopulation(){return this.multipleRacePopulation;}

    //Voting Age Demographic Getters

    public Long getVAPTotalPopulation(){return this.totalVAPPopulation;}

    public Long getWhiteVAPPopulation(){return this.whiteVAPPopulation;}

    public Long getHispanicVAPPopulation(){return this.hispanicVAPPopulation;}

    public Long getAmericanIndianVAPPopulation(){return this.americanIndianVAPPopulation;}

    public Long getNativeHawaiianVAPPopulation(){return this.nativeHawaiianVAPPopulation;}

    public Long getAfricanAmericanVAPPopulation(){return this.africanAmericanVAPPopulation;}

    public Long getAsianVAPPopulation(){return this.asianVAPPopulation;}

    public Long getOtherRaceVAPPopulation(){return this.otherRaceVAPPopulation;}

    public Long getMultipleRaceVAPPopulation(){return this.multipleRaceVAPPopulation;}

    public Long getPopulationFromEnum(CensusCatagories censusCatagories)throws Exception{
        switch(censusCatagories.getStringRepresentation()) {
            case "White":
                return this.whitePopulation;
            case "African American":
                return this.africanAmericanPopulation;
            case "American Indian":
                return americanIndianPopulation;
            case "Asian":
                return asianPopulation;
            case "Hawaiian":
                return nativeHawaiianPopulation;
            case "Hispanic":
                return hispanicPopulation;
            case "Other":
                return otherRacePopulation + multipleRacePopulation;
            default:
                throw new Exception("Enum Does Not Exist");
        }
    }

    public Long getPopulationFromString(String censusCatagories)throws Exception{
        switch(censusCatagories) {
            case "White":
                return this.whitePopulation;
            case "Black or African American":
                return this.africanAmericanPopulation;
            case "American Indian or Alaska Native":
                return americanIndianPopulation;
            case "Asian":
                return asianPopulation;
            case "Native Hawaiian or Other Pacific Islander":
                return nativeHawaiianPopulation;
            case "Hispanic":
                return hispanicPopulation;
            case "Other":
                return otherRacePopulation + multipleRacePopulation;
            default:
                throw new Exception("String Does Not Exist");
        }
    }

    public String toString(){
        String generalDem = "Total: " + getTotalPopulation() + ", White : " + getWhitePopulation() + ", Black : " + getAfricanAmericanPopulation() + 
        ", Hispanic : " + getHispanicPopulation() + ", American Indian : " + getAmericanIndianPopulation() + ", Native Hawaiian :  " + getNativeHawaiianPopulation() +
        ", Asian : "  + getAsianPopulation() + ", Other : " + getOtherRacePopulation() + ", Multiple : " + getMultipleRacePopulation() + "\n";

        String votingAgeDem = "Total VAP: " + getVAPTotalPopulation() + ", White VAP: " + getWhiteVAPPopulation() + ", Black VAP: " + getAfricanAmericanVAPPopulation() + 
        ", Hispanic VAP: " + getHispanicVAPPopulation() + ", American Indian VAP: " + getAmericanIndianVAPPopulation() + ", Native Hawaiian VAP:  " + getNativeHawaiianVAPPopulation() +
        ", Asian VAP: "  + getAsianVAPPopulation() + ", Other VAP: " + getOtherRaceVAPPopulation() + ", Multiple VAP: " + getMultipleRaceVAPPopulation() + "\n";

        return generalDem + votingAgeDem;
    }

}
