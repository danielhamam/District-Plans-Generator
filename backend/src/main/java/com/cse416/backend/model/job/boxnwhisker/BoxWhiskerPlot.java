package com.cse416.backend.model.job.boxnwhisker;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Arrays;


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

    @JsonProperty
    @Column(name = "indexedDistricts")
    private int indexedDistrict;

    @Transient
    @JsonProperty
    private long [] values;

    @JsonProperty
    @Column(name = "enactedPlanValue")
    private long enactedPlanValue;

    @ManyToOne(targetEntity=BoxWhisker.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="boxWhiskerId")
    @JsonIgnore
    private BoxWhisker boxWhisker;

    //Necessary For JPA
    protected BoxWhiskerPlot(){
    }

    public BoxWhiskerPlot(int indexedDistrict, long min, long q1, long q2, long q3, long max, long enactedPlanValue) {
        this.indexedDistrict = indexedDistrict;
        this.values = new long[5];
        this.enactedPlanValue = enactedPlanValue;
        values[0] = this.min = min;
        values[1] = this.q1 = q1;
        values[2] = this.q2 = q2;
        values[3] = this.q3 = q3;
        values[4] = this.max = max;

    }


    public void intialize(){
        this.values = new long[5];
        values[0] = this.min;
        values[1] = this.q1;
        values[2] = this.q3;
        values[3] = this.q2;
        values[4] = this.max;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getQ1() {
        return q1;
    }

    public void setQ1(long q1) {
        this.q1 = q1;
    }

    public long getQ2() {
        return q2;
    }

    public void setQ2(long q2) {
        this.q2 = q2;
    }

    public long getQ3() {
        return q3;
    }

    public void setQ3(long q3) {
        this.q3 = q3;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public int getIndexedDistrict() {
        return indexedDistrict;
    }

    public void setIndexedDistrict(int indexedDistrict) {
        this.indexedDistrict = indexedDistrict;
    }

    public long[] getValues() {
        return values;
    }

    public void setValues(long[] values) {
        this.values = values;
    }

    public long getEnactedPlanValue() {
        return enactedPlanValue;
    }

    public void setEnactedPlanValue(long enactedPlanValue) {
        this.enactedPlanValue = enactedPlanValue;
    }

    public BoxWhisker getBoxWhisker() {
        return boxWhisker;
    }

    public void setBoxWhisker(BoxWhisker boxWhisker) {
        this.boxWhisker = boxWhisker;
    }

    @Override
    public String toString() {
        return "BoxWhiskerPlot{" +
                "id=" + id +
                ", min=" + min +
                ", q1=" + q1 +
                ", q2=" + q2 +
                ", q3=" + q3 +
                ", max=" + max +
                ", indexedDistrict=" + indexedDistrict +
                ", values=" + Arrays.toString(values) +
                ", enactedPlanValue=" + enactedPlanValue +
                '}';
    }
}