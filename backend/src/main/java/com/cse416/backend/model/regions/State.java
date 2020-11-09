package com.cse416.backend.model.regions;

import com.cse416.backend.model.Plan;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class State {
    private String stateName;
    private String stateAbbreviation;
    private int stateFIPSCode;
    private Plan enactedPlan;
    private Precinct [] statePrecincts;
    private Demographic demographic;
    private Boundary boundary;

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

    public Plan getEnactedPlan() {
        return enactedPlan;
    }

    public void setEnactedPlan(Plan enactedPlan) {
        this.enactedPlan = enactedPlan;
    }

    public Precinct[] getStatePrecincts() {
        return statePrecincts;
    }

    public void setStatePrecincts(Precinct[] statePrecincts) {
        this.statePrecincts = statePrecincts;
    }

    public Demographic getDemographic() {
        return demographic;
    }

    public void setDemographic(Demographic demographic) {
        this.demographic = demographic;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public void setBoundary(Boundary boundary) {
        this.boundary = boundary;
    }

    public Map<String, Object>  getClientInitialData(){
        Map<String, Object> clientState = new HashMap<>();
        clientState.put("stateName", this.stateName);
        clientState.put("numOfPrecincts", 10);
        clientState.put("numOfCounties", 10);
        clientState.put("stateName", this.stateName);
        clientState.put("stateAbbreviation", this.stateAbbreviation);
        clientState.put("stateFIPSCode", this.stateFIPSCode);
        clientState.put("enactedPlan", this.enactedPlan.getClientInitialData());
        clientState.put("demographic", this.demographic);
        return clientState;
    }

    @Override
    public String toString() {
        return "State{" +
                "stateName='" + stateName + '\'' +
                ", stateAbbreviation='" + stateAbbreviation + '\'' +
                ", stateFIPSCode=" + stateFIPSCode +
                ", enactedPlan=" + enactedPlan +
                ", statePrecincts=" + Arrays.toString(statePrecincts) +
                ", demographic=" + demographic +
                ", boundary=" + boundary +
                '}';
    }
}