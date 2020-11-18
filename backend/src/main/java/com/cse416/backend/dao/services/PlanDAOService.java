
package com.cse416.backend.dao.services;
import com.cse416.backend.dao.repositories.PlanRepository;
import org.springframework.stereotype.*;

import com.cse416.backend.model.Plan;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.lang.Integer;

import java.util.Optional;


@Service
public class PlanDAOService{

   @Autowired
   private PlanRepository planRepository;

   public List<Plan> getAllPlans(){

      List<Plan> plans = new ArrayList<>();

      planRepository.findAll().forEach(plans::add);

      return plans;
   }

   public Optional<Plan> getPlanById(Integer Id){
       return planRepository.findById(Id);
   }

   public void addPlan(Plan plan){
       planRepository.save(plan);
   }

   public void updatePlan(Plan plan){
       planRepository.save(plan);
   }

   public void deletePlan(Plan plan){
       planRepository.delete(plan);
   }

   public void deletePlanById(Integer Id){
       planRepository.deleteById(Id);
   }

   // public Boolean planExistById(Integer Id){
   //    return planRepository.existById(Id);
   // }

   public Long numberPlanEntities(){
       return planRepository.count();
   }
}