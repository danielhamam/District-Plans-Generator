import Plot from 'react-plotly.js';
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
                    <Plot className="plotView1" type={this.state.typePlot} data={this.state.dataPlot} layout={this.state.layoutPlot} />
                </div>
                <div>
                    <ModalGraph graphTitle={this.graphTitle} data={this.state.dataPlot} layoutPlot={this.state.layoutPlot} typePlot={this.state.typePlot} toggleModal ={this.toggleModalGraph} showModal={this.state.modalOpen} > </ModalGraph>
                </div>
            </div>
        );
    }
}

export default GraphDisplay;
