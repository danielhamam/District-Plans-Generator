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
        System.out.println("Client request for State: " + stateAbbreviation );
        return service.getState(stateAbbreviation);
    }


   @GetMapping(path = "/job/{jobID}", produces = "application/json")
   public String getJob(@PathVariable Integer jobID){
       System.out.println("Client request for JobID: " + jobID );
       return service.getJob(jobID);
   }

    @GetMapping(path = "/precincts", produces = "application/json")
    public String getPrecincts(){
        System.out.println("Client request for state's precincts");
        return service.getPrecincts();
    }

    @GetMapping(path = "/precinct/demographic/{precinctFIPS}", produces = "application/json")
    public String getPrecinctDemographic(@PathVariable String precinctFIPS){
        System.out.println("Client request for the precinct's demographic of precinctFIPS: " + precinctFIPS);
        return service.getPrecinctDemographic(precinctFIPS);
    }

    @GetMapping(path = "/heatmap/{censusEthnicity}", produces = "application/json")
    public String getDemographicHeatmap(@PathVariable String censusEthnicity){
        System.out.println("Client request for the demographic heatmap for " + censusEthnicity);
        return service.getDemographicHeatmap(censusEthnicity);
    }

   @GetMapping(path = "/plan/{jobID}/{type}", produces = "application/json")
   public String getPlan(@PathVariable Integer jobID, @PathVariable String type){
        return "getPlan";
   }

   @GetMapping(path = "/boxwhisker/{jobID}", produces = "application/json")
   public String getBoxwhisker(@PathVariable Integer jobID){
       System.out.println("Client request box and whisker for JobID: " + jobID);
       return service.getBoxWhisker(jobID);
   }

   @PutMapping(path = "/cancel/{jobID}", produces = "application/json")
   public void putCancelJob(@PathVariable Integer jobID){
       System.out.println("Client request to cancel JobID: " + jobID);
       service.cancelJob(jobID);
   }

   @DeleteMapping(path = "/delete/{jobID}", produces = "application/json")
   public void deleteJob(@PathVariable Integer jobID){
       System.out.println("Client request to delete JobID: " + jobID);
       service.deleteJob(jobID);
   }

   @PostMapping(path = "/generate", consumes = "application/json", produces = "application/json")
   public String postGenerateStateJob(@RequestBody Job job){
       System.out.println("Client request to generate a job");
       return service.generateJob(job);
   }


    @GetMapping(path = "/temp", produces = "application/json")
    public String temp(){
        return "getPlanGraph";
    }


}