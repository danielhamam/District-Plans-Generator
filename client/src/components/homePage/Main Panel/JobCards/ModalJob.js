import React, {Component} from 'react';
import { Modal, Button } from 'react-bootstrap';

// Triggered when clicking "View" on a Job

class ModalJob extends Component {
    constructor () {
        super();
        this.state = {
        }
        // To edit class name of modal display
        this.selectStatus = "Select"
        this.selectButtonColor = "primary"
        this.disabledStatus = ""
        this.readyStatus = ""
        this.readyColorStatus = ""
        // To show if pending or ready
    }
    render() {

        // To check to disable or enable select button

        if ( (this.props.selectedJobCheck == true && this.props.currentSelected == false) | this.props.status == false ) {
            // turn the select button gray
            this.selectStatus = "Select"
            this.selectButtonColor = "secondary";
            this.disabledStatus = "disabled";
        }
        else if (this.props.selectedJobCheck == true && this.props.currentSelected == true) {
            this.selectStatus = "Deselect"
            this.selectButtonColor = "primary";
            this.disabledStatus = "active";
        }
        else {
            this.selectStatus = "Select"
            this.selectButtonColor = "primary";
            this.disabledStatus = "active";
        }

        // To display whether it is ready or pending
        if (this.props.status == false) {
            this.readyStatus = "Pending";
            this.readyColorStatus = " jobdisplayPending"
        }
        else if (this.props.status == true) {
            this.readyStatus = "Ready";
            this.readyColorStatus = " jobdisplaySuccess"
        }

        return (
        // "Props" means properties. We communicated with JobCard.js to connect the card's buttons with the modal.
    
                <Modal backdrop="static" show={this.props.showViewModal} onHide={this.props.toggleViewModal}>  
                    <Modal.Header closeButton >
                        <Modal.Title> 
                            <h4 className="jobTitle"> {this.props.JobName} </h4>
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                    {/* First let's display the status of the Job */}
                    <p> 
                        <span> Status: </span> 
                        <span className={"inline " + this.readyColorStatus}> {this.readyStatus} </span>
                    </p>
                    {/* Second let's display the inputs chosen by the user */}
                    <p> 
                        <span> Number of plans: </span> 
                        <span className={"inline "}> {this.props.numberPlans} </span>
                    </p>
                    <p> 
                        <span> Compactness: </span> 
                        <span className={"inline "}> {this.props.compactness} </span>
                    </p>
                    <p> 
                        <span> Population Difference Limit: </span> 
                        <span className={"inline "}> {this.props.populationLimit} </span>
                    </p>
                    <p> 
                        <span> Focused Minority Group(s): </span> 
                        <span className={"inline "}> {this.props.minorityAnalyzed} </span>
                    </p>
                    <p> 
                        <span> Analysis Summary: </span> 
                        <span className={"inline "} > Testing Analysis Summary </span>
                    </p>

                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="danger" onClick={this.props.toggleViewModal}>Close</Button>
                        <Button className = {this.disabledStatus} variant={this.selectButtonColor} onClick={this.props.toggleSelection}>{this.selectStatus}</Button>
                    </Modal.Footer>
                </Modal>
            // {/* </div> */}
        );
    }
}

export default ModalJob;