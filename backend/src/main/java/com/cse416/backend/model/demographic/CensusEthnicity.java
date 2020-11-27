package com.cse416.backend.model.demographic;

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

    public CensusEthnicity(String shortenEthnicityName, String censusEthnicityName){
        this.shortenName = shortenEthnicityName;
        this.censusName = censusEthnicityName;
    }

    public String getId(){return this.shortenName;}

    public String getEthnicityName(){return this.shortenName;}

    public String getCensusEthnicityName(){return this.censusName;}

    public String toString(){
        return "Shorten Name: " + this.shortenName + ", Census Ethnicity Name : " + this.censusName + "\n";
    }

}