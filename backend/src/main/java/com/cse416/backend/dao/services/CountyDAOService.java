
// package com.cse416.backend.dao.services;
// import com.cse416.backend.dao.repositories.CountyRepository;
// import org.springframework.stereotype.*;

// import com.cse416.backend.model.regions.county.County;
// import org.springframework.beans.factory.annotation.Autowired;

// import java.util.ArrayList;
// import java.util.List;
// import java.lang.Integer;

// import java.util.Optional;



// @Service
// public class CountyDAOService{

//    @Autowired
//    private CountyRepository countyRepository;

//    public List<County> getAllCounties(){

//       List<County> counties = new ArrayList<>();

//       countyRepository.findAll().forEach(counties::add);

//       return counties;
//    }

//    public Optional<County> getCountyById(Integer Id){
//        return countyRepository.findById(Id);
//    }

//    public void addCounty(County county){
//        countyRepository.save(county);
//    }

//    public void updateCounty(County county){
//        countyRepository.save(county);
//    }

//    public void deleteCounty(County county){
//        countyRepository.delete(county);
//    }

//    public void deleteCountyById(Integer Id){
//        countyRepository.deleteById(Id);
//    }

//    public Long numberCountyEntities(){
//        return countyRepository.count();
//    }
// }