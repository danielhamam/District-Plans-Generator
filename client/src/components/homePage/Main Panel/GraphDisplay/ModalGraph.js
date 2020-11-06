import React, {Component} from 'react';
import { Modal, Button } from 'react-bootstrap';
import CanvasJSReact from './canvasjs-3.0.5/canvasjs.react';
var CanvasJS = CanvasJSReact.CanvasJS;
var CanvasJSChart = CanvasJSReact.CanvasJSChart;

// Triggered when clicking "View" on a job

class ModalGraph extends Component {
    constructor () {
        super();
        this.state = {}
    }
    render() {
        return (
            <Modal backdrop="static" show={this.props.showModal} onHide={this.props.handleModalGraph}>  
                <Modal.Header closeButton >
                    <Modal.Title> Graph Display </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <CanvasJSChart options = {this.props.graphOptions}/>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="danger" onClick={this.props.handleModalGraph}>Close</Button>
                </Modal.Footer>
            </Modal>
        );
    }
}

export default ModalGraph;