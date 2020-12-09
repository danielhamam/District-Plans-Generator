package com.cse416.backend.model.regions.district;
import java.util.Comparator;

public class DistrictCompare{
    public DistrictCompare() {
    }
}


class CompactnessCompare implements Comparator<District>
{
    public int compare(District m1, District m2)
    {
        if (m1.getCompactness() < m2.getCompactness()) return -1;
        if (m1.getCompactness() > m2.getCompactness()) return 1;
        else return 0;
    }
}

