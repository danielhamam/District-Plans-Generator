package com.cse416.backend.dao;


import com.cse416.backend.model.Job;
import com.cse416.backend.model.Plan;
import com.cse416.backend.model.enums.CensusCatagories;
import com.cse416.backend.model.enums.ClientCompactness;
import com.cse416.backend.model.enums.JobStatus;
import com.cse416.backend.model.regions.Boundary;
import com.cse416.backend.model.regions.Demographic;
import com.cse416.backend.model.regions.District;
import com.cse416.backend.model.regions.Precinct;
import com.cse416.backend.model.regions.State;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository("fakeDao")
public class FakeDataAccessObject{
    private static List<State> stateDB = new  ArrayList<>();
    private static List<Job> jobDB = new ArrayList<>();

    public FakeDataAccessObject(){
        buildNY();
        // stateDB.add(State("California", "CA", 1, null, null, null, null));
        // stateDB.add(State("Geogira", "GA", 1, null, null, null, null));
    }


//     Rakuna (Submit your resume): https://app.rakuna.co/prospect/checkin?token=a12f0f35e4fc5c8e3954801519f7b9d3b4404f71


// Email: Courtney.Sullivan@fcb.com 


// LinkedIn: https://www.linkedin.com/in/courtsull/

    public void buildNY(){
        Precinct [] NYPrecints = new Precinct[10]; 
        Map<CensusCatagories,Integer>d= new HashMap<>();
        d.put(CensusCatagories.WHITE_AMERICAN, 2);
        d.put(CensusCatagories.BLACK_AMERICAN, 2);
        d.put(CensusCatagories.HAWAIIAN_PACIFIC_AMERICA, 2);
        d.put(CensusCatagories.NATIVE_ALASKA_AMERICAN, 2);
        d.put(CensusCatagories.HISPANIC_AMERICA, 2);
        Demographic a = new Demographic(10l, d, null);
        NYPrecints[0] = (new Precinct("precinct" + 0, 0, null, a,null));
        NYPrecints[1] =(new Precinct("precinct" + 1, 1, null, a,null));
        NYPrecints[2] =(new Precinct("precinct" + 2, 2, null, a,null));
        NYPrecints[3] =(new Precinct("precinct" + 3, 3, null, a,null));
        NYPrecints[4] =(new Precinct("precinct" + 4, 4, null, a,null));
        NYPrecints[5] =(new Precinct("precinct" + 5, 5, null, a,null));
        NYPrecints[6] =(new Precinct("precinct" + 6, 6, null, a,null));
        NYPrecints[7] =(new Precinct("precinct" + 7, 7, null, a,null));
        NYPrecints[8] =(new Precinct("precinct" + 8, 8, null, a,null));
        NYPrecints[9] =(new Precinct("precinct" + 9, 9, null, a,null));
        List<District> NYDistricts = new ArrayList<>();
        Map<CensusCatagories,Integer>dd= new HashMap<>();
        dd.put(CensusCatagories.WHITE_AMERICAN, 2);
        dd.put(CensusCatagories.BLACK_AMERICAN, 2);
        dd.put(CensusCatagories.HAWAIIAN_PACIFIC_AMERICA, 2);
        dd.put(CensusCatagories.NATIVE_ALASKA_AMERICAN, 2);
        dd.put(CensusCatagories.HISPANIC_AMERICA, 2);
        Demographic aa = new Demographic(10l, dd, null);
        NYDistricts.add(new District("district" + 0, 0, 0, aa, null));
        NYDistricts.add(new District("district" + 1, 1, 1, aa, null));
        Plan NYEnactedPlan = new Plan("ENACTEDPLAN", "NY", "1", 2, 5, 10, true, 2020, NYDistricts);
        Demographic demographic = new Demographic(100);
        State NY = new State("New York", "NY", 1, demographic, null, NYEnactedPlan, NYPrecints);
        stateDB.add(NY);
        Job job1 = new Job("JOB1", "NY", "1", -1, 2, 500, 0.03, ClientCompactness.MEDUIM ,CensusCatagories.BLACK_AMERICAN,JobStatus.COMPLETED);
        Job job2 = new Job("JOB2", "NY", "2", -1, 3, 1000, 0.2, ClientCompactness.MEDUIM ,CensusCatagories.HAWAIIAN_PACIFIC_AMERICA,JobStatus.PENDING);
        Job job3 = new Job("JOB3", "NY", "3", -1, 4, 100, 0.53, ClientCompactness.MEDUIM ,CensusCatagories.ASIAN_AMERICAN,JobStatus.RUNNING);
        job1.setExtremeDistrictPlan(new Plan("Extreme", "NY", "2", 2, 5, 10, true, 2020, NYDistricts));
        job1.setAverageDistrictPlan(new Plan("Avg", "NY", "3", 2, 5, 10, true, 2020, NYDistricts));
        job1.setRandomDistrictPlan(new Plan("Random", "NY", "4", 2, 5, 10, true, 2020, NYDistricts));
        job2.setExtremeDistrictPlan(new Plan("Extreme", "NY", "2", 2, 5, 10, true, 2020, NYDistricts));
        job2.setAverageDistrictPlan(new Plan("Avg", "NY", "3", 2, 5, 10, true, 2020, NYDistricts));
        job2.setRandomDistrictPlan(new Plan("Random", "NY", "4", 2, 5, 10, true, 2020, NYDistricts));
        job3.setExtremeDistrictPlan(new Plan("Extreme", "NY", "2", 2, 5, 10, true, 2020, NYDistricts));
        job3.setAverageDistrictPlan(new Plan("Avg", "NY", "3", 2, 5, 10, true, 2020, NYDistricts));
        job3.setRandomDistrictPlan(new Plan("Random", "NY", "4", 2, 5, 10, true, 2020, NYDistricts));
        jobDB.add(job1);
        jobDB.add(job2);
        jobDB.add(job3);
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

    public void mutationGenerateJob(Job job){
        UUID v = UUID.randomUUID();
        job.setJobID(""+v);
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
