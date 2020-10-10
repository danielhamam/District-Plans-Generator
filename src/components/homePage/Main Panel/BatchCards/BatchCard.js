import React, {Component} from 'react';
import ModalBatch from './ModalBatch'
import DeleteModal from './DeleteModal'

class BatchCard extends Component {
    constructor () {
        super();
        this.state = {
            selected : false,
            name : "", // originally empty, gets filled when we render
            id : "2",
            summary : "", // this would be the analysis summary, display in modal

            // Toggle Modals
            showViewModal : false,
            showDeleteModal : false,
        }
        this.batchCardClassStyle = "";
        this.goTop = "";
        this.statusColor = "";
        // To make them same as props (for now)
        this.compactness = "";
        this.numberPlans = "";
        this.minorityAnalyzed = "";
        this.populationLimit = "";
        this.status = false; // we're going to say false for pending, true for ready
    }

    toggleSelection = (e) => {
        // do something with batch

        if (this.status == false) return;

        if (this.state.showViewModal == true && this.props.selectedBatchCheck== false) {
            this.setState({showViewModal : false});
            this.goTop="";
        }

        if (this.state.selected == false && this.props.selectedBatchCheck== false) {
            // Select
            this.setState({selected: true});
            this.props.toggleSelectedBatchCheck();
            this.props.changeCurrentBatch(this.state.name);
            this.goTop="goTopBatch ";
        }
        else if (this.state.selected == false && this.props.selectedBatchCheck == true) {
            // Cant Select
            this.setState({selected : false});
            this.goTop="";
        }
        else if (this.state.selected == true && this.props.selectedBatchCheck == true) {
            // Deselect
            this.setState({selected: false});
            this.props.toggleSelectedBatchCheck();
            this.props.changeCurrentBatch("");
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

    deleteBatch = (e) => {
        // Here you would delete the batch card
    }

    render() {

        // Whenever we do setState, it rerenders
        if (this.state.selected == true) {
            this.batchCardClassStyle = "batchCard badge badge-pill badge-dark ";
        }
        else {
            this.batchCardClassStyle = "batchCard badge badge-pill badge-light ";
        }

        // here I would say, let's check if status is ready
        this.status = this.props.status;
        if (this.status == true ) this.statusColor = " batchSuccess ";
        else if (this.status == false) this.statusColor = " batchPending ";

        // --------------------------------------------------------
              // LETS SET THE NAME / COMPACTNESS / NUMBER PLANS
            // Check batch name, if it's empty the id is the name
        // --------------------------------------------------------

        if (this.props.batchName == "") this.setState({name : "Batch " + this.state.id}); // default name 
        else if (this.props.batchName != this.state.name) this.setState({name : this.props.batchName}); // custom name

        this.compactness = this.props.compactness;
        this.numberPlans = this.props.numberPlans;
        this.populationLimit = this.props.populationLimit;
        this.minorityAnalyzed = this.props.minorityAnalyzed;

        return (
            <div> 
                <div className={this.batchCardClassStyle + this.goTop + this.statusColor} onClick={this.toggleSelection}>
                    <div className="batchcardContents">
                        <button className="batchcardDelete badge badge-pill badge-danger" onClick={this.toggleDeleteModal} > <div className="deleteText"> X </div> </button>
                        <span className="batchcardTitle"> {this.state.name} </span> 
                        <button className="batchcardView badge badge-pill badge-dark" onClick={this.toggleViewModal}> <div className="viewText" > View </div> </button>
                    </div> 
                    <br /> 
                    <br />
                </div>
                <DeleteModal showDeleteModal={this.state.showDeleteModal} deleteBatch={this.deleteBatch} toggleDeleteModal={this.toggleDeleteModal} batchName={this.state.name} />
                <ModalBatch populationLimit={this.populationLimit} minorityAnalyzed={this.minorityAnalyzed} compactness={this.compactness} numberPlans={this.numberPlans} status={this.status} currentSelected={this.state.selected} selectedBatchCheck={this.props.selectedBatchCheck} toggleSelection={this.toggleSelection} batchName={this.state.name} toggleViewModal={this.toggleViewModal} showViewModal={this.state.showViewModal}/>
            </div>
        );
    }
}

export default BatchCard;