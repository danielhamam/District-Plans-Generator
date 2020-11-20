
// package com.cse416.backend.dao.services;
// import com.cse416.backend.dao.repositories.DistrictRepository;
// import org.springframework.stereotype.*;

// import com.cse416.backend.model.regions.district.District;
// import org.springframework.beans.factory.annotation.Autowired;

// import java.util.ArrayList;
// import java.util.List;
// import java.lang.Integer;

// import java.util.Optional;



// @Service
// public class DistrictDAOService{

//    @Autowired
//    private DistrictRepository districtRepository;

//    public List<District> getAllDistricts(){

//       List<District> districts = new ArrayList<>();

//       districtRepository.findAll().forEach(districts::add);

//       return districts;
//    }

//    public Optional<District> getDistrictById(Integer Id){
//        return districtRepository.findById(Id);
//    }

//    public void addDistrict(District district){
//        districtRepository.save(district);
//    }

//    public void updateDistrict(District district){
//        districtRepository.save(district);
//    }

//    public void deleteDistrict(District district){
//        districtRepository.delete(district);
//    }

//    public void deleteDistrictById(Integer Id){
//        districtRepository.deleteById(Id);
//    }

//    public Long numberDistrictEntities(){
//        return districtRepository.count();
//    }
// }