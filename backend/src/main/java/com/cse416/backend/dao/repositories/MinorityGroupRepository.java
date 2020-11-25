// package com.cse416.backend.dao.repositories;

// import com.cse416.backend.model.job.minoritygroup.*;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.*;

// import java.lang.*;
// import java.util.*;

// @Repository
// public interface MinorityGroupRepository extends JpaRepository<MinorityGroup, MinorityGroupIdentity>{

//     @Query(value = "SELECT m FROM JobMinorityGroups m WHERE m.jobId = :jobId")
//     List<MinorityGroup> findByJobId(@Param("jobId") Integer jobId);

//     @Query(value = "SELECT m FROM JobMinorityGroups m WHERE m.minorityGroupid = :ethnicityName")
//     List<MinorityGroup> findByEthnicityName(@Param("ethnicityName") String ethnicityName);
// }