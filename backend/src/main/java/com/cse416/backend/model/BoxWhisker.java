package com.cse416.backend.model;

import java.util.List;
import java.util.Map;

public class BoxWhisker {
    //[min,  q1, q2, q3, max]
    private List<Map<Integer, Integer[]>> boxnwhisker;

    public BoxWhisker(List<Map<Integer, Integer[]>> boxnwhisker) {
        this.boxnwhisker = boxnwhisker;
    }
}
