package com.cse416.backend.dao;


import com.cse416.backend.model.plan.BoxWhisker;
import com.cse416.backend.model.job.Job;
import com.cse416.backend.model.plan.Plan;
import com.cse416.backend.model.enums.CensusCatagories;
import com.cse416.backend.model.enums.ClientCompactness;
import com.cse416.backend.model.enums.JobStatus;
import com.cse416.backend.model.demographic.Demographic;
import com.cse416.backend.model.regions.district.District;
import com.cse416.backend.model.regions.precinct.Precinct;
import com.cse416.backend.model.regions.state.State;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository("fakeDao")
public class FakeDataAccessObject{
    private static List<State> stateDB = new  ArrayList<>();
    private static List<Job> jobDB = new ArrayList<>();

    public FakeDataAccessObject(){
        buildNY2();
        State CA = new State("California", "CA", 1, 1000000000, 40, 10,100);
        stateDB.add(CA);
        State GA = new State("Georgia", "GA", 1, 1000000000, 10, 24,1999);
        stateDB.add(GA);
        // stateDB.add(State("California", "CA", 1, null, null, null, null));
        // stateDB.add(State("Geogira", "GA", 1, null, null, null, null));
    }
    public void NYjob(){
            Precinct [] NYPrecints = new Precinct[10];
            Map<CensusCatagories,Integer> ethnicPopulation = new HashMap<>();
            Map<CensusCatagories,Integer> vapPopulation = new HashMap<>();
            int allPopulation = 100;
            int votingPopulation = allPopulation - (int)(((double) allPopulation) * 0.1);
            ethnicPopulation.put(CensusCatagories.WHITE_AMERICAN, allPopulation);
            ethnicPopulation.put(CensusCatagories.AFRICAN_AMERICAN, allPopulation);
            ethnicPopulation.put(CensusCatagories.HAWAIIAN_AMERICAN, allPopulation);
            ethnicPopulation.put(CensusCatagories.LATINO_AMERICAN, allPopulation);
            ethnicPopulation.put(CensusCatagories.OTHER_AMERICAN, allPopulation);
            ethnicPopulation.put(CensusCatagories.ASIAN_AMERICAN, allPopulation);
            vapPopulation.put(CensusCatagories.WHITE_AMERICAN, votingPopulation);
            vapPopulation.put(CensusCatagories.AFRICAN_AMERICAN, votingPopulation);
            vapPopulation.put(CensusCatagories.HAWAIIAN_AMERICAN, votingPopulation);
            vapPopulation.put(CensusCatagories.LATINO_AMERICAN, votingPopulation);
            vapPopulation.put(CensusCatagories.OTHER_AMERICAN, votingPopulation);
            vapPopulation.put(CensusCatagories.ASIAN_AMERICAN, votingPopulation);

            Demographic precinctDemo = new Demographic(allPopulation, ethnicPopulation, vapPopulation);
            NYPrecints[0] = (new Precinct("precinct" + 0, 0, precinctDemo));
            NYPrecints[1] =(new Precinct("precinct" + 1, 1, precinctDemo));
            NYPrecints[2] =(new Precinct("precinct" + 2, 2, precinctDemo));
            NYPrecints[3] =(new Precinct("precinct" + 3, 3, precinctDemo));
            NYPrecints[4] =(new Precinct("precinct" + 4, 4, precinctDemo));
            NYPrecints[5] =(new Precinct("precinct" + 5, 5, precinctDemo));
            NYPrecints[6] =(new Precinct("precinct" + 6, 6, precinctDemo));
            NYPrecints[7] =(new Precinct("precinct" + 7, 7, precinctDemo));
            NYPrecints[8] =(new Precinct("precinct" + 8, 8, precinctDemo));
            NYPrecints[9] =(new Precinct("precinct" + 9, 9, precinctDemo));

            List<District> NYDistricts = new ArrayList<>();
            Map<CensusCatagories,Integer>districtEthnicPopulation= new HashMap<>();
            Map<CensusCatagories,Integer> districtvapPopulation = new HashMap<>();
            districtEthnicPopulation.put(CensusCatagories.WHITE_AMERICAN, 2);
            districtEthnicPopulation.put(CensusCatagories.AFRICAN_AMERICAN, 2);
            districtEthnicPopulation.put(CensusCatagories.AMERICAN_INDIAN, 2);
            districtEthnicPopulation.put(CensusCatagories.OTHER_AMERICAN, 2);
            districtvapPopulation.put(CensusCatagories.ASIAN_AMERICAN, 2);
            districtvapPopulation.put(CensusCatagories.WHITE_AMERICAN, 2);
            districtvapPopulation.put(CensusCatagories.AFRICAN_AMERICAN, 2);
            districtvapPopulation.put(CensusCatagories.AMERICAN_INDIAN, 2);
            districtvapPopulation.put(CensusCatagories.OTHER_AMERICAN, 2);
            districtvapPopulation.put(CensusCatagories.ASIAN_AMERICAN, 2);
            Demographic districtDemo = new Demographic(allPopulation*5, districtEthnicPopulation, districtvapPopulation);
            NYDistricts.add(new District("district" + 1, 1, 0, districtDemo));
            NYDistricts.add(new District("district" + 2, 2, 1, districtDemo));
            NYDistricts.add(new District("district" + 3, 3, 0, districtDemo));
            NYDistricts.add(new District("district" + 4, 4, 1, districtDemo));

            List<CensusCatagories> minorityAnalyzed = new ArrayList<>();
            minorityAnalyzed.add(CensusCatagories.AFRICAN_AMERICAN);
            minorityAnalyzed.add(CensusCatagories.AFRICAN_AMERICAN);
            minorityAnalyzed.add(CensusCatagories.ASIAN_AMERICAN);
//            Job job1 = new Job("JOB1", "NY", "1", -1, 4, 10000, 0.03, ClientCompactness.LOW ,minorityAnalyzed,JobStatus.COMPLETED);
//            Job job2 = new Job("JOB2", "NY", "2", -1, 4, 1000, 0.2, ClientCompactness.MEDIUM ,minorityAnalyzed ,JobStatus.PENDING);
//            Job job3 = new Job("JOB3", "NY", "3", -1, 4, 100, 0.53, ClientCompactness.HIGH ,minorityAnalyzed,JobStatus.RUNNING);
//            jobDB.add(job1);
//            jobDB.add(job2);
//            jobDB.add(job3);

    }


    public void buildNY2(){
        State NY = new State("New York", "NY", 1, 1000000000, 40, 13,100);
        stateDB.add(NY);
        List<CensusCatagories> kk = new ArrayList<>();
        kk.add(CensusCatagories.AFRICAN_AMERICAN);
        kk.add(CensusCatagories.AFRICAN_AMERICAN);
        kk.add(CensusCatagories.ASIAN_AMERICAN);
        Integer [] district = {1,2,3,4};
        Integer [] calues ={12,12,41,214,12} ;
        BoxWhisker bw = new BoxWhisker(district,calues);


        Job job1 = new Job("JOB1", "NY", "1", -1, 2, 500, 0.03, ClientCompactness.LOW ,kk,JobStatus.COMPLETED,bw);
        Job job2 = new Job("JOB2", "NY", "2", -1, 3, 1000, 0.2, ClientCompactness.MEDIUM ,kk ,JobStatus.PENDING,bw);
        Job job3 = new Job("JOB3", "NY", "3", -1, 4, 100, 0.53, ClientCompactness.HIGH ,kk,JobStatus.RUNNING,bw);
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

//     Rakuna (Submit your resume): https://app.rakuna.co/prospect/checkin?token=a12f0f35e4fc5c8e3954801519f7b9d3b4404f71


// Email: Courtney.Sullivan@fcb.com 


// LinkedIn: https://www.linkedin.com/in/courtsull/
