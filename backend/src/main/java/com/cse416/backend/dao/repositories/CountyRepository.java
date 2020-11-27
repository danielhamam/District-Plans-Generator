package com.cse416.backend.dao.repositories;

import com.cse416.backend.model.regions.county.County;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import java.lang.*;
import java.util.*;



@Repository
public interface CountyRepository extends JpaRepository<County, Integer>{

    @Query( "SELECT c FROM County c WHERE c.district.districtId = ?1")
    List<County> findByDistrictId(Integer districtId);

    @Query( "SELECT c FROM County c WHERE c.state.stateAbbreviation = ?1")
    List<County> findByStateId(String stateId);

    @Query( "SELECT c FROM County c WHERE c.state.stateAbbreviation = ?1 and c.district.districtNumber = ?2")
    List<County> findByStateIdAndDistrictNumber(String stateId, Integer districtId);

    @Query( "SELECT c FROM County c WHERE c.countyFIPSCode = ?1")
    County findByCountyFIPSCode(Integer countyFIPSCode);

    @Query( "SELECT c FROM County c WHERE c.countyName = ?1")
    County findByCountyName(String name);

}