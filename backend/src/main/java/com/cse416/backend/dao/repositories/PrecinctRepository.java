package com.cse416.backend.dao.repositories;

import com.cse416.backend.model.regions.precinct.Precinct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import java.lang.*;
import java.util.*;

@Repository
public interface PrecinctRepository extends JpaRepository<Precinct, Integer>{

    @Query(value = "SELECT * FROM Precincts p WHERE p.stateID = :stateId", nativeQuery=true)
    List<Precinct> findByStateId(@Param("stateId") String stateId);

    @Query(value = "SELECT * FROM Precincts p WHERE p.precinctName = :name", nativeQuery=true)
    Precinct findByName(@Param("name") String name);

    @Query(value = "SELECT * FROM Precincts p WHERE p.precinctFIPSCode = :precinctFIPSCode", nativeQuery=true)
    Precinct findByFIPSCode(@Param("precinctFIPSCode") String precinctFIPSCode);

    @Query(value = "SELECT * FROM Precincts p WHERE p.countyId = :countyId", nativeQuery=true)
    List<Precinct> findByCountyId(@Param("countyId") Integer countyId);

    @Query(value = "SELECT * FROM Precincts p WHERE p.districtId = :districtId", nativeQuery=true)
    List<Precinct> findByDistrictId(@Param("districtId") Integer districtId);

}