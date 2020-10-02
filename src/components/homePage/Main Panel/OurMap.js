import { LatLngBounds, Polyline } from 'leaflet';
// import ReactMapGL from 'react-map-gl';
// import 'mapbox-gl/dist/mapbox-gl.css';
import React, { Component } from 'react'
import { Map, Marker, Popup, TileLayer, FeatureGroup, Circle, ZoomControl} from 'react-leaflet'
import { EditControl } from 'react-leaflet-draw';

function OurMap() {

    const position = [39, -105]
    return(
        <Map id="ourMap" center={position} zoom={5} zoomControl={false}>
            <ZoomControl position = 'bottomleft'> </ZoomControl>
          <TileLayer
            attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
        </Map> 
    );
}

export default OurMap;