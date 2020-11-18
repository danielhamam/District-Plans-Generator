package com.cse416.backend.model.job;

import javax.persistence.*;
import java.lang.Integer;
import java.util.*;

import com.cse416.backend.model.plan.BoxWhisker;


public class JobBoxWhiskerPlots{

    private Integer id;
    
    private Job job;
    
    private List<BoxWhisker> plots;

    protected JobBoxWhiskerPlots(){}

    public JobBoxWhiskerPlots(Job job, List<BoxWhisker> plots){
        this.job = job;
        this.plots = plots;
    }

}