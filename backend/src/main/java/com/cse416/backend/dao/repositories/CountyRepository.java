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

    @Query(value = "SELECT * FROM Counties c WHERE c.districtId = :districtId", nativeQuery=true)
    List<County> findByDistrictId(@Param("districtId") Integer districtId);

    @Query(value = "SELECT * FROM Counties c WHERE c.stateId = :stateId", nativeQuery=true)
    List<County> findByStateId(@Param("stateId") String stateId);

    @Query(value = "SELECT * FROM Counties c WHERE c.countyFIPSCode = :countyFIPSCode", nativeQuery=true)
    County findByCountyFIPSCode(@Param("countyFIPSCode") Integer countyFIPSCode);

    @Query(value = "SELECT * FROM Counties c WHERE c.countyName = :name", nativeQuery=true)
    County findByCountyName(@Param("name") String name);

}