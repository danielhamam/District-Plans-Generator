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
import com.cse416.backend.model.regions.district.comparators.AfricanAmericanPopulationCompare;
import com.cse416.backend.model.regions.precinct.Precinct;
import com.cse416.backend.model.regions.state.*;
import com.cse416.backend.model.job.*;
import com.cse416.backend.model.plan.*;
import com.cse416.backend.model.enums.*;
import com.cse416.backend.livememory.Session;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.geojson.FeatureCollection;
import org.springframework.beans.factory.annotation.Autowired;
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
    private GlobalHistory jobHistory;
    private boolean isStatePrecinctsLoaded;
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
        this.jobHistory = new GlobalHistory();
        this.isStatePrecinctsLoaded = false;
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
                System.out.println("./" + algorithmOutputPath + " directory has been created");
            }else{
                System.err.println("algorithm-output directory are already exists");
            }
        }else{
            System.err.println(jobDirectoryName + " directory are already exists");
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
            //State logic
            State state = stateDAO.getStateById(stateAbbrevation);
            state.initializeSystemFiles();
            Demographic stateDemographic = demographicDAO.getDemographicByStateId(stateAbbrevation);
            state.setDemographic(stateDemographic);
            state.setTotalPopulation(stateDemographic.getTotalPopulation());
            session.setState(state);

            //Job logic
            List <Job> jobs = jobDAO.getJobsByStateId(stateAbbrevation);
            for(Job j: jobs){
                List <CensusCatagories> censusCatagoriesEnum = new ArrayList<>();
                for(CensusEthnicity censusEthnicity : j.getMinorityAnalyzedCensusEthnicity()){
                    censusCatagoriesEnum.add(CensusCatagories.getEnumFromString(censusEthnicity.getEthnicityName()));
                    j.setMinorityAnalyzedEnumration(censusCatagoriesEnum);
                }
                createJobDirectory(j);
            }
            session.addJobs(jobs);
            jobHistory.addJobs(jobs);

            //format it for that client
            clientData = createClientStateData(state, jobs);
        }catch(JsonProcessingException error){
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
            error.printStackTrace();
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
            Job serverJob = session.getJobByID(jobID);
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
            HashMap <String, Object> map = new HashMap<>();
            map.put("precinctsGeoJson", heatmapNode);
            clientData = createClient_Data(map);

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

        }catch(NoSuchElementException|JsonProcessingException error){
            error.printStackTrace();
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
        }
        catch(Exception error){
            error.printStackTrace();
        }
        return clientData;
    }


//    public String getPlan(Integer jobID, String type){
//        Job currentJob = session.getJobByID(jobID);
////        Plan plan = currentJob.getPlanByID(planID);
//        String clientData = "{serverError:\"Unknown Server Error\"}";
//        try{
//            clientData = this.createClient_Data(plan);
//
//        }catch(NoSuchElementException|JsonProcessingException error){
//            error.printStackTrace();
//            clientData = "{serverError:\"" + error.getMessage() + "\"}";
//        }
//        catch(Exception error){
//            error.printStackTrace();
//        }
//        return clientData;
//    }

    public String getBoxWhisker(Integer jobID){

        String clientData = "{serverError:\"Unknown Server Error\"}";
        try{
            Job currentJob = session.getJobByID(jobID);
            HashMap <String, Object> map = new HashMap<>();
            map.put("boxWhisker", currentJob.getBoxWhisker());
            clientData = createClient_Data(currentJob.getBoxWhisker());

        }catch(NoSuchElementException|JsonProcessingException error){
            error.printStackTrace();
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
        }
        catch(Exception error){
            error.printStackTrace();
        }
        return clientData;
    }


    public String generateJob(Job job){
        String clientData = "{serverError:null}";
        //TODO: [DATABASE] Implement database functionality. Save job on to the database. Assign ID to Job Object
        try{
            State currentState = session.getState();
            job.setState(currentState);
            List <CensusEthnicity> censusEthnicities = covertClientCensusToDatabaseCensus(job);
            job.setMinorityAnalyzed(censusEthnicities);
            //TODO: Delete line below
            job.setJobID(99999);
            session.addJob(job);
//            temp(job);

            createJobDirectory(job);
            initiateAlgorithm(job);
            //jobDAO.addJob(job);
            clientData = createClient_Data(job);
        }catch(IOException error){
            clientData = "{serverError:\"" + error.getMessage() + "\"}";
            error.printStackTrace();
        }
        catch(Exception error){
            error.printStackTrace();
        }
        //System.out.println("Generating Job: " + job);
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

    private void temp(String stateAbbrevation){

    }


    public void cancelJob(Integer jobID){

        try{
            Job job = session.getJobByID(jobID);
            System.out.println("Attempting to cancel a job " + jobID + ". It's status: " + job.getStatus());
            System.out.println("\tSize of thread pool: " + threads.size());
            if(!job.getStatus().equals(JobStatus.COMPLETED)){
                Algorithm currentThread =  threads.stream()
                        .filter(thread -> job.getJobID().equals(thread.getJob().getJobID()))
                        .findFirst()
                        .orElseThrow(Exception::new);
                currentThread.cancelJobDriver();
                threads.remove(currentThread);
                jobDAO.deleteJob(job);
                System.out.println("Job " + job.getJobID() + " has been removed");

            }
            else{
                deleteJob(jobID);
            }

        }catch(Exception error){
            error.printStackTrace();
        }

    }

    public void deleteJob(Integer jobID){
        //TODO: Ask if we delete from the database does it get refelected in the server-side thru JPA
        System.out.println("Attempting to remove a job");
//        jobHistory.deleteJob(jobID);
        Job job = session.getJobByID(jobID);
        if(job.getStatus().equals(JobStatus.COMPLETED)){
            //        System.out.println("Successfully deleted the job for the server");
            jobDAO.deleteJob(job);
            System.out.println("Job " + job.getJobID() + " has been removed");
        }else{
            jobDAO.deleteJob(job);
            System.out.println("Successfully deleted job from the DB"); 
            System.out.println(job.toString() + " has been removed");
        }

    }


        public class Algorithm implements Runnable {
            private Thread proxyThread = null;
            private String netid;
            private String jobsDirectory;
            private String jobDirectory;
            private boolean die = false;
            private boolean isAlgorithmLocal;
            private boolean isComputeLocationDetermined;
            private boolean isJobCancelled;
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


                System.out.println("Starting Thread...");
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
                        System.out.println(this + " thread is still running." +
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
                        if(status.equals(JobStatus.COMPLETED)){
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
                        kill();
                        error.printStackTrace();
                    }
                    catch (Exception error) {
                        error.printStackTrace();
                        System.out.println(error.getCause().getMessage() + " Exception Killing thread..");
                        kill();
                    }
                }
                System.out.println("Killed thread: " + this);
            }


            public void cancelJobDriver() {
                this.isJobCancelled = true;
            }

            private void cancelJob()throws IOException, InterruptedException{
                if(isAlgorithmLocal){
                    System.out.println("Canceling job locally.");
                    for(Process process : localAlgorithmProcesses) {
                       process.destroy();
                    }
                }else{
                    System.out.println("Canceling job remotely.");
                    ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/bash/CancelAlgorithm.sh",
                            netid, job.getSeawulfJobID());
                    pb.redirectErrorStream(true);
                    Process tempProcess = pb.start();
                    shortSleepThread();
                    //printProcessOutput(tempProcess);
                }
                job.setStatus(JobStatus.CANCELLED);
                System.out.println("Job " + job.getJobID() + " status: " + job.getStatus());
                kill();
            }

            private void longSleepThread() throws InterruptedException{
                //long sleep = 900000; //15 minutes
                long sleep = 9000; //15 minutes
                double durationInMinutes = (sleep)/(60000 + 0.0);
                System.out.println("Sleeping Thread " + this + " : Long sleep for " + durationInMinutes + " minutes");
                Thread.sleep(sleep);
            }

            private void shortSleepThread() throws InterruptedException{
                long sleep = 30000; //0.5 minutes
                double durationInMinutes = (sleep)/(60000 + 0.0);
                System.out.println("Sleeping Thread " + this + " : Short sleep for " + durationInMinutes + " minutes");
                Thread.sleep(sleep);
            }

            private void generateSummaryFile()throws IOException{
                System.out.println("Generating Summary File");
                State state = job.getState();

                String compactnessLimit = job.getClientCompactness().getRepresentation();
                String populationDifferenceLimit = ""+job.getPopulationDifference();
                List <String> minorityGroups =
                        job.getMinorityAnalyzedEnumration()
                                .stream()
                                .map(e -> e.getFullName())
                                .collect(Collectors.toList());

                Constraints constraints = new Constraints(compactnessLimit, populationDifferenceLimit, minorityGroups);
                //TODO: Add Congressional Districts GeoJson
                Districting averageDistricting = new Districting("0", constraints, null);
                Districting extremeDistricting = new Districting("1",constraints, null);
                List <Districting> districtings = new ArrayList<>();
                districtings.add(averageDistricting);
                districtings.add(extremeDistricting);
                Summary summary = new Summary(state.getStateName(), state.getStateAbbreviation(),
                        state.getPrecinctsGeoJson(),districtings);
                job.setSummary(summary);
                FileWriter file = new FileWriter(new File(jobDirectory).getAbsolutePath() + "/" + "Summary.json");
                mapper.writeValue(file, summary);

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
                //System.out.println(plansNode.get(Integer.parseInt(averagePlan.getType())));

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
                //System.out.println(objectNode.toPrettyString());
                String filename = "ConvertPlans.json";
                String filePath = new File(jobDirectory).getAbsolutePath() + "/" + filename;
                FileWriter CovertPlansFile = new FileWriter(filePath);
                mapper.writeValue(CovertPlansFile, objectNode);
                ProcessBuilder pb = new ProcessBuilder("python3",
                        "src/main/resources/python/preprocessing/ConvertPlanDistrictsToGeoJson.py", jobDirectory);
                Process temp = pb.start();
                printProcessOutput(temp);
                shortSleepThread();
                setDistrictGeoJson(averagePlan, "AverageDistrict.json");
                setDistrictGeoJson(extremeplan, "ExtremeDistrict.json");
                setDistrictGeoJson(randomPlan, "RandomDistrict.json");

            }

            private void determinePlans(List <Plan> allPlans)throws Exception{
                if(allPlans.size() == 0){
                    throw new Exception("List size zero : allPlans.size() == 0");
                }
                double size = allPlans.size();
                double compactnessListSum = allPlans.stream()
                        .map(e -> e.getAverageDistrictCompactness())
                        .reduce(0D, Double::sum);
                double compactnessListMean = compactnessListSum/size;


                Plan averagePlan = allPlans.get(0);
                Plan extremeplan = allPlans.get(0);
                Plan randomPlan = null;

                //Set average plan and extreme plan
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


                //Todo: Set Average, Extreme, Random GeoJson
                job.setAverageDistrictPlan(averagePlan);
                job.setExtremeDistrictPlan(extremeplan);
                job.setRandomDistrictPlan(randomPlan);
                System.out.println("The Average Plan: " + averagePlan + "\n");
                System.out.println("The Extreme Plan: " + extremeplan + "\n");
                System.out.println("The Random Plan: " + randomPlan + "\n");


            }

            private void createBoxWhisker(List <Plan> allPlans)throws Exception{
                if(allPlans.size() == 0){
                    throw new Exception("List size zero : allPlans.size() == 0");
                }
                //System.out.println("Creating Box and Whisker Plot." + " Number of plans " + allPlans.size());

                //Get ethnicity to sort list by
                CensusCatagories minorityAnalyzed  =  job.getMinorityAnalyzedEnumration().get(0);
                Comparator comparator = District.getComparatorByCensusCatagories(minorityAnalyzed);
                System.out.println("Sorting by " + minorityAnalyzed + " using " + comparator);
                for (Plan plan : allPlans) {
                    Collections.sort(plan.getDistricts(), comparator);
                }

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
                    boxWhiskerPlots.add(new BoxWhiskerPlot(districtIndex+1, min,q1,q2,q3,max));

                }
                job.setBoxWhisker(new BoxWhisker(boxWhiskerPlots));
                //System.out.println("\n" + job.getBoxWhisker() + "\n");


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
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(algorithmOutputFile);
                JsonNode plansNode = rootNode.get("plans");
                List <Plan> plansList = new ArrayList<>();
                int planIndex = 0;
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
                    planIndex++;

                    //prettyPrintPlan(plan);
                }
                job.setAllPlans(plansList);
                createBoxWhisker(plansList);
                determinePlans(plansList);
                createPlanGeojson(plansNode);
                generateSummaryFile();
            }

            private void initiateServerProcessing()throws IOException, Exception{
                String algorithmOutputPath = jobDirectory + "/algorithm-output/AlgorithmOutput.json";
                String algorithmOutputAbsolutePath = new File(algorithmOutputPath).getAbsolutePath();
                File algorithmOutput = new File(algorithmOutputAbsolutePath);
                if(algorithmOutput.exists()){
                    System.out.println("AlgorithmOutput.json exists...\nStarting Processing...");
                    processAlgorithmOutput(algorithmOutput);
                }else{
                    System.out.println("AlgorithmOutput.json does not exists...");
                }

            }

            private void extractDataFromJob()throws IOException, InterruptedException{
                if(isAlgorithmLocal) {
                    String pythonArg = jobDirectory + "algorithm-output/";
                    ProcessBuilder pbTwo = new ProcessBuilder("python3",
                            "src/main/resources/python/preprocessing/MergeOutputFiles.py", pythonArg);
                    pbTwo.redirectErrorStream(true);
                    Process tempProcess = pbTwo.start();
                    printProcessOutput(tempProcess);
                    shortSleepThread();
                }
                else{
                    System.out.println("Extract Data from Seawulf");
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
                System.out.println(filename + " recieved from the seawulf. It'S contents: " + response);
                return response;
            }

            private void monitorAlgorithm()throws IOException, InterruptedException{
                boolean doesFileExist = false;
                if(isAlgorithmLocal) {
                    boolean isProcessesDone = true;
                    for(Process process : localAlgorithmProcesses) {
                        if(process.isAlive()) {
                            isProcessesDone = false;
                            break;
                        }
                    }
                    if(isProcessesDone){
                        job.setStatus(JobStatus.COMPLETED);
                        System.out.println("All processes Completed");
                        //jobDAO.updateJob(job);
                    }else{
                        System.out.println("Processes still running");
                    }
                    shortSleepThread();
                }
                else{
                    System.out.println("Monitoring The Seawulf.");
                    ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/bash/MonitorSeawulf.sh",
                            netid, jobsDirectory, job.getJobName().toLowerCase(), job.getSeawulfJobID());
                    pb.redirectErrorStream(true);
                    Process tempProcess = pb.start();
                    //printProcessOutput(tempProcess);
                    shortSleepThread();
                    String jobStatus = getContentsFile("monitor.txt");
                    JobStatus status =  JobStatus.getEnumFromString(jobStatus);
                    if(!job.getStatus().equals(status)){
                        job.setStatus(status);
                        //jobDAO.updateJob(job);
                    }
                }
            }

            private void determineAlgorithmComputeLocation()throws IOException, InterruptedException{
                String algorithmInputPath = jobDirectory + "AlgorithmInput.json";
                if(isAlgorithmLocal){
                    System.out.println("Running algorithm locally...");
                    String localPythonScript = "src/main/resources/python/algorithm/Algorithm.py";
                    for(int i = 0; i < job.getNumDistrictingPlan(); i++){
                        ProcessBuilder pb = new ProcessBuilder("python3", localPythonScript,
                                algorithmInputPath, jobDirectory + "algorithm-output/", "" +i);
                        pb.redirectErrorStream(true);
                        Process temp = pb.start();
                        localAlgorithmProcesses.add(temp);
                        //printProcessOutput(temp);
                    }
                }
                else{
                    System.out.println("Running algorithm remotely.");
                    ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/bash/InitiateAlgorithm.sh",
                            netid, jobsDirectory, job.getJobName().toLowerCase(), ""+job.getNumDistrictingPlan());
                    pb.redirectErrorStream(true);
                    Process tempProcess = pb.start();
                    //printProcessOutput(tempProcess);
                    shortSleepThread();
                    String seawulfJobID = getContentsFile("seawulfjobid.txt");
                    job.setSeawulfJobID(seawulfJobID);
                    //jobDAO.updateJob(job);
                }
                isComputeLocationDetermined = true;
            }

        }
    
}
