import React, {Component} from 'react';
import ViewJobModal from './ViewJobModal'
import DeleteJobModal from './DeleteJobModal'

class JobCard extends Component {
    constructor () {
        super();
        this.state = {
            selected : false,
            showViewModal : false,
            showDeleteModal : false,
        }
        this.JobCardClassStyle = "";
        this.goTop = "";
        this.statusColor = "";
    }

    toggleSelection = (e) => {

        if (this.props.status == "Pending") return;

        if (this.state.selected == false && this.props.selectedJobCheck== false) { // Select
            this.setState({selected: true});
            this.props.toggleSelectedCard();
            this.props.updateCurrentJob(this.props.jobCard, true);
            this.goTop="goTopJob ";
        }
        else if (this.state.selected == false && this.props.selectedJobCheck == true) { // Cant Select
            this.setState({selected : false});
            this.goTop="";
        }
        else if (this.state.selected == true && this.props.selectedJobCheck == true) { // Deselect
            this.setState({selected: false});
            this.props.toggleSelectedCard();
            this.props.updateCurrentJob(null, false);
            this.goTop="";
        }
        else {} 
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

    handleModalAction = (e, job, type) => { // type 0 = cancel, 1 = delete
        this.toggleDeleteModal(e);
        if (type == "delete") this.props.deleteJob(job);
        else this.props.cancelJob(job) 
    }

    render() {
        if (this.state.selected == true) this.JobCardClassStyle = "jobCard badge badge-pill badge-dark ";
        else this.JobCardClassStyle = "jobCard badge badge-pill badge-light ";
        if (this.props.status == "Completed" ) this.statusColor = " jobSuccess ";
        else if (this.props.status == "Pending") this.statusColor = " jobPending ";
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
                <DeleteJobModal showDeleteModal={this.state.showDeleteModal} handleModalAction={this.handleModalAction} 
                toggleDeleteModal={this.toggleDeleteModal} jobName={this.props.jobName} jobCard={this.props.jobCard} 
                status={this.props.status}
                />
                <ViewJobModal populationDifference={this.props.populationDifference} minorityAnalyzed={this.props.minorityAnalyzed} compactness={this.props.compactness} 
                plansAmount={this.props.plansAmount} status={this.props.status} currentSelected={this.state.selected} selectedJobCheck={this.props.selectedJobCheck} 
                toggleSelection={this.toggleSelection} jobName={this.props.jobName} toggleViewModal={this.toggleViewModal} showViewModal={this.state.showViewModal}
                />
            </div>
        );
    }
}

export default JobCard;