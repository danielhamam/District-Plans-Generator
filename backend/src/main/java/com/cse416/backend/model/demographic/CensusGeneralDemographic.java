package com.cse416.backend.model.demographic;
import javax.persistence.*;
import java.lang.Integer;
import com.cse416.backend.model.demographic.Population;
import java.util.*;


// @Entity
// @Table(name = "CensusGeneralDemographic")
public class CensusGeneralDemographic{

    // @Id 
    // @GeneratedValue
    private Integer censusGeneralDemographicId;

    private Long totalPopulation;

    // @OneToMany
    private List<Population> populations;

    //Necessary for JPA
    // public CensusGeneralDemographic(){}

    public CensusGeneralDemographic(Long totalPopulation, List<Population> populations){
        this.totalPopulation = totalPopulation;
        this.populations = populations;
    }

    public Long getTotalPopulation(){return this.totalPopulation;}

    public Integer getId(){return this.censusGeneralDemographicId;}

    public List<Population> getPopulations(){return this.populations;}

    public String toString(){

            String str = "";

            for(int i = 0; i < this.populations.size(); i++){
                
                Population getPopulation = (Population)this.populations.get(i);

                str += getPopulation.toString() + "\n";
            }

            return str;
    }
}