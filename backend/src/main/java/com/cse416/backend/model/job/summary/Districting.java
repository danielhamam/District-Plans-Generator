package com.cse416.backend.model.job.summary;

import com.cse416.backend.model.regions.district.District;
import com.fasterxml.jackson.databind.JsonNode;
import org.geojson.FeatureCollection;

import javax.persistence.Transient;
import java.util.List;

public class Districting {

    @Transient
    private String districtingID;


    @Transient
    private JsonNode districtPrecinctGraph;

    @Transient
    private List<District> districtList;



    public Districting(String districtingID, JsonNode districtPrecinctGraph, List<District> districtList) {
        this.districtingID = districtingID;
        this.districtPrecinctGraph = districtPrecinctGraph;
        this.districtList = districtList;
    }

    public JsonNode getDistrictPrecinctGraph() {
        return districtPrecinctGraph;
    }

    public void setDistrictPrecinctGraph(JsonNode districtPrecinctGraph) {
        this.districtPrecinctGraph = districtPrecinctGraph;
    }

    public List<District> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<District> districtList) {
        this.districtList = districtList;
    }

    public String getDistrictingID() {
        return districtingID;
    }

    public void setDistrictingID(String districtingID) {
        this.districtingID = districtingID;
    }

}
