package com.cse416.backend.model.enums;


public enum JobStatus {
    CANCELLED("CA"),
    COMPLETED("CD"),
    PENDING("PD"),
    RUNNING("R"),
    FAILED("F"),
    NODEFAILED("NF"),
    STOPPED("ST"),
    SUSPENDED("S");



    private final String codeName;


    JobStatus(String codeName) {
        this.codeName = codeName;
    }

    public String getCodeName() {
        return codeName;
    }

    static public JobStatus getEnumFromString(String str) {
        JobStatus seawulfJobStateCode = null;
        for (JobStatus enumaration : JobStatus.values()) {
            if (enumaration.getCodeName().equals(str)) {
                seawulfJobStateCode = enumaration;
            }
        }
        return seawulfJobStateCode;
    }
}
