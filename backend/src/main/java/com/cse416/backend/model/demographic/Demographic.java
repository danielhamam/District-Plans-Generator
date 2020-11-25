package com.cse416.backend.model.demographic;
import com.cse416.backend.model.demographic.CensusEthnicity;
import com.cse416.backend.model.regions.precinct.*;
import com.cse416.backend.model.regions.state.*;
import com.cse416.backend.model.regions.district.*;
import com.cse416.backend.model.regions.county.*;


import javax.persistence.*;

import java.lang.*;
import java.util.*;


@Entity
@Table(name="Demographics")
public class Demographic {

    @Id
    @GeneratedValue
    private Integer demographicId;

    private Long totalPopulation;

    private Long whitePopulation;

    private Long hispanicPopulation;

    private Long americanIndianPopulation;

    private Long nativeHawaiianPopulation;

    private Long africanAmericanPopulation;

    private Long asianPopulation;

    private Long otherRacePopulation;

    private Long multipleRacePopulation;

    private Long totalVAPPopulation;

    private Long whiteVAPPopulation;

    private Long hispanicVAPPopulation;

    private Long americanIndianVAPPopulation;

    private Long nativeHawaiianVAPPopulation;

    private Long africanAmericanVAPPopulation;

    private Long asianVAPPopulation;

    private Long otherRaceVAPPopulation;

    private Long multipleRaceVAPPopulation;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "districtId")
    private District district;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "stateId")
    private State state;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "countyId")
    private County county;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "precinctId")
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


}
