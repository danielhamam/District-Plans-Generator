import React, {Component} from 'react';
import ModalBatch from './ModalBatch'

class BatchCard extends Component {
    constructor () {
        super();
        this.state = {
            showModal : false,
            selected : false,
            name : "My new batch",
            id : "2"
        }

        this.classNameCustom = "";
    }

    toggleSelection = (e) => {
        // do something with batch
        if (this.state.selected == false && this.props.selectedCard == false) {
            this.setState({selected: true});
            this.props.toggleSelectedCard();
        }
        else if (this.state.selected == false && this.props.selectedCard == true) {
            this.setState({selected : false});
        }
        else if (this.state.selected == true && this.props.selectedCard == true) {
            this.setState({selected: false});
            this.props.toggleSelectedCard();
        }
        else {
            // is currently selected, but not selected card. do nothing. (impossible scenario)
        }
    }

    toggleModal = (e) => {
        e.stopPropagation();
        if (this.state.showModal == true) this.setState({showModal : false});
        else this.setState({showModal : true});
    }

    render() {
        // Whenever we do setState, it rerenders
        if (this.state.selected == true) {
            this.classNameCustom = "batchCard badge badge-pill badge-dark";
        }
        else {
            this.classNameCustom = "batchCard badge badge-pill badge-light";
        }
        // Check batch name, if it's empty the id is the name
        if (this.state.name == "") this.setState({name : "Batch " + this.state.id});

        return (
            <div>
                <ModalBatch toggleModal={this.toggleModal} showModal={this.state.showModal}/>
                <div className={this.classNameCustom} onClick={this.toggleSelection} >
                    <div className="batchcardContents">
                        <button className="batchcardDelete badge badge-pill badge-danger "> <div className="deleteText"> X </div> </button>
                        <span className="batchcardTitle"> {this.state.name} </span> 
                        <button className="batchcardView badge badge-pill badge-dark" onClick={this.toggleModal}> <div className="viewText" > View </div> </button>
                    </div> 
                    <br /> 
                    <br />
                </div>
            </div>
        );
    }
}

export default BatchCard;