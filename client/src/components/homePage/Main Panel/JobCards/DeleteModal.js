import React, {Component} from 'react';
import { Modal, Button } from 'react-bootstrap';

// Triggered when clicking "View" on a Job

class DeleteModal extends Component {
    constructor () {
        super();
        this.state = {
        }
    }

    render() {

        return (
        // "Props" means properties. We communicated with JobCard.js to connect the card's buttons with the modal.
    
                <Modal backdrop="static" show={this.props.showDeleteModal} onHide={this.props.showDeleteModal}>  
                    <Modal.Header closeButton >
                    <h5> Are you sure you want to delete this job? </h5>
                    </Modal.Header>
                    <Modal.Body> <p className="jobTitle"> NOTE: You can not undo this action.</p> </Modal.Body>
                    <Modal.Footer>
                        <Button variant="danger" onClick={this.props.toggleDeleteModal} >No</Button>
                        <Button variant="primary" onClick={(e) => this.props.handleDeleteJob(e, this.props.jobCard)}>Yes</Button>
                    </Modal.Footer>
                </Modal>
            // {/* </div> */}
            
        );
    }
}

export default DeleteModal;