package com.cse416.backend.model.job.boxnwhisker;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoxWhisker {
    //[min,  q1, q2, q3, max]
    private List<BoxWhiskerPlot> boxWhisker;

    public BoxWhisker(){
        boxWhisker = new ArrayList<>();
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
