package com.cse416.backend.dao.services;
import com.cse416.backend.dao.repositories.*;
import org.springframework.stereotype.*;

import com.cse416.backend.model.job.boxnwhisker.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;



@Service
public class BoxWhiskerPlotDAOService{

   @Autowired
   private BoxWhiskerPlotRepository boxWhiskerPlotRepository;

   public List<BoxWhiskerPlot> getAllBoxWhiskerPlots(){

      List<BoxWhiskerPlot> boxWhiskerPlots = new ArrayList<>();

      boxWhiskerPlotRepository.findAll().forEach(boxWhiskerPlots::add);

      return boxWhiskerPlots;
   }

   public Optional<BoxWhiskerPlot> getBoxWhiskerPlotById(Integer Id){
       return boxWhiskerPlotRepository.findById(Id);
   }

   public List<BoxWhiskerPlot> getBoxWhiskerPlotByBoxWhiskerId(Integer boxWhiskerId){
       return boxWhiskerPlotRepository.findPlotsByBoxWhiskerId(boxWhiskerId);
   }

   public void addBoxWhiskerPlot(BoxWhiskerPlot boxWhiskerPlot){
       boxWhiskerPlotRepository.save(boxWhiskerPlot);
   }

   public void updateBoxWhiskerPlot(BoxWhiskerPlot boxWhiskerPlot){
       boxWhiskerPlotRepository.save(boxWhiskerPlot);
   }

   public void deleteBoxWhiskerPlot(BoxWhiskerPlot boxWhiskerPlot){
       boxWhiskerPlotRepository.delete(boxWhiskerPlot);
   }

   public void deleteBoxWhiskerPlotById(Integer Id){
       boxWhiskerPlotRepository.deleteById(Id);
   }

   public Long numberBoxWhiskerPlotEntities(){
       return boxWhiskerPlotRepository.count();
   }
}