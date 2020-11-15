package com.cse416.backend.dao;

import com.cse416.backend.model.regions.County;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.*;
import java.lang.Integer;

@Repository
public interface CountyRepository extends CrudRepository<County, Integer>{}