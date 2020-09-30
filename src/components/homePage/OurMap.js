// import ReactMapGL from 'react-map-gl';
// import 'mapbox-gl/dist/mapbox-gl.css';
import React, { Component } from 'react'
import { Map, Marker, Popup, TileLayer } from 'react-leaflet'

function ourMap() {
    const position = [39, -95]

    return(
        <Map id="ourMap" center={position} zoom={5}>
        <TileLayer
          attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
        </Map> 
    )
}

export default ourMap;