package com.cse416.backend.livememory;

import java.util.List;
import java.util.NoSuchElementException;

import com.cse416.backend.model.Job;
import com.cse416.backend.model.regions.State;
import com.cse416.backend.model.Plan;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class Session{
    private State state;
    private List<Job> jobs;
    private Plan enactedPlan;

    public Session(){
        this.jobs = new ArrayList<>();
    }

    public void setState(State state){
        this.state = state;
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

    public boolean deleteJob(String jobID){
//        this.jobs.stream()
//                .filter(job -> !jobID.equals(job.getJobID()))
//                .collect(Collectors.toList());
        boolean isDeletionSuccesful = false;
        for(Job job : this.jobs){
            if(jobID.equals(job.getJobID())){
                this.jobs.remove(job);
                isDeletionSuccesful = true;
                break;
            }
        }
        return isDeletionSuccesful;
    }




}
