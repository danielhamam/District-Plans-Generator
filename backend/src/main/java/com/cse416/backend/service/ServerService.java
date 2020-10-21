package com.cse416.backend.service;


import com.cse416.backend.dao.ServerFakeDataAccessObject;
import com.cse416.backend.model.Batch;
import com.cse416.backend.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ServerService {

    private final ServerFakeDataAccessObject fake;
    //private final Algorithm algorithm
    //private final SeawulfAdapater seawulfAdapater

    @Autowired
    public ServerService(@Qualifier("fakeDao") ServerFakeDataAccessObject fake) {

        this.fake = fake;
    }


    public String index() {

        return "Greetings from Spring Boot!";
    }

    public State getState(String stateAbberivation){
        State state = fake.queryGetStateByAbberivation(stateAbberivation);
        return state;
    }


    public void generateStateBatch(Batch batch, String stateAbberivation){
        //should we include multithreadingtSb
        boolean isAlgorithmLocal = true;

        if(batch.getnumberOfDistricting() > 10){
            isAlgorithmLocal = false;
        }

        if(isAlgorithmLocal){
            localAlgorithmDeployment(batch);
        }
        else{
            seawulfAlgorithmDeployment();
        }
        State state = this.getState(stateAbberivation);
        saveBatch(state, batch);
    }

    public Batch localAlgorithmDeployment(Batch batch){
        boolean wasGeneratingSuccesful = false;


        return null;

    }

    public void seawulfAlgorithmDeployment(){

    }

    public int saveBatch(State state, Batch batch){
        fake.muatationSaveBatch(state, batch);
        return 0;
    }



}
