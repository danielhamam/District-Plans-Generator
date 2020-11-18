package com.cse416.backend.model.enums;

public enum JobStatus  {
    PENDING("Pending"),
    RUNNING("Running"),
    COMPLETED("Completed"),
    CANCELED("Canceled");

    private final String representation;

    private JobStatus(String representation) {
        this.representation = representation;
    }
}