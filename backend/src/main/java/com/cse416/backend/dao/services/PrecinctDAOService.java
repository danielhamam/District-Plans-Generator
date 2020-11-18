
package com.cse416.backend.dao.services;
import com.cse416.backend.dao.repositories.PrecinctRepository;
import org.springframework.stereotype.*;

import com.cse416.backend.model.regions.Precinct;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.lang.Integer;

import java.util.Optional;

@Service
public class PrecinctDAOService{

   @Autowired
   private PrecinctRepository precinctRepository;

   public List<Precinct> getAllPrecincts(){

      List<Precinct> precincts = new ArrayList<>();

      precinctRepository.findAll().forEach(precincts::add);

      return precincts;
   }

   public Optional<Precinct> getPrecinctById(Integer Id){
       return precinctRepository.findById(Id);
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

   // public Boolean precinctExistById(Integer Id){
   //    return precinctRepository.existById(Id);
   // }

   public Long numberPrecinctEntities(){
       return precinctRepository.count();
   }
}