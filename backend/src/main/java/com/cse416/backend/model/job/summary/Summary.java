package com.cse416.backend.model.job.summary;
import com.fasterxml.jackson.databind.JsonNode;
import org.geojson.FeatureCollection;

import javax.persistence.Transient;
import java.util.List;
import java.util.Map;

public class Summary {

    @Transient
    private String stateName;

    @Transient
    private String stateID;

    @Transient
    private JsonNode statePrecinctGraph;

    @Transient
    private FeatureCollection precinctGeoJson;

    @Transient
    private String averageDistricting;

    @Transient
    private String extremeDistricting;

    @Transient
    private String randomDistricting;

    @Transient
    private List<Districting> districting;

    @Transient
    private Constraints constraints;


    public Summary(String stateName, String stateID, JsonNode statePrecinctGraph, FeatureCollection precinctGeoJson,
                   String averageDistricting, String extremeDistricting, String randomDistricting,
                   List<Districting> districting, Constraints constraints) {
        this.stateName = stateName;
        this.stateID = stateID;
        this.statePrecinctGraph = statePrecinctGraph;
        this.precinctGeoJson = precinctGeoJson;
        this.averageDistricting = averageDistricting;
        this.extremeDistricting = extremeDistricting;
        this.randomDistricting = randomDistricting;
        this.districting = districting;
        this.constraints = constraints;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateID() {
        return stateID;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    public JsonNode getStatePrecinctGraph() {
        return statePrecinctGraph;
    }

    public void setStatePrecinctGraph(JsonNode statePrecinctGraph) {
        this.statePrecinctGraph = statePrecinctGraph;
    }

    public FeatureCollection getPrecinctGeoJson() {
        return precinctGeoJson;
    }

    public void setPrecinctGeoJson(FeatureCollection precinctGeoJson) {
        this.precinctGeoJson = precinctGeoJson;
    }

    public String getAverageDistricting() {
        return averageDistricting;
    }

    public void setAverageDistricting(String averageDistricting) {
        this.averageDistricting = averageDistricting;
    }

    public String getExtremeDistricting() {
        return extremeDistricting;
    }

    public void setExtremeDistricting(String extremeDistricting) {
        this.extremeDistricting = extremeDistricting;
    }

    public String getRandomDistricting() {
        return randomDistricting;
    }

    public void setRandomDistricting(String randomDistricting) {
        this.randomDistricting = randomDistricting;
    }

    public List<Districting> getDistricting() {
        return districting;
    }

    public void setDistricting(List<Districting> districting) {
        this.districting = districting;
    }

    public Constraints getConstraints() {
        return constraints;
    }

    public void setConstraints(Constraints constraints) {
        this.constraints = constraints;
    }
}