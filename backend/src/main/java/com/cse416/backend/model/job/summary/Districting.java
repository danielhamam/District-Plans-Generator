package com.cse416.backend.model.job.summary;

import org.geojson.FeatureCollection;

import javax.persistence.Transient;

public class Districting {

    @Transient
    private String districtingID;

    @Transient
    private Constraints constraints;

    @Transient
    private FeatureCollection congressionalDistrictGeoJSON;

    public Districting(String districtingID, Constraints constraints, FeatureCollection congressionalDistrictGeoJSON) {
        this.districtingID = districtingID;
        this.constraints = constraints;
        this.congressionalDistrictGeoJSON = congressionalDistrictGeoJSON;
    }

    public String getDistrictingID() {
        return districtingID;
    }

    public void setDistrictingID(String districtingID) {
        this.districtingID = districtingID;
    }

    public Constraints getConstraints() {
        return constraints;
    }

    public void setConstraints(Constraints constraints) {
        this.constraints = constraints;
    }

    public FeatureCollection getCongressionalDistrictGeoJSON() {
        return congressionalDistrictGeoJSON;
    }

    public void setCongressionalDistrictGeoJSON(FeatureCollection congressionalDistrictGeoJSON) {
        this.congressionalDistrictGeoJSON = congressionalDistrictGeoJSON;
    }
}
