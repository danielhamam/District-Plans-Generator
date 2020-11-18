package com.cse416.backend.model.demographic;

import javax.persistence.*;
import java.lang.Integer;

@Entity
@Table(name="Population")
public class Population{

    @Id
    @GeneratedValue
    private Integer populationId;

    private Long population;

    @Column(nullable=false, length=100)
    private String censusEthnicityName;

    //Necessary for JPA
    protected Population(){}

    public Population(Long population, String censusEthnicityName){
        this.population = population;
        this.censusEthnicityName = censusEthnicityName;
    }

    public Integer getPopulationId(){return this.populationId;}

    public String getEthnicityName(){return this.censusEthnicityName;}

    public Long getPopulation(){return this.population;}

    public String toString(){
        return this.censusEthnicityName + " population : " + this.population.toString();
    }

}