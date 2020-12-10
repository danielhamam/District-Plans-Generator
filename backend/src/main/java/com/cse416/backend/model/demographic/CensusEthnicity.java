package com.cse416.backend.model.demographic;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.lang.Integer;

@Entity
@Table(name = "CensusEthnicities")
public class CensusEthnicity {

    @Id
    @Column(nullable=false, length=100)
    private String shortenName;

    @Column(nullable=false, length=255)
    private String censusName;

    //Necessary for JPA
    protected CensusEthnicity(){}

    public CensusEthnicity(@JsonProperty("shortenName")String shortenName,
                           @JsonProperty("censusName")String censusName){
        this.shortenName = shortenName;
        this.censusName = censusName;
    }

    public String getId(){return this.shortenName;}

    public String getEthnicityName(){return this.shortenName;}

    public String getCensusEthnicityName(){return this.censusName;}

    @Override
    public String toString() {
        return "CensusEthnicity{" +
                "shortenName='" + shortenName + '\'' +
                ", censusName='" + censusName + '\'' +
                '}';
    }
}