import HomeScreen from "./components/homePage/HomeScreen";
import DeveloperScreen from "./components/developerscreen/Developer"
import React, { Component } from "react";
import { BrowserRouter, Route, Redirect } from "react-router-dom";
import Switch from "react-bootstrap/esm/Switch";
import * as endpoint from './endpoint/Client';
import testJobCards from './json/TestJobCards.json'
import './css/project_styles.css';

// App.js is the parent component
class App extends Component {
    state = {

      // State:
      currentState : "Select a state",
      enactedPlan : testJobCards.enactedPlan,

      // Jobs:
      jobCards : testJobCards.jobs, // holds all the jobs retrieved back from the serverside (UPDATED BY JSON)
      currentJob : "",

      // Map View Filters:
      selectedFilters : null, // current demographic filters
      precinctView : false, // show precincts
      districtView : false, // show districts
      stateView : true, // show stateView

      // Checks for Selection
      selectedPlanCheck: false,
      selectedJobCheck: false

      // selectedJobName : "", // take name of selected district
      // selectedPlanName : "", // take name of selected district
      // todoLists: testTodoListData.todoLists, // Portion of my code taken from CSE 316
      
    }

   /**
   * This function updates which state is selected, and fetches data pertaining to the state from the server.
   * 
   * @param {String} stateName The state to now be shown to the user
   * 
   * In addition to changing the state name, the function takes updates "Your Jobs", which hold the jobs
   * belonging to the selected state (updates the array), and updates STATE DETAILS
   * 
   */
  changeCurrentState = async (stateName) => {
    let getState =  {
      state: stateName
    }
    try {
      let res = await endpoint.getState(getState);
      console.log(res)
      this.setState({currentState : stateName});
      // this.setState({ jobCards : ____}); // update the jobCards in state
    } catch (exception) {
      console.error(exception);
    }
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
  createJob = async (userInputs) => { // userInputs is an OBJECT of the constraints user selected. Let's gather them here. 
      
      let res = await endpoint.generateJob(userInputs); // use of .then here? or keep that in client.js for fetch?
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
   * @param {String} job Represents the COMPLETED job object to be deleted.
   * 
   */
  deleteJob = (job) => { // string
    // REMOVE IT IF IT EXISTS
    let indexOfJob = this.state.jobCards.indexOf(job);
    if (indexOfJob >= 0)
        this.state.jobCards.splice(indexOfJob, 1);
    this.setState({ jobCards : this.state.jobCards})
  }

   /**
   * This function DELETES a plan from the currently selected job
   * 
   * @param {String} plan Represents the plan object to be deleted.
   * 
   * Job can either be none (enacted plan) or currentJob (average, random, extreme)
   * 
   */
  deletePlan = (plan) => { // string

    // What plan is it?
    if (this.state.currentJob != "" && plan.type != "Enacted Plan") {
      let job = this.state.currentJob;
      let indexOfJob = this.state.jobCards.indexOf(job);
      let indexOfPlan = this.state.jobCards[indexOfJob].districtPlans.indexOf(plan);
      if (indexOfPlan >= 0)
        this.state.jobCards[indexOfJob].districtPlans.splice(indexOfPlan, 1);
      this.setState({ jobCards : this.state.jobCards})
    }
    // else, we are deleting the enacted plan
    // this.state.jobCards.enactedPlan.splice(indexOfPlan, 1);
    
    this.setState({ jobCards : this.state.jobCards})

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

updateCurrentJob = (job, selected) => {
  if (selected == true) { // job just got selected
    this.setState({currentJob : job});
    this.setState({currentJobName : job.jobName});
  }
  else { // job just got de-selected
    this.setState({currentJob : ""});
    this.setState({currentJobName : ""});
  }
}

  getSelectedJob = () => {
    // returns selected job
    // .map and .filter use
    // call in your districting plans and do .districtPlans from job's database for plans
  }
  
  toggleSelectedCard = () => {
    if (this.state.selectedJobCheck == false) this.setState({selectedJobCheck : true});
    else this.setState({selectedJobCheck : false});
}

  toggleSelectedPlanCheck = () => {
    if (this.state.selectedPlanCheck == false) this.setState({selectedPlanCheck: true});
    else this.setState({selectedPlanCheck : false});
}

  render() {
  return (
    <div >

            <HomeScreen 
            jobCards={this.state.jobCards} currentState={this.state.currentState} changeSelectedFilters={this.changeSelectedFilters} changeCurrentState={this.changeCurrentState} 
            currentJob ={this.state.currentJob} updateCurrentJob={this.updateCurrentJob} selectedPlanCheck={this.state.selectedPlanCheck} 
            toggleSelectedPlanCheck={this.toggleSelectedPlanCheck} selectedJobCheck={this.state.selectedJobCheck} toggleSelectedCard={this.toggleSelectedCard}
            enactedPlan = {this.state.enactedPlan} deleteJob={this.deleteJob} deletePlan={this.deletePlan} createJob={this.createJob}
            />

            <DeveloperScreen/>            

      </div>
    );
  }
}

export default App;
