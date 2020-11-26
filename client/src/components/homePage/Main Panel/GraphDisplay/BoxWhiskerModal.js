import React, {Component} from 'react';
import { Modal, Button } from 'react-bootstrap';
import CanvasJSReact from './canvasjs-3.0.5/canvasjs.react';
var CanvasJS = CanvasJSReact.CanvasJS;
var CanvasJSChart = CanvasJSReact.CanvasJSChart;

// Triggered when clicking "View" on a job

class BoxWhiskerModal extends Component {
    constructor () {
        super();
        this.state = {
            title : ""
        }
    }

    receiveModal = () => {

        if (this.state.title != "No Job Selected") {
            return (
                <Modal backdrop="static" show={this.props.showModal} onHide={this.props.handleBoxWhiskerModal}>  
                    <Modal.Header closeButton >
                        <Modal.Title> {this.state.title} </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <CanvasJSChart options = {this.props.graphOptions}/>
                        <br /> 
                        <div> 
                            <b> Focus Group(s): </b> { this.props.currentJob != "" ? (this.props.currentJob.minorityAnalyzed).join(', ') : "" }
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="danger" onClick={this.props.handleBoxWhiskerModal}>Close</Button>
                    </Modal.Footer>
                </Modal>
            )
        }
        else {
            return (
                <Modal backdrop="static" show={this.props.showModal} onHide={this.props.handleBoxWhiskerModal}>  
                <Modal.Header closeButton >
                    <Modal.Title> {this.state.title} </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    Please select a job from the "Your Jobs" tab on the sidebar.
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="danger" onClick={this.props.handleBoxWhiskerModal}>Close</Button>
                </Modal.Footer>
                </Modal>
            )
        }
    }




    render() {
        if (this.props.currentJob == "" && this.state.title != "No Job Selected") this.setState({title : "No Job Selected"})
        else if (this.props.currentJob != "" && this.state.title != this.props.currentJob.jobName) this.setState({title : this.props.currentJob.jobName})

        return (
            <div> 
                {this.receiveModal()}                  
            </div>
        );
    }
}

export default BoxWhiskerModal;