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

    @Query("SELECT s FROM State s WHERE s.stateAbbreviation = ?1")
    State findByStateId(String id);

    @Query("SELECT s FROM State s WHERE s.stateName = ?1")
    State findBystateName(String name);

    @Query("SELECT s FROM State s WHERE s.stateFIPSCode = ?1")
    State findBystateFIPSCode(Integer stateFIPSCode);
}