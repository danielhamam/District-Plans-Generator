import React, {Component} from 'react';
import { Modal, Button, Accordion, Card } from 'react-bootstrap';

// Triggered when clicking "View" on a Job

class ViewJobModal extends Component {
    constructor () {
        super();
        this.state = {}
        // To edit class name of modal display
        this.selectStatus = "Select"
        this.selectButtonColor = "primary"
        this.disabledStatus = ""
        this.readyColorStatus = ""
    }
    render() {
        if ( (this.props.selectedJobCheck == true && this.props.currentSelected == false) | this.props.status == "Pending" ) {
            this.selectStatus = "Select"
            this.selectButtonColor = "secondary"; // gray
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
        if (this.props.status == "Pending") this.readyColorStatus = " jobdisplayPending"
        else if (this.props.status == "Running") this.readyColorStatus = " jobdisplayRunning"
        else if (this.props.status == "Completed") this.readyColorStatus = " jobdisplaySuccess"
        return (
                <Modal backdrop="static" show={this.props.showViewModal} onHide={this.props.toggleXclose}>  
                    <Modal.Header closeButton >
                        <Modal.Title> 
                            <h4 className="jobTitle"> {this.props.jobName} </h4>
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                    {/* First let's display the status of the Job */}
                    <p> 
                        <span> Status: </span> 
                        <span className={"inline " + this.readyColorStatus}> {this.props.status} </span>
                    </p>
                    {/* Second let's display the inputs chosen by the user */}
                    <p> 
                        <span> Number of plans: </span> 
                        <span className={"inline "}> {this.props.plansAmount} </span>
                    </p>
                    <p> 
                        <span> Compactness: </span> 
                        <span className={"inline "}> {this.props.compactness} </span>
                    </p>
                    <p> 
                        <span> Population Difference Limit: </span> 
                        <span className={"inline "}> {this.props.populationDifference} </span>
                    </p>
                    <p> 
                        <span> Focused Minority Group(s): </span> 
                        <span className={"inline "}> {this.props.minorityAnalyzed.join(", ")} </span>
                    </p>

                    <Accordion defaultActiveKey="1">
                        <Card>
                            <Card.Header>
                                <Accordion.Toggle as={Button} variant="link" eventKey="0">
                                    More Summary Information
                                </Accordion.Toggle>
                            </Card.Header>
                            <Accordion.Collapse eventKey="0">
                                <Card.Body>
                                    This is where we have more summary info. test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test 
                                </Card.Body>
                            </Accordion.Collapse>
                        </Card>
                    </Accordion>
                    {/* <p> 
                        <span> Analysis Summary: </span> 
                        <span className={"inline "} > Testing Analysis Summary </span>
                    </p> */}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="danger" onClick={this.props.toggleViewModal}>Close</Button>
                        <Button className = {this.disabledStatus} variant={this.selectButtonColor} onClick={this.props.toggleSelection}>{this.selectStatus}</Button>
                    </Modal.Footer>
                </Modal>
        );
    }
}

export default ViewJobModal;