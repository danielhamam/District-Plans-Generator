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


    @OneToMany(targetEntity=BoxWhiskerPlot.class, cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "boxWhisker")
    private List<BoxWhiskerPlot> boxWhiskerPlots;

    @OneToOne(targetEntity=Job.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="jobId")
    @JsonIgnore
    private Job job;

    //Necessary For JPA
    protected BoxWhisker(){
    }


    public BoxWhisker(List<BoxWhiskerPlot> boxWhiskerPlots) {
        this.boxWhiskerPlots = boxWhiskerPlots;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<BoxWhiskerPlot> getBoxWhiskerPlots() {
        return boxWhiskerPlots;
    }

    public void setBoxWhiskerPlots(List<BoxWhiskerPlot> boxWhiskerPlots) {
        this.boxWhiskerPlots = boxWhiskerPlots;
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
                ", boxWhiskerPlots=" + boxWhiskerPlots +
                '}';
    }

}