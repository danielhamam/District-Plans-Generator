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
            
            options : {
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
				title: "Voting Age Population (VAP) by Demographic Filter: "
            },
            axisX: {
                title: "District"
            },
			
            data: [{
                type: "boxAndWhisker",
                legendText: "Calculated",
                showInLegend: true,
				color: "red",
				upperBoxColor: "#A72A17",
				lowerBoxColor: "#A3A3A3",
				yValueFormatString: "###.0# ",
				dataPoints: [
                    { label: "1", y: [61, 65, 63.5, 70, 68] },
                    { label: "2", y: [63, 68, 66.5, 76, 72] },
                    { label: "3", y: [65, 71, 69.5, 78, 75] },
                    { label: "4", y: [67, 73, 72, 80, 78] },
                    { label: "5", y: [69, 76, 75, 83, 80] },
                    { label: "6", y: [71, 78, 78,  85, 83] },
					{ label: "7", y: [74, 81, 81, 87, 86] },
					
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
            
            },

            modalOpen : false
        }
    }

    toggleModalGraph = () => {
        if (this.state.modalOpen == false) this.setState({modalOpen : true});
        else this.setState({modalOpen : false});
    }

    render() {         

        return (
            <div> 
                <div onClick={this.toggleModalGraph} >
                    <CanvasJSChart options = {this.state.options}/>

                    {/* <Plot className="plotView1" type={this.state.typePlot} data={this.state.dataPlot} layout={this.state.layoutPlot} /> */}
                </div>
                <div>
                    <ModalGraph options={this.state.options} toggleModal ={this.toggleModalGraph} showModal={this.state.modalOpen} > </ModalGraph>
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
