package com.cse416.backend.model.job.boxnwhisker;

import java.lang.reflect.Array;
import com.cse416.backend.model.job.boxnwhisker.BoxWhiskerPlot;
import com.cse416.backend.model.job.Job;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.*;

@Entity
@Table(name = "BoxWhiskers")
public class BoxWhisker {
    //[min,  q1, q2, q3, max]

    @Id
    @GeneratedValue
    @Column(name = "boxWhiskerId")
    private Integer id;

    @OneToMany(targetEntity=BoxWhiskerPlot.class,cascade = CascadeType.ALL, 
    fetch = FetchType.LAZY)
    private List<BoxWhiskerPlot> boxWhisker;
    
    @OneToOne(targetEntity=Job.class, fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinColumn(name = "jobId")
    @JsonIgnore
    private Job job;

    public BoxWhisker(){
        boxWhisker = new ArrayList<>();
    }

    public BoxWhisker(List<BoxWhiskerPlot> boxWhisker) {
        this.boxWhisker = boxWhisker;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<BoxWhiskerPlot> getBoxWhisker() {
        return boxWhisker;
    }

    public void setBoxWhisker(List<BoxWhiskerPlot> boxWhisker) {
        this.boxWhisker = boxWhisker;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "BoxWhisker{" +
                "id=" + id +
                ", boxWhisker=" + boxWhisker +
                ", job=" + job +
                '}';
    }

}