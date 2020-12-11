// package com.cse416.backend.model.plan.comparators;

// import com.cse416.backend.model.plan.Plan;
// import com.cse416.backend.model.regions.district.comparators.TotalPopulationCompare;

// import java.util.Collections;
// import java.util.Comparator;

// public class PopulationDifferenceCompare implements Comparator<Plan> {
//     @Override
//     public int compare(Plan p1, Plan p2) {

//         //Calculate Plan's maximum population district and minimum population district
//         long p1Max = Collections.max(p1.getDistricts(),
//                 new TotalPopulationCompare()).getDemographic().getTotalPopulation();
//         long p1Min = Collections.min(p1.getDistricts(),
//                 new TotalPopulationCompare()).getDemographic().getTotalPopulation();
//         long p1Difference = p1Max - p1Min;

//         //Calculate Plan's maximum population district and minimum population district
//         long p2Max = Collections.max(p2.getDistricts(),
//                 new TotalPopulationCompare()).getDemographic().getTotalPopulation();
//         long p2Min = Collections.min(p2.getDistricts(),
//                 new TotalPopulationCompare()).getDemographic().getTotalPopulation();
//         long p2Difference = p2Max - p2Min;


//         return Long.compare(p1Difference, p2Difference);
//     }

// }
