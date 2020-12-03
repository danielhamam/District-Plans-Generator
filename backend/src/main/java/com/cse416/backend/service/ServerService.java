package com.cse416.backend.service;

import com.cse416.backend.livememory.GlobalHistory;
import com.cse416.backend.model.demographic.Demographic;
import com.cse416.backend.model.regions.precinct.Precinct;
import com.cse416.backend.model.regions.state.*;
import com.cse416.backend.model.job.*;
import com.cse416.backend.model.plan.*;
import com.cse416.backend.model.enums.*;
import com.cse416.backend.livememory.Session;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.hibernate.engine.query.ParameterRecognitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.cse416.backend.dao.services.*;


import java.io.*;
import java.util.*;


@Service
public class ServerService {

    private final ObjectMapper mapper;
    private Session session;
    private GlobalHistory jobHistory;
    private boolean isStatePrecinctsLoaded;
    private boolean runAlgoLocally = false;


    //DAO Servicers
    @Autowired
    private JobDAOService jobDAO;

    @Autowired
    private CountyDAOService countyDAO;

    @Autowired
    private DemographicDAOService demographicDAO;

    @Autowired
    private CensusEthnicityDAOService censusEthnicityDAO;

    @Autowired
    private DistrictDAOService districtDAO;

    @Autowired
    private PlanDAOService planDAO;

    @Autowired
    private PrecinctDAOService precinctDAO;

    @Autowired
    private StateDAOService stateDAO;

    @Autowired
    private BoxWhiskerDAOService boxWhiskerDAO;

    @Autowired
    private BoxWhiskerPlotDAOService boxWhiskerPlotDAO;

    @Autowired
    public ServerService() {
        this.mapper = new ObjectMapper();
        this.session = new Session();
        this.jobHistory = new GlobalHistory();
        this.isStatePrecinctsLoaded = false;
    }

    private String createClient_Data(Object obj)throws JsonProcessingException{
        return mapper.writeValueAsString(obj);
    }

    private String createClientStateData(State state, List <Job> jobs)throws JsonProcessingException{
        Map <String,Object> clientData = new HashMap<>();
        List<Object> clientJob = new ArrayList<>();
        jobs.forEach(job -> clientJob.add(job));
        clientData.put("state", state);
        clientData.put("jobs", clientJob);
        return  mapper.writeValueAsString(clientData);
    }

    private String createAlgorithmData(State state, Job job)throws JsonProcessingException{
        Map <String,Object> AlgorithmData = new HashMap<>();
        Map <String,Object> insideOfAlgorithmData = new HashMap<>();
        insideOfAlgorithmData.put("state", state.getAlgorithmJson());
        insideOfAlgorithmData.put("job", job);
        AlgorithmData.put("data",insideOfAlgorithmData);
        return  mapper.writeValueAsString(AlgorithmData);
    }

    private void createJobDirectory(String jobDirectoryName, String algorithmContents)throws IOException{
        jobDirectoryName = jobDirectoryName.toLowerCase();
        String jobDirectoryRelativePath = "src/main/resources/system/jobs";
        String jobDirectoryAbsolutePath =  new File(jobDirectoryRelativePath).getAbsolutePath();
        String newDirectoryAbsolutePath =  jobDirectoryAbsolutePath+"/"+jobDirectoryName;
        boolean directoryCreated = new File(jobDirectoryAbsolutePath + "/" + jobDirectoryName).mkdirs();
        if(directoryCreated){
            System.out.println("./" + jobDirectoryName + " directory has been created. Writing AlgorithmInput.json");
            FileWriter algorithmInputFileWriter = new FileWriter(newDirectoryAbsolutePath+"/"+"AlgorithmInput.json");
            algorithmInputFileWriter.write(algorithmContents);
            algorithmInputFileWriter.close();
        }else{
            System.err.println(jobDirectoryName + " has not been created some error has occured with");
        }

    }

    public String connectingClient(){
        //TODO: [DISCUSS] There may be no need to implement this function.
        //                We keep all the state geoJson on client-side.
        //                This would need implement if the application includes more states
        //TODO: [SERVER] Implementation TBD
        //TODO: [DATABASE] Implementation TBD
        return "connectingClient";
    }
    
    public String getState(String stateAbbrevation){
        String clientData = "{serverError:null}";
        try{
            State state = stateDAO.getStateById(stateAbbrevation);
            //System.out.println(state);
            Demographic stateDemographic = demographicDAO.getDemographicByStateId(stateAbbrevation);
            state.setDemographic(stateDemographic);
            state.setTotalPopulation(stateDemographic.getTotalPopulation());
            List <Job> jobs = jobDAO.getJobsByStateId(stateAbbrevation);
            System.out.println(state.getStateAbbreviation());
            state.initializeSystemFiles();
            this.session.setState(state);
            this.session.addJobs(jobs);
            this.jobHistory.addJobs(jobs);
            //getPrecinctAsync(stateAbbrevation);
            clientData = createClientStateData(state, jobs);
        }catch(JsonProcessingException error){
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
        }
        catch(Exception error){
            error.printStackTrace();
        }
        return clientData;
    }

    private void getPrecinctAsync(String stateAbbrevation){
        new Thread(new Runnable() {
            public void run() {
                isStatePrecinctsLoaded = false;
                State state = session.getState();
                System.out.println("Getting " + stateAbbrevation + " precincts");
                List <Precinct> precincts = precinctDAO.getPrecinctsByStateId(stateAbbrevation);
                state.setStatePrecincts(precincts);
                for(Precinct p: precincts){
                    Demographic precinctDemographic =  demographicDAO.getDemographicByPrecinctId(p.getPrecinctId());
                    p.setDemographic(precinctDemographic);
                    System.out.println(p);
                }
                System.out.println("Finished getting " + stateAbbrevation + " precincts");
                isStatePrecinctsLoaded = true;
            }
        }).start();
    }

    public String getJob(Integer jobID){
        String clientData = "{serverError:\"Unknown Server Error\"}";
        try{
            //TODO: [DECISION] Since the job object will not have a reference to the geoJSON for each plan a decision
            //                 needs to be made on how it is going to acquire the random, average, extreme plan data.
            //                 Would it look in the in seawulf? would it query to the database?
            Job serverJob = session.getJobByID(jobID);
            Optional databaseJob = jobDAO.getJobById(serverJob.getJobID());
            //TODO: Figure out what jobDAO.getJobById() does. Does it updated the existing live memory job or is it a
            // whole other job
            System.out.println(databaseJob);
            Map<String, Object> dataObject = serverJob.getClientPlans();
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


    public String getDemographicHeatmap(String censusEthnicity){
        String clientData = "{serverError:\"Unknown Server Error\"}";
        System.out.println(censusEthnicity);
        try{
            State state = session.getState();
            File heatmapFile = state.getDemographicHeatMap(censusEthnicity);
            JsonNode heatmapNode = mapper.readTree(heatmapFile);
            clientData = createClient_Data(heatmapNode);

        }catch(Exception error){
            error.printStackTrace();
        }

        return clientData;
    }

    private void waitForLoadedPrecincts(){
        while(!isStatePrecinctsLoaded){
            System.out.println("Waiting For Precincts");
        }
        System.out.println("Precincts Loaded");

    }

    public String getPrecincts(){
        String clientData = "{serverError:\"Unknown Server Error\"}";
        try{
            Object precinctsGeoJson = this.session.getState().getClientPrecinctsGeoJson();
            clientData = this.createClient_Data(precinctsGeoJson);

        }catch(NoSuchElementException|JsonProcessingException error){
            error.printStackTrace();
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
        }
        catch(Exception error){
            error.printStackTrace();
        }
        return clientData;
    }


    public String getPrecinctDemographic(String name){
        String clientData = "{serverError:\"Unknown Server Error\"}";
        try{
            Precinct precinct =  precinctDAO.getPrecinctByName(name);
            Demographic demographic =  demographicDAO.getDemographicByPrecinctId(precinct.getPrecinctId());
            System.out.println(demographic);
            clientData = this.createClient_Data(demographic);

        }catch(NoSuchElementException|JsonProcessingException error){
            error.printStackTrace();
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
        }
        catch(Exception error){
            error.printStackTrace();
        }
        return clientData;
    }


    public String getPlan(Integer jobID, String planID){
        Job currentJob = session.getJobByID(jobID);
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
        String clientData = "{serverError:null}";
        //TODO: [DATABASE] Implement database functionality. Save job on to the database. Assign ID to Job Object
        try{
            State currentState = session.getState();
            job.setState(currentState);
            jobDAO.addJob(job);

//            String algorithmInputContents = createAlgorithmData(currentState, job);
//            createJobDirectory(job.getJobName(), algorithmInputContents);
//            initiateAlgorithm(job);

            clientData = createClient_Data(job);
        }catch(IOException error){
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
            error.printStackTrace();
        }
        catch(Exception error){
            error.printStackTrace();
        }
        System.out.println("Generating Job: " + job);
        return clientData;
    }

    private void initiateAlgorithm(Job job){
        System.out.println("Initiating Algorithm... Creating Thread");
        AlgorithmInterface algorithmInterface = new AlgorithmInterface("carlopez", job, runAlgoLocally);
        algorithmInterface.start();
    }

    public String generateHeatMap(){
        return "generateHeatMap";

    }

    public void cancelJob(Integer jobID){
        //TODO: Confirm if we are deleting a job if we cancel it. TBD...
        // Leaning towards deleting it. Check up Danny
        Job job = session.getJobByID(jobID);
        job.setStatus(JobStatus.CANCELLED);
        jobDAO.updateJob(job);
    }

    public void deleteJob(Integer jobID){
        jobHistory.deleteJob(jobID);
        Job job = session.deleteJob(jobID);
        jobDAO.deleteJob(job);
    }


//    public List <Job> getStateJobsInformation(String stateAbbrev){
//        List <Job> jobs = fake.queryGetStateJobsInformation(stateAbbrev);
//        return jobs;
//    }
    
}
