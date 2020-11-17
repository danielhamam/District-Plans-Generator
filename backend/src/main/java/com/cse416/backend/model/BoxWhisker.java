package com.cse416.backend.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoxWhisker {
    //[min,  q1, q2, q3, max]
    private List<Map<Integer, Integer[]>> boxWhisker;

    public BoxWhisker(List<Map<Integer, Integer[]>> boxnwhisker) {
        this.boxWhisker = boxnwhisker;
    }

    public BoxWhisker(Integer[] district, Integer[] values) {
        List<Map<Integer, Integer[]>>  newBoxWhisker = new ArrayList<>();
        for(int i =0; i < district.length; i++){
            Map <Integer, Integer[]> map = new HashMap<>(district[i],values[i]);
            newBoxWhisker.add(map);
        }
        this.boxWhisker = newBoxWhisker;

    }
}
