package com.cse416.backend.dao;


import com.cse416.backend.model.Batch;
import com.cse416.backend.model.State;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository("fakeDao")
public class ServerFakeDataAccessObject implements BatchDao{
    private static List<State> stateDB = new  ArrayList<>();
    private static List<Batch> DB = new ArrayList<>();

    public ServerFakeDataAccessObject(){
        stateDB.add(new State ("New York", "NY", new ArrayList<Batch>(),
                10000000, 1000000, 100000));
        stateDB.add(new State ("Georgia", "GA", new ArrayList<Batch>(),
                10000000, 1000000, 100000));
        stateDB.add(new State ("California", "CA", new ArrayList<Batch>(),
                10000000, 1000000, 100000));

    }

    public State queryGetStateByAbberivation(String stateAbberivation) {
        return stateDB.stream()
                .filter(e -> stateAbberivation.equals(e.getAbberivation()))
                .findFirst().orElse(null);
    }

    public void muatationSaveBatch(State state, Batch batch){
        state.addBatch(batch);
    }

    @Override
    public int generateBatch(UUID id, Batch batch) {
        Batch newBatch = new Batch(id, batch.getnumberOfDistricting(), batch.getName(), batch.getIsAvailable(),
                batch.getPopulationDifference(), batch.getCompactness());
        DB.add(newBatch);
        System.out.println(newBatch.toString());

        return 0;
    }

}
