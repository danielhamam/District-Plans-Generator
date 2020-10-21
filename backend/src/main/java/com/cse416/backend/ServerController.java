package com.cse416.backend;

import com.cse416.backend.model.Batch;
import com.cse416.backend.model.State;
import com.cse416.backend.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
public class ServerController {

    private final ServerService service;


    @Autowired
    public ServerController(ServerService service){
        this.service = service;

    }

    @GetMapping("/")
    public String index() {
        return service.index();
    }

    @CrossOrigin(origins = "*")
    @GetMapping(path = "/home", consumes = "application/json", produces = "application/json")
    public State getState(@RequestBody Map<String, Object> clientInput){
        String stateAbberivation = (String)clientInput.get("state");
        State state  = service.getState(stateAbberivation);
        List<Object> listFromState = createListFromState(state);
        //return listFromState;
        return state;

    }

//    @GetMapping("/")
//    public void getBatch(){
//
//    }
//
//    @PutMapping("/")
//    public void cancelStateBatch(){
//
//    }
//
//    @PutMapping(path ="/home/{stateAbberivation}")
//    public void deleteStateBatch(@RequestBody Batch batch, @PathVariable String stateAbberivation){
//
//
//    }
//
//    @PutMapping("/")
//    public void updateStateBatch(){
//
//    }

    @PostMapping(path = "/home", consumes = "application/json", produces = "application/json")
    public void generateStateBatch(@RequestBody Map<String, Object> clientInput){
        String stateAbberivation = (String)clientInput.get("state");
        Batch batch = createBatchFromMap(clientInput);
        service.generateStateBatch(batch, stateAbberivation);
    }

    private Batch createBatchFromMap(Map<String, Object> mapping){
        System.out.println(mapping);
        String batchName = (String)mapping.get("name");
        boolean isAvailable = (boolean) mapping.get("isAvailable");
        int numberOfDistricting = (int) mapping.get("numberOfDistricting");
        double populationDifference = (int)mapping.get("populationDifference");
        double compactness = (double) (int)mapping.get("compactness");
        return new Batch(numberOfDistricting, batchName, isAvailable,
                populationDifference, compactness);
    }

    private List<Object> createListFromState(State state){
        return Arrays.asList(
                state.getName(),
                state.getPopulation(),
                state.getVotingAgePopulation());
    }




    /*
    {
        "numberOfDistricting" : 10,
        "name": "batch1",
        "isAvailable": false,
        "populationDifference": 10.0,
        "compactness": 10.0,
    }
     */






}
//+ServerController(serverService : ServerService)
//        +getStateInfo(state: String): JSON
//        +getAllStateBatches(state : String): JSON
//        +getStateBatch(batchID : String): JSON
//        +cancelStateBatch(batchID : String): JSON
//        +deleteStateBatch(batchID : String): JSON
//        +updateStateBatchName(batchID : String): JSON
//        +generateStateBatch(name  : String, compactness : double, populationDifference : int,  state: String, numberOfPlans : int): JSON
//        +sendStateDistrictings(): JSON