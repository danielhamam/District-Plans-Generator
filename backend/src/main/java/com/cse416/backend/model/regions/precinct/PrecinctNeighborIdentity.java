// package com.cse416.backend.model.regions.precinct;

// import java.io.Serializable;

// import javax.persistence.*;

// @Embeddable
// public class PrecinctNeighborIdentity implements Serializable{
    
//     private Integer precinctId;

//     private Integer precinctNeighborId;

//     protected PrecinctNeighborIdentity(){}

//     public PrecinctNeighborIdentity(Integer precinctId, Integer precinctNeighborId){
//         this.precinctId = precinctId;
//         this.precinctNeighborId = precinctNeighborId;
//     }

//     public void setPrecinctId(Integer precinctId){ this.precinctId = precinctId;}

//     public void setPrecinctNeighborId(Integer precinctNeighborId){this.precinctNeighborId = precinctNeighborId;}

//     public Integer getPrecinctId(){return this.precinctId;}

//     public Integer getPrecinctNeighborId(){return this.precinctNeighborId;}

//     @Override
//     public boolean equals(Object object){

//         if (object == null) return false;
//         if(!(object instanceof PrecinctNeighborIdentity)) return false;
//         if(object == this) return true;

//         return (this.getPrecinctId() == ((PrecinctNeighborIdentity)object).getPrecinctId()) && (this.getPrecinctNeighborId() == ((PrecinctNeighborIdentity)object).getPrecinctNeighborId());
//     }

//     @Override 
//     public int hashCode(){return precinctId.hashCode() * precinctNeighborId.hashCode();}

// }