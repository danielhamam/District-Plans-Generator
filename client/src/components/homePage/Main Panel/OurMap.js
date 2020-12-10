import React, { Component } from 'react';
import { Map, TileLayer, ZoomControl, GeoJSON} from 'react-leaflet';
import HeatmapLayer from 'react-leaflet-heatmap-layer';
// import { heatPoints } from '../../../json/HeatPoints.js';

import MarylandStateBoundary from '../../../json/MARYLAND/md_state.json';
import GeorgiaStateBoundary from '../../../json/GEORGIA/ga_state.json';
import PennsylvaniaStateBoundary from '../../../json/PENNSYLVANIA/pa_state.json';

class OurMap extends Component {
    constructor () {
        super();
        this.state = {}

        this.mapCenter = [39, -105];
        // Map Filters
        this.precinctView = false;
        this.districtView = false;
        this.stateView = true;
        this.gradient = {
            0.0: 'green',
            0.5: 'yellow',
            1.0: 'red'
        }
    }
  

    handleZoomChange = (e) => { 

        this.mapZoom = e.target._zoom;
        this.props.changeCurrentZoom(e.target._zoom);

        if (this.mapZoom <= 7) { // 0 = delete view, 1 = insert view
            this.props.changeViewFromZoom("Districts", 0); 
            this.props.changeViewFromZoom("Precincts", 0); 
        }
        else if (this.mapZoom == 8) {
            this.props.changeViewFromZoom("Precincts", 0); 
            this.props.changeViewFromZoom("Districts", 1); 
            console.log("district view")
        }
        else if (this.mapZoom >= 9) {
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
        else if (this.props.currentState == "Maryland") {
            this.mapCenter = [38.5, -78.417931];
            this.mapZoom = 7.2;
        }
        else if (this.props.currentState == "Pennsylvania") {
            this.mapCenter = [40.712776, -79.005974];
            this.mapZoom = 7;
        }
        else {
            this.mapCenter = [39, -105];
            this.mapZoom = 5;
        }

        return (
                <Map id="ourMap" center={this.mapCenter} zoom={this.mapZoom} onzoomend={(e) => this.handleZoomChange(e)} >
                <TileLayer
                    attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />

                <GeoJSON weight={1} color="red" key="Maryland" data={MarylandStateBoundary} onClick={ () => this.props.changeCurrentState("MD", "Maryland")}/> 
                <GeoJSON weight={1} color="red" key="Georgia" data={GeorgiaStateBoundary} onClick={ () => this.props.changeCurrentState("GA", "Georgia")} />
                <GeoJSON weight={1} color="red" key='Pennsylvania' data={PennsylvaniaStateBoundary} onClick={ () => this.props.changeCurrentState("PA", "Pennsylvania")}/>

                {/* From map view filter */}
                { (this.props.districtsView) ? this.props.districtsContent : ""}
                {this.props.precinctsView ? this.props.precinctsContent : ""}

                </Map> 
            );
        }
}

export default OurMap;