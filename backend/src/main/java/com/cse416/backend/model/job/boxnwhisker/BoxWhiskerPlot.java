package com.cse416.backend.model.job.boxnwhisker;



public class BoxWhiskerPlot{

    private int id;
    private int min;
    private int q1;
    private int q2;
    private int q3;
    private int max;
    private int [] enactedPlan;


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