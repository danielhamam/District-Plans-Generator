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

    @Query("SELECT p FROM Plan p WHERE p.job.generatedId = ?1")
    List<Plan> findByJobId(Integer jobId);
}