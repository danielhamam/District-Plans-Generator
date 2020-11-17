package com.cse416.backend.model.livememory;

import java.util.List;
import java.util.NoSuchElementException;

import com.cse416.backend.model.Job;
import com.cse416.backend.model.regions.State;
import com.cse416.backend.model.Plan;
import java.util.ArrayList;


public class Session{
    private State state;
    private List<Job> jobs;
    private Plan enactedPlan;

    public Session(State state){
        this.state = state;
        this.jobs = new ArrayList<>();
        this.enactedPlan = null;
    }

    public State getState(){
        return this.state;
    }

    public Job getJobByID(String jobID)throws NoSuchElementException{
        return this.jobs.stream()
                .filter(job -> jobID.equals(job.getJobID()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public void addJobs(List<Job> jobs){
        this.jobs.addAll(jobs);
    }

    

}
