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
import java.util.*;


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
    
    public String getState(String stateAbbrevation){
        State state = fake.queryGetStateInformation(stateAbbrevation);
        List <Job> jobs = this.getStateJobsInformation(stateAbbrevation);
        this.session = new Session(state);
        this.session.addJobs(jobs);
        String clientData = "{serverError:null}";
        try{
            clientData = createClientStateData(state, jobs);
        }catch(JsonProcessingException error){
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
        jobs.forEach(job -> clientJob.add(job));
        clientData.put("state", state);
        clientData.put("jobs", clientJob);
        String clientDataString = mapper.writeValueAsString(clientData);
        return clientDataString;
    }

    public String getJob(String jobID){
        String clientData = "{serverError:\"Unknown Server Error\"}";
        try{
            Job requestedJob = this.session.getJobByID(jobID);
            Map<String, Object> dataObject = requestedJob.getClientPlans();
            clientData = this.createClient_Data(dataObject);
        }catch(NoSuchElementException|JsonProcessingException error){
            error.printStackTrace();
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
        }
        catch(Exception error){
            error.printStackTrace();
        }
        return clientData;
    }

    public String getBoundries(String boundryType){
        return "getBoundries";
    }

    public String getDemographicFilter(String jobID, String planID, List <CensusCatagories> censusCategory){
        Job currentJob = this.session.getJobByID(jobID);
        Plan plan = currentJob.getPlanByID(planID);
        System.out.println(censusCategory);


        return "getBoundries";
    }

    public String getPlan(String jobID, String planID){
        Job currentJob = this.session.getJobByID(jobID);
        Plan plan = currentJob.getPlanByID(planID);
        String clientData = "{serverError:\"Unknown Server Error\"}";
        try{
            clientData = this.createClient_Data(plan);

        }catch(NoSuchElementException|JsonProcessingException error){
            error.printStackTrace();
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
        }
        catch(Exception error){
            error.printStackTrace();
        }
        return clientData;
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
