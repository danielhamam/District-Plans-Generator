import React, {useEffect} from "react"
import * as endpoint from '../../endpoint/Client';

const Dev = () =>{

    const generateJobHandler = () =>{
        let newBatch =  {
            "numberOfDistricting" : 10,
            "name": "batch1",
            "isAvailable": false,
            "populationDifference": 10.0,
            "compactness": 10.0,
            "state": "NY"
        }
        let res = endpoint.generateJob(newBatch)
      }

    async function getStateHandler(){
    let getState =  {
        state: "NY"
    }
    let res = await endpoint.getState(getState);
    console.log(res)
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