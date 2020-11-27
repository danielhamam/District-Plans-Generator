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

    @Query("SELECT p FROM Precinct p WHERE p.state.stateAbbreviation = ?1")
    List<Precinct> findByStateId(String stateId);

    @Query("SELECT p FROM Precinct p WHERE p.precinctName = ?1")
    Precinct findByName(String name);

    @Query("SELECT p FROM Precinct p WHERE p.precinctFIPSCode = ?1")
    Precinct findByFIPSCode(String precinctFIPSCode);

    @Query("SELECT p FROM Precinct p WHERE p.county.countyId = ?1")
    List<Precinct> findByCountyId(Integer countyId);

    @Query("SELECT p FROM Precinct p WHERE p.district.districtId = ?1")
    List<Precinct> findByDistrictId(Integer districtId);

}