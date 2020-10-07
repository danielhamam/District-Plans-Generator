import React, {Component} from 'react';
import { Modal, Button } from 'react-bootstrap';

// Triggered when clicking "View" on a batch

class ModalBatch extends Component {
    constructor () {
        super();
        this.state = {
        }
        // To edit class name of modal display
        this.nameSelect = "Select"
        this.selectButtonColor = "primary"
        this.activeSelection = ""
        // To show if pending or ready
        this.statusDisplay = ""
        this.colorStatus = ""
    }
    render() {

        // To check to disable or enable select button

        if ( (this.props.selectedCard == true && this.props.currentSelected == false) | this.props.status == false ) {
            // turn the select button gray
            this.nameSelect = "Select"
            this.selectButtonColor = "secondary";
            this.activeSelection = "disabled";
        }
        else if (this.props.selectedCard == true && this.props.currentSelected == true) {
            this.nameSelect = "Deselect"
            this.selectButtonColor = "primary";
            this.activeSelection = "active";
        }
        else {
            this.nameSelect = "Select"
            this.selectButtonColor = "primary";
            this.activeSelection = "active";
        }

        // To display whether it is ready or pending
        if (this.props.status == false) {
            this.statusDisplay = "Pending";
            this.colorStatus = " batchdisplayPending"
        }
        else if (this.props.status == true) {
            this.statusDisplay = "Ready";
            this.colorStatus = " batchdisplaySuccess"
        }

        return (
        // "Props" means properties. We communicated with BatchCard.js to connect the card's buttons with the modal.
    
                <Modal backdrop="static" show={this.props.showModal} onHide={this.props.toggleModal}>  
                    <Modal.Header closeButton >
                        <Modal.Title> 
                            <h4 className="batchTitle"> {this.props.batchName} </h4>
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                    {/* First let's display the status of the batch */}
                    <p> 
                        <span> Status: </span> 
                        <span className={"inline " + this.colorStatus}> {this.statusDisplay} </span>
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
                        <Button variant="danger" onClick={this.props.toggleModal}>Close</Button>
                        <Button className = {this.activeSelection} variant={this.selectButtonColor} onClick={this.props.toggleSelection}>{this.nameSelect}</Button>
                    </Modal.Footer>
                </Modal>
            // {/* </div> */}
            
        );
    }
}

export default ModalBatch;