import React, { Component } from 'react';
import { Map, TileLayer, ZoomControl, GeoJSON} from 'react-leaflet';
import HeatmapLayer from 'react-leaflet-heatmap-layer';
import { heatPoints } from './realWorldTest.js';

import CaliforniaStateBoundary from '../../../json/CALIFORNIA/CaliforniaStateBoundaries.json'
import GeorgiaStateBoundary from '../../../json/GEORGIA/GeorgiaStateBoundaries.json';
import NewYorkStateBoundary from '../../../json/NEW_YORK/NewYorkStateBoundaries.json';
// import NYDistricts from '../../../json/NEW_YORK/NewYorkDistricts.json';

class OurMap extends Component {
    constructor () {
        super();
        this.state = {}

        this.mapCenter = [39, -105];
        // Map Filters
        this.precinctView = false;
        this.districtView = false;
        this.stateView = true;
    }

    handleZoomChange = (e) => { 

        this.mapZoom = e.target._zoom;
        this.props.changeCurrentZoom(this.mapZoom);

        if (this.mapZoom <= 7) { // 0 = delete view, 1 = insert view
            this.props.changeViewFromZoom("Districts", 0); 
            this.props.changeViewFromZoom("Precincts", 0); 
        }
        else if (this.mapZoom == 8) {
            this.props.changeViewFromZoom("Precincts", 0); 
            this.props.changeViewFromZoom("Districts", 1); 
            console.log("district view")
        }
        else if (this.mapZoom > 9) {
            this.props.changeViewFromZoom("Districts", 0);
            this.props.changeViewFromZoom("Precincts", 1);
            console.log("precinct view")
        }
        else {}
    }

    render() {

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

        return (
                <Map id="ourMap" center={this.mapCenter} zoom={this.mapZoom} onzoomend={(e) => this.handleZoomChange(e)} >
                    <HeatmapLayer
                                // fitBoundsOnLoad
                                // fitBoundsOnUpdate
                                points={heatPoints}
                                longitudeExtractor={m => m[1]}
                                latitudeExtractor={m => m[0]}
                                intensityExtractor={m => parseFloat(m[2])} 
                    />
                <TileLayer
                    attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

                <GeoJSON weight="1" color="red" key="California" data={CaliforniaStateBoundary} onClick={ () => this.props.changeCurrentState("California")}/> 
                <GeoJSON weight="1" color="red" key="Georgia" data={GeorgiaStateBoundary} onClick={ () => this.props.changeCurrentState("Georgia")} />
                <GeoJSON weight="1" color="red" key='NewYork' data={NewYorkStateBoundary} onClick={ () => this.props.changeCurrentState("New York")}/>

                {/* From map view filter */}
                {this.props.districtsView ? this.props.districtsContent : ""}
                {this.props.precinctsView ? this.props.precinctsContent : ""}

                </Map> 
            );
        }
}

export default OurMap;