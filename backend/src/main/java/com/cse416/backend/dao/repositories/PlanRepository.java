package com.cse416.backend.dao.repositories;

import com.cse416.backend.model.plan.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import java.lang.*;
import java.util.*;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer>{

    @Query(value = "SELECT * FROM Plans p WHERE p.stateID = :stateId", nativeQuery=true)
    List<Plan> findByStateId(@Param("stateId") String stateId);

    @Query(value = "SELECT * FROM Plans p WHERE p.jobId = :jobId", nativeQuery=true)
    List<Plan> findByJobId(@Param("jobId") Integer jobId);
}