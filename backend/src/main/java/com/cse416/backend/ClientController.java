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


   @GetMapping(path = "/job/{jobID}", produces = "application/json")
   public String getJob(@PathVariable Integer jobID){
       System.out.println("Sending jobID " + jobID + " to Client");
       return service.getJob(jobID);
   }

    @GetMapping(path = "/precincts", produces = "application/json")
    public String getPrecincts(){
        System.out.println("Sending precincts");
        return service.getPrecincts();
    }

    @GetMapping(path = "/precinct/demographic/{precinctName}", produces = "application/json")
    public String getPrecinctDemographic(@PathVariable String precinctName){
        System.out.println("Sending precincts demographic");
        return service.getPrecinctDemographic(precinctName);
    }


    @PostMapping(path = "/heatmap", produces = "application/json")
    public String getDemographicHeatmap(@RequestBody Map <String,List> mapping){
        List <String> censusEthnicity = (List)mapping.get("demographicHeatmap");
        return service.getDemographicHeatmap(censusEthnicity);
    }

   @GetMapping(path = "/plan/{jobID}/{planID}", produces = "application/json")
   public String getPlan(@PathVariable Integer jobID, @PathVariable String planID){
        return "getPlan";
   }

   @GetMapping(path = "/boxwhisker/{jobID}/{planID}", produces = "application/json")
   public String getBoxwhisker(@PathVariable Integer jobID, @PathVariable String planID, @RequestBody String catagory){

        return "getPlanGraph";
   }

   @PutMapping(path = "/cancel/{jobID}", produces = "application/json")
   public void putCancelStateJob(@PathVariable Integer jobID){
       service.cancelJob(jobID);
   }

   @DeleteMapping(path = "/delete/{jobID}", produces = "application/json")
   public void deleteStateJob(@PathVariable Integer jobID){
       service.deleteJob(jobID);
   }

   @PostMapping(path = "/generate", consumes = "application/json", produces = "application/json")
   public String postGenerateStateJob(@RequestBody Job job){
        String clientData = service.generateJob(job);
        return clientData;
   }


}