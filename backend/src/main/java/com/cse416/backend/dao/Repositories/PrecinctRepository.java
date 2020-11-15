package com.cse416.backend.dao;

import com.cse416.backend.model.regions.Precinct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.*;
import java.lang.Integer;

@Repository
public interface PrecinctRepository extends CrudRepository<Precinct, Integer>{}