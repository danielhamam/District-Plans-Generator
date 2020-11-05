package com.cse416.backend.model.regions;

import com.cse416.backend.model.enums.CensusCatagories;

import java.lang.reflect.Array;
import java.util.HashMap;

public class Demographic {
    long totalPopulation;
    String referenceType;
    HashMap<CensusCatagories, Array> ethnicityPopulation;
    HashMap<CensusCatagories, Array> agePopulation;
    HashMap<CensusCatagories, Array> votingAgePopulation;

    public Demographic(long totalPopulation){
        this.totalPopulation = totalPopulation;

    }
    
    public Demographic(long totalPopulation, String referenceType, HashMap<CensusCatagories, Array> ethnicityPopulation, 
                    HashMap<CensusCatagories, Array> agePopulation, HashMap<CensusCatagories, Array> votingAgePopulation){

    }

    public HashMap<CensusCatagories, Array> getAgePopulation() {
        return agePopulation;
    }

    public void setAgePopulation(HashMap<CensusCatagories, Array> agePopulation) {
        this.agePopulation = agePopulation;
    }
}
