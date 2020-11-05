import React, {Component} from 'react';
import ModalJob from './ModalJob'
import DeleteModal from './DeleteModal'

class JobCard extends Component {
    constructor () {
        super();
        this.state = {

            // Attributes of job card
            selected : false,
            name : "", // originally empty, gets filled when we render
            id : "2",
            summary : "", // this would be the analysis summary, display in modal

            // Toggle Modals
            showViewModal : false,
            showDeleteModal : false,

            // Helper Variables
            enactedFirst : false, // true = showed upon startup, false = never showed. We want enacted to be in affect in start

        }

        // Class Name (Designs, vary based on selection)
        this.JobCardClassStyle = "";
        this.goTop = "";
        this.statusColor = "";
    }

    toggleSelection = (e) => {
        // do something with Job

        if (this.props.status == "Pending") return;

        if (this.state.showViewModal == true && this.props.selectedJobCheck== false) {
            this.setState({showViewModal : false});
            this.goTop="";
        }

        if (this.state.selected == false && this.props.selectedJobCheck== false) {
            // Select
            this.setState({selected: true});
            this.props.toggleSelectedCard();
            this.props.updateCurrentJob(this.props.jobCard, true);
            this.goTop="goTopJob ";
        }
        else if (this.state.selected == false && this.props.selectedJobCheck == true) {
            // Cant Select
            this.setState({selected : false});
            this.goTop="";
        }
        else if (this.state.selected == true && this.props.selectedJobCheck == true) {
            // Deselect
            this.setState({selected: false});
            this.props.toggleSelectedCard();
            this.props.updateCurrentJob(null, false);
            this.goTop="";
        }
        else {
            // is currently selected, but not selected card. do nothing. (impossible scenario)
        }

        // and check if you did this through modal
    }

    toggleViewModal = (e) => {
        e.stopPropagation();
        if (this.state.showViewModal == true) this.setState({showViewModal : false});
        else this.setState({showViewModal : true});
    }

    toggleDeleteModal = (e) => {
        e.stopPropagation();
        if (this.state.showDeleteModal == false) this.setState({showDeleteModal : true});
        else this.setState({showDeleteModal : false});
    }

    handleDeleteJob = (e, job) => {
        this.toggleDeleteModal(e);
        this.props.deleteJob(job);
    }

    render() {

        if (this.state.enactedFirst == false && this.props.selectedJobCheck == false) {
            if (this.props.jobCard.jobID == 1) {
                this.toggleSelection();
                this.setState({enactedFirst : true});
            }
        }

        // Whenever we do setState, it rerenders
        if (this.state.selected == true) {
            this.JobCardClassStyle = "jobCard badge badge-pill badge-dark ";
        }
        else {
            this.JobCardClassStyle = "jobCard badge badge-pill badge-light ";
        }

        // here I would say, let's check if status is ready
        if (this.props.status == "Completed" ) this.statusColor = " jobSuccess ";
        else if (this.props.status == "Pending") this.statusColor = " jobPending ";

        // --------------------------------------------------------
              // LETS SET THE NAME / COMPACTNESS / NUMBER PLANS
            // Check Job name, if it's empty the id is the name
        // --------------------------------------------------------

        // if (this.props.jobName == "") this.setState({name : "Job " + this.jobCard.id}); // default name (id)
        // else if (this.props.jobName != this.state.name) this.setState({name : this.props.jobName}); // user-custom name

        return (
            <div> 
                <div className={this.JobCardClassStyle + this.goTop + this.statusColor} onClick={this.toggleSelection}>
                    <div className="jobcardContents">
                        <button className="jobcardDelete badge badge-pill badge-danger" onClick={this.toggleDeleteModal} > <div className="deleteText"> X </div> </button>
                        <span className="jobcardTitle"> {this.props.jobName} </span> 
                        <button className="jobcardView badge badge-pill badge-dark" onClick={this.toggleViewModal}> <div className="viewText" > View </div> </button>
                    </div> 
                    <br /> 
                    <br />
                </div>
                <DeleteModal showDeleteModal={this.state.showDeleteModal} handleDeleteJob={this.handleDeleteJob} 
                toggleDeleteModal={this.toggleDeleteModal} jobName={this.props.jobName} jobCard={this.props.jobCard} />
                <ModalJob populationLimit={this.props.populationLimit} minorityAnalyzed={this.props.minorityAnalyzed} compactness={this.props.compactness} 
                numberPlans={this.props.numberPlans} status={this.props.status} currentSelected={this.state.selected} selectedJobCheck={this.props.selectedJobCheck} 
                toggleSelection={this.toggleSelection} jobName={this.props.jobName} toggleViewModal={this.toggleViewModal} showViewModal={this.state.showViewModal}
                />
            </div>
        );
    }
}

export default JobCard;