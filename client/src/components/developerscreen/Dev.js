import React, {useEffect} from "react"
import {
    generateBatch,
    getState
 } from '../../endpoint/Client';



const Dev = () =>{

    const generateBatchHandler = () =>{
        let newBatch =  {
            "numberOfDistricting" : 10,
            "name": "batch1",
            "isAvailable": false,
            "populationDifference": 10.0,
            "compactness": 10.0,
            "state": "NY"
        }
        let res = generateBatch(newBatch)
      }

    //   const getStateHandler = () =>{
    //     let newBatch =  {
    //        "state": "NY"
    //     }

    //     getState(newBatch);
        
    //   }
    


    return(
        <div>
            <h1> Hello Devloper </h1>

            {/* <button onClick={getStateHandler}> Get State </button> */}
            <button onClick={generateBatchHandler}> Generate Batch</button>

        </div>
    );
}

export default Dev;