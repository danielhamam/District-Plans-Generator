import React, { Component } from 'react'
import { render } from 'react-dom'
// import { Map, Marker, Popup, TileLayer } from 'react-leaflet'
import Sidebar from "./Sidebar.js";
import OurMap from "./OurMap.js";
import Select from 'react-select';
import makeAnimated from 'react-select/animated';
import ModalBatch from './BatchCards/ModalBatch';

class MainPanel extends Component {
  constructor () {
    super();
    this.state = { 
        currentState : "Select a state",
    }
    // this.currentCollapsed = this.currentCollapsed.bind(this);
}

    changeCurrentStatefromSidebar = (newName) => {
      this.setState({currentState : newName});
    }

    changeCurrentStatefromMap = (newName) => {
      this.setState({currentState : newName});
    }

    render() {

        const componentsAnimation = makeAnimated();

        const clusterOptions = [
            { value: 'precincts', label: 'Precincts'},
            { value: 'districts', label: 'Districts'},
            { value: 'counties', label: 'Counties'}
          ]
          const options = [
            {
              label: "Ethnicity",
              options: [
                { label:"White", value: "white"},
                { label:"African American", value: "africanamerican"},
                { label:"Latino", value: "latino"},
                { label:"Asian", value: "asian"},
                { label:"American Indian", value: "americanindian"},
                { label:"Hawaiian", value: "hawaiian"},
                { label:"Other", value: "other"},
              ]},
              {
              label: "Political Party",
              options: [
                { label:"Democratic", value: "democratic"},
                { label:"Republican", value: "republican"},
                { label:"Libertarian", value: "libertarian"},
                { label:"Green Party", value: "greenparty"},
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
                {/* Sidebar */}
                <div id="sideBarWrapper"> 
                    {/* < ModalBatch />  */}
                    <Sidebar currentState={this.state.currentState} changeCurrentStatefromSidebar={this.changeCurrentStatefromSidebar}/>
                </div>
                {/* Map Panel */}
                <div id="mapPanelWrapper" className="container-fluid"> {/* bootstrap it so it's responsive */}
                    <OurMap changeCurrentStatefromMap={this.changeCurrentStatefromMap} currentState={this.state.currentState} />
                    {/* Map Filters  */}
                    <div id="mapFilters">
                        <Select isSearchable={true} placeholder="Choose option(s) to filter map" components={componentsAnimation} className="basic-multi-select" options={options} isMulti={true}/>
                    </div>
                    {/* <div id="currentItems"> 
                        <div id="currentBatchMap"> Current Batch: </div>
                        <div id="currentPlanMap"> Current Plan: </div> 
                    </div> */}
                </div>

            </div> 
                // {/* <OurMap/>  */}
        );

    }
}

export default MainPanel;