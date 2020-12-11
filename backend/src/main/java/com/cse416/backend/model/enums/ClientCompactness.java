package com.cse416.backend.model.enums;

public enum ClientCompactness  {
    HIGH("High Compactness"),
    MEDIUM("Medium Compactness"),
    LOW("Low Compactness");

    private String representation;

    ClientCompactness(String representation){
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }
}