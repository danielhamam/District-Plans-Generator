import { LatLngBounds, Polyline } from 'leaflet';
// import ReactMapGL from 'react-map-gl';
// import 'mapbox-gl/dist/mapbox-gl.css';
import React, { Component } from 'react'
import { Map, Marker, Popup, TileLayer, FeatureGroup, Circle, ZoomControl, GeoJSON} from 'react-leaflet'
import { EditControl } from 'react-leaflet-draw';

// ---------------------------------------------
//                CALIFORNIA IMPORTS
// ---------------------------------------------

import CaliforniaStateBoundary from '../../../json/CALIFORNIA/CaliforniaStateBoundary.json'
import CaliforniaPlaces from '../../../json/CALIFORNIA/CA_Places_TIGER2016.json'

// ---------------------------------------------
//                GEORGIA IMPORTS
// ---------------------------------------------

import GeorgiaStateBoundary from '../../../json/GEORGIA/GeorgiaStateBoundary.json'
import GeorgiaPrecincts from '../../../json/GEORGIA/GeorgiaPrecincts.json' 
import GeorgiaDistricts from '../../../json/GEORGIA/GeorgiaDistricts.json' 

// ---------------------------------------------
//                NEW YORK IMPORTS
// ---------------------------------------------
import NewYorkStateBoundary from '../../../json/NEW_YORK/NewYorkStateBoundary.json'
import NYSAssemblyDistricts from '../../../json/NEW_YORK/NYSAssemblyDistricts.json'

class OurMap extends Component {
    constructor () {
        super();
        this.state = {
            // centerMap : [39, -105],
            // mapZoom : 5
        }
        // const USAcenter = [39, -105];
        // this.currentPositon = USAcenter;
        // const center=

        this.centerMap = [39, -105];
        this.mapZoom = 5;

    }
    render() {

        // Change center/zoom based on which state is selected
        if (this.props.currentState == "Georgia") {
            this.centerMap = [32.5, -85.00078];
            this.mapZoom = 7;
        }
        else if (this.props.currentState == "California") {
            this.centerMap = [36.778259, -119.417931];
            this.mapZoom = 7;
        }
        else if (this.props.currentState == "New York") {
            this.centerMap = [40.712776, -74.005974];
            this.mapZoom = 7;
        }
        else {
            this.centerMap = [39, -105];
            this.mapZoom = 5;
        }

        return(
                <Map id="ourMap" center={this.centerMap} zoom={this.mapZoom} zoomControl={false}>
                    <ZoomControl position = 'bottomleft'> </ZoomControl>
                <TileLayer
                    attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

                    <GeoJSON key="California" data={CaliforniaStateBoundary} /> 
                    {/* <GeoJSON key="California" data={CaliforniaPlaces} />  */}
                    
                    <GeoJSON key='Georgia' data={GeorgiaStateBoundary} />
                    {/* <GeoJSON key='Georgia' data={GeorgiaPrecincts} /> */}
                    {/* <GeoJSON key='Georgia' data={GeorgiaDistricts} /> */}

                    <GeoJSON key='NewYork' data={NewYorkStateBoundary} />
                    {/* <GeoJSON key='NewYorkDistricts' data={NYSAssemblyDistricts} /> */}

                </Map> 

                /* /* <div id="currentBatchMap"> Current Batch: </div>
                <br />
                <div id="currentPlanMap"> Current Plan: </div> */ 
            );
        }
}

export default OurMap;