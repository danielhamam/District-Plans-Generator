import React, {Component} from 'react';
import ModalBatch from './ModalBatch'

class BatchCard extends Component {
    constructor () {
        super();
        this.state = {
            showModal : false,
            selected : false,
            name : "", // originally empty, gets filled when we render
            id : "2",
            // put inputs as one of the varialbes
            summary : "", // this would be the analysis summary, display in modal
            status : false, // we're going to say false for pending, true for ready
        }
        this.classNameCustom = "";
        this.goTop = "";
        this.statusColor = "";
        // To make them same as props (for now)
        this.compactness = "";
        this.numberPlans = "";
    }

    toggleSelection = (e) => {
        // do something with batch

        if (this.state.status == false) {
            return;
        }

        if (this.state.showModal == true && this.props.selected == false) {
            this.setState({showModal : false});
            this.goTop="";
        }

        if (this.state.selected == false && this.props.selectedCard == false) {
            // Select
            this.setState({selected: true});
            this.props.toggleSelectedCard();
            this.props.changeSidebarBatch(this.state.name);
            this.goTop="goTopBatch ";
        }
        else if (this.state.selected == false && this.props.selectedCard == true) {
            // Cant Select
            this.setState({selected : false});
            this.goTop="";
        }
        else if (this.state.selected == true && this.props.selectedCard == true) {
            // Deselect
            this.setState({selected: false});
            this.props.toggleSelectedCard();
            this.props.changeSidebarBatch("");
            this.goTop="";
        }
        else {
            // is currently selected, but not selected card. do nothing. (impossible scenario)
        }

        // and check if you did this through modal
    }

    toggleModal = (e) => {
        e.stopPropagation();
        if (this.state.showModal == true) this.setState({showModal : false});
        else this.setState({showModal : true});
    }

    deleteBatch = (e) => {
        e.stopPropagation();
    }

    render() {

        // Whenever we do setState, it rerenders
        if (this.state.selected == true) {
            this.classNameCustom = "batchCard badge badge-pill badge-dark ";
        }
        else {
            this.classNameCustom = "batchCard badge badge-pill badge-light ";
        }
                // here I would say, let's check if status is ready
        if (this.state.status == true) this.statusColor = "";
        else if (this.state.status == false) this.statusColor = " batchPending ";

        // --------------------------------------------------------
              // LETS SET THE NAME / COMPACTNESS / NUMBER PLANS
            // Check batch name, if it's empty the id is the name
        // --------------------------------------------------------

        if (this.props.batchName == "") this.setState({name : "Batch " + this.state.id}); // default name 
        else if (this.props.batchName != this.state.name) this.setState({name : this.props.batchName}); // custom name

        this.compactness = this.props.compactness;
        this.numberPlans = this.props.numberPlans;

        return (
            <div> 
                <div className={this.classNameCustom + this.goTop + this.statusColor} onClick={this.toggleSelection}>
                    <div className="batchcardContents">
                        <button className="batchcardDelete badge badge-pill badge-danger" onClick={this.deleteBatch} > <div className="deleteText"> X </div> </button>
                        <span className="batchcardTitle"> {this.state.name} </span> 
                        <button className="batchcardView badge badge-pill badge-dark" onClick={this.toggleModal}> <div className="viewText" > View </div> </button>
                    </div> 
                    <br /> 
                    <br />
                </div>
                <ModalBatch compactness={this.compactness} numberPlans={this.numberPlans} status={this.state.status} currentSelected={this.state.selected} selectedCard={this.props.selectedCard} toggleSelection={this.toggleSelection} batchName={this.state.name} toggleModal={this.toggleModal} showModal={this.state.showModal}/>
            </div>
        );
    }
}

export default BatchCard;