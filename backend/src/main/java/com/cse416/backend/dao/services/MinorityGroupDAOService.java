
// package com.cse416.backend.dao.services;
// import org.springframework.stereotype.*;
// import com.cse416.backend.dao.repositories.MinorityGroupRepository;

// import com.cse416.backend.model.job.minoritygroup.*;

// import org.springframework.beans.factory.annotation.Autowired;

// import java.util.*;
// import java.lang.Integer;



// @Service
// public class MinorityGroupDAOService{

//    @Autowired
//    private MinorityGroupRepository minorityGroupRepository;

//    public Optional<MinorityGroup> getMinorityGroupById(MinorityGroupIdentity Id){
//        return minorityGroupRepository.findById(Id);
//    }

//    public List<MinorityGroup> getMinorityGroupByShortenEthnicityName(String ethnicityName){
//        return minorityGroupRepository.findByEthnicityName(ethnicityName);
//    }

//    public List<MinorityGroup> getMinorityGroupByJobId(Integer jobId){
//        return minorityGroupRepository.findByJobId(jobId);
//    }

//    public void addMinorityGroup(MinorityGroup minorityGroup){
//        minorityGroupRepository.save(minorityGroup);
//    }

//    public void updateMinorityGroup(MinorityGroup minorityGroup){
//        minorityGroupRepository.save(minorityGroup);
//    }

//    public void deleteMinorityGroup(MinorityGroup minorityGroup){
//        minorityGroupRepository.delete(minorityGroup);
//    }

//    public void deleteMinorityGroupById(MinorityGroupIdentity Id){
//        minorityGroupRepository.deleteById(Id);
//    }

//    public Long numberMinorityGroupEntities(){
//        return minorityGroupRepository.count();
//    }
// }