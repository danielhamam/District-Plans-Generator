
package com.cse416.backend.dao.services;
import com.cse416.backend.dao.repositories.DistrictRepository;
import org.springframework.stereotype.*;

import com.cse416.backend.model.regions.district.District;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;
import java.lang.Integer;




@Service
public class DistrictDAOService{

   @Autowired
   private DistrictRepository districtRepository;

   public Optional<District> getDistrictById(Integer Id){
       return districtRepository.findById(Id);
   }

   public District getDistrictByStateIdAndDistrictNumber(String stateAbbrevation, Integer districtNumber){
        return districtRepository.findByStateIdAndDistrictNumber(stateAbbrevation, districtNumber);
   }

   public List<District> getDistrictsByState(String stateAbbrevation){
       return districtRepository.findByStateId(stateAbbrevation);
   }

   public void addDistrict(District district){
       districtRepository.save(district);
   }

   public void updateDistrict(District district){
       districtRepository.save(district);
   }

   public void deleteDistrict(District district){
       districtRepository.delete(district);
   }

   public void deleteDistrictById(Integer Id){
       districtRepository.deleteById(Id);
   }

   public Long numberDistrictEntities(){
       return districtRepository.count();
   }
}