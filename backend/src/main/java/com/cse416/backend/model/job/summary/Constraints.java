package com.cse416.backend.model.job.summary;

import javax.persistence.Transient;
import java.util.List;

public class Constraints {

    @Transient
    private String compactnessLimit;

    @Transient
    private String populationDifferenceLimit;

    @Transient
    private List<String> minorityGroups;

    public Constraints(String compactnessLimit, String populationDifferenceLimit, List<String> minorityGroups) {
        this.compactnessLimit = compactnessLimit;
        this.populationDifferenceLimit = populationDifferenceLimit;
        this.minorityGroups = minorityGroups;
    }

    public String getCompactnessLimit() {
        return compactnessLimit;
    }

    public void setCompactnessLimit(String compactnessLimit) {
        this.compactnessLimit = compactnessLimit;
    }

    public String getPopulationDifferenceLimit() {
        return populationDifferenceLimit;
    }

    public void setPopulationDifferenceLimit(String populationDifferenceLimit) {
        this.populationDifferenceLimit = populationDifferenceLimit;
    }

    public List<String> getMinorityGroups() {
        return minorityGroups;
    }

    public void setMinorityGroups(List<String> minorityGroups) {
        this.minorityGroups = minorityGroups;
    }
}
