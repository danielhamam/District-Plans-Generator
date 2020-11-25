package com.cse416.backend.dao.services;
import com.cse416.backend.dao.repositories.CensusEthnicityRepository;
import org.springframework.stereotype.*;

import com.cse416.backend.model.demographic.CensusEthnicity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;



@Service
public class CensusEthnicityDAOService{

   @Autowired
   private CensusEthnicityRepository censusEthnicityRepository;

   public List<CensusEthnicity> getAllEthnicities(){

      List<CensusEthnicity> ethnicities = new ArrayList<>();

      censusEthnicityRepository.findAll().forEach(ethnicities::add);

      return ethnicities;
   }

   public Optional<CensusEthnicity> getCensusEthnicityById(String Id){
       return censusEthnicityRepository.findById(Id);
   }

   public void addCensusEthnicity(CensusEthnicity censusEthnicity){
       censusEthnicityRepository.save(censusEthnicity);
   }

   public void updateCensusEthnicity(CensusEthnicity censusEthnicity){
       censusEthnicityRepository.save(censusEthnicity);
   }

   public void deleteCensusEthnicity(CensusEthnicity censusEthnicity){
       censusEthnicityRepository.delete(censusEthnicity);
   }

   public void deleteCensusEthnicityById(String Id){
       censusEthnicityRepository.deleteById(Id);
   }

   public Long numberCensusEthnicityEntities(){
       return censusEthnicityRepository.count();
   }
}