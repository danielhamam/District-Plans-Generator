
// package com.cse416.backend.dao.services;
// import org.springframework.stereotype.*;
// import com.cse416.backend.dao.repositories.PrecinctNeighborRepository;

// import com.cse416.backend.model.regions.precinct.*;
// import org.springframework.beans.factory.annotation.Autowired;

// import java.util.*;
// import java.lang.Integer;

// import java.util.Optional;


// @Service
// public class PrecinctNeighborDAOService{

//    @Autowired
//    private PrecinctNeighborRepository precinctNeighborRepository;

//    public Optional<PrecinctNeighbor> getPrecinctNeighborById(PrecinctNeighborIdentity Id){
//        return precinctNeighborRepository.findById(Id);
//    }

//    public List<PrecinctNeighbor> getPrecinctNeighbors(String precinctId){
//        return precinctNeighborRepository.findByPrecinctId(precinctId);
//    }

//    public void addPrecinctNeighbor(PrecinctNeighbor precinctNeighbor){
//        precinctNeighborRepository.save(precinctNeighbor);
//    }

//    public void updatePrecinctNeighbor(PrecinctNeighbor precinctNeighbor){
//        precinctNeighborRepository.save(precinctNeighbor);
//    }

//    public void deletePrecinctNeighbor(PrecinctNeighbor precinctNeighbor){
//        precinctNeighborRepository.delete(precinctNeighbor);
//    }

//    public void deletePrecinctNeighborById(PrecinctNeighborIdentity Id){
//        precinctNeighborRepository.deleteById(Id);
//    }

//    public Long numberPrecinctNeighborEntities(){
//        return precinctNeighborRepository.count();
//    }
// }