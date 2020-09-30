import React, { Component } from 'react'
import { render } from 'react-dom'
// import { Map, Marker, Popup, TileLayer } from 'react-leaflet'
import USA from "./USA.jpg"
import Sidebar from "./sidebar.js"
import OurMap from "./OurMap.js";

class MainPanel extends Component {
    render() {
        // const position = [51.505, -0.09]
        return (
            <div > 
                <OurMap/>
            </div>
        );

    }
}

export default MainPanel;
