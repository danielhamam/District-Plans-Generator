package com.cse416.backend.model.plan.comparators;
import com.cse416.backend.model.plan.Plan;
import java.util.Comparator;

public class CompactnessCompare implements Comparator<Plan> {

    @Override
    public int compare(Plan p1, Plan p2) {
        return Double.compare(p1.getAverageDistrictCompactness(), p2.getAverageDistrictPopulation());
    }

}
