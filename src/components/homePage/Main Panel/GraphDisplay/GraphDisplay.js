// import Plot from 'react-plotly.js';
import React, {Component} from 'react';
import ModalGraph from './ModalGraph'
import { Modal } from 'react-bootstrap';

import CanvasJSReact from './canvasjs-3.0.5/canvasjs.react';
var CanvasJS = CanvasJSReact.CanvasJS;
var CanvasJSChart = CanvasJSReact.CanvasJSChart;

class GraphDisplay extends Component {
    constructor () {
        super();
        // var graphTitle = "Districting Plans";
        this.state = { 
            selectedFilters : null,
            modalOpen : false,
            graphOptions : {
                animationEnabled: true,
                theme: "light2",
                title:{
                    text: "VAP Filter vs. Indexed Districts" // Existing plan v.s probabilistic plan - R. Kelly's words. Existing plan should "overlap" or be compared alongside with these district plans.
                },
                legend:{
                    horizontalAlign: "right",
                    verticalAlign: "top",
                },
                axisY: {
                    title: "Voting Age Population (VAP) by Demographic Filter",
                },
                axisX: {
                    title: "Indexed Districts"
                },
                
                data: [{
                    type: "boxAndWhisker",
                    legendText: "Generated",
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
                        { x: 0, y: 68},
                        { x: 1, y: 71},
                        { x: 2, y: 73},
                        { x: 3, y: 74},
                        { x: 4, y: 77},
                        { x: 5, y: 80},
                        { x: 6, y: 83},
                    ]
                }]
            },
        }
    }

    toggleModalGraph = () => {
        // console.log(this.state.selectedFilters);
        if (this.state.modalOpen == false) this.setState({modalOpen : true});
        else this.setState({modalOpen : false});
    }

    render() {         

        // if(this.state.selectedFilters != this.props.selectedFilters) {
        //     this.setState({selectedFilters : this.props.selectedFilters});
        // }

        // Selected filters would be chosen from the map, and then passed to here. 
        // No need to have a dropdown allowing them to choose from 

        return (
            <div className="graphDisplayWrapper"> 
                <div onClick={this.toggleModalGraph} >
                    <div id="plotView1"> 
                        <CanvasJSChart options = {this.state.graphOptions}/>
                    </div>
                </div>
                <div>
                    <ModalGraph graphOptions={this.state.graphOptions} toggleModal ={this.toggleModalGraph} showModal={this.state.modalOpen} > </ModalGraph>
                </div>
                    < br />
                    < br />
                    < br />
            </div>
        );
    }
}

export default GraphDisplay;
