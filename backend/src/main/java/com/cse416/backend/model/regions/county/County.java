package com.cse416.backend.model.regions.county;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.cse416.backend.model.regions.precinct.*;
import com.cse416.backend.model.regions.district.*;
import com.cse416.backend.model.regions.state.*;
import com.cse416.backend.model.demographic.*;


import java.lang.*;
import java.util.*;
import javax.persistence.*;



@Entity
@Table(name="Counties")
public class County {

    @Id
    @GeneratedValue
    private Integer countyId;

    private String countyName;

    private Integer countyFIPSCode;

    @ManyToOne(targetEntity=District.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="districtId")
    private District district;

    @ManyToOne(targetEntity=State.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="stateId")
    private State state;

    @Column(name = "numberOfPrecincts")
    private int numOfPrecincts;

    @JsonIgnore 
    @OneToMany(targetEntity=Precinct.class,cascade = CascadeType.ALL, 
    fetch = FetchType.LAZY, orphanRemoval = true,  mappedBy ="county")
    private List<Precinct> precincts;

    @Transient
    private Demographic demographic;

    //Neccessary for JPA
    protected County (){}

    public Integer getId(){ return countyId;}

    public String getName() {
        return countyName;
    }

    public Integer getFIPSCode() {
        return countyFIPSCode;
    }

    public List<Precinct> getPrecincts() {
        return precincts;
    }

    public int getNumOfPrecincts() {
        return numOfPrecincts;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (! (obj instanceof County)) {
            return false;
        }
        County county = ((County) obj);
        Integer countyFIPS = county.getFIPSCode();
        return countyFIPS.equals(countyFIPSCode);
    }


//    //Idea from effective Java : Item 9
//    @Override
//    public int hashCode() {
//        int result = 17;
//        result = 31 * result + countyFIPSCode.hashCode();
//        result = 31 * result + countyId.hashCode();
//        result = 31 * result + district.hashCode();
//        result = 31 * result + state.hashCode();
//        return result;
//    }

    @Override
    public String toString(){
        return "County Name " + getName() + " FIPSCode : " + getFIPSCode();
    }

}
