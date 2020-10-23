import React, {Component} from 'react';
import { Modal, Button } from 'react-bootstrap';
import CanvasJSReact from './canvasjs-3.0.5/canvasjs.react';
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
        return (
        // "Props" means properties. We communicated with BatchCard.js to connect the card's buttons with the modal.
    
                <Modal backdrop="static" show={this.props.showModal} onHide={this.props.toggleModal}>  
                    <Modal.Header closeButton >
                        <Modal.Title> Graph Display </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <CanvasJSChart options = {this.props.graphOptions}/>
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