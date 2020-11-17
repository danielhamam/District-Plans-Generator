package com.cse416.backend.model.livememory;

import com.cse416.backend.model.Job;

import java.util.ArrayList;
import java.util.List;

public class GlobalHistory {
    private List<Job> jobs;

    public GlobalHistory(){
        this.jobs = new ArrayList<>();
    }

    public void addJob(Job job){
        this.jobs.add(job);
    }

    public void addJobs(List<Job>jobs){
        this.jobs.addAll(jobs);
    }

    public void deleteJob(Job job){
        this.jobs.stream()
                .filter(j -> j == job);
    }

}

