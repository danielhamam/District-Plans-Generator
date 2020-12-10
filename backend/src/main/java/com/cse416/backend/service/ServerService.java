package com.cse416.backend.service;

import com.cse416.backend.livememory.GlobalHistory;
import com.cse416.backend.model.demographic.CensusEthnicity;
import com.cse416.backend.model.demographic.Demographic;
import com.cse416.backend.model.job.boxnwhisker.BoxWhisker;
import com.cse416.backend.model.job.boxnwhisker.BoxWhiskerPlot;
import com.cse416.backend.model.regions.district.District;
import com.cse416.backend.model.regions.district.comparators.AfricanAmericanPopulationCompare;
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
    private boolean runAlgoLocally;
    private List<AlgorithmInterface> algorithmInterfaceThreads;


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
        this.runAlgoLocally = false;
        this.algorithmInterfaceThreads = new ArrayList<>();
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
                System.out.println(j.getMinorityAnalyzedCensusEthnicity());
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
            List <CensusEthnicity> censusEthnicities = covertClientCensusToDatabaseCensus(job);
            job.setMinorityAnalyzed(censusEthnicities);
//            temp(job);
//            createJobDirectory(job);
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
        getMinorityAnalyzedEnumration.forEach(
                e -> censusEthnicities.add(censusEthnicityDAO.getCensusEthnicityById(e.getShortenName()).get())
        );
        return censusEthnicities;
    }

    private void initiateAlgorithm(Job job){
        System.out.println("Initiating Algorithm... Creating Thread");
        Algorithm algorithmInterface = new Algorithm("carlopez", job, runAlgoLocally);
        algorithmInterface.start();
    }

//    private void temp(String stateAbbrevation){
//        new Thread(new Runnable() {
//            public void run() {
//                isStatePrecinctsLoaded = false;
//                State state = session.getState();
//                System.out.println("Getting " + stateAbbrevation + " precincts");
//                List <Precinct> precincts = precinctDAO.getPrecinctsByStateId(stateAbbrevation);
//                state.setStatePrecincts(precincts);
//                for(Precinct p: precincts){
//                    Demographic precinctDemographic =  demographicDAO.getDemographicByPrecinctId(p.getPrecinctId());
//                    p.setDemographic(precinctDemographic);
//                    System.out.println(p);
//                }
//                System.out.println("Finished getting " + stateAbbrevation + " precincts");
//                isStatePrecinctsLoaded = true;
//            }
//        }).start();
//    }



    public void cancelJob(Integer jobID){
        //TODO: Hook it up so that the job will cancel and delete
        System.out.println("Attempting to cancel a job");
        try{
            Job job = session.getJobByID(jobID);
            if(!job.getStatus().equals(JobStatus.COMPLETED)){
                AlgorithmInterface currentThread =  algorithmInterfaceThreads.stream()
                        .filter(thread -> job.equals(thread.getJob()))
                        .findFirst()
                        .orElseThrow(Exception::new);
                currentThread.cancelJobDriver();
                jobDAO.deleteJob(job);
                System.out.println(job.toString() + " has been removed");
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
            System.out.println("Successfully deleted the job for the database");
            System.out.println(job.toString() + " has been removed");
        }else{
            jobDAO.deleteJob(job);
        }

    }


        public class Algorithm implements Runnable {
            private Thread proxyThread = null;
            private String jobDirectoryRelativePath;
            private String netid;
            private String jobDirectory;
            private boolean die = false;
            private boolean isAlgorithmLocal;
            private boolean isComputeLocationDetermined;
            private boolean isJobCancelled;
            private Process localAlgorithmProcess;
            private Job job;



            public Algorithm(String netid, Job job, boolean runAlgoLocally) {
                this.netid = netid;
                this.job = job;
                this.isAlgorithmLocal = runAlgoLocally;
                this.isComputeLocationDetermined = false;
                this.isJobCancelled = false;
                this.jobDirectory = "src/main/resources/system/jobs/";
                this.jobDirectoryRelativePath = "../system/jobs/";
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
                    try{
                        String algorithmOutputPath = jobDirectory + "AlgorithmOutput.json";
                        String algorithmOutputAbsolutePath = new File(algorithmOutputPath).getAbsolutePath();
                        File algorithmOutput = new File(algorithmOutputAbsolutePath);
                        processAlgorithmOutput(algorithmOutput);
                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }


//                System.out.println("Starting Thread...");
//                proxyThread = new Thread(this);
//                proxyThread.start();
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

            private void cancelJob()throws IOException{
                if(isAlgorithmLocal){
                    if(localAlgorithmProcess.isAlive()){
                        localAlgorithmProcess.destroy();
                    }
                    kill();
                }else{
                    System.out.println("Canceling job remotely...  Bash output...");
                    ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/bash/CancelAlgorithm.sh",
                            netid, job.getSeawulfJobID());
                    pb.redirectErrorStream(true);
                    Process tempProcess = pb.start();
                    printProcessOutput(tempProcess);
                }
            }

            private void longSleepThread() throws InterruptedException{
                long sleep = 900000; //15 minutes
                double durationInMinutes = sleep/600000;
                System.out.println("Sleeping Thread " + this + " : Long sleep for " + durationInMinutes + " minutes");
                Thread.sleep(sleep);
            }

            private void shortSleepThread() throws InterruptedException{
                long sleep = 30000; //0.5 minutes
                double durationInMinutes = sleep/600000;
                System.out.println("Sleeping Thread " + this + " : Short sleep for " + durationInMinutes + " minutes");
                Thread.sleep(sleep);
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
                }

                //TODO: Do I need to assign precinctId, countyId, stateId for it to save?
                Demographic districtDemographic = new Demographic(districtTotalPopulation, districtWhitePopulation,
                        districtHispanicPopulation, districtAmericanIndianPopulation, districtNativeHawaiianPopulation,
                        districtAfricanAmericanPopulation, districtAsianPopulation, districtOtherRacePopulation,
                        districtMultipleRacePopulation, districtTotalVAPPopulation, districtWhiteVAPPopulation,
                        districtHispanicVAPPopulation, districtAmericanIndianVAPPopulation,
                        districtNativeHawaiianVAPPopulation, districtAfricanAmericanVAPPopulation,
                        districtAsianVAPPopulation, districtOtherRaceVAPPopulation, districtMultipleRaceVAPPopulation);


                District district = new District(districtID, job.getState(), plan, precinctList);
                district.setDemographic(districtDemographic);


                //Todo: Figure out how to identify counties in district

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

            private void getAveragePlan(){

            }

            private void createBoxWhisker(List <Plan> allPlans)throws Exception{
                if(allPlans.size() ==0){
                    throw new Exception("List size zero : allPlans.size() == 0");
                }
                System.out.println("Creating Box and Whisker Plot." + " Number of plans " + allPlans.size());

                //Get ethnicity to sort list by
                CensusCatagories minorityAnalyzed  =  job.getMinorityAnalyzedEnumration().get(0);
                Comparator comparator = District.getComparatorByCensusCatagories(minorityAnalyzed);
                for (Plan plan : allPlans) {
                    Collections.sort(plan.getDistricts(), comparator);
                }

                //For each district. Go through all the plans. Create BoxWhiskerPlot Object
                List<BoxWhiskerPlot> boxWhiskerPlots = new ArrayList<>();
                for (int districtIndex = 0; districtIndex < job.getNumDistrictingPlan(); districtIndex++) {
                    List<Long> districtsPopulationOfPlans = new ArrayList<>();
                    for (Plan plan : allPlans) {
                        District planDistrict = plan.getDistricts().get(districtIndex);
                        Long population = planDistrict.getDemographic().getVAPByCensusCatagories(minorityAnalyzed);
                        districtsPopulationOfPlans.add(population);
                    }
                    Collections.sort(districtsPopulationOfPlans);
                    int size = districtsPopulationOfPlans.size();
                    long min = districtsPopulationOfPlans.get(0);
                    long q1 = districtsPopulationOfPlans.get(size/4);
                    long q2 = districtsPopulationOfPlans.get(size/2);
                    long q3 = districtsPopulationOfPlans.get(size*3/4);
                    long max = districtsPopulationOfPlans.get(size-1);
                    boxWhiskerPlots.add(new BoxWhiskerPlot(districtIndex, min,q1,q2,q3,max));
                }
                job.setBoxWhisker(new BoxWhisker(boxWhiskerPlots));
                System.out.print(job.getBoxWhisker());
            }

            private void prettyPrintPlan(Plan plan){
                int districtElement = 1;
                for (District d: plan.getDistricts()) {
                    String s = "District " + districtElement + ":" + "\n" + d.getDemographic() + "Districts Precinct: {";
                    for(Precinct p : d.getPrecincts()){
                        s = s + p.getPrecinctFIPSCode() + ", ";
                    }
                    s = s + "}\n";
                    System.out.println(s);
                    districtElement++;
                }

            }

            private void processAlgorithmOutput(File algorithmOutputFile)throws IOException, Exception{
                //Covert data from AlgorithmOutput.json to Java Objects
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(algorithmOutputFile);
                JsonNode plansNode = rootNode.get("plans");
                List <Plan> plansList = new ArrayList<>();

                for(JsonNode planObject : plansNode){
                    //Create Plan object for Job Object
                    double averageDistrictPopulation = planObject.get("averageDistrictPopulation").asDouble();
                    double averageDistrictCompactness = planObject.get("averageDistrictPopulation").asDouble();
                    Plan plan = new Plan(job, job.getNumOfDistricts(),
                            averageDistrictPopulation, averageDistrictCompactness);

                    //Create Plan's districts
                    List <District> districts = processingCreatePlanDistricts(plan, planObject);

                    //setters or adders
                    plan.setDistricts(districts);
                    plansList.add(plan);

                    prettyPrintPlan(plan);
                }
                job.setAllPlans(plansList);
                createBoxWhisker(plansList);
            }

            private void initiateServerProcessing()throws IOException, Exception{
                String algorithmOutputPath = jobDirectory + job.getJobName().toLowerCase() +
                        "/algorithm-output/AlgorithmOutput.json";
                String algorithmOutputAbsolutePath = new File(algorithmOutputPath).getAbsolutePath();
                File algorithmOutput = new File(algorithmOutputAbsolutePath);
                if(algorithmOutput.exists()){
                    System.out.println("AlgorithmOutput.json exists...\nStarting Processing...");
                    processAlgorithmOutput(algorithmOutput);
                }else{
                    System.out.println("Error -> AlgorithmOutput.json does not exists...");
                }

            }

            private void extractDataFromJob()throws IOException, InterruptedException{
                if(isAlgorithmLocal) {
                    //TODO: For local algorithm run
                }
                else{
                    System.out.println("Extract Data from Seawulf");
                    String seawulfDirectory = "./jobs/" + job.getJobName().toLowerCase() + "/algorithm-output/";
                    ProcessBuilder pbOne = new ProcessBuilder("bash", "src/main/resources/bash/FetchDirectory.sh",
                            netid, jobDirectory + job.getJobName().toLowerCase() + "/", seawulfDirectory);
                    pbOne.redirectErrorStream(true);
                    Process tempProcess = pbOne.start();
                    printProcessOutput(tempProcess);
                    shortSleepThread();
                    //TODO: CALL PYTHON TO MERGE JSON FILES
                    String pythonArg = jobDirectory + job.getJobName().toLowerCase() + "/algorithm-output/";
                    ProcessBuilder pbTwo = new ProcessBuilder("python3",
                            "src/main/resources/python/preprocessing/MergeOutputFiles.py", pythonArg);
                    pbTwo.redirectErrorStream(true);
                    tempProcess = pbTwo.start();
                    printProcessOutput(tempProcess);
                    shortSleepThread();
                }

            }

            private String getContentsFile(String filename)throws IOException{
                String filepath = jobDirectory + job.getJobName().toLowerCase() + "/" + filename;
                String absoluteFilePath = new File(filepath).getAbsolutePath();
                String response = new BufferedReader(new FileReader(absoluteFilePath)).readLine();
                response = response.trim();
                System.out.println(filename + " recieved from the seawulf. It'S contents: " + response);
                return response;
            }

            private void monitorAlgorithm()throws IOException, InterruptedException{
                boolean doesFileExist = false;
                if(isAlgorithmLocal) {
                    doesFileExist = !localAlgorithmProcess.isAlive();
                    if(doesFileExist){
                        job.setStatus(JobStatus.COMPLETED);
                        //jobDAO.updateJob(job);
                    }
                }
                else{
                    System.out.println("Monitoring The Seawulf.");
                    ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/bash/MonitorSeawulf.sh",
                            netid, jobDirectory, job.getJobName().toLowerCase(), job.getSeawulfJobID());
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
                String algorithmInputFileName = "AlgorithmInput.json";
                String algorithmInputPath = jobDirectory + algorithmInputFileName;
                if(isAlgorithmLocal){
                    System.out.println("Running algorithm locally... Python output...");
                    String localPythonScript = "src/main/resources/python/algorithm/Algorithm.py";
                    ProcessBuilder pb = new ProcessBuilder("python3", localPythonScript, algorithmInputPath);
                    pb.redirectErrorStream(true);
                    localAlgorithmProcess = pb.start();
                    printProcessOutput(localAlgorithmProcess);
                }
                else{
                    System.out.println("Running algorithm remotely.");
                    ProcessBuilder pb = new ProcessBuilder("bash", "src/main/resources/bash/InitiateAlgorithm.sh",
                            netid, jobDirectory, job.getJobName().toLowerCase(), ""+job.getNumDistrictingPlan());
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
