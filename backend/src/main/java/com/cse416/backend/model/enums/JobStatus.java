package com.cse416.backend.model.enums;


public enum JobStatus {
    CANCELLED("CA", "CANCELLED"),
    COMPLETING("-", "COMPLETING"),
    COMPLETED("CD", "COMPLETED"),
    PENDING("PD", "PENDING"),
    RUNNING("R", "RUNNING"),
    FAILED("F", "FAILED"),
    NODEFAILED("NF", "NODEFAILED"),
    STOPPED("ST", "STOPPED"),
    SUSPENDED("S", "SUSPENDED"),
    UNKNOWN("UN", "UNKNOWN");



    private final String codeName;
    private final String longName;


    JobStatus(String codeName, String longName){
        this.codeName = codeName;
        this.longName = longName;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getLongName() {
        return longName;
    }



    static public JobStatus getEnumFromString(String str) {
        JobStatus seawulfJobStateCode = UNKNOWN;
        for (JobStatus enumaration : JobStatus.values()) {
            if (enumaration.getCodeName().equals(str)) {
                seawulfJobStateCode = enumaration;
                break;
            }
        }

        for (JobStatus enumaration : JobStatus.values()) {
            if (enumaration.getLongName().equals(str)) {
                System.out.println(enumaration);
                seawulfJobStateCode = enumaration;
                break;
            }
        }
        return seawulfJobStateCode;
    }
}
