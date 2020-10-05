import React, {Component} from 'react';
import { Modal, Button } from 'react-bootstrap';

// Triggered when clicking "View" on a batch

class DeleteModal extends Component {
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
    
                <Modal backdrop="static" show={this.props.showDeleteModal} onHide={this.props.showDeleteModal}>  
                    <Modal.Header closeButton >
                    <h5> Are you sure you want to delete this batch? </h5>
                    </Modal.Header>
                    <Modal.Body> <p className="batchTitle"> NOTE: You can not undo this action.</p> </Modal.Body>
                    <Modal.Footer>
                        <Button variant="danger" onClick={this.props.toggleDeleteModal} >No</Button>
                        <Button variant="primary" onClick={this.props.deleteBatch}>Yes</Button>
                    </Modal.Footer>
                </Modal>
            // {/* </div> */}
            
        );
    }
}

export default DeleteModal;