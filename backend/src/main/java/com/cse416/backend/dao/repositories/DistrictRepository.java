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

    @Query(value = "SELECT * FROM Districts d WHERE d.stateId = :stateId", nativeQuery=true)
    List<District> findByStateId(@Param("stateId") String stateId);

    @Query(value = "SELECT * FROM Districts d WHERE d.stateId = :stateId and d.districtNumber = :districtNumber", nativeQuery=true)
    District findByStateIdAndDistrictNumber(@Param("stateId") String stateId, @Param("districtNumber") Integer districtNumber);
}