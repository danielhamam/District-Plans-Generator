// import Plot from 'react-plotly.js';
import React, {Component} from 'react';
import ModalGraph from './ModalGraph'
import { Modal } from 'react-bootstrap';

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

        return (
            <div> 
                <div onClick={this.toggleModalGraph} >

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
