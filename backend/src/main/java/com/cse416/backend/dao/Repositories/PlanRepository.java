package com.cse416.backend.dao;

import com.cse416.backend.model.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.*;
import java.lang.Integer;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Integer>{}