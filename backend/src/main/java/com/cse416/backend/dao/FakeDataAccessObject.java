package com.cse416.backend.dao;


import com.cse416.backend.model.Job;
import com.cse416.backend.model.Plan;
import com.cse416.backend.model.enums.CensusCatagories;
import com.cse416.backend.model.enums.ClientCompactness;
import com.cse416.backend.model.enums.JobStatus;
import com.cse416.backend.model.regions.Boundary;
import com.cse416.backend.model.regions.Demographic;
import com.cse416.backend.model.regions.Precinct;
import com.cse416.backend.model.regions.State;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository("fakeDao")
public class FakeDataAccessObject{
    private static List<State> stateDB = new  ArrayList<>();
    private static List<Job> jobDB = new ArrayList<>();

    public FakeDataAccessObject(){
        stateDB.add(buildNY());
        jobDB.add(new Job("JOB1", "NY", "1", -1, 2, 500, 0.03, ClientCompactness.MEDUIM ,CensusCatagories.Black,JobStatus.COMPLETED));
        // stateDB.add(State("California", "CA", 1, null, null, null, null));
        // stateDB.add(State("Geogira", "GA", 1, null, null, null, null));
        
    }

    public State buildNY(){
        Precinct [] NYPrecints = new Precinct[10]; 
        NYPrecints[0] = (new Precinct("precinct" + 0, 0, null, new Demographic(10l),null));
        NYPrecints[1] =(new Precinct("precinct" + 1, 1, null, new Demographic(10l),null));
        NYPrecints[2] =(new Precinct("precinct" + 2, 2, null, new Demographic(10l),null));
        NYPrecints[3] =(new Precinct("precinct" + 3, 3, null, new Demographic(10l),null));
        NYPrecints[4] =(new Precinct("precinct" + 4, 4, null, new Demographic(10l),null));
        NYPrecints[5] =(new Precinct("precinct" + 5, 5, null, new Demographic(10l),null));
        NYPrecints[6] =(new Precinct("precinct" + 6, 6, null, new Demographic(10l),null));
        NYPrecints[7] =(new Precinct("precinct" + 7, 7, null, new Demographic(10l),null));
        NYPrecints[8] =(new Precinct("precinct" + 8, 8, null, new Demographic(10l),null));
        NYPrecints[9] =(new Precinct("precinct" + 9, 9, null, new Demographic(10l),null));
        Plan NYEnactedPlan = new Plan("ENACTEDPLAN", "NY", "1", 2, 5, 10, true, 2020);
        Demographic demographic = new Demographic(100);
        State NY = new State("New York", "NY", 1, demographic, null, NYEnactedPlan, NYPrecints);
        return NY;
    }

    public State queryGetStateInformation(String stateAbbrev) {
        return stateDB.stream()
                .filter(state -> stateAbbrev.equals(state.getStateAbbreviation()))
                .findFirst().orElse(null);
    }

    public List<Job> queryGetStateJobsInformation(String stateAbbrev) {
        List<Job> jobs = new ArrayList<>();
        for(Job job : jobDB){
            if(job.getStateAbbrev().equals(stateAbbrev)){
                jobs.add(job);
            }
        }

        return jobs;
    }


    public State queryGetStateBoundary(String stateAbbrev) {
        return stateDB.stream()
                .filter(e -> stateAbbrev.equals(e.getStateAbbreviation()))
                .findFirst().orElse(null);
    }

    public Precinct queryGetAllPrecinctInformation(String stateAbberivation, String planID) {
        return null;
    }

   
    // public void muatationSaveBatch(State state, Batch batch){
    //     state.addBatch(batch);
    // }

    // @Override
    // public int generateBatch(UUID id, Batch batch) {
    //     Batch newBatch = new Batch(id, batch.getnumberOfDistricting(), batch.getName(), batch.getIsAvailable(),
    //             batch.getPopulationDifference(), batch.getCompactness());
    //     DB.add(newBatch);
    //     System.out.println(newBatch.toString());

    //     return 0;
    // }

}
