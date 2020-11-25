
package com.cse416.backend.dao.services;
import com.cse416.backend.dao.repositories.StateRepository;
import org.springframework.stereotype.*;

import com.cse416.backend.model.regions.state.State;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.lang.Integer;



@Service
public class StateDAOService{

   @Autowired
   private StateRepository stateRepository;

   public List<State> getAllStates(){

      List<State> states = new ArrayList<>();

      stateRepository.findAll().forEach(states::add);

      return states;
   }

   public Optional<State> getStateById(String Id){
       return stateRepository.findById(Id);
   }

   public State getStateByName(String name){
        return stateRepository.findByName(name);
    }

    public State getStateByFIPSCode(Integer stateFIPSCode){
        return stateRepository.findByFIPSCode(stateFIPSCode);
    }

   public void addState(State state){
       stateRepository.save(state);
   }

   public void updateState(State state){
       stateRepository.save(state);
   }

   public void deleteState(State state){
       stateRepository.delete(state);
   }

   public void deleteStateById(String Id){
       stateRepository.deleteById(Id);
   }

   public Long numberStateEntities(){
       return stateRepository.count();
   }
}