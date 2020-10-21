package com.cse416.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class State {
    private String name;
    private String abberivation;
    @JsonIgnore private List<Batch> batches;
    private long population;
    private long votingAgePopulation;
    private long idealPopulation;


    public State(String name, String abberivation, List<Batch> batches, long population, long votingAgePopulation, long idealPopulation) {
        this.name = name;
        this.abberivation = abberivation;
        this.batches = batches;
        this.population = population;
        this.votingAgePopulation = votingAgePopulation;
        this.idealPopulation = idealPopulation;
    }

    public String getName() {
        return name;
    }

    public String getAbberivation() {
        return abberivation;
    }


    public List<Batch> getBatches() {
        return batches;
    }

    public void addBatch(Batch batch) {
        batches.add(batch);
    }

    public long getPopulation() {
        return population;
    }


    public long getVotingAgePopulation() {
        return votingAgePopulation;
    }


    public long getIdealPopulation() {
        return idealPopulation;
    }

    public void setIdealPopulation(long idealPopulation) {
        this.idealPopulation = idealPopulation;
    }

    @Override
    public String toString() {
        return "State{" +
                "name='" + name + '\'' +
                ", abberivation='" + abberivation + '\'' +
                ", batches=" + batches +
                ", population=" + population +
                ", votingAgePopulation=" + votingAgePopulation +
                ", idealPopulation=" + idealPopulation +
                '}';
    }
}