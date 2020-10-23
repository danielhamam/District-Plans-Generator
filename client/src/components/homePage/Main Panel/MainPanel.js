import React, { Component } from 'react'
import Sidebar from "./Sidebar.js";
import OurMap from "./OurMap.js";
import Select from 'react-select';
import makeAnimated from 'react-select/animated';

class MainPanel extends Component {
  constructor () {
    super();
    this.state = { 
        currentState : "Select a state",
        selectedFilters : null,

        // Map Filters
        precinctView : false,
        districtView : false,
        stateView : true
    }
}
    changeCurrentStatefromSidebar = (newName) => {
      this.setState({currentState : newName});
    }

    changeCurrentStatefromMap = (newName) => {
      this.setState({currentState : newName});
    }

    changeSelectedFilters = (selected) => {
      this.setState({selectedFilters : selected});
    }

    render() {

      console.log(this.state.selectedFilters);

        const componentsAnimation = makeAnimated();

        const clusterOptions = [
            { value: 'precincts', label: 'Precincts'},
            { value: 'districts', label: 'Districts'},
            { value: 'counties', label: 'Counties'}
          ]
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
                { label:"Precincts", value: "precincts"},
                { label:"Districts", value: "districts"},
                { label:"Counties", value: "counties"},
              ]},
          ];

        return (
            <div id="mainPanelWrapper">
              {/* -------------- */}
                {/* Sidebar */}

                <div id="sideBarWrapper"> 
                    <Sidebar currentBatchName={this.props.currentBatchName} updateCurrentBatchName={this.props.updateCurrentBatchName} selectedPlanCheck={this.props.selectedPlanCheck} toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} selectedBatchCheck={this.props.selectedBatchCheck} toggleSelectedBatchCheck={this.props.toggleSelectedBatchCheck} currentState={this.state.currentState} changeCurrentStatefromSidebar={this.changeCurrentStatefromSidebar} selectedFilters={this.state.selectedFilters}/>
                </div>

               {/* -------------- */}
                {/* Map Panel */}

                <div id="mapPanelWrapper" className="container-fluid"> {/* bootstrap it so it's responsive */}
                    <OurMap changeCurrentStatefromMap={this.changeCurrentStatefromMap} currentState={this.state.currentState} />
                    {/* Map Filters  */}
                    <div id="mapFilters">
                        <Select isSearchable={true} placeholder="Choose option(s) to filter map" components={componentsAnimation} className="basic-multi-select" options={options} isMulti={true} onChange={this.changeSelectedFilters}/>
                    </div>

                {/* -------------- */}
                </div>
            </div> 
        );

    }
}

export default MainPanel;