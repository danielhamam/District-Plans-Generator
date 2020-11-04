package com.cse416.backend.service;


import com.cse416.backend.dao.FakeDataAccessObject;
import com.cse416.backend.model.Job;
import com.cse416.backend.model.Plan;
import com.cse416.backend.model.regions.*;
import com.cse416.backend.model.enums.CensusCatagories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.util.List;

@Service
public class ServerService {

    private final FakeDataAccessObject fake;
    //private final Algorithm algorithm
    //private final SeawulfAdapater seawulfAdapater

    @Autowired
    public ServerService(@Qualifier("fakeDao") FakeDataAccessObject fake) {

        this.fake = fake;
    }

    public String getState(String stateAbbrev){
        return "getState";
    }

    public String getJob(String state){
        return "getJob";
    }

    public String getBoundries(String boundryType){
        return "getBoundries";
    }

    // public String getDemographicFilter(String jobID, String planID, List <CensusCategory> censusCategory, String CensusCategoryboundaryType, List<Integer> desiredRegion ){

    //     return "getBoundries";
    // }

    public String getPlan(String jobID, String planID){
        return "getPlan";
    }

    public String BoxWhisker(String jobID, String planID){
        return "BoxWhisker";
    }

    public String generateJob(Job job){
        return "generateJob";
    }
    public String generateHeatMap(){
        return "generateHeatMap";

    }

    public int saveJob(Job job){
        return 0;
    }

    public void cancelJob(String jobID){

    }

    public void deleteJob(String jobID){

    }

    public void updateJob(String jobID){

    }

    public void setSession(State state){

    }


    public State getStateInformation(String stateAbbrev){
        return null;
    }

    public Boundary getStateBoundary(String stateAbbrev){
        return null;
    }

    public Demographic getStateDemographic(String stateAbbrev){
        return null;
    }

    public Job getJobInformation(String stateAbbrev){
        return null;
    }

    public Plan getPlanInformation(String stateAbbrev){
        return null;
    }

    public District getDistrictInformation(String stateAbbrev){
        return null;
    }

    public Boundary getDistrictBoundary(String stateAbbrev){
        return null;
    }

    public Demographic getDistrictDemographic(String stateAbbrev){
        return null;
    }

    public Precinct getPrecinctInformation(String stateAbbrev){
        return null;
    }

    public Boundary getPrecinctBoundary(String stateAbbrev){
        return null;
    }

    public Demographic getPrecinctDemographic(String stateAbbrev){
        return null;
    }
    
}
