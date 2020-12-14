
package com.cse416.backend.dao.services;
import com.cse416.backend.dao.repositories.JobRepository;
import org.springframework.stereotype.*;

import com.cse416.backend.model.job.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.lang.Integer;


@Service
@Transactional
public class JobDAOService{

   @Autowired
   private JobRepository jobRepository;

   public List<Job> getAllJobs(){

      List<Job> jobs = new ArrayList<>();

      jobRepository.findAll().forEach(jobs::add);

      return jobs;
   }

   public Optional<Job> getJobById(Integer Id){
       return jobRepository.findById(Id);
   }

   public List<Job> getJobsByStateId(String stateAbbrevation){
        return jobRepository.findByStateId(stateAbbrevation);
   }

   public void addJob(Job job){
       jobRepository.save(job);
   }

   public void updateJob(Job job){
       jobRepository.save(job);
   }

   public void deleteJob(Job job){
       jobRepository.delete(job);
   }

   public void deleteJobById(Integer Id){
       jobRepository.deleteById(Id);
   }

   public Long numberJobEntities(){
       return jobRepository.count();
   }
}