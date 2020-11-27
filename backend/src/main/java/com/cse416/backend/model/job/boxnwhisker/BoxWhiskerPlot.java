package com.cse416.backend.model.job.boxnwhisker;
import javax.persistence.*;


@Entity
@Table(name = "BoxWhiskerPlots")
public class BoxWhiskerPlot{

    @Id
    @GeneratedValue
    @Column(name = "boxWhiskerPlotId")
    private int id;

    @Column(name = "mininum")
    private int min;

    @Column(name = "lowerQuartile")
    private int q1;

    @Column(name = "median")
    private int q2;

    @Column(name = "upperQuartile")
    private int q3;

    @Column(name = "maximum")
    private int max;

    @Transient
    private int [] enactedPlan;

    @ManyToOne(targetEntity=BoxWhisker.class, fetch = FetchType.LAZY)
    @JoinColumn(name="boxwhiskerId")
    private BoxWhisker boxWhisker;


    public BoxWhiskerPlot(int id, int min, int q1, int q2, int q3, int max) {
        this.id = id;
        this.min = min;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.max = max;
    }

    public int getId() {
        return id;
    }

    public int getMin() {
        return min;
    }

    public int getQ1() {
        return q1;
    }

    public int getQ2() {
        return q2;
    }

    public int getQ3() {
        return q3;
    }

    public int getMax() {
        return max;
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
                '}';
    }
}