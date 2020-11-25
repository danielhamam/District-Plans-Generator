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
            { label:"White", value: "white"},
            { label:"Black or African American", value: "africanamerican"},
            { label:"Hispanic", value: "latino"},
            { label:"Asian", value: "asian"},
            { label:"American Indian or Alaska Native", value: "americanindian"},
            { label:"Native Hawaiian or Other Pacific Islander", value: "hawaiian"},
            { label:"Other", value: "other"},
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

                    // Job-related content
                    currentJob = {this.props.currentJob} updateCurrentJob={this.props.updateCurrentJob}
                    updateCurrentJobName={this.props.updateCurrentJobName} toggleSelectedCard={this.props.toggleSelectedCard}
                    createJob={this.props.createJob} cancelJob = {this.props.cancelJob} deleteJob={this.props.deleteJob} 
                    selectedJobCheck={this.props.selectedJobCheck} generateBoxWhiskerValues={this.props.generateBoxWhiskerValues}

                    // Map-related content
                    selectedFilters={this.props.selectedFilters}

                    // Plan-related content
                    deletePlan={this.props.deletePlan} selectedPlanCheck={this.props.selectedPlanCheck} 
                    toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} 
                    
                    />
                </div>

                <div id="mapPanelWrapper" className="container-fluid"> {/* bootstrap it so it's responsive */}
                    <OurMap changeCurrentState={this.props.changeCurrentState} 
                    currentState={this.props.currentState} changeCurrentZoom={this.changeCurrentZoom} 
                    districtsView = {this.props.districtsView} districtsContent = {this.props.districtsContent}
                    precinctsView = {this.props.precinctsView} precinctsContent = {this.props.precinctsContent}
                    changeViewFromZoom={this.props.changeViewFromZoom} disableDistrictView={this.disableDistrictView} d
                    enableDistrictView={this.enableDistrictView}
                    />
                    <div id="mapFilters"> {/* Map Filters  */}
                        <Select isSearchable={true} value={this.props.selectedFilters} placeholder="Choose option(s) to filter map" components={componentsAnimation} 
                        className="basic-multi-select" options={options} isMulti={true} onChange={this.props.changeSelectedFilters}
                        />
                    </div>
                </div>

                {/* Precinct Modal */}
                <PrecinctModal
                selectedFeature = {this.props.selectedFeature} 
                togglePrecinctModal = {this.props.togglePrecinctModal}
                />

            </div> 
        );

    }
}

export default MainPanel;