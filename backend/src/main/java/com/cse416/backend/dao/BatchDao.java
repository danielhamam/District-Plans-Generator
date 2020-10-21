package com.cse416.backend.dao;

import com.cse416.backend.model.Batch;

import java.util.UUID;

public interface BatchDao {

    int generateBatch(UUID id, Batch batch);

    default int generateBatch(Batch batch){
        UUID id = UUID.randomUUID();
        return generateBatch(id, batch);

    }

}
