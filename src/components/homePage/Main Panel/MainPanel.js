import React, { Component } from 'react'
import { render } from 'react-dom'
// import { Map, Marker, Popup, TileLayer } from 'react-leaflet'
import Sidebar from "./Sidebar.js";
import OurMap from "./OurMap.js";
import Select from 'react-select';
import makeAnimated from 'react-select/animated';
import ModalBatch from './BatchCards/ModalBatch';

class MainPanel extends Component {

    render() {

        const componentsAnimation = makeAnimated();

        const clusterOptions = [
            { value: 'precincts', label: 'Precincts' },
            { value: 'districts', label: 'Districts' },
            { value: 'counties', label: 'Counties'}
          ]
          const ethncityOptions = [
            { value: 'precincts', label: 'Precincts' },
            { value: 'districts', label: 'Districts' },
            { value: 'counties', label: 'Counties'}
          ]
          const politicalPartyOptions = [
            { value: 'democratic', label: 'Democratic' },
            { value: 'republic', label: 'Republic' },
            { value: 'libertarian', label: 'Libertarian'},
            { value: 'greenparty', label: 'Green Party'}
          ]

          const options = [
            {
              label: "Ethnicity",
              options: [
                { label:"White", value: "Group 1, option 1"},
                { label:"Black", value: "Group 1, option 2"},
                { label:"Latino", value: "Group 1, option 3"},
                { label:"Asian", value: "Group 1, option 4"},
                { label:"American Indian", value: "Group 1, option 5"},
                { label:"Hawaiian", value: "Group 1, option 6"},
                { label:"Other", value: "Group 1, option 7"},
              ]},
              {
              label: "Political Party",
              options: [
                { label:"Democratic", value: "Group 2, option 1"},
                { label:"Republic", value: "Group 2, option 2"},
                { label:"Libertarian", value: "Group 2, option 3"},
                { label:"Green Party", value: "Group 2, option 4"},
                { label:"Other", value: "Group 2, option 5"},
              ]},
          ];

        return (
            <div id="mainPanelWrapper">
                {/* Sidebar */}
                <div id="sideBarWrapper"> 
                    {/* < ModalBatch />  */}
                    <Sidebar />
                </div>
                {/* Map Panel */}
                <div id="mapPanelWrapper" className="container-fluid"> {/* bootstrap it so it's responsive */}
                    <OurMap />
                {/* Map Filters  */}
                    <div id="mapFilters">
                        <Select placeholder="Choose option(s) to filter map" components={componentsAnimation} className="basic-multi-select" options={options} isMulti={true}/>
                    </div>
                </div>

            </div> 
                // {/* <OurMap/>  */}
        );

    }
}

export default MainPanel;