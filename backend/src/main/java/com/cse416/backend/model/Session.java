package com.cse416.backend.model;

import java.util.List;

import com.cse416.backend.model.Job;
import com.cse416.backend.model.regions.State;
import com.cse416.backend.model.Plan;

public class Session{
    private State state;
    private List<Job> jobs;
    private Plan enactedPlan;

    public Session(State state){
        this.state = state;
    }

    public void addJobs(List<Job> jobs){
        this.jobs.addAll(jobs);
    }

    

}
