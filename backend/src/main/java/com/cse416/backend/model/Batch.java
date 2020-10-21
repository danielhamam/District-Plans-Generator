package com.cse416.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Batch{
    private UUID id;
    private int numberOfDistricting;
    private String name;
    private boolean isAvailable;
    private double populationDifference;
    private double compactness;
    @JsonIgnoreProperties

    public Batch(@JsonProperty("numberOfDistricting") int numberOfDistricting,
                 @JsonProperty("name") String name,
                 @JsonProperty("isAvailable") boolean isAvailable,
                 @JsonProperty("populationDifference") double populationDifference,
                 @JsonProperty("compactness") double compactness) {
        this.numberOfDistricting = numberOfDistricting;
        this.name = name;
        this.isAvailable = isAvailable;
        this.populationDifference = populationDifference;
        this.compactness = compactness;
    }

    public Batch(UUID id, int numberOfDistricting, String name, boolean isAvailable,
                 double populationDifference, double compactness){
        this.id = id;
        this.numberOfDistricting = numberOfDistricting;
        this.name = name;
        this.isAvailable = isAvailable;
        this.populationDifference = populationDifference;
        this.compactness = compactness;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public double getPopulationDifference() {
        return populationDifference;
    }

    public void setPopulationDifference(double populationDifference) {
        this.populationDifference = populationDifference;
    }

    public double getCompactness() {
        return compactness;
    }

    public void setCompactness(double compactness) {
        this.compactness = compactness;
    }

    public int getnumberOfDistricting() {
        return numberOfDistricting;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "id=" + id +
                ", numberOfDistricting=" + numberOfDistricting +
                ", name='" + name + '\'' +
                ", isAvailable='" + isAvailable + '\'' +
                ", populationDifference=" + populationDifference +
                ", compactness=" + compactness +
                '}';
    }
}

