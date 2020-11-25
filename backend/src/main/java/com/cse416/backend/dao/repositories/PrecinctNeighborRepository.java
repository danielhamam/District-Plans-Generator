// package com.cse416.backend.dao.repositories;

// import com.cse416.backend.model.regions.precinct.*;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.*;

// import java.lang.*;
// import java.util.*;

// @Repository
// public interface PrecinctNeighborRepository extends JpaRepository<PrecinctNeighbor, PrecinctNeighborIdentity>{

//     @Query(value = "SELECT p FROM PrecinctNeighbors p WHERE p.precinctId = :precinctId")
//     List<PrecinctNeighbor> findByPrecinctId(@Param("precinctId") String precinctId);
// }