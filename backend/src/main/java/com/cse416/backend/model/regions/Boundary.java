package com.cse416.backend.model.regions;

import java.util.List;

public class Boundary {
    List<List<Integer>> coordinates;

    public List<List<Integer>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<Integer>> coordinates) {
        this.coordinates = coordinates;
    }
}
