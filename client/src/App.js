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
      jobCards : [],
      selectedJobCheck: false,
      selectedPlanCheck: false,
      currentState : "Select a state",
      currentJobName : "No Job Selected: ", // name of the currently selected job
      // selectedJobName : "", // take name of selected district
      // selectedPlanName : "", // take name of selected district
      // todoLists: testTodoListData.todoLists, // Portion of my code taken from CSE 316
              
      // Map View Filters 
      selectedFilters : null,
      precinctView : false,
      districtView : false,
      stateView : true
      
    }

  receiveJobs = () => {

  }

  cancelJob = (jobID) => { // string

  }

  createJob = () => {
    let newBatch =  {
      "numberOfDistricting" : 10,
      "name": "batch1",
      "isAvailable": false,
      "populationDifference": 10.0,
      "compactness": 10.0,
      "state": "NY"
  }
    let res = generateJob();
  }

  deleteJob = (id) => { // string

  }

  deletePlan = (id) => { // string

  }

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
  }

  changeSelectedFilters = (selected) => {
    this.setState({selectedFilters : selected});
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
