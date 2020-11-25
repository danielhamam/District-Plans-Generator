package com.cse416.backend.dao.repositories;

import com.cse416.backend.model.demographic.Demographic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import java.lang.*;
import java.util.*;

@Repository
public interface DemographicRepository extends JpaRepository<Demographic, Integer>{

    @Query(value = "SELECT * FROM Demographics d WHERE d.precinctId = :precinctId", nativeQuery=true)
    Demographic findByPrecinctId(@Param("precinctId") String precinctId);

    @Query(value = "SELECT * FROM Demographics d WHERE d.districtId = :districtId", nativeQuery=true)
    Demographic findByDistrictId(@Param("districtId") Integer districtId);

    @Query(value = "SELECT * FROM Demographics d WHERE d.stateId = :stateId", nativeQuery=true)
    Demographic findByStateId(@Param("stateId") String stateId);

    @Query(value = "SELECT * FROM Demographics d WHERE d.countyId = :countyId", nativeQuery=true)
    Demographic findByCountyId(@Param("countyId") Integer countyId);

} 
