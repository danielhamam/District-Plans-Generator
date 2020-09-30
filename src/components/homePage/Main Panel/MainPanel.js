import React, { Component } from 'react'
import { render } from 'react-dom'
// import { Map, Marker, Popup, TileLayer } from 'react-leaflet'
import Sidebar from "./Sidebar.js"
import OurMap from "./OurMap.js";

class MainPanel extends Component {
    render() {
        // const position = [51.505, -0.09]
        return (
            <div id="mainPanelWrapper">
                
                {/* Sidebar */}
                <div id="sideBarWrapper"> 
                    <Sidebar />
                </div>

                {/* Map Panel */}
                <div id="mapPanelWrapper" className="container-fluid p-0"> {/* bootstrap it so it's responsive */}
                    <OurMap />
                </div>

            </div> 
                // {/* <OurMap/>  */}
        );

    }
}

export default MainPanel;
