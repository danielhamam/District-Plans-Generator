package com.cse416.backend.service;

import com.cse416.backend.livememory.GlobalHistory;
import com.cse416.backend.model.demographic.CensusEthnicity;
import com.cse416.backend.model.demographic.Demographic;
import com.cse416.backend.model.job.boxnwhisker.BoxWhisker;
import com.cse416.backend.model.job.boxnwhisker.BoxWhiskerPlot;
import com.cse416.backend.model.job.summary.Constraints;
import com.cse416.backend.model.job.summary.Districting;
import com.cse416.backend.model.job.summary.Summary;
import com.cse416.backend.model.plan.comparators.CompactnessCompare;
import com.cse416.backend.model.regions.county.County;
import com.cse416.backend.model.regions.district.District;
import com.cse416.backend.model.regions.precinct.Precinct;
import com.cse416.backend.model.regions.state.*;
import com.cse416.backend.model.job.*;
import com.cse416.backend.model.plan.*;
import com.cse416.backend.model.enums.*;
import com.cse416.backend.livememory.Session;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.geojson.FeatureCollection;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.cse416.backend.dao.services.*;


import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ServerService {

    private final ObjectMapper mapper;
    private Session session;
    private List<Algorithm> threads;


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
        this.threads = new ArrayList<>();


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


    private void createJobDirectory(Job job)throws IOException{
        State currentState = session.getState();
        String algorithmContents = createAlgorithmData(currentState, job);
        String jobDirectoryName = job.getJobName().toLowerCase();
        String jobDirectoryRelativePath = "src/main/resources/system/jobs";
        String jobDirectoryAbsolutePath =  new File(jobDirectoryRelativePath).getAbsolutePath();
        String newDirectoryAbsolutePath =  jobDirectoryAbsolutePath+"/"+jobDirectoryName;
        boolean jobDirCreated = new File(jobDirectoryAbsolutePath + "/" + jobDirectoryName).mkdirs();
        if(jobDirCreated){
            System.out.println("./" + jobDirectoryName + " directory has been created. Writing AlgorithmInput.json");
            FileWriter algorithmInputFileWriter = new FileWriter(newDirectoryAbsolutePath+"/"+"AlgorithmInput.json");
            algorithmInputFileWriter.write(algorithmContents);
            algorithmInputFileWriter.close();
            String algorithmOutputPath = jobDirectoryAbsolutePath + "/" + jobDirectoryName + "/algorithm-output";
            boolean algorithmOutputDirCreated = new File(algorithmOutputPath).mkdirs();
            if(algorithmOutputDirCreated){
                System.out.println(algorithmOutputPath + " directory has been created");
            }else{
                System.err.println("algorithm-output directory are already exists");
            }
        }else{
            System.err.println(jobDirectoryName + " directory are already exists");
        }

    }

    public String connectingClient(){
        //TODO: Implement
        return "connectingClient";
    }
    
    public String getState(String stateAbbrevation){
        String clientData = "{serverError:null}";
        try{
            //State logic
            State state = stateDAO.getStateById(stateAbbrevation);
            state.initializeSystemFiles();
            Demographic stateDemographic = demographicDAO.getDemographicByStateId(stateAbbrevation);
            state.setDemographic(stateDemographic);
            state.setTotalPopulation(stateDemographic.getTotalPopulation());
            session.setState(state);
            loadStateDemographics();

            //Job logic
            List <Job> jobs = jobDAO.getJobsByStateId(stateAbbrevation);
            for(Job j: jobs){
                j.setState(state);
                List <CensusCatagories> censusCatagoriesEnum = new ArrayList<>();
                for(CensusEthnicity censusEthnicity : j.getMinorityAnalyzedCensusEthnicity()){
                    censusCatagoriesEnum.add(CensusCatagories.getEnumFromString(censusEthnicity.getEthnicityName()));
                    j.setMinorityAnalyzedEnumration(censusCatagoriesEnum);
                }
                createJobDirectory(j);

//                if(!JobStatus.FINISHED.equals(j.getStatus())){
//                    System.out.println(j.getJobID() + " restarting algorithm for jobs.");
//                    reInitiateAlgorithm(j);
//                }
            }
//            session.addJobs(jobs);
            clientData = createClientStateData(state, jobs);
            System.out.println("Server func getState() successful");
        }catch(JsonProcessingException error){
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
            error.printStackTrace();
        }
        catch(Exception error){
            error.printStackTrace();
        }

        return clientData;
    }

    @Async
    private void loadStateDemographics(){
        new Thread(new Runnable() {
            public void run() {
                State state = session.getState();
                for(District district : state.getStateDistricts()){
                    Demographic demo =  demographicDAO.getDemographicByDistrictId(district.getDistrictId());
                    district.setDemographic(demo);
                }
                System.out.println(state.getStateAbbreviation() +
                        " demographics for precinct and districts have been loaded!");
            }
        }).start();
    }






    public String getJob(Integer jobID){
        String clientData = "{serverError:\"Unknown Server Error\"}";
        try{
            Job job = jobDAO.getJobById(jobID).orElseThrow(NoSuchElementException::new);
            job.initializeJobFiles();
            Map<String, Object> dataObject = new HashMap<>();
            dataObject.put("districtPlans", job.getClientPlans());
            clientData = this.createClient_Data(dataObject);
            System.out.println("Server func getJob() successful. Sending plans to client.");
        }catch(NoSuchElementException|JsonProcessingException error){
            error.printStackTrace();
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
        }
        catch(Exception error){
            error.printStackTrace();
        }
        return clientData;
    }

    public String getJobSummary(Integer jobID){
        String clientData = "{serverError:\"Unknown Server Error\"}";
        try{
            Job job = jobDAO.getJobById(jobID).orElseThrow(NoSuchElementException::new);
            job.initializeJobFiles();
            Map<String, Object> dataObject = new HashMap<>();
            dataObject.put("jobsummary", job.getSummary());
            clientData = this.createClient_Data(dataObject);
            System.out.println("Server func getJobSummary() successful. Sending plans to client.");
        }catch(NoSuchElementException|JsonProcessingException error){
            error.printStackTrace();
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
        }
        catch(Exception error){
            error.printStackTrace();
        }
        return clientData;
    }


    public String getJobsUpdate(){
        String clientData = "{serverError:\"Unknown Server Error\"}";
        try{
            Map<String, Object> dataObject = new HashMap<>();
            List <Job> jobs = jobDAO.getAllJobs();
            for(Job j: jobs){
                List <CensusCatagories> censusCatagoriesEnum = new ArrayList<>();
                for(CensusEthnicity censusEthnicity : j.getMinorityAnalyzedCensusEthnicity()){
                    censusCatagoriesEnum.add(CensusCatagories.getEnumFromString(censusEthnicity.getEthnicityName()));
                    j.setMinorityAnalyzedEnumration(censusCatagoriesEnum);
                }
            }
            dataObject.put("jobs", jobDAO.getAllJobs());
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
            HashMap <String, Object> map = new HashMap<>();
            map.put("precinctsGeoJson", heatmapNode);
            clientData = createClient_Data(map);
            System.out.println("Server func getDemographicHeatmap() successful");
        }catch(Exception error){
            error.printStackTrace();
        }
        return clientData;
    }

    public String getPrecincts(){
        String clientData = "{serverError:\"Unknown Server Error\"}";
        try{
            Object precinctsGeoJson = this.session.getState().getClientPrecinctsGeoJson();
            clientData = this.createClient_Data(precinctsGeoJson);
            System.out.println("Server func getPrecincts() successful");
        }catch(NoSuchElementException|JsonProcessingException error){
            error.printStackTrace();
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
        }
        catch(Exception error){
            error.printStackTrace();
        }
        return clientData;
    }


    public String getPrecinctDemographic(String fips){
        String clientData = "{serverError:\"Unknown Server Error\"}";
        try{
            System.out.println(fips);
            Precinct precinct =  precinctDAO.getPrecinctByFIPSCode(fips);
            Demographic demographic =  demographicDAO.getDemographicByPrecinctId(precinct.getPrecinctId());
            clientData = this.createClient_Data(demographic);
            System.out.println("Server func getPrecinctDemographic() successful");
        }catch(NoSuchElementException|JsonProcessingException error){
            error.printStackTrace();
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
        }
        catch(Exception error){
            error.printStackTrace();
        }
        return clientData;
    }


    public String getBoxWhisker(Integer jobID){

        String clientData = "{serverError:\"Unknown Server Error\"}";
        try{
            Job job = jobDAO.getJobById(jobID).orElseThrow(NoSuchElementException::new);
            HashMap <String, Object> map = new HashMap<>();
            map.put("graph", job.getBoxWhisker());
            clientData = createClient_Data(map);
            System.out.println("Server func getBoxWhisker() successful");
        }catch(NoSuchElementException|JsonProcessingException error){
            error.printStackTrace();
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
        }
        catch(Exception error){
            error.printStackTrace();
        }
        return clientData;
    }


    private List <CensusEthnicity> covertClientCensusToDatabaseCensus(Job job){
        //Retrieve the minority group to analyze provide the client
        List<CensusCatagories> getMinorityAnalyzedEnumration = job.getMinorityAnalyzedEnumration();
        List <CensusEthnicity> censusEthnicities = new ArrayList<>();
        getMinorityAnalyzedEnumration.forEach(e -> {
            System.out.println(e.getShortenName());
            censusEthnicities.add(censusEthnicityDAO.getCensusEthnicityById(e.getShortenName()).get());

        });
        return censusEthnicities;
    }

    private void initiateAlgorithm(Job job){
        System.out.println("Initiating Algorithm... Creating Thread");
        Algorithm algorithmInterface = null;
        if(job.getNumDistrictingPlan() <= 10){
            algorithmInterface = new Algorithm("carlopez", job, true);
        }
        else{
            algorithmInterface = new Algorithm("carlopez", job, false);
        }
        algorithmInterface.start();
        threads.add(algorithmInterface);
    }

    private void reInitiateAlgorithm(Job job){
        System.out.println("Initiating Algorithm... Creating Thread");
        Algorithm algorithmInterface = null;
        if(job.getNumDistrictingPlan() <= 10){
            algorithmInterface = new Algorithm("carlopez", job, true, true);
        }
        else{
            algorithmInterface = new Algorithm("carlopez", job, false, true);
        }
        algorithmInterface.start();
        threads.add(algorithmInterface);
    }



    public void cancelJob(Integer jobID){
        Job job = jobDAO.getJobById(jobID).orElseThrow(NoSuchElementException::new);
        try{
            System.out.println("Attempting to cancel a job " + jobID + ". It's status: " + job.getStatus());
            if(!job.getStatus().equals(JobStatus.FINISHED)){
                Algorithm currentThread =  threads.stream()
                        .filter(thread -> job.getJobID().equals(thread.getJob().getJobID()))
                        .findFirst()
                        .orElseThrow(NoSuchElementException::new);
                currentThread.cancelJobDriver();
                threads.remove(currentThread);
                System.out.println("Thread removed. Thread pool size: " + threads.size());
                jobDAO.deleteJob(job);
                System.out.println("Job " + job.getJobID() + " has been cancelled and removed");
            }
            else{
                System.out.println("Since status is: " + job.getStatus() +  " redirecting control to cancelJob");
                deleteJob(jobID);
            }

        }catch (NoSuchElementException error){
            System.out.println("NoSuchElementException. Thus mismatched happened between server session." +
                    "Overrwriting normal execution in catch statement -> Hard deleting job" +
                    " ID: " + job.getJobID());
            jobDAO.deleteJob(job);
        }catch(Exception error){
            error.printStackTrace();
        }

    }

    public void deleteJob(Integer jobID){
        try {
            Job job = jobDAO.getJobById(jobID).orElseThrow(NoSuchElementException::new);
            System.out.println("Attempting to delete a job " + jobID + ". It's status: " + job.getStatus());
            if (job.getStatus() == (JobStatus.FINISHED)) {
                jobDAO.deleteJob(job);
                System.out.println("Job " + job.getJobID() + " has been removed");
            } else {
                //TODO: What does //jobDAO.cancelJob() do?
                System.out.println("Since status is: " + job.getStatus() + " redirecting control to cancelJob");
                cancelJob(jobID);
                ////jobDAO.cancelJob(job);
            }
        }catch(Exception error){
            error.printStackTrace();
        }
    }

    public String generateJob(Job job){
        String clientData = "{serverError:null}";
        //TODO: [DATABASE] Implement database functionality. Save job on to the database. Assign ID to Job Object
        try{
            //session.addJob(job);
            State currentState = session.getState();
            job.setState(currentState);
            List <CensusEthnicity> censusEthnicities = covertClientCensusToDatabaseCensus(job);
            job.setMinorityAnalyzed(censusEthnicities);
            //TODO: Delete line below
            //job.setJobID(Math.abs(UUID.randomUUID().hashCode()));
            createJobDirectory(job);
            jobDAO.addJob(job);
            initiateAlgorithm(job);
            clientData = createClient_Data(job);
            System.out.println("Server func generateJob() successful");

        }catch(IOException error){
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
            error.printStackTrace();
        }
        catch(Exception error){
            error.printStackTrace();
        }
        return clientData;
    }




    public class Algorithm implements Runnable {
            private Thread proxyThread = null;
            private final String netid;
            private final String jobsDirectory;
            private final String jobDirectory;
            private volatile boolean die = false;
            private volatile boolean isAlgorithmLocal;
            private volatile boolean isComputeLocationDetermined;
            private volatile boolean isJobCancelled;
            private List<Process> localAlgorithmProcesses;
            private Job job;


            public Algorithm(String netid, Job job, boolean runAlgoLocally) {
                this.netid = netid;
                this.job = job;
                this.isAlgorithmLocal = runAlgoLocally;
                this.isComputeLocationDetermined = false;
                this.isJobCancelled = false;
                this.jobsDirectory = "src/main/resources/system/jobs/";
                this.jobDirectory = "src/main/resources/system/jobs/" + job.getJobName().toLowerCase() + "/";
                this.localAlgorithmProcesses = new ArrayList<>();

            }



            //This constructor is to reintialize the algorithm in the event that the server didn't wait for a job's completion in a previous server session
            public Algorithm(String netid, Job job, boolean runAlgoLocally, boolean reInitiateAlgorithm) {
                this.netid = netid;
                this.job = job;
                this.isAlgorithmLocal = runAlgoLocally;
                if(isAlgorithmLocal){
                    this.isComputeLocationDetermined = false;
                }
                else{
                    this.isComputeLocationDetermined = true;
                }
                this.isJobCancelled = false;
                this.jobsDirectory = "src/main/resources/system/jobs/";
                this.jobDirectory = "src/main/resources/system/jobs/" + job.getJobName().toLowerCase() + "/";
                this.localAlgorithmProcesses = new ArrayList<>();

            }

            public Job getJob(){
                return job;
            }

            private void printProcessOutput(Process process) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    StringBuilder builder = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        builder.append("\t\t");
                        builder.append(line);
                        builder.append(System.getProperty("line.separator"));
                    }
                    String result = builder.toString();
                    System.out.format("Process:\n%s\n", result);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void start() {
                if (proxyThread == null) {
//                    try{
//                        String algorithmOutputPath = jobDirectory + "algorithm-output/" + "AlgorithmOutput.json";
//                        String algorithmOutputAbsolutePath = new File(algorithmOutputPath).getAbsolutePath();
//                        File algorithmOutput = new File(algorithmOutputAbsolutePath);
//                        processAlgorithmOutput(algorithmOutput);
//                    }
//                    catch (Exception e){
//                        e.printStackTrace();
//
//                    }


                System.out.println("Thread " + hashCode()  + " starting...");
                proxyThread = new Thread(this);
                proxyThread.start();
                }
            }

            public void kill(){
                die = true;
            }



            public void run() {
                while (!die) {
                    try {
                        System.out.println("Thread " + hashCode() + " is still running." +
                                "Job's ID: " + job.getJobID() + "\t" +
                                "Job's status: " + job.getStatus() + "\t" +
                                "Job's seawulfJobID: " + job.getSeawulfJobID());

                        if(isJobCancelled){
                            cancelJob();
                            break;
                        }

                        if (!isComputeLocationDetermined) {
                            determineAlgorithmComputeLocation();
                            continue;
                        }

                        monitorAlgorithm();


                        JobStatus status = job.getStatus();
                        if(status.equals(JobStatus.PROCESSING)){
                            System.out.println("Job" + job.getJobID() + " status: " + job.getStatus());
                            extractDataFromJob();
                            initiateServerProcessing();
                            kill();
                        }

                        longSleepThread();
                    }
                    catch(InterruptedException error){
                        System.out.println(this + "Interrupted thread..");
                        error.printStackTrace();
                    }
                    catch (IOException error) {
                        System.out.println("IOException Killing thread..");
                        error.printStackTrace();
                        kill();
                    }
                    catch (Exception error) {
                        error.printStackTrace();
                        System.out.println(error.getCause().getMessage() + " Exception Killing thread.");
                        kill();
                    }
                }
                System.out.println("Killed thread: " + this);
            }


            public void cancelJobDriver() {
                this.isJobCancelled = true;
            }

            private void cancelJob()throws IOException, InterruptedException{
                //Job tempJob = jobDAO.getJobById(job.getJobID()).orElseThrow(NoSuchElementException::new);
                if(isAlgorithmLocal){
                    System.out.println("JobID " + job.getJobID() + ": " + "Canceling job locally.");
                    for(Process process : localAlgorithmProcesses) {
                       process.destroy();

                    }
                }else{
                    System.out.println("JobID " + job.getJobID() + ": " + "Canceling job remotely.");
                    ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/bash/CancelAlgorithm.sh",
                            netid, job.getSeawulfJobID());
                    pb.redirectErrorStream(true);
                    Process tempProcess = pb.start();
                    shortSleepThread();
                    //printProcessOutput(tempProcess);
                }

                job.setStatus(JobStatus.CANCELLED);
                System.out.println("Job " + job.getJobID() + " cancelled");
                kill();
            }

            private void longSleepThread() throws InterruptedException{
                long sleep = 90000; //15 minutes
                if(isAlgorithmLocal){
                    sleep = 65500; //15 minutes
                }
                double durationInMinutes = (sleep)/(60000 + 0.0);
                System.out.println("JobID " + job.getJobID() + ": " +
                        "Sleeping Thread " + hashCode() + " : Long sleep for " + durationInMinutes + " minutes");
                Thread.sleep(sleep);
            }

            private void shortSleepThread() throws InterruptedException{
                long sleep = 30000; //0.5 minutes
                if(isAlgorithmLocal){
                    sleep = 35500; //15 minutes
                }
                double durationInMinutes = (sleep)/(60000 + 0.0);
                System.out.println("JobID " + job.getJobID() + ": " +
                        "Sleeping Thread " + hashCode() + " : Short sleep for " + durationInMinutes + " minutes");
                Thread.sleep(sleep);
            }

            private void generateSummaryFile()throws IOException{
                State state = job.getState();

                String compactnessLimit = job.getClientCompactness().getRepresentation();
                String populationDifferenceLimit = ""+ job.getPopulationDifference();
                List <String> minorityGroups =
                        job.getMinorityAnalyzedEnumration()
                                .stream()
                                .map(e -> e.getFullName())
                                .collect(Collectors.toList());

                Constraints constraints = new Constraints(compactnessLimit, populationDifferenceLimit, minorityGroups);
                //TODO: Add Congressional Districts GeoJson
                Districting averageDistricting = new Districting("0", constraints,
                        job.getAverageDistrictPlan().getDistrictsGeoJson());
                Districting extremeDistricting = new Districting("1",constraints,
                        job.getExtremeDistrictPlan().getDistrictsGeoJson());
                Districting randomDistricting = new Districting("2",constraints,
                        job.getRandomDistrictPlan().getDistrictsGeoJson());
                List <Districting> districtings = new ArrayList<>();
                districtings.add(averageDistricting);
                districtings.add(extremeDistricting);
                districtings.add(randomDistricting);
                Summary summary = new Summary(state.getStateName(), state.getStateAbbreviation(),
                        state.getPrecinctsGeoJson(),districtings);
                job.setSummary(summary);
                FileWriter file = new FileWriter(new File(jobDirectory).getAbsolutePath() + "/" + "Summary.json");
                mapper.writeValue(file, summary);
                System.out.println("JobID " + job.getJobID() + ": Generating Summary File");
            }


            private ObjectNode formatObjectNode(JsonNode node, String type){
                ObjectNode objectNode = node.deepCopy();
                objectNode.put("type", type);
                return objectNode;

            }

            private void setDistrictGeoJson(Plan plan, String fileName)throws IOException{
                String filePath = jobDirectory + fileName;
                File file = new File(new File(filePath).getAbsolutePath());
                plan.setDistrictsGeoJson(mapper.readValue(file, FeatureCollection.class));
            }

            private void createPlanGeojson(JsonNode plansNode)throws IOException, InterruptedException{
                Plan averagePlan = job.getAverageDistrictPlan();
                Plan extremeplan = job.getExtremeDistrictPlan();
                Plan randomPlan = job.getRandomDistrictPlan();

                //Format data
                ObjectNode objectNode = mapper.createObjectNode();
                ArrayNode clientPlans = mapper.createArrayNode();
                int planID = Integer.parseInt(averagePlan.getType());
                clientPlans.add(formatObjectNode(plansNode.get(planID-1), "Average"));
                planID = Integer.parseInt(extremeplan.getType());
                clientPlans.add(formatObjectNode(plansNode.get(planID-1), "Extreme"));
                planID = Integer.parseInt(randomPlan.getType());
                clientPlans.add(formatObjectNode(plansNode.get(planID-1), "Random"));
                objectNode.set("plans", clientPlans);

                //write file
                String filename = "ConvertPlans.json";
                String filePath = new File(jobDirectory).getAbsolutePath() + "/" + filename;
                FileWriter CovertPlansFile = new FileWriter(filePath);
                mapper.writeValue(CovertPlansFile, objectNode);
                System.out.println("JobID " + job.getJobID() + ": " + filePath + " has been created.");

                //Run ConvertPlanDistrictsToGeoJson.py
                ProcessBuilder pb = new ProcessBuilder("python3",
                        "src/main/resources/python/preprocessing/ConvertPlanDistrictsToGeoJson.py", jobDirectory);
                Process temp = pb.start();
                printProcessOutput(temp);
                shortSleepThread();
                System.out.println("JobID " + job.getJobID() +
                        ": Python script done. *District.json files should be crated");

                //setters
                setDistrictGeoJson(averagePlan, "AverageDistrict.json");
                setDistrictGeoJson(extremeplan, "ExtremeDistrict.json");
                setDistrictGeoJson(randomPlan, "RandomDistrict.json");
                System.out.println("JobID " + job.getJobID() + ": Plan district geoJson crated");
            }

            private void determinePlans(List <Plan> allPlans)throws Exception{
                if(allPlans.size() == 0){
                    throw new Exception("List size zero : allPlans.size() == 0");
                }

                //Calculate stat numbers. Used to determine avg, extreme and random
                double size = allPlans.size();
                double compactnessListSum = allPlans.stream()
                        .map(e -> e.getAverageDistrictCompactness())
                        .reduce(0D, Double::sum);
                double compactnessListMean = compactnessListSum/size;

                //Set average plan and extreme plan
                Plan averagePlan = allPlans.get(0);
                Plan extremeplan = allPlans.get(0);
                Plan randomPlan = null;
                for(Plan plan : allPlans){
                    double newValue = Math.abs(compactnessListMean - plan.getAverageDistrictCompactness());
                    double oldValue = Math.abs(compactnessListMean - averagePlan.getAverageDistrictCompactness());
                    if(newValue < oldValue){
                        averagePlan = plan;
                    }

                    if(newValue > oldValue){
                        extremeplan = plan;
                    }
                }
                //Set random plan
                for(Plan plan : allPlans){
                    if(plan != averagePlan && plan != extremeplan){
                        randomPlan = plan;
                    }
                }

                //setters
                List <Plan> plans = new ArrayList<>();
                job.setAverageDistrictPlan(averagePlan);
                job.setExtremeDistrictPlan(extremeplan);
                job.setRandomDistrictPlan(randomPlan);
                plans.add(averagePlan);
                plans.add(extremeplan);
                plans.add(randomPlan);

                System.out.println("JobID " + job.getJobID() + ": Average, extreme, and random plans determined");
            }

            private void createBoxWhisker(List <Plan> allPlans)throws Exception{
                if(allPlans.size() == 0){
                    throw new Exception("List size zero : allPlans.size() == 0");
                }
                //System.out.println("Creating Box and Whisker Plot." + " Number of plans " + allPlans.size());

                //Get ethnicity to sort list by
                CensusCatagories minorityAnalyzed  =  job.getMinorityAnalyzedEnumration().get(0);
                Comparator comparator = District.getComparatorByCensusCatagories(minorityAnalyzed);
                //System.out.println("Sorting by " + minorityAnalyzed + " using " + comparator);
//                Hibernate.initialize(job.getState().getStateDistricts());
                List <District> enactedPlanDistrict =  job.getState().getStateDistricts();

                for (Plan plan : allPlans) {
                    Collections.sort(plan.getDistricts(), comparator);
                }
                Collections.sort(enactedPlanDistrict, comparator);

                //For each district. Go through all the plans. Create BoxWhiskerPlot Object
                List<BoxWhiskerPlot> boxWhiskerPlots = new ArrayList<>();
                for (int districtIndex = 0; districtIndex < job.getNumOfDistricts(); districtIndex++) {
                    List<Long> districtsPopulationOfPlans = new ArrayList<>();
                    for (Plan plan : allPlans) {
                        District planDistrict = plan.getDistricts().get(districtIndex);
                        Long population = planDistrict.getDemographic().getVAPByCensusCatagories(minorityAnalyzed);
                        districtsPopulationOfPlans.add(population);
//                        System.out.print(plan.getType() + "[ District: "+ planDistrict.getDistrictNumber()
//                                + ", Population: " + population +"]\t");

                    }
//                    System.out.println();
                    Collections.sort(districtsPopulationOfPlans);
//                    System.out.println("Population for district " + (districtIndex+1) + " :" +  districtsPopulationOfPlans);
                    int size = districtsPopulationOfPlans.size();
                    long min = districtsPopulationOfPlans.get(0);
                    long q1 = districtsPopulationOfPlans.get(size/4);
                    long q2 = districtsPopulationOfPlans.get(size/2);
                    long q3 = districtsPopulationOfPlans.get(size*3/4);
                    long max = districtsPopulationOfPlans.get(size-1);
                    long enactedPlanValue = enactedPlanDistrict.get(districtIndex)
                            .getDemographic().getVAPByCensusCatagories(minorityAnalyzed);
                    boxWhiskerPlots.add(new BoxWhiskerPlot(districtIndex+1, min,q1,q2,q3,max, enactedPlanValue));

                }
                BoxWhisker temp = new BoxWhisker(boxWhiskerPlots);
                boxWhiskerDAO.addBoxWhisker(temp);
                job.setBoxWhisker(temp);
                //Job job = jobDAO.getJobById();
                //System.out.println("\n" + job.getBoxWhisker() + "\n");
                System.out.println("JobID " + job.getJobID() + ": Box and whisker graph created. " + temp);


            }

            private District processingCreateDistrict(int districtID, Plan plan, JsonNode districtObject){
                Long districtTotalPopulation = 0L;
                Long districtWhitePopulation = 0L;
                Long districtHispanicPopulation = 0L;
                Long districtAmericanIndianPopulation = 0L;
                Long districtNativeHawaiianPopulation = 0L;
                Long districtAfricanAmericanPopulation = 0L;
                Long districtAsianPopulation = 0L;
                Long districtOtherRacePopulation = 0L;
                Long districtMultipleRacePopulation = 0L;
                Long districtTotalVAPPopulation = 0L;
                Long districtWhiteVAPPopulation = 0L;
                Long districtHispanicVAPPopulation = 0L;
                Long districtAmericanIndianVAPPopulation = 0L;
                Long districtNativeHawaiianVAPPopulation = 0L;
                Long districtAfricanAmericanVAPPopulation = 0L;
                Long districtAsianVAPPopulation = 0L;
                Long districtOtherRaceVAPPopulation = 0L;
                Long districtMultipleRaceVAPPopulation = 0L;

                List <Precinct> precinctList = new ArrayList<>();
                List <County> countiesList = new ArrayList<>();

                JsonNode districtPrecinctList = districtObject.get("precincts");
                for(JsonNode precinctObject : districtPrecinctList){
                    String precinctFIPSCode =  precinctObject.asText();
//                    Precinct precinct = job.getState().getPrecinctByFIPSCode(precinctFIPSCode);
//                    Demographic precinctDemographic = precinct.getDemographic();

                    Precinct precinct = precinctDAO.getPrecinctByFIPSCode(precinctFIPSCode);
                    Demographic precinctDemographic =
                            demographicDAO.getDemographicByPrecinctId(precinct.getPrecinctId());
                    districtTotalPopulation += precinctDemographic.getTotalPopulation();
                    districtWhitePopulation += precinctDemographic.getWhitePopulation();
                    districtHispanicPopulation += precinctDemographic.getHispanicPopulation();
                    districtAmericanIndianPopulation += precinctDemographic.getAmericanIndianPopulation();
                    districtNativeHawaiianPopulation += precinctDemographic.getNativeHawaiianPopulation();
                    districtAfricanAmericanPopulation += precinctDemographic.getAfricanAmericanPopulation();
                    districtAsianPopulation += precinctDemographic.getAsianPopulation();
                    districtOtherRacePopulation += precinctDemographic.getOtherRacePopulation();
                    districtMultipleRacePopulation += precinctDemographic.getMultipleRacePopulation();
                    districtTotalVAPPopulation += precinctDemographic.getVAPTotalPopulation();
                    districtWhiteVAPPopulation += precinctDemographic.getWhiteVAPPopulation();
                    districtHispanicVAPPopulation += precinctDemographic.getHispanicVAPPopulation();
                    districtAmericanIndianVAPPopulation += precinctDemographic.getAmericanIndianVAPPopulation();
                    districtNativeHawaiianVAPPopulation += precinctDemographic.getNativeHawaiianVAPPopulation();
                    districtAfricanAmericanVAPPopulation += precinctDemographic.getAfricanAmericanVAPPopulation();
                    districtAsianVAPPopulation += precinctDemographic.getAsianVAPPopulation();
                    districtOtherRaceVAPPopulation += precinctDemographic.getOtherRaceVAPPopulation();
                    districtMultipleRaceVAPPopulation += precinctDemographic.getMultipleRaceVAPPopulation();
                    precinctList.add(precinct);
                    County precinctCounty = precinct.getCounty();
                    if(!countiesList.contains(precinctCounty)){
                        countiesList.add(precinctCounty);
                    }

                }

                Demographic districtDemographic = new Demographic(districtTotalPopulation, districtWhitePopulation,
                        districtHispanicPopulation, districtAmericanIndianPopulation, districtNativeHawaiianPopulation,
                        districtAfricanAmericanPopulation, districtAsianPopulation, districtOtherRacePopulation,
                        districtMultipleRacePopulation, districtTotalVAPPopulation, districtWhiteVAPPopulation,
                        districtHispanicVAPPopulation, districtAmericanIndianVAPPopulation,
                        districtNativeHawaiianVAPPopulation, districtAfricanAmericanVAPPopulation,
                        districtAsianVAPPopulation, districtOtherRaceVAPPopulation, districtMultipleRaceVAPPopulation);


                District district = new District(districtID, job.getState(), plan, countiesList, precinctList);
                //Todo: figure out how to fulliful the varibles in demogarphic
                //Todo: figure out how save to the database
                district.setDemographic(districtDemographic);
                return district;
            }

            private List <District> processingCreatePlanDistricts(Plan plan, JsonNode planObject){
                //Create and add District objects for Plan object
                int districtElement = 0;
                List <District> districts = new ArrayList<>();
                JsonNode graphDistricts = planObject.get("graph_districts");
                for(JsonNode districtObject : graphDistricts){
                    //Create districts for the plan
                    District tempDistricts =  processingCreateDistrict(districtElement, plan, districtObject);
                    districts.add(tempDistricts);
                    districtElement++;
                }
                return districts;

            }

            private void prettyPrintPlan(Plan plan){
                int districtElement = 1;
                StringBuilder s = new StringBuilder();
                s.append("Plan " + plan.getType() + " [\n");
                for (District d: plan.getDistricts()) {
                    s.append("District " + districtElement + ":" + "\n" + d.getDemographic() + "Districts Precinct: {");
                    for(Precinct p : d.getPrecincts()){
                        s.append(p.getPrecinctFIPSCode() + ", ");
                    }
                    s.append("}\n\n");
                    districtElement++;
                }
                s.append("]");
                System.out.println(s);

            }

            private void processAlgorithmOutput(File algorithmOutputFile)throws IOException, Exception{
                //Covert data from AlgorithmOutput.json to Java Objects
                System.out.println("Starting processing...");
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(algorithmOutputFile);
                JsonNode plansNode = rootNode.get("plans");
                List <Plan> plansList = new ArrayList<>();
                for(JsonNode planObject : plansNode){
                    //Create Plan object for Job Object
                    double averageDistrictPopulation = planObject.get("averageDistrictPopulation").asDouble();
                    double averageDistrictCompactness = planObject.get("averageDistrictCompactness").asDouble();
                    String planID = planObject.get("planId").asText();
                    Plan plan = new Plan(job, job.getNumOfDistricts(),
                            averageDistrictPopulation, averageDistrictCompactness, planID);

                    //Create Plan's districts
                    List <District> districts = processingCreatePlanDistricts(plan, planObject);

                    //setters or adders
                    plan.setDistricts(districts);
                    plansList.add(plan);
                    //prettyPrintPlan(plan);
                }
                System.out.println("All plan has been created to java objects");
                job.setAllPlans(plansList);
                createBoxWhisker(plansList);
                determinePlans(plansList);
                createPlanGeojson(plansNode);
                generateSummaryFile();
                job.getAverageDistrictPlan().setType("Average");
                job.getExtremeDistrictPlan().setType("Extreme");
                job.getRandomDistrictPlan().setType("Random");
                //Job tempJob = jobDAO.getJobById(job.getJobID()).get();
                job.setStatus(JobStatus.FINISHED);
                Job temp = jobDAO.getJobById(job.getJobID()).orElseThrow(NoSuchElementException::new);
                temp.setStatus(job.getStatus());
                jobDAO.updateJob(temp);
                System.out.println("JobID " + job.getJobID() + ": server processing done");
            }

            private void initiateServerProcessing()throws IOException, Exception{
                String algorithmOutputPath = jobDirectory + "/algorithm-output/AlgorithmOutput.json";
                String algorithmOutputAbsolutePath = new File(algorithmOutputPath).getAbsolutePath();
                File algorithmOutput = new File(algorithmOutputAbsolutePath);
                if(algorithmOutput.exists()){
                    System.out.println("JobID " + job.getJobID() + ": " +
                            " AlgorithmOutput.json exists.");
                    processAlgorithmOutput(algorithmOutput);
                }else{
                    System.out.println("JobID " + job.getJobID() + ": " + "AlgorithmOutput.json does not exists...");
                }

            }

            private void extractDataFromJob()throws IOException, InterruptedException{
                if(isAlgorithmLocal) {
                    System.out.println("JobID " + job.getJobID() + ": " +  "Extract Data from local machine");
                    String pythonArg = jobDirectory + "algorithm-output/";
                    ProcessBuilder pbTwo = new ProcessBuilder("python3",
                            "src/main/resources/python/preprocessing/MergeOutputFiles.py", pythonArg);
                    pbTwo.redirectErrorStream(true);
                    Process tempProcess = pbTwo.start();
                    printProcessOutput(tempProcess);
                    shortSleepThread();
                }
                else{
                    System.out.println("JobID " + job.getJobID() + ": " +  "Extract Data from Seawulf");
                    String seawulfDirectory = "./jobs/" + job.getJobName().toLowerCase() + "/algorithm-output/";
                    ProcessBuilder pbOne = new ProcessBuilder("bash", "src/main/resources/bash/FetchDirectory.sh",
                            netid, jobsDirectory + job.getJobName().toLowerCase() + "/", seawulfDirectory);
                    pbOne.redirectErrorStream(true);
                    Process tempProcess = pbOne.start();
                    printProcessOutput(tempProcess);
                    shortSleepThread();
                    String pythonArg = jobDirectory + "algorithm-output/";
                    ProcessBuilder pbTwo = new ProcessBuilder("python3",
                            "src/main/resources/python/preprocessing/MergeOutputFiles.py", pythonArg);
                    pbTwo.redirectErrorStream(true);
                    tempProcess = pbTwo.start();
                    printProcessOutput(tempProcess);
                    shortSleepThread();
                }

            }

            private String getContentsFile(String filename)throws IOException{
                String filepath = jobDirectory + filename;
                String absoluteFilePath = new File(filepath).getAbsolutePath();
                String response = new BufferedReader(new FileReader(absoluteFilePath)).readLine();
                response = response.trim();
                System.out.println("JobID " + job.getJobID() + filename
                        + " recieved from the seawulf. It'S contents: " + response);
                return response;
            }

            private void monitorAlgorithm()throws IOException, InterruptedException{
                //Job tempJob = jobDAO.getJobById(job.getJobID()).orElseThrow(NoSuchElementException::new);
                if(isAlgorithmLocal) {
                    boolean isProcessesDone = true;
                    
                    for(Process process : localAlgorithmProcesses) {
                        if(process.isAlive()) {
                            System.out.println("JobID " + job.getJobID() + ": " + process.toString()  + " is still running");
                            isProcessesDone = false;
                            break;
                        }
                        
                    }
                    if(isProcessesDone){
                        job.setStatus(JobStatus.PROCESSING);
                        System.out.println("JobID " + job.getJobID() + " All processes Completed");
                        Job temp = jobDAO.getJobById(job.getJobID()).orElseThrow(NoSuchElementException::new);
                        temp.setStatus(job.getStatus());
                        jobDAO.updateJob(temp);
                    }else{
                        System.out.println("JobID " + job.getJobID() + ": "+ "Processes still running");
                    }
                    shortSleepThread();
                }
                else{
                    System.out.println("JobID " + job.getJobID() + ": "+ "Monitoring The Seawulf.");
                    ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/bash/MonitorSeawulf.sh",
                            netid, jobsDirectory, job.getJobName().toLowerCase(), job.getSeawulfJobID());
                    pb.redirectErrorStream(true);
                    Process tempProcess = pb.start();
                    printProcessOutput(tempProcess);
                    shortSleepThread();
                    String jobStatus = getContentsFile("monitor.txt");
                    JobStatus status =  JobStatus.getEnumFromString(jobStatus);
                    if(!job.getStatus().equals(status)){
                        job.setStatus(status);
                        //tempJob.setStatus(status);
                        Job temp = jobDAO.getJobById(job.getJobID()).orElseThrow(NoSuchElementException::new);
                        temp.setStatus(job.getStatus());
                        jobDAO.updateJob(temp);
                    }
                }


            }

            private void determineAlgorithmComputeLocation()throws IOException, InterruptedException{

                String algorithmInputPath = jobDirectory + "AlgorithmInput.json";
                if(isAlgorithmLocal){
                    System.out.println("JobID " + job.getJobID() + ": "+ "Running algorithm locally...");
                    String localPythonScript = "src/main/resources/python/algorithm/Algorithm_2.py";
                    for(int i = 0; i < job.getNumDistrictingPlan(); i++){
                        ProcessBuilder pb = new ProcessBuilder("python3", localPythonScript,
                                algorithmInputPath, jobDirectory + "algorithm-output/", "" +i);
                        pb.redirectErrorStream(true);
                        Process temp = pb.start();
                        localAlgorithmProcesses.add(temp);
                        //printProcessOutput(temp);
                    }
                    job.setStatus(JobStatus.RUNNING);
                    //tempJob.setStatus(JobStatus.RUNNING);
                }
                else{
                    System.out.println("JobID " + job.getJobID() + ": "+ "Running algorithm remotely.");
                    ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/bash/InitiateAlgorithm.sh",
                            netid, jobsDirectory, job.getJobName().toLowerCase(), ""+job.getNumDistrictingPlan());
                    pb.redirectErrorStream(true);
                    Process tempProcess = pb.start();
                    printProcessOutput(tempProcess);
                    shortSleepThread();
                    String seawulfJobID = getContentsFile("seawulfjobid.txt");
                    job.setSeawulfJobID(seawulfJobID);
                }
                Job temp = jobDAO.getJobById(job.getJobID()).orElseThrow(NoSuchElementException::new);
                temp.setStatus(job.getStatus());
                jobDAO.updateJob(temp);
                isComputeLocationDetermined = true;
            }

        }


    
}
