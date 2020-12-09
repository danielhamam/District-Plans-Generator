package com.cse416.backend.model.regions.district.comparators;
import com.cse416.backend.model.regions.district.District;
import java.util.Comparator;

public class AfricanAmericanPopulationCompare implements Comparator<District> {
    public int compare(District d1, District d2)
    {
        long d1Population = d1.getDemographic().getAfricanAmericanVAPPopulation();
        long d2Population = d2.getDemographic().getAfricanAmericanVAPPopulation();
        if (d1Population < d2Population) return 1;
        if (d1Population > d2Population) return 1;
        else return 0;
    }
}
