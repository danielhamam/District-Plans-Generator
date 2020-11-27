package com.cse416.backend.dao.services;
import com.cse416.backend.dao.repositories.*;
import org.springframework.stereotype.*;

import com.cse416.backend.model.job.boxnwhisker.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;



@Service
public class BoxWhiskerDAOService{

   @Autowired
   private BoxWhiskerRepository boxWhiskerRepository;

   public List<BoxWhisker> getAllBoxWhiskers(){

      List<BoxWhisker> boxWhiskers = new ArrayList<>();

      boxWhiskerRepository.findAll().forEach(boxWhiskers::add);

      return boxWhiskers;
   }

   public Optional<BoxWhisker> getBoxWhiskerById(Integer Id){
       return boxWhiskerRepository.findById(Id);
   }

   public BoxWhisker getBoxWhiskerByJobId(Integer jobId){
       return boxWhiskerRepository.findBoxWhiskerByJobId(jobId);
   }

   public void addBoxWhisker(BoxWhisker boxWhisker){
       boxWhiskerRepository.save(boxWhisker);
   }

   public void updateBoxWhisker(BoxWhisker boxWhisker){
       boxWhiskerRepository.save(boxWhisker);
   }

   public void deleteBoxWhisker(BoxWhisker boxWhisker){
       boxWhiskerRepository.delete(boxWhisker);
   }

   public void deleteBoxWhiskerById(Integer Id){
       boxWhiskerRepository.deleteById(Id);
   }

   public Long numberBoxWhiskerEntities(){
       return boxWhiskerRepository.count();
   }
}