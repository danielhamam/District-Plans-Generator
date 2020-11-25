
package com.cse416.backend.dao.services;
import com.cse416.backend.dao.repositories.PrecinctRepository;
import org.springframework.stereotype.*;

import com.cse416.backend.model.regions.precinct.Precinct;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;
import java.lang.Integer;


@Service
public class PrecinctDAOService{

   @Autowired
   private PrecinctRepository precinctRepository;

   public Optional<Precinct> getPrecinctById(Integer Id){
       return precinctRepository.findById(Id);
   }

   public Precinct getPrecinctByName(String name){
        return precinctRepository.findByName(name);
    }

    public Precinct getPrecinctByFIPSCode(String precinctFIPSCode){
        return precinctRepository.findByFIPSCode(precinctFIPSCode);
    }

    public List<Precinct> getPrecinctsByStateId(String stateAbbreviation){
        return precinctRepository.findByStateId(stateAbbreviation);
    }

    public List<Precinct> getPrecinctsByDistrictId(Integer districtId){
        return precinctRepository.findByDistrictId(districtId);
    }

    public List<Precinct> getPrecinctsByCountyId(Integer countyId){
        return precinctRepository.findByCountyId(countyId);
    }

   public void addPrecinct(Precinct precinct){
       precinctRepository.save(precinct);
   }

   public void updatePrecinct(Precinct precinct){
       precinctRepository.save(precinct);
   }

   public void deletePrecinct(Precinct precinct){
       precinctRepository.delete(precinct);
   }

   public void deletePrecinctById(Integer Id){
       precinctRepository.deleteById(Id);
   }

   public Long numberPrecinctEntities(){
       return precinctRepository.count();
   }
}