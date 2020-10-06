import React, {Component} from 'react';
import { Modal, Button } from 'react-bootstrap';
// import Plot from 'react-plotly.js';

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

        const options = {
			animationEnabled: true,
			theme: "light2",
			title:{
				text: "Graph Title"
			},
			axisY: {
				title: "Demographic Filter"
			},
			data: [{
				type: "boxAndWhisker",
				color: "black",
				upperBoxColor: "#A3A3A3",
				lowerBoxColor: "#494949",
				yValueFormatString: "###.0# years",
				dataPoints: [
					{ label: "District", y: [67.5, 70.55, 76.705, 79.406, 73.15] },
					{ label: "District", y: [67.41, 71.03, 78.05, 80.657, 74.36] },
					{ label: "District", y: [64.94, 70.565, 78.17,  80.94, 75.345] },
					{ label: "District", y: [69.18, 71.06, 76.819, 79.425, 73.4] },
					{ label: "District", y: [69.62, 72.045, 78.7, 81.70, 75.8] },
					{ label: "District", y: [65.86, 70.255, 78.0625, 85, 75.24] },
					{ label: "District", y: [61.31, 68.625, 72.035, 75.56, 70.915]}
				]
			}]
		}

        return (
        // "Props" means properties. We communicated with BatchCard.js to connect the card's buttons with the modal.
    
                <Modal backdrop="static" show={this.props.showModal} onHide={this.props.toggleModal}>  
                    <Modal.Header closeButton >
                        <Modal.Title> Testing Graph Display </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <CanvasJSChart options = {options}/>
                        {/* <Plot className="plotView2" layout={this.props.layoutPlot} type={this.props.typePlot} data={this.props.dataPlot} /> */}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="danger" onClick={this.props.toggleModal}>Close</Button>
                        <Button variant="primary" >Testing Button</Button>
                    </Modal.Footer>
                </Modal>
            // {/* </div> */}
            
        );
    }
}

export default ModalGraph;