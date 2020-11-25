// package com.cse416.backend.model.job.minoritygroup;

// import javax.persistence.*;

// @Entity
// @Table(name = "JobMinorityGroups")
// public class MinorityGroup {

//     @EmbeddedId
//     private MinorityGroupIdentity minorityGroupIdentity;

//     protected MinorityGroup(){}

//     public MinorityGroup(MinorityGroupIdentity minorityGroupIdentity){
//         this.minorityGroupIdentity = minorityGroupIdentity;
//     }

//     public void setMinorityGroupIdentity(MinorityGroupIdentity minorityGroupIdentity){
//         this.minorityGroupIdentity = minorityGroupIdentity;
//     }

//     public MinorityGroupIdentity getMinorityGroupIdentity(){return this.minorityGroupIdentity;}

// }