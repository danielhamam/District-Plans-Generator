import React, {Component} from 'react';
import { Modal, Button } from 'react-bootstrap';
import Select from 'react-select';

// import { Canvas } from 'react-canvas-js'

import CanvasJSReact from './canvasjs-3.0.5/canvasjs.react';
//var CanvasJSReact = require('./canvasjs.react');
var CanvasJS = CanvasJSReact.CanvasJS;
var CanvasJSChart = CanvasJSReact.CanvasJSChart;

// Triggered when clicking "View" on a batch

class ModalGraph extends Component {
    constructor () {
        super();
        this.state = {
        }
    }
    render() {

        const filterOptions = [
            {
              label: "Ethnicity",
              options: [
                { label:"White", value: "white"},
                { label:"African American", value: "africanamerican"},
                { label:"Latino", value: "latino"},
                { label:"Asian", value: "asian"},
                { label:"American Indian", value: "americanindian"},
                { label:"Hawaiian", value: "hawaiian"},
                { label:"Other", value: "other"},
              ]},
              {
              label: "Political Party",
              options: [
                { label:"Democratic", value: "democratic"},
                { label:"Republican", value: "republican"},
                { label:"Libertarian", value: "libertarian"},
                { label:"Green Party", value: "greenparty"},
                { label:"Other", value: "other"},
              ]},
              {
              label: "Cluster",
              options: [
                { label:"Precincts", value: "precincts"},
                { label:"Districts", value: "districts"},
                { label:"Counties", value: "counties"},
              ]},
          ];

        return (
        // "Props" means properties. We communicated with BatchCard.js to connect the card's buttons with the modal.
    
                <Modal backdrop="static" show={this.props.showModal} onHide={this.props.toggleModal}>  
                    <Modal.Header closeButton >
                        <Modal.Title> Graph Display </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <CanvasJSChart options = {this.props.options}/>
                        {/* <Plot className="plotView2" layout={this.props.layoutPlot} type={this.props.typePlot} data={this.props.dataPlot} /> */}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="danger" onClick={this.props.toggleModal}>Close</Button>
                        {/* <Button variant="primary" >Testing Button</Button> */}
                    </Modal.Footer>
                </Modal>
            // {/* </div> */}
            
        );
    }
}

export default ModalGraph;