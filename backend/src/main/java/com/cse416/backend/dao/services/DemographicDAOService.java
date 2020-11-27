
package com.cse416.backend.dao.services;
import com.cse416.backend.dao.repositories.DemographicRepository;
import org.springframework.stereotype.*;

import com.cse416.backend.model.demographic.Demographic;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.lang.Integer;




@Service
public class DemographicDAOService{

   @Autowired
   private DemographicRepository demographicRepository;

   public Optional<Demographic> getDemographicById(Integer Id){
       return demographicRepository.findById(Id);
   }

   public Demographic getDemographicByPrecinctId(Integer precinctId){
       return demographicRepository.findByPrecinctId(precinctId);
   }

   public Demographic getDemographicByDistrictId(Integer districtId){
       return demographicRepository.findByDistrictId(districtId);
   }

   public Demographic getDemographicByStateId(String stateId){
       return demographicRepository.findByStateId(stateId);
   }

   public Demographic getDemographicByCountyId(Integer countyId){
       return demographicRepository.findByCountyId(countyId);
   }

   public void addDemographic(Demographic demographic){
       demographicRepository.save(demographic);
   }

   public void updateDemographic(Demographic demographic){
       demographicRepository.save(demographic);
   }

   public void deleteDemographic(Demographic demographic){
       demographicRepository.delete(demographic);
   }

   public void deleteDemographicById(Integer Id){
       demographicRepository.deleteById(Id);
   }

   public Long numberDemographicEntities(){
       return demographicRepository.count();
   }
}