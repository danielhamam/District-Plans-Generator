// import Plot from 'react-plotly.js';
import React, {Component} from 'react';
import ModalGraph from './ModalGraph'
import { Modal } from 'react-bootstrap';

import CanvasJSReact from './canvasjs-3.0.5/canvasjs.react';
//var CanvasJSReact = require('./canvasjs.react');
var CanvasJS = CanvasJSReact.CanvasJS;
var CanvasJSChart = CanvasJSReact.CanvasJSChart;

class GraphDisplay extends Component {
    constructor () {
        super();
        var graphTitle = "Districting Plans";
        this.state = { 
            dataPlot : { 
                y: [0, 1, 1, 2, 3, 5, 8, 13, 21],
                boxpoints: 'all',
                jitter: 0.3,
                pointpos: -1.8,
                type: 'box'
            },
            layoutPlot : {autosize:true, title:graphTitle},
            typePlot : {type: 'box', x: [1, 2, 3], y: [2, 5, 3]},
            modalOpen : false
        }
    }

    toggleModalGraph = () => {
        if (this.state.modalOpen == false) this.setState({modalOpen : true});
        else this.setState({modalOpen : false});
    }

    render() {

        const options = {
			animationEnabled: true,
			theme: "light2",
			title:{
				text: "District Comparison" // Existing plan v.s probabilistic plan - R. Kelly's words. Existing plan should "overlap" or be compared alongside with these district plans.
            },
            legend:{
                horizontalAlign: "right",
                verticalAlign: "top",
            },
			axisY: {
				title: "Percent Population by Demographic Filter: "
            },
            axisX: {
                title: "District"
            },
			// data: [{
			// 	type: "boxAndWhisker",
			// 	color: "black",
			// 	upperBoxColor: "#A3A3A3",
			// 	lowerBoxColor: "#494949",
			// 	yValueFormatString: "###.0# ",
			// 	dataPoints: [
			// 		{ label: "1", y: [67.5, 70.55, 76.705, 79.406, 73.15] },
			// 		{ label: "2", y: [67.41, 71.03, 78.05, 80.657, 74.36] },
			// 		{ label: "3", y: [64.94, 70.565, 78.17,  80.94, 75.345] },
			// 		{ label: "4", y: [69.18, 71.06, 76.819, 79.425, 73.4] },
			// 		{ label: "5", y: [69.62, 72.045, 78.7, 81.70, 75.8] },
			// 		{ label: "6", y: [65.86, 70.255, 78.0625, 85, 75.24] },
			// 		{ label: "7", y: [61.31, 68.625, 72.035, 75.56, 70.915]}
			// 	]
            // }]
            data: [{
                type: "boxAndWhisker",
                legendText: "Calculated",
                showInLegend: true,
				color: "black",
				upperBoxColor: "#A72A17",
				lowerBoxColor: "#A3A3A3",
				yValueFormatString: "###.0# ",
				dataPoints: [
					{ label: "1", y: [67.5, 70.55, 76.705, 79.406, 73.15] },
					{ label: "2", y: [67.41, 71.03, 78.05, 80.657, 74.36] },
					{ label: "3", y: [64.94, 70.565, 78.17,  80.94, 75.345] },
					{ label: "4", y: [69.18, 71.06, 76.819, 79.425, 73.4] },
					{ label: "5", y: [69.62, 72.045, 78.7, 81.70, 75.8] },
					{ label: "6", y: [65.86, 70.255, 78.0625, 85, 75.24] },
					{ label: "7", y: [61.31, 68.625, 72.035, 75.56, 70.915]}
				]
            },
            {
                type: "scatter",
                legendText: "Enacted",
                showInLegend: true,
                markerSize: 8,
                color: "#007BFF",
                toolTipContent: "District Percentage: {y}",
				dataPoints: [
                    { x: 0, y: 70},
					{ x: 1, y: 74},
					{ x: 2, y: 65},
					{ x: 3, y: 82},
					{ x: 4, y: 87},
					{ x: 5, y: 92},
					{ x: 6, y: 72},
				]
			}]
            
            }
            
        

        return (
            <div> 
                <div onClick={this.toggleModalGraph} >
                    <CanvasJSChart options = {options}/>

                    {/* <Plot className="plotView1" type={this.state.typePlot} data={this.state.dataPlot} layout={this.state.layoutPlot} /> */}
                </div>
                <div>
                    <ModalGraph graphTitle={this.graphTitle} data={this.state.dataPlot} layoutPlot={this.state.layoutPlot} typePlot={this.state.typePlot} toggleModal ={this.toggleModalGraph} showModal={this.state.modalOpen} > </ModalGraph>
                </div>
                    < br />
                    <button className="btn-primary buttonGraphEdit" onClick={this.toggleModalGraph}> Graph Edit Panel</button>
                    < br />
                    < br />
                    <small className="form-text text-muted"> This graph represents the currently selected districting plan compared to the enacted plan. If you would like to edit the contents of the graph, please click on the graph or select the below button to open the editing panel. </small>
            </div>
        );
    }
}

export default GraphDisplay;
