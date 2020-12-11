package com.cse416.backend.model.job.summary;
import org.geojson.FeatureCollection;

import javax.persistence.Transient;
import java.util.List;

public class Summary {

    @Transient
    private String stateName;

    @Transient
    private String stateID;

    @Transient
    private FeatureCollection precinctGeoJson;

    @Transient
    private List<Districting> districting;


    public Summary(String stateName, String stateID, FeatureCollection precinctGeoJson, List<Districting> districting){
        this.stateName = stateName;
        this.stateID = stateID;
        this.precinctGeoJson = precinctGeoJson;
        this.districting = districting;
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

    public FeatureCollection getPrecinctGeoJson() {
        return precinctGeoJson;
    }

    public void setPrecinctGeoJson(FeatureCollection precinctGeoJson) {
        this.precinctGeoJson = precinctGeoJson;
    }

    public List<Districting> getDistricting() {
        return districting;
    }

    public void setDistricting(List<Districting> districting) {
        this.districting = districting;
    }
}