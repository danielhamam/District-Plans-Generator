import React, {Component} from 'react';
import { Modal, Button } from 'react-bootstrap';

// Trigger: onClick "Delete" on a plan

class DeletePlanModal extends Component {
    constructor () {
        super();
        this.state = {}
    }
    render() {
        return (
                <Modal backdrop="static" show={this.props.showDeleteModal} onHide={this.props.showDeleteModal}>  
                    <Modal.Header closeButton >
                    <h5> Are you sure you want to delete this district plan? </h5>
                    </Modal.Header>
                    <Modal.Body> <p className="jobTitle"> NOTE: You can not undo this action.</p> </Modal.Body>
                    <Modal.Footer>
                        <Button variant="danger" onClick={this.props.toggleDeleteModal} >No</Button>
                        <Button variant="primary" onClick={(e) => this.props.handleDeletePlan(e, this.props.plan)}>Yes</Button>
                    </Modal.Footer>
                </Modal>
        );
    }
}

export default DeletePlanModal;