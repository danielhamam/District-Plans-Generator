import React, { Component } from 'react'
import Sidebar from "./Sidebar.js";
import OurMap from "./OurMap.js";
import Select from 'react-select';
import makeAnimated from 'react-select/animated';

import PrecinctModal from "./PrecinctModal.js"

class MainPanel extends Component {
  constructor () {
    super();
    this.state = {
      disablePrecinctView : false,
      disableDistrictView : false
    }
    this.currentZoom = 0; // Holds zoom level (for enablePrecinctView and enableDistrictView)
}

  changeCurrentZoom = (newZoom) => {
    this.currentZoom = newZoom;
    console.log("New zoom is " + this.currentZoom);
  }

  // disableDistrictView = () => { this.setState({disableDistrictView : true}); }
  // enableDistrictView = () => { this.setState({disableDistrictView : false}); }

    render() {

      // console.log(this.state.selectedFilters);
      const componentsAnimation = makeAnimated();
      const options = [
        {
          label: "Demographic Heat Map",
          options: [
            { label:"White", value: "white", isDisabled : (this.props.disableWhite || this.props.currentState == "Select a state") },
            { label:"Black or African American", value: "africanamerican", isDisabled : (this.props.disableBlack || this.props.currentState == "Select a state") },
            { label:"Hispanic", value: "latino", isDisabled : (this.props.disableHispanic || this.props.currentState == "Select a state") },
            { label:"Asian", value: "asian", isDisabled : (this.props.disableAsian || this.props.currentState == "Select a state") },
            { label:"American Indian or Alaska Native", value: "americanindian", isDisabled : (this.props.disableAmericanIndian || this.props.currentState == "Select a state") },
            { label:"Native Hawaiian or Other Pacific Islander", value: "hawaiian", isDisabled : (this.props.disableHawaiian || this.props.currentState == "Select a state") },
            { label:"Other", value: "other", isDisabled : (this.props.disableOther || this.props.currentState == "Select a state") },
          ]},
          {
          label: "Cluster",
          options: [
            { label:"Precincts", value: "precincts", isDisabled : (this.state.disablePrecinctView || this.props.currentState == "Select a state") },
            { label:"Districts", value: "districts", isDisabled : (this.state.disableDistrictView || !this.props.selectedPlanCheck) },
          ]},
      ];

        return (
            <div id="mainPanelWrapper">
                <div id="sideBarWrapper"> 

                    <Sidebar 

                    // State-related content
                    currentState={this.props.currentState} enactedPlan = {this.props.enactedPlan} changeCurrentState={this.props.changeCurrentState}
                    jobCards={this.props.jobCards} totalPopulation={this.props.totalPopulation} numOfPrecincts={this.props.numOfPrecincts} numOfCounties={this.props.numOfCounties}
                    numOfDistricts={this.props.numOfDistricts}

                    // Job-related content
                    currentJob = {this.props.currentJob} minoritiesAnalyzed = {this.props.currentJob.minorityAnalyzed} updateCurrentJob={this.props.updateCurrentJob}
                    updateCurrentJobName={this.props.updateCurrentJobName} toggleSelectedCard={this.props.toggleSelectedCard}
                    createJob={this.props.createJob} cancelJob = {this.props.cancelJob} deleteJob={this.props.deleteJob} 
                    selectedJobCheck={this.props.selectedJobCheck} generateBoxWhiskerValues={this.props.generateBoxWhiskerValues}

                    // Map-related content
                    selectedFilters={this.props.selectedFilters}

                    // Plan-related content
                    deletePlan={this.props.deletePlan} selectedPlanCheck={this.props.selectedPlanCheck} 
                    toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} firstLoadChange = {this.props.firstLoadChange} 
                    firstLoad = {this.props.firstLoad} 
                    
                    />
                </div>

                <div id="mapPanelWrapper" className="container-fluid"> {/* bootstrap it so it's responsive */}
                    <OurMap changeCurrentState={this.props.changeCurrentState} 
                    currentState={this.props.currentState} changeCurrentZoom={this.changeCurrentZoom} 
                    districtsView = {this.props.districtsView} districtsContent = {this.props.districtsContent}
                    precinctsView = {this.props.precinctsView} precinctsContent = {this.props.precinctsContent}
                    changeViewFromZoom={this.props.changeViewFromZoom} disableDistrictView={this.disableDistrictView} d
                    enableDistrictView={this.enableDistrictView} demographicJSON = {this.props.demographicJSON} 
                    demographicMax={this.props.demographicMax} districtsViewSelect = {this.props.districtsViewSelect}
                    />
                    <div id="mapFilters"> {/* Map Filters  */}
                        <Select isSearchable={true} value={this.props.selectedFilters} placeholder="Choose option(s) to filter map" components={componentsAnimation} 
                        className="basic-multi-select" options={options} isMulti={true} onChange={this.props.changeSelectedFilters}
                        />
                    </div>
                </div>

                {/* Precinct Modal */}
                <PrecinctModal
                precinctName = {this.props.precinctName}
                selectedFeature = {this.props.selectedFeature} 
                featureObject = {this.props.featureObject}
                togglePrecinctModal = {this.props.togglePrecinctModal}
                />

            </div> 
        );

    }
}

export default MainPanel;