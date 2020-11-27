
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

   public State getStateById(String Id){
       return stateRepository.findByStateId(Id);
   }

   public State getStateByName(String name){
        return stateRepository.findBystateName(name);
    }

    public State getStateByFIPSCode(Integer stateFIPSCode){
        return stateRepository.findBystateFIPSCode(stateFIPSCode);
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

   public Long numberStateEntities(){
       return stateRepository.count();
   }
}