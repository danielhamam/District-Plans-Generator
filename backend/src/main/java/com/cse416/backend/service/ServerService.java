package com.cse416.backend.service;

import com.cse416.backend.dao.FakeDataAccessObject;
import com.cse416.backend.model.*;
import com.cse416.backend.model.enums.*;
import com.cse416.backend.model.regions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;


import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Service
public class ServerService {

    private final FakeDataAccessObject fake;
    private final ObjectMapper mapper;
    private Session session;
    //private GlobalHistory history;

    @Autowired
    public ServerService(@Qualifier("fakeDao") FakeDataAccessObject fake) {
        this.fake = fake;
        this.mapper = new ObjectMapper();

    }

    public String connectingClient(){
        return "connectingClient";
    }
    
    public String getState(String stateAbbrev){
        State state = fake.queryGetStateInformation(stateAbbrev);
        List <Job> jobs = this.getStateJobsInformation(stateAbbrev);
        this.session = new Session(state);
        this.session.addJobs(jobs);
        String clientData = "{serverError:null}";
        try{
            clientData = createClientStateData(state, jobs);
        }catch(JsonProcessingException error){
            error.getMessage();
            clientData = "{serverError:\"" + error.getMessage() + "\"}"; 
        }
        catch(Exception error){
            error.printStackTrace();
        }
        return clientData;
    }

    public String createClientStateData(State state, List <Job> jobs)throws JsonProcessingException{
        Map <String,Object> clientData = new HashMap<>();
        List<Object> clientJob = new ArrayList<>();
        jobs.forEach(job -> clientJob.add(job.getClientInitialData()));
        clientData.put("state", state.getClientInitialData());
        clientData.put("batches", clientJob);
        String clientDataString = mapper.writeValueAsString(clientData);
        return clientDataString;
    }

    public String getJob(String state){
        return "getJob";
    }

    public String getBoundries(String boundryType){
        return "getBoundries";
    }

    public String getDemographicFilter(String jobID, String planID, List <CensusCatagories> censusCategory, String CensusCategoryboundaryType, List<Integer> desiredRegion ){

        return "getBoundries";
    }

    public String getPlan(String jobID, String planID){
        return "getPlan";
    }

    public String BoxWhisker(String jobID, String planID){
        return "BoxWhisker";
    }

    public String generateJob(Job job){
        job.setStateAbbrev(this.session.getState().getStateAbbreviation());
        String clientData = "{serverError:null}";
        this.fake.mutationGenerateJob(job);
        try{
            clientData = this.createClient_Data(job);
        }catch(JsonProcessingException error){
            error.getMessage();
            clientData = "{serverError:\"" + error.getMessage() + "\"}"; 
        }
        catch(Exception error){
            error.printStackTrace();
        }
        System.out.println("Generating Job: " + job);
        return clientData;
    }

    public String createClient_Data(Object obj)throws JsonProcessingException{
        return mapper.writeValueAsString(obj);
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


    public Boundary getStateBoundary(String stateAbbrev){
        //fake.queryGetStateBoundary(stateAbbrev);
        return null;
    }


    public List <Job> getStateJobsInformation(String stateAbbrev){
        List <Job> jobs = fake.queryGetStateJobsInformation(stateAbbrev);
        return jobs;
    }

    public Job getJobInformation(String stateAbbrev, String jobID){
        return null;
    }

    public Plan getPlanInformation(String stateAbbrev, String planID){
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

    public List<Precinct> getDesiredPrecinctInformation(String stateAbbrev, String planID, List <Integer> desiredPrecinct){
        return null;
    }

    public List<Precinct> getAllPrecinctInformation(String stateAbbrev, String planID){
        //fake.queryGetAllPrecinctInformation(stateAbbrev, planID);
        return null;
    }

    public Boundary getPrecinctBoundary(String stateAbbrev){
        return null;
    }

    public Demographic getPrecinctDemographic(String stateAbbrev){
        return null;
    }
    
}
