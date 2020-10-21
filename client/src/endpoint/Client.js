const SERVER_ORIGIN  = "localhost:8080" 
const URL = "http://" + SERVER_ORIGIN;
const HOME_PATH = "/home"
const myHeaders = new Headers();
myHeaders.append("Content-Type", "application/json");
myHeaders.append("Access-Control-Allow-Origin", "*");


function createFetchOptions(method, data){
    let body = JSON.stringify(data)
    let requestOptions = {
        method: method,
        mode: 'cors',
        headers: myHeaders,
        body: body
    }
    console.log(requestOptions)
    return requestOptions
}

export const generateBatch = (data) =>{
    console.log("generateBatch")
    const requestOptions = createFetchOptions('POST', data);
    const NEW_URL = URL + HOME_PATH
    return fetch(NEW_URL, requestOptions)
    .then(response => response.text())
    .then(result => console.log(result))
    .catch(error => console.log('error is:', error));
}
  

export const getState = () => {
    // Default options are marked with *
    console.log("asdf");
    // return await fetch('http://localhost:8080/home',{ 
    //     method: 'get',
    //     headers: {
    //     'Accept': 'application/json, text/plain, */*',
    //     'Content-Type': 'application/json',
    //     },
    //     credentials: 'same-origin',
    //     body: JSON.stringify(data) // body data type must match "Content-Type" header
    // }).then(res => res.json());
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Access-Control-Allow-Origin", "*");

    var raw = JSON.stringify({"state":"NY"});

    var requestOptions = {
    method: 'GET',
    headers: myHeaders,
    redirect: 'follow',
    mode: 'cors',
    
    };

    const a = fetch("http://localhost:8080/home", requestOptions)
    .then(response => response.text())
    .then(result => console.log(result))
    .catch(error => console.log('error and here is it:', error));


  }

