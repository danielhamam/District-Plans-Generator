package com.cse416.backend.model.job.boxnwhisker;

import java.lang.reflect.Array;
import com.cse416.backend.model.job.boxnwhisker.BoxWhiskerPlot;
import com.cse416.backend.model.job.Job;
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
    
    @OneToOne(targetEntity=Job.class, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "jobId")
    private Job job;

    public BoxWhisker(){
        boxWhisker = new ArrayList<>();
    }

    public BoxWhisker(List<BoxWhiskerPlot> boxWhisker) {
        this.boxWhisker = boxWhisker;
    }

    @Override
    public String toString() {
        return "BoxWhisker{" +
                "id=" + id +
                ", boxWhisker=" + boxWhisker +
                ", job=" + job +
                '}';
    }

    //    public BoxWhisker(Integer[] district, Integer[] values) {
//        List<Map<Integer, Integer[]>>  newBoxWhisker = new ArrayList<>();
//        for(int i =0; i < district.length; i++){
//            Map <Integer, Integer[]> map = new HashMap<>(district[i],values[i]);
//            newBoxWhisker.add(map);
//        }
//        this.boxWhisker = newBoxWhisker;
//
//    }
}