package com.cse416.backend.service;

import com.cse416.backend.dao.FakeDataAccessObject;
import com.cse416.backend.livememory.GlobalHistory;
import com.cse416.backend.model.*;
import com.cse416.backend.model.enums.*;
import com.cse416.backend.livememory.Session;
import com.cse416.backend.model.regions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


@Service
public class ServerService {

    private final FakeDataAccessObject fake;
    private final ObjectMapper mapper;
    private Session session;
    private GlobalHistory jobHistory;
    private boolean runAlgoLocally  = true;

    @Autowired
    public ServerService(@Qualifier("fakeDao") FakeDataAccessObject fake) {
        this.fake = fake;
        this.mapper = new ObjectMapper();
        this.session = new Session();

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
        State state = fake.queryGetStateInformation(stateAbbrevation);
        List <Job> jobs = this.getStateJobsInformation(stateAbbrevation);
        this.session.setState(state);
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

    private String createClientStateData(State state, List <Job> jobs)throws JsonProcessingException{
        Map <String,Object> clientData = new HashMap<>();
        List<Object> clientJob = new ArrayList<>();
        jobs.forEach(job -> clientJob.add(job));
        clientData.put("state", state);
        clientData.put("jobs", clientJob);
        return  mapper.writeValueAsString(clientData);
    }

    public String getJob(String jobID){
        String clientData = "{serverError:\"Unknown Server Error\"}";
        try{
            Job requestedJob = this.session.getJobByID(jobID);
            //TODO: [DECISION] Since the job object will not have a reference to the geoJSON for each plan a decision
            //                 needs to be made on how it is going to acquire the random, average, extreme plan data.
            //                 Would it look in the in seawulf? would it query to the database?
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
        job.setStateAbbrev(session.getState().getStateAbbreviation());
        String clientData = "{serverError:null}";
        //TODO: [DATABASE] Implement database functionality. Save job on to the database. Assign ID to Job Object
        fake.mutationGenerateJob(job);

        try{
            //TODO: [SERVER] Implement USECASE 21
            determineAlgorithmComputeLocation(job);
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

    private void determineAlgorithmComputeLocation(Job job)throws IOException {
        if(runAlgoLocally){
            System.out.println("Running algorithm locally");
            ProcessBuilder pb = new ProcessBuilder("python3", "src/main/resources/algorithm/Algorithm2.py");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            printProcessOutput(process);
        }
        else{
            //TODO: [SERVER/ALGO] Implement slurm bash script, send the file to the seawulf.
            System.out.println("Running algorithm remotely");
//            ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/trigger.sh");
//            Process process = pb.start();
//            printProcessOutput(process);
        }
    }

    private void printProcessOutput(Process process) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            String result = builder.toString();
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createClient_Data(Object obj)throws JsonProcessingException{
        return mapper.writeValueAsString(obj);
    }


    public String generateHeatMap(){
        return "generateHeatMap";

    }

    public int saveJob(Job job){
        return 0;

    }

    public void cancelJob(String jobID){
        //TODO: [SERVER] implement cancel job functionality. Update job status to cancel and cancel all
        //               process corresponding to the job.
        //TODO: [DATABASE] implement cancel job functionality.
        //                 mutation function to update job status of a job on the remote database.
        //

    }

    public void deleteJob(String jobID){
        this.session.deleteJob(jobID);
        this.jobHistory.deleteJob(jobID);
        //TODO: [DATABASE] implement delete job functionality.
        //                 mutation function to delete the job on the remote database.
    }

    public void updateJob(String jobID){
        //TODO: [SERVER] implement server functionality for updating attribution for job
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
