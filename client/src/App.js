import HomeScreen from "./components/homePage/HomeScreen";
import DeveloperScreen from "./components/developerscreen/Developer"
import React, { Component } from "react";
import { BrowserRouter, Route, Redirect } from "react-router-dom";
import Switch from "react-bootstrap/esm/Switch";
import * as endpoint from './endpoint/Client';
import testJobCards from './json/TestJobCards.json'
import './css/project_styles.css';

// Map GeoJSONS
import {GeoJSON} from 'react-leaflet';

  // NEW YORK:
import NYDistricts from './json/NEW_YORK/NewYorkDistricts.json';
import NYPrecincts from './json/NEW_YORK/NewYorkPrecincts.json';
// import NYDistricts from './json/Preprocessed_Data/ny_districts.json'

// App.js is the parent component
class App extends Component {
    state = {

      // State:
      currentState : "Select a state",
      enactedPlan : testJobCards.enactedPlan, // holds district view

      // Jobs:
      jobCards : testJobCards.jobs, // holds all the jobs retrieved back from the serverside (UPDATED BY JSON)
      currentJob : "",

      // Map View Filters:
      selectedFilters : null, // current demographic filters
      precinctsView : false, // currently showing precincts
      districtsView : false, // currently showing districts
      filterDistrictsView : false, // currently showing districts via selected map view filter
      filterPrecinctsView : false, // currently showing districts via selected map view filter
      stateView : true, // show stateView

      // Map View Content
      districtsContent : null,
      precinctsContent : null,

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

    this.setState({currentJob : ""}) // any current job is no longer selected now
    this.setState({currentState : stateName});
    if (stateName == "Georgia") stateName = "GE"
    else if (stateName == "New York") stateName = "NY"
    else stateName = "CA"
    let stateObject =  {
      state: stateName
    }
    try {
      let res = await endpoint.getState(stateObject);
      console.log(res)
      this.setState({ jobCards : res.jobs}); // update the current jobCards
      this.setState({ enactedPlan : res.state.enactedPlan}); // update the current enacted plan
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

      try {
        let res = await endpoint.generateJob(userInputs); // bug right here
        console.log(res)

        this.state.jobCards.push(res);
        this.setState({jobCards : this.state.jobCards})

      } catch (exception) {
        console.error(exception);
      }
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

    let foundDistrictsView = false
    let foundPrecinctsView = false

    this.setState({selectedFilters : mapFilters});

    // Precondition --> if array is empty, reset district and precinct view
    if (mapFilters == null) {
      this.setState({districtsView : false})
      this.setState({districtsContent : null })
      // precinct view
      this.setState({precinctsView : false})
      this.setState({precinctsContent : null })
      return;
    }

    for (var i = 0; i < mapFilters.length; i++) {
      if (mapFilters[i].label == "Precincts")  { // precinct view
        this.setState({precinctsView : true})
        this.setState({precinctsContent : <GeoJSON weight="1" color="red" key='NewYorkPrecincts' data={NYPrecincts} /> })
        foundPrecinctsView = true;
        this.setState({ filterPrecinctsView : true })
      }
      else if (mapFilters[i].label == "Districts") {  // district view
        console.log("DISTRICTS VIEW ON")
        this.setState({districtsView : true})
        // this.setState({districtsContent : <GeoJSON weight="1" color="red" key='NewYorkDistricts' data={NYDistricts} /> })
        foundDistrictsView = true; // MARKED AS FOUND
        this.setState({filterDistrictsView : true})
      }
      console.log(mapFilters[i].label); // this is how you access the label of the array element at position i 
    }
      // If filter "Precincts View" isn't found, then reset state variables
      if (foundPrecinctsView == false) {
        this.setState({precinctsView : false})
        this.setState({precinctsContent : null })
        this.setState({filterPrecinctsView : false })
      }
      // If filter "Districts View" isn't found, then reset state variables
      if (foundDistrictsView == false) {
        this.setState({districtsView : false})
        this.setState({districtsContent : null})
        this.setState({filterDistrictsView : false})
      }
  }

  /**
   * This function is responsible for maintaining the map changes based off the user ZOOMING IN AND OUT
   * 
   * @param {String} viewType Represents the map filters selected by the user, can be either 
   * @param {Int} actionType Represents the map filters selected by the user, can be either 
   * 
   */
  changeViewFromZoom = (viewType, actionType) => { // status can be 0 (delete view) or 1 (insert view).

    // "Districts" is currently selected as a map view filter
    if (viewType == "Districts") {
      if (this.state.filterDistrictsView == true) return; // nothing you can do
      else if (this.state.filterDistrictsView == false && actionType == 1) { // insert districts into map based on zoom
        this.setState({districtsView : true})
        this.setState({districtsContent : <GeoJSON weight="1" color="red" key='NewYorkDistricts' data={NYDistricts} /> })
      }
      else if (this.state.filterDistrictsView == false && actionType == 0) { // delete districts from map based on zoom
        this.setState({districtsView : false})
        this.setState({districtsContent : null })
      }
    }

    // "Precincts" is currently selected as a map view filter
    if (viewType == "Precincts") {
      if (this.state.filterPrecinctsView == true) return; // nothing you can do
      else if (this.state.filterPrecinctsView == false && actionType == 1) { // insert precincts into map based on zoom
        this.setState({precinctsView : true})
        this.setState({precinctsContent : <GeoJSON weight="1" color="red" key='NewYorkPrecincts' data={NYPrecincts} /> })
      }
      else if (this.state.filterPrecinctsView == false && actionType == 0) { // delete precincts from map based on zoom
        this.setState({precinctsView : false})
        this.setState({precinctsContent : null })
      }
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

  toggleSelectedPlanCheck = (districtPlan) => {
    if (this.state.selectedPlanCheck == false) {
      this.setState({selectedPlanCheck: true});
      // this.setState({districtContent : districtPlan.districtGeoJSON})
       this.setState({districtsContent : <GeoJSON weight="1" color="red" key='NewYorkDistricts' data={NYDistricts} /> })
    }
    else {
      this.setState({selectedPlanCheck : false});
      this.setState({districtsContent : null})
    }
  }

  render() {
  return (
    <div >

            <HomeScreen 
            jobCards={this.state.jobCards} currentState={this.state.currentState} changeCurrentState={this.changeCurrentState} 
            currentJob ={this.state.currentJob} updateCurrentJob={this.updateCurrentJob} selectedPlanCheck={this.state.selectedPlanCheck} 
            selectedJobCheck={this.state.selectedJobCheck} toggleSelectedCard={this.toggleSelectedCard}
            enactedPlan = {this.state.enactedPlan} deleteJob={this.deleteJob} deletePlan={this.deletePlan} createJob={this.createJob} cancelJob={this.cancelJob}
            generateBoxWhiskerValues={this.generateBoxWhiskerValues} 

            // Handling use cases for precinct and district views
            changeSelectedFilters={this.changeSelectedFilters} changeViewFromZoom={this.changeViewFromZoom}
            selectedPlanCheck={this.state.selectedPlanCheck} toggleSelectedPlanCheck={this.toggleSelectedPlanCheck}
            districtsView = {this.state.districtsView} districtsContent = {this.state.districtsContent}
            precinctsView = {this.state.precinctsView} precinctsContent = {this.state.precinctsContent}


            />

            <DeveloperScreen/>            

      </div>
    );
  }
}

export default App;
