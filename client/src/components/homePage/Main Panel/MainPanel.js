import React, { Component } from 'react'
import Sidebar from "./Sidebar.js";
import OurMap from "./OurMap.js";
import Select from 'react-select';
import makeAnimated from 'react-select/animated';

class MainPanel extends Component {
  constructor () {
    super();
    this.state = {

      // District View and Precinct View only enable if at a certain zoom level. Otherwise disabled
      disablePrecinctView : true,
      disableDistrictView : true
    }

    // Holds zoom level as to update enablePrecinctView and enableDistrictView
    this.mapZoom = 5;
}

    handleZoomChange = (e) => {
      // console.log("now: " + this.mapZoom);
      this.mapZoom = e.target._zoom;

      // if zoom level is below 6, then we can have both views available
      if (this.mapZoom >= 6) {
        this.setState({disablePrecinctView : false})
        this.setState({disableDistrictView : false})
      }

      if (this.mapZoom == 6) {
          // district view
          console.log("district view")
      }
      else if (this.mapZoom == 9 && this.districtView != true) {
          console.log("precinct view")
          // precinct view
      }
      // else only state view
    }

    render() {

      console.log(this.state.selectedFilters);

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
                { label:"Precincts", value: "precincts", isDisabled : this.state.disablePrecinctView},
                { label:"Districts", value: "districts", isDisabled : this.state.disableDistrictView},
              ]},
          ];

        return (
            <div id="mainPanelWrapper">
              {/* -------------- */}
                {/* Sidebar */}

                <div id="sideBarWrapper"> 
                    <Sidebar 
                    jobCards={this.props.jobCards} changeCurrentState={this.props.changeCurrentState} updateCurrentJob={this.props.updateCurrentJob} updateCurrentJobName={this.props.updateCurrentJobName} 
                    selectedPlanCheck={this.props.selectedPlanCheck} toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} selectedJobCheck={this.props.selectedJobCheck} 
                    toggleSelectedCard={this.props.toggleSelectedCard} currentState={this.props.currentState} selectedFilters={this.props.selectedFilters}
                    currentJob ={this.props.currentJob} enactedPlan = {this.props.enactedPlan} deleteJob={this.props.deleteJob} deletePlan={this.props.deletePlan}
                    createJob={this.props.createJob} cancelJob = {this.props.cancelJob} generateBoxWhiskerValues={this.props.generateBoxWhiskerValues}
                    />
                </div>

               {/* -------------- */}
                {/* Map Panel */}

                <div id="mapPanelWrapper" className="container-fluid"> {/* bootstrap it so it's responsive */}
                    <OurMap changeCurrentState={this.props.changeCurrentState} 
                    currentState={this.props.currentState} handleZoomChange={this.handleZoomChange} />
                    {/* Map Filters  */}
                    <div id="mapFilters">
                        <Select isSearchable={true} placeholder="Choose option(s) to filter map" components={componentsAnimation} 
                        className="basic-multi-select" options={options} isMulti={true} onChange={this.props.changeSelectedFilters}
                        />
                    </div>

                {/* -------------- */}
                </div>
            </div> 
        );

    }
}

export default MainPanel;