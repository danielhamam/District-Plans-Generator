import HomeScreen from "./components/homePage/HomeScreen";
import DeveloperScreen from "./components/developerscreen/Dev"
import React, { Component } from "react";
import { BrowserRouter, Route, Redirect } from "react-router-dom";
import Switch from "react-bootstrap/esm/Switch";
import {
  generateJob,
  getState
} from './endpoint/Client';
import './css/project_styles.css';

// App.js is the parent component
class App extends Component {
    state = {

      // State:
      currentState : "Select a state",

      // Jobs:
      jobCards : [], // holds all the jobs retrieved back from the serverside (UPDATED BY JSON)
      currentJobName : "No Job Selected: ", // name of the currently selected job
      selectedJobCheck: false, // see if there is a job currently selected

      // Map View Filters:
      selectedFilters : null, // current demographic filters
      precinctView : false, // show precincts
      districtView : false, // show districts
      stateView : true, // show stateView

      // District Plans
      selectedPlanCheck: false,

      // selectedJobName : "", // take name of selected district
      // selectedPlanName : "", // take name of selected district
      // todoLists: testTodoListData.todoLists, // Portion of my code taken from CSE 316
      
    }

   /**
   * This function creates a new job and calls generateJob in the Client.js endpoint
   * 
   * @param {Array} userInputs Holds the inputs entered by the user to generate a new job
   * 
   * Inputs: Districts (int), Districting Plans (int), Compactness (String), Population Difference (int (%)), 
   * Minority Focus Group(s) (Array<String>) , Job Name (String)
   *
   * the event occurs.
   */
  createJob = (userInputs) => {
      // New batch is a test 
      let newBatch =  {
        "numberOfDistricting" : 10,
        "name": "batch1",
        "isAvailable": false,
        "populationDifference": 10.0,
        "compactness": 10.0,
        "state": "NY"
    }
      let res = generateJob(newBatch);
  }

   /**
   * This function CANCELS a job by sending the job ID to backend, receives "successfully cancelled" status back.
   * 
   * @param {String} jobID Represents the ID of the COMPLETED job to be cancelled.
   * 
   */
  cancelJob = (jobID) => { // string

  }
   /**
   * This function DELETES a job by sending the job ID to backend, receives "successfully deleted" status back.
   * 
   * @param {String} jobID Represents the ID of the COMPLETED job to be deleted.
   * 
   */
  deleteJob = (jobID) => { // string

  }

   /**
   * This function 
   * 
   * @param {String} minorityGroups Represents the minority or minorities, which were selected when generating the
   * currently selected job, in order to generate their voting age populations per district in the selected district plan
   * 
   */
  generateBoxWhiskerValues = (minorityGroups) => {
      // open questions: do I send plan ID? district plan? 
  }

   /**
   * This function updates the currently selected filters so as to change the view of the demographic heat map
   * 
   * @param {Array<String>} mapFilters Represents the map filters selected by the user, can be either 
   * cluster or demographic heat map. Multiple filters can be invoked at the same time. 
   * 
   */
  changeSelectedFilters = (mapFilters) => {
    // open questions: check if change for demographic heat map or for cluster? Separate functions?
      // does updates one at a time (but if you have two filters does it over again)
    this.setState({selectedFilters : mapFilters});
    for (var i = 0; i < mapFilters.length; i++) {
      console.log(mapFilters[i].label); // this is how you access the label of the array element at position i 
    }
  }

// --------------------------------------------------------------------------
// --------------------------------------------------------------------------
// --------------------------------------------------------------------------


  getSelectedJob = () => {
    // returns selected job
    // .map and .filter use
    // call in your districting plans and do .districtPlans from job's database for plans
  }
  
  toggleSelectedJobCheck = () => {
    if (this.state.selectedJobCheck == false) this.setState({selectedJobCheck : true});
    else this.setState({selectedJobCheck : false});
}

  toggleSelectedPlanCheck = () => {
    if (this.state.selectedPlanCheck == false) this.setState({selectedPlanCheck: true});
    else this.setState({selectedPlanCheck : false});
}

  updateCurrentJobName = (name) => {
    if (name == "") this.setState({currentJobName : "No Job Selected: "});
    else this.setState({currentJobName : name + ":"});
  }

  // Map Manipulation Functions
  changeCurrentState = (newName) => {
    this.setState({currentState : newName});
    // this.setState({ jobCards : ____}); // update the jobCards in state
  }

  render() {
  return (
    <div >

        {/* <BrowserRouter>
          <Switch>
            <Redirect exact from="/" to={{ pathname: "/home" }} />
            <Route path="/home"> */}
            <HomeScreen currentState={this.state.currentState} changeSelectedFilters={this.changeSelectedFilters} changeCurrentState={this.changeCurrentState} currentJobName ={this.state.currentJobName} updateCurrentJobName={this.updateCurrentJobName} selectedPlanCheck={this.state.selectedPlanCheck} toggleSelectedPlanCheck={this.toggleSelectedPlanCheck} selectedJobCheck={this.state.selectedJobCheck} toggleSelectedJobCheck={this.toggleSelectedJobCheck}/>
            {/* </Route> */}
            {/* <Route path="/dev"> */}
            <DeveloperScreen/>            
            {/* </Route> */}
          {/* </Switch> */}
        {/* // </BrowserRouter> */}
      </div>
    );
  }
}

export default App;
