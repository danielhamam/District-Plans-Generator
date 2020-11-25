package com.cse416.backend.service;

import com.cse416.backend.livememory.GlobalHistory;
import com.cse416.backend.model.regions.state.*;
import com.cse416.backend.model.job.*;
import com.cse416.backend.model.plan.*;
import com.cse416.backend.model.enums.*;
import com.cse416.backend.livememory.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.cse416.backend.dao.services.*;


import java.io.*;
import java.util.*;


@Service
public class ServerService {

//    private final FakeDataAccessObject fake;
    private final ObjectMapper mapper;
    private Session session;
    private GlobalHistory jobHistory;
    private boolean runAlgoLocally  = true;

    //DAO Servicers
    private JobDAOService jobDAO;
    private CountyDAOService countyDAO;
    private DemographicDAOService demographicDAO;
    private CensusEthnicityDAOService censusEthnicityDAO;
    private DistrictDAOService districtDAO;
    private PlanDAOService planDAO;
    private PrecinctDAOService precinctDAO;
    private StateDAOService stateDAO;

    @Autowired
    public ServerService() {
//        this.fake = fake;
        this.mapper = new ObjectMapper();
        this.session = new Session();
        this.jobHistory = new GlobalHistory();

        //DAO SERVICES
        this.jobDAO = new JobDAOService();
        this.countyDAO = new CountyDAOService();
        this.demographicDAO = new DemographicDAOService();
        this.censusEthnicityDAO = new CensusEthnicityDAOService();
        this.districtDAO = new DistrictDAOService();
        this.planDAO = new PlanDAOService();
        this.precinctDAO = new PrecinctDAOService();
        this.stateDAO = new StateDAOService();

    }

    private String createClient_Data(Object obj)throws JsonProcessingException{
        return mapper.writeValueAsString(obj);
    }

    private String createClientStateData(State state, List <Job> jobs)throws JsonProcessingException{
        Map <String,Object> clientData = new HashMap<>();
        List<Object> clientJob = new ArrayList<>();
        jobs.forEach(job -> clientJob.add(job));
        clientData.put("state", state);
        clientData.put("system/jobs", clientJob);
        return  mapper.writeValueAsString(clientData);
    }

    private String createAlgorithmData(Object stateObj, Job job)throws JsonProcessingException{
        Map <String,Object> AlgorithmData = new HashMap<>();
        Map <String,Object> insideOfAlgorithmData = new HashMap<>();
        insideOfAlgorithmData.put("state", stateObj);
        insideOfAlgorithmData.put("job", job);
        AlgorithmData.put("data",insideOfAlgorithmData);
        return  mapper.writeValueAsString(insideOfAlgorithmData);
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
        //TODO: [DATABASE] Replace the line below to fetch the state from the remote database.
        //      Mutation function to update job status of a job on the remote database.
        Optional state = stateDAO.getStateById(stateAbbrevation);
        System.out.print(state);
//        List <Job> jobs = null;
//        System.out.println(jobs);
//        this.session.setState(state);
//        this.session.addJobs(jobs);
//        this.jobHistory.addJobs(jobs);
//        String clientData = "{serverError:null}";
//        try{
//            clientData = createClientStateData(state, jobs);
//        }catch(JsonProcessingException error){
//            clientData = "{serverError:\"" + error.getMessage() + "\"}";
//        }
//        catch(Exception error){
//            error.printStackTrace();
//        }
//        return clientData;
        return "OKAY";
    }

    public String getJob(String jobID){
        String clientData = "{serverError:\"Unknown Server Error\"}";
        try{
            //TODO: [DECISION] Since the job object will not have a reference to the geoJSON for each plan a decision
            //                 needs to be made on how it is going to acquire the random, average, extreme plan data.
            //                 Would it look in the in seawulf? would it query to the database?
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

    public String getPrecinct(){
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
//        State currentState = session.getState();
        //job.setStateAbbrev(currentState.getStateAbbreviation());
        String clientData = "{serverError:null}";
        //TODO: [DATABASE] Implement database functionality. Save job on to the database. Assign ID to Job Object
        //fake.mutationGenerateJob(job);
        // this.jobService.addJob(job);
        

        try{
            //TODO: [SERVER] Implement USECASE 21
//            String algorithmInputContents = createAlgorithmData(currentState.getAlgorithmJson(), job);
            String algorithmInputContents = "Hello";
            createJobDirectory(job.getJobName(), algorithmInputContents);
            initiateAlgorithm(job);
            clientData = createClient_Data(job);
        }catch(IOException error){
            error.getMessage();
            clientData = "{serverError:\"" + error.getMessage() + "\"}"; 
        }
        catch(Exception error){
            error.printStackTrace();
        }
        System.out.println("Generating Job: " + job);
        return clientData;
    }

    private void initiateAlgorithm(Job job){
        System.out.println("Initiating Algorithm... Creating Thread");
        AlgorithmInterface algorithmInterface = new AlgorithmInterface(job, null, runAlgoLocally);
        algorithmInterface.start();
    }

    public String generateHeatMap(){
        return "generateHeatMap";

    }

    public int saveJob(Job job){
        //TODO: [DATABASE] This could help communicate to the database. Helper function. Determine usage & importance
        return 0;
    }

    public void cancelJob(String jobID){
        this.session.cancelJob(jobID);
        //TODO: [DATABASE] implement cancel job functionality.
        //                 mutation function to update job status of a job on the remote database.
        //

    }

    public void deleteJob(String jobID){
        session.deleteJob(jobID);
        jobHistory.deleteJob(jobID);
        //fake.deleteJob(jobID);
        //TODO: [DATABASE] implement delete job functionality.
        //                 mutation function to delete the job on the remote database.
    }

    public void updateJob(String jobID){
        //TODO: [SERVER] implement server functionality for updating attribution for job
    }

//    public List <Job> getStateJobsInformation(String stateAbbrev){
//        List <Job> jobs = fake.queryGetStateJobsInformation(stateAbbrev);
//        return jobs;
//    }
    
}
