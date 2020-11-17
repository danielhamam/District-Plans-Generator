package com.cse416.backend.livememory;

import com.cse416.backend.model.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class GlobalHistory {
    private List<Job> jobs;

    public GlobalHistory(){
        this.jobs = new ArrayList<>();
    }

    public void addJob(Job job){
        this.jobs.add(job);
    }

    public Job getJobByID(String jobID)throws NoSuchElementException {
        return this.jobs.stream()
                .filter(job -> jobID.equals(job.getJobID()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    public void addJobs(List<Job> jobs){
        this.jobs.addAll(jobs);
    }

    public boolean deleteJob(String jobID){
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

