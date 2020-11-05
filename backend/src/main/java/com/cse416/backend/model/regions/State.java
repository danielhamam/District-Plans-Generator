package com.cse416.backend.model.regions;

import com.cse416.backend.model.Plan;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Map;

public class State {
    String stateName;
    String stateAbbreviation;
    int stateFIPSCode;
    Plan enactedPlan;
    Precinct [] statePrecincts;
    Demographic demographic;
    Boundary boundary;
    
    public State(String stateName, String stateAbbreviation, int stateFIPSCode, Demographic demographic, Boundary boundary, Plan enactedPlan, Precinct [] statePrecincts){
        this.stateName = stateName;
        this.stateAbbreviation = stateAbbreviation;
        this.stateFIPSCode = stateFIPSCode;
        this.boundary = boundary;
        this.demographic = demographic;
        this.enactedPlan = enactedPlan;
        this.statePrecincts = statePrecincts;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public int getStateFIPSCode() {
        return stateFIPSCode;
    }

    public void setStateFIPSCode(int stateFIPSCode) {
        this.stateFIPSCode = stateFIPSCode;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }

    public Demographic getDemographic() {
        return demographic;
    }

    public void setDemographic(Demographic demographic) {
        this.demographic = demographic;
    }

    public void setPlan(Plan enactedPlan){
        this.enactedPlan = enactedPlan;
    }

    public Plan getPlan(){
        return this.enactedPlan;
    }
}