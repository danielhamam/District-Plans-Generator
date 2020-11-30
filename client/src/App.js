import HomeScreen from "./components/homePage/HomeScreen";
import DeveloperScreen from "./components/developerscreen/Developer"

import React, { Component } from "react";
import * as endpoint from './endpoint/Client';
import testJobCards from './json/TestJobCards.json'
import './css/project_styles.css';

import {GeoJSON} from 'react-leaflet';
import GADistricts from './json/GEORGIA/ga_congressionalDistrict.json';
import GAPrecincts from './json/GEORGIA/ga_precincts.json';

class App extends Component {
    state = {
      // State:
      currentState : "Select a state",
      enactedPlan : testJobCards.enactedPlan, 
      totalPopulation : 0,
      numOfDistricts : 0,
      numOfPrecincts : 0,
      numOfCounties : 0,

      // Jobs:
      jobCards : testJobCards.jobs,
      currentJob : "",
      currentPlan : "",
      planState : null,

      // Map View Filters:
      selectedFilters : null,
      precinctsView : false,
      districtsView : false, 
      filterDistrictsView : false, 
      filterPrecinctsView : false, 
      stateView : true,

      // Map View Content
      districtsContent : null,
      precinctsContent : null,

      // Checks for Selection
      selectedPlanCheck: false,
      selectedJobCheck: false,

      // Modals
      featureObject : {
        totalPopulation : "",
        vaptotalPopulation : "",
        whitePopulation : "",
        whiteVAPPopulation : "",
        africanAmericanPopulation : "",
        africanAmericanVAPPopulation : "",
        hispanicPopulation : "",
        hispanicVAPPopulation : "",
        asianPopulation : "",
        asianVAPPopulation : "",
        americanIndianPopulation : "",
        americanIndianVAPPopulation : "",
        nativeHawaiianPopulation : "",
        nativeHawaiianVAPPopulation : "",
        multipleRacePopulation : "",
        multipleRaceVAPPopulation : "",
        otherRacePopulation : "",
        otherRaceVAPPopulation : ""
      },
      togglePrecinctModal : false,
      precinctName : "",
      selectedFeature : {}
    }

  changeCurrentState = async (stateAbrev, stateName) => {

    if (stateName == this.state.currentState) return;
    
    this.setState({currentJob : ""})
    this.setState({currentState : stateName});
    let stateObject =  {
      state: stateAbrev
    }
    try {
      let res = await endpoint.getState(stateObject);
      console.log(res)
      this.setState({ jobCards : res.jobs}); 

      // Reset Views / Contents of Filters 
      this.setState({ districtsContent : null})
      this.setState({ precinctsContent : null})
      this.setState({ districtsView : null})
      this.setState({ precinctsView : null})
      this.setState({selectedFilters : null})
      this.changeSelectedFilters(null)

      // Clear any selected job/plan
      this.setState({currentJob : ""})
      this.setState({selectedPlanCheck: false})
      
      if (this.state.planState != null ) {
        this.state.planState.setState({selected : false});
        this.state.planState.districtPlanClassStyle = "";
        this.state.planState.goTop = "";
      }

      // Initialize state object
      this.setState({ enactedPlan : res.state.enactedPlan}); 
      this.setState({ totalPopulation : res.state.totalPopulation});
      this.setState({ numOfDistricts : res.state.numOfDistricts});
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
        if (res.status == "PENDING") res.status = "Pending";
        if (res.status == "COMPLETED") res.status = "Completed"
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

  deleteJob = async (job) => { 
    let indexOfJob = this.state.jobCards.indexOf(job);
    if (indexOfJob >= 0)
        this.state.jobCards.splice(indexOfJob, 1);
      try {
        let res = await endpoint.deleteJob(job);
        console.log(res)
      } catch (exception) {
        console.error(exception);
      }
    this.setState({ jobCards : this.state.jobCards})
  }

  togglePrecinctModal = async () => {
    if (this.state.togglePrecinctModal == false) this.setState({togglePrecinctModal : true})
    else this.setState({togglePrecinctModal : false})
  }

  getPrecinctDemographic = async (feature) => {
    this.setState({selectedFeature : feature})
    try {
      let nameObject = {
        name : feature.properties.NAME
      }
      this.setState({precinctName : feature.properties.NAME})
      let res = await endpoint.getPrecinctDemographic(nameObject);
      this.setState({featureObject : res})
    } catch (exception) {
      console.error(exception);
    }
  }

  onEachFeature = (feature, layer) => {
    console.log('onEachFeature fired: ');
        layer.on({
            mouseover: (e) => this.getPrecinctDemographic(feature),
            // mouseout: (e) => this.togglePrecinctModal(feature)
        });
  }
  
  getPrecincts = async () => {
    this.setState({togglePrecinctModal : true})
    try {
      let res = await endpoint.getStatePrecincts();
      this.setState({precinctsContent : 
      <GeoJSON 
        weight={1} 
        color="red" 
        key='precincts' 
        data={res.precinctsGeoJson} 
        onEachFeature = {this.onEachFeature}
        // onmouseover = {this.onEachFeature}
        
      />});
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

  changeSelectedFilters = async (mapFilters) => {
    let foundDistrictsView = false
    let foundPrecinctsView = false
    let foundOtherFilter = false
    this.setState({selectedFilters : mapFilters});
    if (mapFilters == null) { // reset
      this.setState({districtsView : false}) 
      this.setState({precinctsView : false})
      this.setState({togglePrecinctModal : false})
      this.setState({filterDistrictsView : false})
      this.setState({filterPrecinctsView : false })
      this.setState({precinctsContent : null })
      return;
    }

    // If not null
    let revisedMapFilters = JSON.parse(JSON.stringify(mapFilters));

    for (var i = 0; i < mapFilters.length; i++) {
      if (mapFilters[i].label == "Precincts")  { // precinct view
        this.setState({precinctsView : true})
        this.getPrecincts();
        // this.setState({precinctsContent : <GeoJSON weight={1} color="red" key='NewYorkPrecincts' data={NYPrecincts} /> })
        foundPrecinctsView = true;
        this.setState({ filterPrecinctsView : true })
        delete revisedMapFilters[i]
      }
      else if (mapFilters[i].label == "Districts") {  // district view
        console.log("DISTRICTS VIEW ON")
        this.setState({districtsView : true})
        foundDistrictsView = true;
        this.setState({filterDistrictsView : true})
        delete revisedMapFilters[i]
      }
      else { 
        // Found filter that's not districtView or precinctView
        foundOtherFilter = true;
      }
    }
      if (foundPrecinctsView == false) { // if not selected
        this.setState({precinctsView : false})
        this.setState({togglePrecinctModal : false})
        this.setState({precinctsContent : null })
        this.setState({filterPrecinctsView : false })
      }
      if (foundDistrictsView == false) {
        this.setState({districtsView : false})
        this.setState({filterDistrictsView : false})
      }
      // if filters isn't null, and it's not district or precinct view
      if (foundOtherFilter == true) {
        try {
          let res = await endpoint.generateHeatMap(revisedMapFilters);
          console.log(res)
        } catch (exception) {
          console.error(exception);
        }
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
        this.setState({togglePrecinctModal : false})
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

  toggleSelectedPlanCheck = (districtPlan, state) => {
    if (this.state.selectedPlanCheck == false) {
      this.setState({selectedPlanCheck: true});
      this.setState({currentPlan : districtPlan})
      this.setState({planState : state})
      this.setState({districtsContent : <GeoJSON weight={1} color="red" key='GeorgiaDistricts' data={districtPlan.districtsGeoJson} /> })
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
            numOfDistricts={this.state.numOfDistricts}
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
            selectedFilters = {this.state.selectedFilters}

            // For Precinct Modal
            precinctName = {this.state.precinctName}
            featureObject = {this.state.featureObject}
            selectedFeature = {this.state.selectedFeature} 
            togglePrecinctModal = {this.state.togglePrecinctModal}
            />

            <DeveloperScreen/>            

      </div>
    );
  }
}

export default App;
