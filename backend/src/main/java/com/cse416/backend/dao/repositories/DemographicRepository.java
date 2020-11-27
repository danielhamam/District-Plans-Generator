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

    @Query("SELECT d FROM Demographic d WHERE d.precinct.precinctId = ?1")
    Demographic findByPrecinctId(Integer precinctId);

    @Query("SELECT d FROM Demographic d WHERE d.district.districtId = ?1")
    Demographic findByDistrictId(Integer districtId);

    @Query("SELECT d FROM Demographic d WHERE d.state.stateAbbreviation = ?1")
    Demographic findByStateId(String stateId);

    @Query("SELECT d FROM Demographic d WHERE d.county.countyId = ?1")
    Demographic findByCountyId(Integer countyId);

} 
