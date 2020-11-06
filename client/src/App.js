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

      // Modals


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
    // let getState =  {
    //   state: stateName
    // }
    // try {
    //   let res = await endpoint.getState(getState);
    //   console.log(res)
      this.setState({currentState : stateName});
      // this.setState({ jobCards : ____}); // update the jobCards in state
    // } catch (exception) {
    //   console.error(exception);
    // }
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
   * This function CANCELS a job by sending the job object to the server, receives "successfully cancelled" status back.
   * 
   * @param {String} job Represents the object of the PENDING job to be cancelled.
   * 
   */
  cancelJob = (job) => { // string
    // Remove job from jobCards
    let indexOfJob = this.state.jobCards.indexOf(job);
    if (indexOfJob >= 0)
        this.state.jobCards.splice(indexOfJob, 1);
    this.setState({ jobCards : this.state.jobCards})
  }
   /**
   * This function DELETES a job by sending the job object to the server, receives "successfully deleted" status back.
   * 
   * @param {String} job Represents the object of the DELETED job to be cancelled.
   * 
   */
  deleteJob = (job) => { // string
    // Remove job from jobCards
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
   * NOTE: This isn't a required use case. This won't actually delete a plan, but would get rid 
   * of current view of it. It will re-appear if the webpage is refreshed. 
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
   * Sending: {currentJob} which stands for the currently selected job by the user. This is the job the user wants to view
   * the box and whisker plot for, and would like to compare to enacted plan.
   * 
   * Note: Through the job object, we are telling the server the focus minority or minorities analyzed, which were 
   * selected when generating the currently selected job. This is to generate their voting age population(s) per indexed 
   * district in the selected district plan
   * 
   */
  generateBoxWhiskerValues = () => {
    console.log("sending this:");
    console.log(this.state.currentJob)
    try {
      // let res = await endpoint.getState(currentJob);
      // console.log(res)
    } catch (exception) {
      console.error(exception);
    }
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

   /**
   * This function updates the currentJob state variable to the user-selected job 
   * 
   * @param {JobCard} Job Represents the object of the SELECTED job by the user.
   * 
   */
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


// --------------------------------------------------------------------------
// --------------------------------------------------------------------------
// --------------------------------------------------------------------------
  
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
            enactedPlan = {this.state.enactedPlan} deleteJob={this.deleteJob} deletePlan={this.deletePlan} createJob={this.createJob} cancelJob={this.cancelJob}
            generateBoxWhiskerValues={this.generateBoxWhiskerValues}
            />

            <DeveloperScreen/>            

      </div>
    );
  }
}

export default App;
