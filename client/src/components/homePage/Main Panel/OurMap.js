import React, { Component } from 'react';
import { Map, TileLayer, ZoomControl, GeoJSON} from 'react-leaflet';
import HeatmapLayer from 'react-leaflet-heatmap-layer';
import { heatPoints } from './realWorldTest.js';
// import HeatmapLayer from '../src/HeatmapLayer';

// ---------------------------------------------
//                CALIFORNIA IMPORTS
// ---------------------------------------------

import CaliforniaStateBoundary from '../../../json/CALIFORNIA/CaliforniaStateBoundaries.json'
// import CaliforniaDistricts from '../../../json/CALIFORNIA/CaliforniaDistricts.json'
// import CaliforniaPrecincts from '../../../json/CALIFORNIA/CaliforniaPrecinct.json'

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
        // this.mapZoom = 5;

        // Map Filters
        this.precinctView = false;
        this.districtView = false;
        this.stateView = true;
    }

    // 6 for district view
    // 8 or 9 for precinct view
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
        // scrollWheelZoom={false} 
        // zoomControl={true}
        return(
                <Map id="ourMap" center={this.mapCenter} zoom={this.mapZoom} onzoomend={(e) => this.props.handleZoomChange(e)} >
                    <HeatmapLayer
                                // fitBoundsOnLoad
                                // fitBoundsOnUpdate
                                points={heatPoints}
                                longitudeExtractor={m => m[1]}
                                latitudeExtractor={m => m[0]}
                                intensityExtractor={m => parseFloat(m[2])} 
                    />
                    {/* <ZoomControl position = 'bottomleft' > </ZoomControl> */}
                <TileLayer
                    attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

                    {/* <GeoJSON weight="1" color="red" key="California" data={CaliforniaStateBoundary} onClick={ () => this.props.changeCurrentState("California")}/>  */}
                    {/* <GeoJSON key="California" data={CaliforniaDistricts} onClick={ () => this.props.changeCurrentState("California")}/>   */}
                    <GeoJSON weight="1" color="red" key="California" data={CaliforniaStateBoundary} onClick={ () => this.props.changeCurrentState("California")}/> 
                    
                    <GeoJSON weight="1" color="red" key="Georgia" data={GeorgiaStateBoundary} onClick={ () => this.props.changeCurrentState("Georgia")} />
                    {/* <GeoJSON key='Georgia' data={GeorgiaPrecincts} /> */}
                    {/* <GeoJSON key='Georgia' data={GeorgiaDistricts} onClick={ () => this.props.changeCurrentState("Georgia")}/>  /> */}

                    <GeoJSON weight="1" color="red" key='NewYork' data={NewYorkStateBoundary} onClick={ () => this.props.changeCurrentState("New York")}/>
                    {/* <GeoJSON key='NewYorkDistricts' data={NYSAssemblyDistricts} /> */}
                    {/* <GeoJSON key='NewYorkDistricts' data={NewYorkDistricts} />  */}
                    {/* <GeoJSON key='NewYork' data={NYDistricts} onClick={ () => this.props.changeCurrentState("New York")}/> */}

                </Map> 
            );
        }
}

export default OurMap;