package com.cse416.backend.dao.repositories;

import com.cse416.backend.model.job.boxnwhisker.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;
import java.lang.*;
import java.util.*;


@Repository
public interface BoxWhiskerPlotRepository extends JpaRepository<BoxWhiskerPlot, Integer>{

    @Query("SELECT b FROM BoxWhiskerPlot b WHERE b.boxWhisker.id = ?1")
    List<BoxWhiskerPlot> findPlotsByBoxWhiskerId(Integer boxWhiskerId);
}