const SERVER_ORIGIN  = "localhost:8080" ;
const URL = "http://" + SERVER_ORIGIN;
const SERVER_PATHS = {
    CONNECTING: "/connect",
    STATE: "/state",
    BOUNDARIES: "/boundaries",
    PRECINCTS: "/precincts",
    PRECINCT_DEMOGRAPHIC: "/precinct/demographic",	
    JOB: "/job",
    PLAN: "/plan",
    BOXWHISKER: "/boxwhisker",
    DELETE:"/delete",
    CANCEL:"/cancel",
    GENERATE_JOB: "/generate",
    GENERATE_HEATMAP: "/heatmap",
}
const myHeaders = new Headers();
myHeaders.append("Content-Type", "application/json");
myHeaders.append("Access-Control-Allow-Origin", "*");

/** 
* @param method HTTP Request Method 
* @param data HTTP body
* @returns  An object containing any custom settings that you want to apply to the fetch() request.
*
**/
function createFetchOptions(method, data){
    let requestOptions;
    if (method == "GET"){
        requestOptions = {
            method: method,
            mode: 'cors',
            headers: myHeaders,
        }
    }
    else{
        let body = JSON.stringify(data)
        requestOptions = {
            method: method,
            mode: 'cors',
            headers: myHeaders,
            body: body,
        }
    }
    console.log(requestOptions)
    return requestOptions
}

export async function connectToServer(){
    console.log("Connecting to Server");
    const requestOptions = createFetchOptions('GET');
    const NEW_URL = URL + SERVER_PATHS.CONNECTING;
    const response = fetch(NEW_URL, requestOptions).catch(error =>  error);
    return response; 
}

/** 
* @param path The desired state a user wants. 
**/
export async function getState(data){
    console.log("Gettting State");
    const requestOptions = createFetchOptions('GET');
    const NEW_URL = URL + SERVER_PATHS.STATE + "/" + data.state;
    const response = await fetch(NEW_URL, requestOptions).catch(error => error);
    return await response.json()
}

export async function getStatePrecincts(){ 
	    console.log("Gettting State Precincts"); 
	    const requestOptions = createFetchOptions('GET'); 
	    const NEW_URL = URL + SERVER_PATHS.PRECINCTS; 
	    const response = await fetch(NEW_URL, requestOptions).catch(error => error); 
	    return await response.json() 
} 

export async function getPrecinctDemographic(precinctObj){
	    console.log("Gettting Precinct's Demographic"); 
	    const requestOptions = createFetchOptions('GET'); 
	    const NEW_URL = URL + SERVER_PATHS.PRECINCT_DEMOGRAPHIC + "/" + precinctObj.name; 
	    const response = await fetch(NEW_URL, requestOptions).catch(error => error); 
	    return await response.json() 
}


export async function getBoundaries(path){

}

export async function getJob(data){
    console.log("Getting Job");
    const requestOptions = createFetchOptions('GET');
    const NEW_URL = URL + SERVER_PATHS.JOB + "/" + data.job.id;
    const response = fetch(NEW_URL, requestOptions).catch(error =>  error);
    return response; 
}
export async function getPlan(data){
    console.log("Getting Plan");
    const requestOptions = createFetchOptions('GET');
    const NEW_URL = URL + SERVER_PATHS.JOB + "/" + data.job.id + "/" + data.plan.id
    const response = fetch(NEW_URL, requestOptions).catch(error =>  error);
    return response; 
}
export async function getPlanGraph(data){
    console.log("Getting Box n Whisker For Plan");
    const requestOptions = createFetchOptions('GET');
    const NEW_URL = URL + SERVER_PATHS.BOXWHISKER + "/" + data.job.id + "/" + data.plan.id
    const response = fetch(NEW_URL, requestOptions).catch(error =>  error);
    return response; 
}
export async function cancelJob(data){
    console.log("Cancel");
    const requestOptions = createFetchOptions('PUT');
    const NEW_URL = URL + SERVER_PATHS.BOXWHISKER + "/" + data.job.id + "/" + data.plan.id
    const response = fetch(NEW_URL, requestOptions).catch(error =>  error);
    return response; 
}

export async function deleteJob(job){
    console.log("Delete");
    const requestOptions = createFetchOptions('DELETE');
    const NEW_URL = URL + SERVER_PATHS.DELETE + "/" + job.jobID
    const response = fetch(NEW_URL, requestOptions).catch(error =>  error);
    return response;
}

export async function generateJob(data){
    console.log("Generating Job");
    const requestOptions = createFetchOptions('POST', data);
    const NEW_URL = URL + SERVER_PATHS.GENERATE_JOB;
    const response = await fetch(NEW_URL, requestOptions).catch(error => error);
    return await response.json()
}

export async function generateHeatMap(data){
    console.log("Generating Job");
    const requestOptions = createFetchOptions('POST', data);
    const NEW_URL = URL + SERVER_PATHS.GENERATE_HEATMAP;
    const response = await fetch(NEW_URL, requestOptions).catch(error => error);
    return await response.json()

}
