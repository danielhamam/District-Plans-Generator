package com.cse416.backend.dao.repositories;

import com.cse416.backend.model.job.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import java.lang.*;
import java.util.*;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer>{

    @Query("SELECT j FROM Job j WHERE j.state.stateAbbreviation = ?1")
    List<Job> findByStateId(String stateId);
}