import HomeScreen from "./components/homePage/HomeScreen";
import DeveloperScreen from "./components/developerscreen/Developer"
import React, { Component } from "react";
import { BrowserRouter, Route, Redirect } from "react-router-dom";
import Switch from "react-bootstrap/esm/Switch";
import * as endpoint from './endpoint/Client';
import testJobCards from './json/TestJobCards.json'
import './css/project_styles.css';

import {GeoJSON} from 'react-leaflet';
import NYDistricts from './json/NEW_YORK/NewYorkDistricts.json';
import NYPrecincts from './json/NEW_YORK/NewYorkPrecincts.json';

class App extends Component {
    state = {

      // State:
      currentState : "Select a state",
      enactedPlan : testJobCards.enactedPlan, 
      totalPopulation : 0,
      numOfPrecincts : 0,
      numOfCounties : 0,

      // Jobs:
      jobCards : testJobCards.jobs,
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

    }

  changeCurrentState = async (stateName) => {

    this.setState({currentJob : ""}) // any current job is no longer selected now
    this.setState({currentState : stateName});
    if (stateName == "Georgia") stateName = "GA"
    else if (stateName == "New York") stateName = "NY"
    else stateName = "CA"
    let stateObject =  {
      state: stateName
    }
    try {
      let res = await endpoint.getState(stateObject);
      console.log(res)
      this.setState({ jobCards : res.jobs}); 
      this.setState({ enactedPlan : res.state.enactedPlan}); 
      this.setState({ totalPopulation : res.state.demographic.totalPopulation});
      this.setState({ numOfPrecincts : res.state.numOfPrecincts});
      this.setState({ numOfCounties : res.state.numOfCounties}); 
    } catch (exception) {
      console.error(exception);
    }
  }

  createJob = async (userInputs) => {

      try {
        let res = await endpoint.generateJob(userInputs); // bug right here
        console.log(res)
        let labelsMinorities = [];
        res.minorityAnalyzed.forEach(element => { // values --> keys
            switch (element) {
              case "WHITE_AMERICAN": 
                labelsMinorities.push("White");
                break;
              case "AFRICAN_AMERICAN": 
                labelsMinorities.push("African American");
                break;
              case "LATINO_AMERICAN": 
                labelsMinorities.push("Latino");
                break;
              case "ASIAN_AMERICAN": 
                labelsMinorities.push("Asian");
                break;
              case "AMERICAN_INDIAN": 
                labelsMinorities.push("American Indian");
                break;
              case "HAWAIIAN_AMERICAN": 
                labelsMinorities.push("Hawaiian");
                break;
              case "OTHER_AMERICAN": 
                labelsMinorities.push("Other");
                break;
            }
        })
        res.minorityAnalyzed = labelsMinorities
        this.state.jobCards.push(res);
        this.setState({jobCards : this.state.jobCards})
      } catch (exception) {
        console.error(exception);
      }
  }

  cancelJob = (job) => { 
    let indexOfJob = this.state.jobCards.indexOf(job);
    if (indexOfJob >= 0)
        this.state.jobCards.splice(indexOfJob, 1);
    this.setState({ jobCards : this.state.jobCards})
  }

  deleteJob = (job) => { 
    let indexOfJob = this.state.jobCards.indexOf(job);
    if (indexOfJob >= 0)
        this.state.jobCards.splice(indexOfJob, 1);
    this.setState({ jobCards : this.state.jobCards})
  }
  
  getPrecincts = async () => {
    try {
      let res = await endpoint.getStatePrecincts();
      this.setState({precinctsContent : <GeoJSON weight="1" color="red" key='precincts' data={res.precinctsGeoJson} />});
    } catch (exception) {
      console.error(exception);
    }
  }

  deletePlan = (plan) => {

    if (this.state.currentJob != "" && plan.type != "Enacted Plan") {
      let job = this.state.currentJob;
      let indexOfJob = this.state.jobCards.indexOf(job);
      let indexOfPlan = this.state.jobCards[indexOfJob].districtPlans.indexOf(plan);
      if (indexOfPlan >= 0)
        this.state.jobCards[indexOfJob].districtPlans.splice(indexOfPlan, 1);
      this.setState({ jobCards : this.state.jobCards})
    }
    this.setState({ jobCards : this.state.jobCards })
  }

  generateBoxWhiskerValues = () => {
    console.log(this.state.currentJob)
    try {
      // let res = await endpoint.generateBoxWhisker(currentJob);
      // console.log(res)
    } catch (exception) {
      console.error(exception);
    }
  }

  changeSelectedFilters = (mapFilters) => {

    let foundDistrictsView = false
    let foundPrecinctsView = false

    this.setState({selectedFilters : mapFilters});

    if (mapFilters == null) { // reset
      this.setState({districtsView : false}) 
      this.setState({precinctsView : false})
      this.setState({filterDistrictsView : false})
      this.setState({filterPrecinctsView : false })
      this.setState({precinctsContent : null })
      return;
    }

    for (var i = 0; i < mapFilters.length; i++) {
      if (mapFilters[i].label == "Precincts")  { // precinct view
        this.setState({precinctsView : true})
        this.getPrecincts();
        // this.setState({precinctsContent : <GeoJSON weight="1" color="red" key='NewYorkPrecincts' data={NYPrecincts} /> })
        foundPrecinctsView = true;
        this.setState({ filterPrecinctsView : true })
      }
      else if (mapFilters[i].label == "Districts") {  // district view
        console.log("DISTRICTS VIEW ON")
        this.setState({districtsView : true})
        foundDistrictsView = true;
        this.setState({filterDistrictsView : true})
      }
    }
      if (foundPrecinctsView == false) { // if not selected
        this.setState({precinctsView : false})
        this.setState({precinctsContent : null })
        this.setState({filterPrecinctsView : false })
      }
      if (foundDistrictsView == false) {
        this.setState({districtsView : false})
        this.setState({filterDistrictsView : false})
      }
  }

  changeViewFromZoom = (viewType, actionType) => { // actionType = 0 (delete view) or 1 (insert view)

    if (viewType == "Districts") {
      if (this.state.filterDistrictsView == true) return; 
      else if (this.state.filterDistrictsView == false && actionType == 1) {
        this.setState({districtsView : true})
      }
      else if (this.state.filterDistrictsView == false && actionType == 0) this.setState({districtsView : false})
    }

    if (viewType == "Precincts") {
      if (this.state.filterPrecinctsView == true) return; 
      else if (this.state.filterPrecinctsView == false && actionType == 1) { 
        this.setState({precinctsView : true})
        this.getPrecincts();
        // this.setState({precinctsContent : <GeoJSON weight="1" color="red" key='NewYorkPrecincts' data={NYPrecincts} /> })
      }
      else if (this.state.filterPrecinctsView == false && actionType == 0) { 
        this.setState({precinctsView : false})
        this.setState({precinctsContent : null })
      }
    }
  }

  updateCurrentJob = (job, selected) => {
    if (selected == true) { // job just selected
      this.setState({currentJob : job});
      this.setState({currentJobName : job.jobName});
    }
    else { // job just de-selected
      this.setState({currentJob : ""});
      this.setState({currentJobName : ""});
    }
  }

  toggleSelectedCard = () => {
    if (this.state.selectedJobCheck == false) this.setState({selectedJobCheck : true});
    else this.setState({selectedJobCheck : false});
  }

  toggleSelectedPlanCheck = (districtPlan) => {
    if (this.state.selectedPlanCheck == false) {
      this.setState({selectedPlanCheck: true});
      this.setState({districtsContent : <GeoJSON weight="1" color="red" key='NewYorkDistricts' data={districtPlan.districtsGeoJson} /> })
    }
    else {
      this.setState({selectedPlanCheck : false});
      this.setState({districtsContent : null});
    }
  }

  render() {
  return (
    <div >
            <HomeScreen 

            // State-related data
            currentState={this.state.currentState} jobCards={this.state.jobCards}
            numOfPrecincts={this.state.numOfPrecincts} numOfCounties={this.state.numOfCounties}
            totalPopulation={this.state.totalPopulation} enactedPlan = {this.state.enactedPlan}
            currentJob ={this.state.currentJob} changeCurrentState={this.changeCurrentState} 

            // Job-related methods
            updateCurrentJob={this.updateCurrentJob} deleteJob={this.deleteJob} toggleSelectedCard={this.toggleSelectedCard}
            createJob={this.createJob} cancelJob={this.cancelJob} selectedJobCheck={this.state.selectedJobCheck}
            generateBoxWhiskerValues={this.generateBoxWhiskerValues} 

            // Plan-related methods
            selectedPlanCheck={this.state.selectedPlanCheck} deletePlan={this.deletePlan}

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
