// package com.cse416.backend.model.regions.precinct;

// import javax.persistence.*;

// @Entity
// @Table(name = "PrecinctNeighbors")
// public class PrecinctNeighbor {

//     @EmbeddedId
//     private PrecinctNeighborIdentity precinctNeighborIdentity;

//     protected PrecinctNeighbor(){}

//     public PrecinctNeighbor(PrecinctNeighborIdentity precinctNeighborIdentity){
//         this.precinctNeighborIdentity = precinctNeighborIdentity;
//     }

//     public void setPrecinctNeighborIdentity(PrecinctNeighborIdentity precinctNeighborIdentity){
//         this.precinctNeighborIdentity = precinctNeighborIdentity;
//     }

//     public PrecinctNeighborIdentity getPrecinctNeighborIdentity(){return this.precinctNeighborIdentity;}

// }