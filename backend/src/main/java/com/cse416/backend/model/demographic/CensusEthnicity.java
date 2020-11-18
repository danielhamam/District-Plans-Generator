package com.cse416.backend.model.demographic;

import javax.persistence.*;
import java.lang.Integer;

@Entity
@Table(name = "CensusEthnicities")
public class CensusEthnicity {

    @Id
    @Column(nullable=false, length=100)
    private String ethnicityName;

    @Column(nullable=false, length=255)
    private String censusEthnicityName;

    //Necessary for JPA
    protected CensusEthnicity(){}

    public CensusEthnicity(String shortenEthnicityName, String censusEthnicityName){
        this.ethnicityName = shortenEthnicityName;
        this.censusEthnicityName = censusEthnicityName;
    }

    public String getId(){return this.ethnicityName;}

    public String getEthnicityName(){return this.ethnicityName;}

    public String getCensusEthnicityName(){return this.censusEthnicityName;}

    public String toString(){
        return "Ethnicity Name: " + this.ethnicityName + " Census Ethnicity Name : " + this.censusEthnicityName;
    }

}