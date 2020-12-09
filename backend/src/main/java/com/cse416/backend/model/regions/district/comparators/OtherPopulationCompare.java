package com.cse416.backend.model.regions.district.comparators;

import com.cse416.backend.model.regions.district.District;

import java.util.Comparator;

public class OtherPopulationCompare implements Comparator<District> {
    public int compare(District d1, District d2)
    {
        long d1Population = d1.getDemographic().getOtherRacePopulation() +
                d1.getDemographic().getMultipleRaceVAPPopulation();
        long d2Population = d2.getDemographic().getOtherRacePopulation() +
                d2.getDemographic().getMultipleRaceVAPPopulation();
        if (d1Population < d2Population) return 1;
        if (d1Population > d2Population) return 1;
        else return 0;
    }
}
