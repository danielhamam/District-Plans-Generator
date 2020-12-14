package com.cse416.backend.model.job.boxnwhisker;


import com.cse416.backend.model.job.Job;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "BoxWhiskers")
public class BoxWhisker {


    @Id
    @GeneratedValue
    @Column(name = "boxWhiskerId")
    private Integer id;


    @OneToMany(targetEntity=BoxWhiskerPlot.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="boxWhiskerPlotId")
    private List<BoxWhiskerPlot> boxWhisker;

    @OneToOne(targetEntity=Job.class, fetch = FetchType.EAGER, cascade = {CascadeType.DETACH})
    @JsonIgnore
    private Job job;

    //Necessary For JPA
    protected BoxWhisker(){
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