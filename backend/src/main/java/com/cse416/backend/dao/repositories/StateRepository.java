package com.cse416.backend.dao.repositories;

import com.cse416.backend.model.regions.state.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import java.lang.*;
import java.util.*;

@Repository
public interface StateRepository extends JpaRepository<State, String>{

    @Query(value = "SELECT * FROM States s WHERE s.stateName = :name", nativeQuery=true)
    State findByName(@Param("name") String name);

    @Query(value = "SELECT * FROM States s WHERE s.stateFIPSCode = :stateFIPSCode", nativeQuery=true)
    State findByFIPSCode(@Param("stateFIPSCode") Integer stateFIPSCode);
}