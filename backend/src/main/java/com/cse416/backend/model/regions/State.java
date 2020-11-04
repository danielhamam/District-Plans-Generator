package com.cse416.backend.model.regions;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class State {
    private String name;
    private String abberivation;
    private long population;
    private long votingAgePopulation;
    private long idealPopulation;

    public State(){

    }
}