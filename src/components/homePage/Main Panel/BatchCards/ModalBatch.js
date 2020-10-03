import React, {Component} from 'react';
import { Modal, Button } from 'react-bootstrap';

// Triggered when clicking "View" on a batch

class ModalBatch extends Component {
    constructor () {
        super();
        this.state = {
        }
        this.nameSelect = "Select"
        this.selectButtonColor = "primary"
        this.activeStatus = ""
    }
    render() {

        if (this.props.selectedCard == true && this.props.currentSelected == false) {
            // turn the select button gray
            this.nameSelect = "Select"
            this.selectButtonColor = "secondary";
            this.activeStatus = "disabled";
        }
        else if (this.props.selectedCard == true && this.props.currentSelected == true) {
            this.nameSelect = "Deselect"
            this.selectButtonColor = "primary";
            this.activeStatus = "active";
        }
        else {
            this.nameSelect = "Select"
            this.selectButtonColor = "primary";
            this.activeStatus = "active";
        }

        return (
        // "Props" means properties. We communicated with BatchCard.js to connect the card's buttons with the modal.
    
                <Modal backdrop="static" show={this.props.showModal} onHide={this.props.toggleModal}>  
                    <Modal.Header closeButton >
                        <Modal.Title> {this.props.batchName} </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <p>All former inputs here</p>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="danger" onClick={this.props.toggleModal}>Close</Button>
                        <Button className = {this.activeStatus} variant={this.selectButtonColor} onClick={this.props.toggleSelection}>{this.nameSelect}</Button>
                    </Modal.Footer>
                </Modal>
            // {/* </div> */}
            
        );
    }
}

export default ModalBatch;