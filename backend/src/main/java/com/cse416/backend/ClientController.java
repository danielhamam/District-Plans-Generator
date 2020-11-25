package com.cse416.backend;
import com.cse416.backend.model.job.*;
import com.cse416.backend.model.enums.CensusCatagories;
import com.cse416.backend.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;

//./mvnw spring-boot:run



@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClientController {

    private final ServerService service;


    @Autowired
    public ClientController(ServerService service){
        this.service = service;
    }

    @GetMapping(path = "/connect", produces = "application/json")
    public String connectingClient(){
        return "connectingClient";
    }

    @GetMapping(path = "/state/{stateAbbreviation}", produces = "application/json")
    public String getState(@PathVariable String stateAbbreviation){
        System.out.println("Sending " + stateAbbreviation + " to Client");
        String clientData = service.getState(stateAbbreviation);
        return clientData;
    }

    // @GetMapping(path = "/boundaries/{jobID}/{planID}", produces = "application/json")
    // public String getBoundries(@PathVariable String jobID, String boundaryType){
    //     return "getBoundries";

    // }


   @GetMapping(path = "/job/{jobID}", produces = "application/json")
   public String getJob(@PathVariable String jobID){
       System.out.println("Sending jobID " + jobID + " to Client");
       return service.getJob(jobID);
   }

    @GetMapping(path = "/precincts", produces = "application/json")
    public String getPrecinct(){
        System.out.println("Sending precincts");
        return service.getPrecinct();
    }

    @GetMapping(path = "/demographicfilter/{jobID}/{planID}", produces = "application/json")
    public String getDemographicFilter(@PathVariable String jobID, @PathVariable String planID, Map <String, Object> demographicFilter){
        List censusCatagories = Arrays.asList(demographicFilter.get("demographicFilter"));
        service.getDemographicFilter(jobID, planID, censusCatagories);
        return "getDemographicFilter";
    }

   @GetMapping(path = "/plan/{jobID}/{planID}", produces = "application/json")
   public String getPlan(@PathVariable String jobID, @PathVariable String planID){

        return "getPlan";
   }

   @GetMapping(path = "/boxwhisker/{jobID}/{planID}", produces = "application/json")
   public String getBoxwhisker(@PathVariable String jobID, @PathVariable String planID, @RequestBody String catagory){

        return "getPlanGraph";
   }
//
//    public String getGlobalHistory(){
//
//    }

   @PutMapping(path = "/cancel/{jobID}", produces = "application/json")
   public void putCancelStateJob(@PathVariable String jobID){
       service.cancelJob(jobID);
   }

   @DeleteMapping(path = "/delete/{jobID}", produces = "application/json")
   public void deleteStateJob(@PathVariable String jobID){
       service.deleteJob(jobID);
   }

   @PostMapping(path = "/generate", consumes = "application/json", produces = "application/json")
   public String postGenerateStateJob(@RequestBody Job job){
        String clientData = service.generateJob(job);
        return clientData;
   }


    // private Job createJobFromMap(Map<String, Object> mapping){
    //     String JobName = (String)mapping.get("name");
    //     boolean isAvailable = (boolean) mapping.get("isAvailable");
    //     int numberOfDistricting = (int) mapping.get("numberOfDistricting");
    //     double populationDifference = (double) mapping.get("populationDifference");
    //     double compactness = (double) mapping.get("compactness");
    //     return new Job(numberOfDistricting, JobName, isAvailable,
    //             populationDifference, compactness);
    // }

    // private List<Object> createListFromState(State state){
    //     return Arrays.asList(
    //             state.getName(),
    //             state.getPopulation(),
    //             state.getVotingAgePopulation());
    // }



}