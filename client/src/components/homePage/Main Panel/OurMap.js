import React, { Component } from 'react';
import { Map, TileLayer, ZoomControl, GeoJSON} from 'react-leaflet';

// ---------------------------------------------
//                CALIFORNIA IMPORTS
// ---------------------------------------------

import CaliforniaStateBoundary from '../../../json/CALIFORNIA/CaliforniaStateBoundaries.json'

// ---------------------------------------------
//                GEORGIA IMPORTS
// ---------------------------------------------

import GeorgiaStateBoundary from '../../../json/GEORGIA/GeorgiaStateBoundaries.json';
// import GeorgiaDistricts from  '../../../json/GEORGIA/GeorgiaDistricts.json';

// ---------------------------------------------
//                NEW YORK IMPORTS
// ---------------------------------------------
import NewYorkStateBoundary from '../../../json/NEW_YORK/NewYorkStateBoundaries.json';
// import NYDistricts from '../../../json/NEW_YORK/NewYorkDistricts.json';

class OurMap extends Component {
    constructor () {
        super();
        this.state = {}

        this.mapCenter = [39, -105];
        this.mapZoom = 5;

        // Map Filters
        this.precinctView = false;
        this.districtView = false;
        this.stateView = true;
    }

    render() {

        // Change center/zoom based on which state is selected
        if (this.props.currentState == "Georgia") {
            this.mapCenter = [32.5, -85.00078];
            this.mapZoom = 7;
        }
        else if (this.props.currentState == "California") {
            this.mapCenter = [36.778259, -119.417931];
            this.mapZoom = 6;
        }
        else if (this.props.currentState == "New York") {
            this.mapCenter = [42.712776, -77.005974];
            this.mapZoom = 6.5;
        }
        else {
            this.mapCenter = [39, -105];
            this.mapZoom = 5;
        }

        return(
                <Map id="ourMap" center={this.mapCenter} zoom={this.mapZoom} zoomControl={false}>
                    <ZoomControl position = 'bottomleft'> </ZoomControl>
                <TileLayer
                    attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

                    <GeoJSON weight="1" color="red" key="California" data={CaliforniaStateBoundary} onClick={ () => this.props.changeCurrentStatefromMap("California")}/> 
                    {/* <GeoJSON key="California" data={CaliforniaDistricts} onClick={ () => this.props.changeCurrentStatefromMap("California")}/>  />  */}
                    
                    <GeoJSON weight="1" color="red" key="Georgia" data={GeorgiaStateBoundary} onClick={ () => this.props.changeCurrentStatefromMap("Georgia")} />
                    {/* <GeoJSON key='Georgia' data={GeorgiaPrecincts} /> */}
                    {/* <GeoJSON key='Georgia' data={GeorgiaDistricts} onClick={ () => this.props.changeCurrentStatefromMap("Georgia")}/>  /> */}

                    <GeoJSON weight="1" color="red" key='NewYork' data={NewYorkStateBoundary} onClick={ () => this.props.changeCurrentStatefromMap("New York")}/>
                    {/* <GeoJSON key='NewYorkDistricts' data={NYSAssemblyDistricts} /> */}
                    {/* <GeoJSON key='NewYorkDistricts' data={NewYorkDistricts} />  */}
                    {/* <GeoJSON key='NewYork' data={NYDistricts} onClick={ () => this.props.changeCurrentStatefromMap("New York")}/> */}

                </Map> 
            );
        }
}

export default OurMap;