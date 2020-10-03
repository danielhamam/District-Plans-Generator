import React, {Component} from 'react';
import { Modal, Button } from 'react-bootstrap';

// Triggered when clicking "View" on a batch

class ModalBatch extends Component {
    constructor () {
        super();
        this.state = {
        }
    }
    render() {

        return (
            
        // "Props" means properties. We communicated with BatchCard.js to connect the card's buttons with the modal.
    
                <Modal show={this.props.showModal} onHide={this.props.toggleModal}>  
                    <Modal.Header closeButton >
                        <Modal.Title>Batch test</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <p>All former inputs here</p>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={this.props.toggleModal}>Close</Button>
                        <Button variant="primary">Select</Button>
                    </Modal.Footer>
                </Modal>
            // {/* </div> */}
            
        );
    }
}

export default ModalBatch;