package com.cse416.backend.model.job.boxnwhisker;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;


@Entity
@Table(name = "BoxWhiskerPlots")
public class BoxWhiskerPlot{

    @Id
    @GeneratedValue
    @Column(name = "boxWhiskerPlotId")
    @JsonIgnore
    private long id;

    @Column(name = "mininum")
    @JsonIgnore
    private long min;

    @Column(name = "lowerQuartile")
    @JsonIgnore
    private long q1;

    @Column(name = "median")
    @JsonIgnore
    private long q2;

    @Column(name = "upperQuartile")
    @JsonIgnore
    private long q3;

    @Column(name = "maximum")
    @JsonIgnore
    private long max;

    @Transient
    @JsonProperty
    private int indexedDistrict;

    @Transient
    @JsonProperty
    private long [] values;

    @ManyToOne(targetEntity=BoxWhisker.class, fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinColumn(name="boxwhiskerId")
    private BoxWhisker boxWhisker;

    //Necessary For JPA
    protected BoxWhiskerPlot(){}

    public BoxWhiskerPlot(int indexedDistrict, long min, long q1, long q2, long q3, long max) {
        this.indexedDistrict = indexedDistrict;
        this.min = min;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.max = max;
    }

    @Override
    public String toString() {
        return "BoxWhiskerPlot{" +
                "id=" + id +
                ", indexedDistrict=" + indexedDistrict +
                ", min=" + min +
                ", q1=" + q1 +
                ", q2=" + q2 +
                ", q3=" + q3 +
                ", max=" + max +
                '}';
    }
}