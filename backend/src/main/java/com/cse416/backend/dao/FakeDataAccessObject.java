package com.cse416.backend.dao;


import com.cse416.backend.model.Job;
import com.cse416.backend.model.regions.State;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository("fakeDao")
public class FakeDataAccessObject{
    // private static List<State> stateDB = new  ArrayList<>();
    // private static List<Batch> DB = new ArrayList<>();

    public FakeDataAccessObject(){
    

    }

    // public State queryGetStateByAbberivation(String stateAbberivation) {
    //     return stateDB.stream()
    //             .filter(e -> stateAbberivation.equals(e.getAbberivation()))
    //             .findFirst().orElse(null);
    // }

    // public void muatationSaveBatch(State state, Batch batch){
    //     state.addBatch(batch);
    // }

    // @Override
    // public int generateBatch(UUID id, Batch batch) {
    //     Batch newBatch = new Batch(id, batch.getnumberOfDistricting(), batch.getName(), batch.getIsAvailable(),
    //             batch.getPopulationDifference(), batch.getCompactness());
    //     DB.add(newBatch);
    //     System.out.println(newBatch.toString());

    //     return 0;
    // }

}
