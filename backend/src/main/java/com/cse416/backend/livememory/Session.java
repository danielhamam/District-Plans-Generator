package com.cse416.backend.livememory;

import java.util.List;
import java.util.NoSuchElementException;

import com.cse416.backend.model.job.Job;
import com.cse416.backend.model.enums.JobStatus;
import com.cse416.backend.model.regions.state.State;
import com.cse416.backend.model.plan.Plan;
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
        jobs =  new ArrayList<>();
    }

    public State getState(){
        return this.state;
    }

    public Job getJobByID(Integer jobID)throws NoSuchElementException{
        return this.jobs.stream()
                .filter(job -> jobID.equals(job.getJobID()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void addJobs(List<Job> jobs){
        this.jobs.addAll(jobs);
    }

    public void addJob(Job jobs){
        this.jobs.add(jobs);
    }

    public Job deleteJob(Integer jobID){
        Job deletedJob = null;
        for(Job job : this.jobs){
            if(jobID.equals(job.getJobID())){
                jobs.remove(job);
                deletedJob = job;
            }
        }
        return deletedJob;
    }




}
