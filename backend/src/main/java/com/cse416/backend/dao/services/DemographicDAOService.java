//
//package com.cse416.backend.dao.services;
//import com.cse416.backend.dao.repositories.DemographicRepository;
//import org.springframework.stereotype.*;
//
//import com.cse416.backend.model.Demographic;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.lang.Integer;
//
//import java.util.Optional;
//
//
//
//@Service
//public class DemographicDAOService{
//
//    @Autowired
//    private DemographicRepository demographicRepository;
//
//    public List<Demographic> getAllDemographics(){
//
//       List<Demographic> demographics = new ArrayList<>();
//
//       demographicRepository.findAll().forEach(demographics::add);
//
//       return demographics;
//    }
//
//    public Optional<Demographic> getDemographicById(Integer Id){
//        return demographicRepository.findById(Id);
//    }
//
//    public void addDemographic(Demographic demographic){
//        demographicRepository.save(demographic);
//    }
//
//    public void updateDemographic(Demographic demographic){
//        demographicRepository.save(demographic);
//    }
//
//    public void deleteDemographic(Demographic demographic){
//        demographicRepository.delete(demographic);
//    }
//
//    public void deleteDemographicById(Integer Id){
//        demographicRepository.deleteById(Id);
//    }
//
//    // public Boolean demographicExistById(Integer Id){
//    //    return demographicRepository.existById(Id);
//    // }
//
//    public Long numberDemographicEntities(){
//        return demographicRepository.count();
//    }
//}