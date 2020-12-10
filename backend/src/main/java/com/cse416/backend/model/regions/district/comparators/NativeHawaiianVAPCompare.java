package com.cse416.backend.model.regions.district.comparators;

import com.cse416.backend.model.regions.district.District;

import java.util.Comparator;

public class NativeHawaiianVAPCompare implements Comparator<District> {

    @Override
    public int compare(District d1, District d2) {
        long d1Population = d1.getDemographic().getNativeHawaiianVAPPopulation();
        long d2Population = d2.getDemographic().getNativeHawaiianVAPPopulation();
        return Long.compare(d1Population, d2Population);
    }
}
