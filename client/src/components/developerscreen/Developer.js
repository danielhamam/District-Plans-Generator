import React, {useEffect} from "react"
import * as endpoint from '../../endpoint/Client';

const Dev = () =>{

    async function generateJobHandler(){
        let newBatch =  {
            jobName : "Batch", // name of the job
            districtsAmount : 10, // # district 
            plansAmount : 1000, // # district plans
            compactness : "LOW", // compactness (string can be low, intermediate or high)
            populationDifference : 1.2, // population difference varies from 0-1.7%
            minorityFocus : "HISPANIC_AMERICAN"
        }
        let res = await endpoint.generateJob(newBatch);
        console.log(res);
    }

    async function getStateHandler(){
    let getState =  {
        state: "NY"
    }
    let res = await endpoint.getState(getState);
    console.log(res);
    }



    return(
        <div>
            <h1> Hello Devloper </h1>

            <button onClick={getStateHandler}> Get State </button>
            <button onClick={generateJobHandler}> Generate Job</button>

        </div>
    );
}

export default Dev;