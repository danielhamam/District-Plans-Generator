// package com.cse416.backend.model.job.minoritygroup;

// import java.io.Serializable;

// import javax.persistence.*;

// @Embeddable
// public class MinorityGroupIdentity implements Serializable{
    
//     private String censusEthnicityId;

//     private Integer jobId;

//     protected MinorityGroupIdentity(){}

//     public MinorityGroupIdentity(String ethnicityName, Integer jobId){
//         this.censusEthnicityId = ethnicityName;
//         this.jobId = jobId;
//     }

//     public void setMinorityGroupId(String ethnicityName){ this.censusEthnicityId = ethnicityName;}

//     public void setJobId(Integer jobId){this.jobId = jobId;}

//     public String getMinorityGroupId(){return this.censusEthnicityId;}

//     public Integer getJobId(){return this.jobId;}

//     @Override
//     public boolean equals(Object object){

//         if (object == null) return false;
//         if(!(object instanceof MinorityGroupIdentity)) return false;
//         if(object == this) return true;

//         return (this.getJobId() == ((MinorityGroupIdentity)object).getJobId()) && (this.getMinorityGroupId() == ((MinorityGroupIdentity)object).getMinorityGroupId());
//     }

//     @Override 
//     public int hashCode(){return censusEthnicityId.hashCode() * jobId.hashCode();}


// }