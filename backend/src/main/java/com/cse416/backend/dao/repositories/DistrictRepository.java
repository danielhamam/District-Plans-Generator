package com.cse416.backend.dao.repositories;

import com.cse416.backend.model.regions.district.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import java.lang.*;
import java.util.*;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer>{

    @Query("SELECT d FROM District d WHERE d.state.stateAbbreviation = ?1")
    List<District> findByStateId(String stateId);

    @Query("SELECT d FROM District d WHERE d.state.stateAbbreviation = ?1 and d.districtNumber = ?2")
    District findByStateIdAndDistrictNumber(String stateId, Integer districtNumber);
}