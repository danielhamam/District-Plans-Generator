
package com.cse416.backend.dao.services;
import com.cse416.backend.dao.repositories.CountyRepository;
import org.springframework.stereotype.*;

import com.cse416.backend.model.regions.county.County;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.Integer;
import java.util.*;



@Service
public class CountyDAOService{

   @Autowired
   private CountyRepository countyRepository;

    public List<County> getCountiesByDistrictId(Integer districtId){
        return countyRepository.findByDistrictId(districtId);
    }

    public List<County> getCountiesByStateId(String stateAbbrevation){
        return countyRepository.findByStateId(stateAbbrevation);
    }

    public County getCountyByFIPSCode(Integer id){
        return countyRepository.findByCountyFIPSCode(id);
    }

    public County getCountyByName(String name){
        return countyRepository.findByCountyName(name);
    }

   public Optional<County> getCountyById(Integer id){
       return countyRepository.findById(id);
   }

   public void addCounty(County county){
       countyRepository.save(county);
   }

   public void updateCounty(County county){
       countyRepository.save(county);
   }

   public void deleteCounty(County county){
       countyRepository.delete(county);
   }

   public void deleteCountyById(Integer Id){
       countyRepository.deleteById(Id);
   }

   public Long numberCountyEntities(){
       return countyRepository.count();
   }
}