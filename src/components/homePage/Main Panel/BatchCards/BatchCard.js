import React, {Component} from 'react';
import ModalBatch from './ModalBatch'

class BatchCard extends Component {
    constructor () {
        super();
        this.state = {
            showModal : false,
            selected : false
        }

        this.classNameCustom = "";
    }

    toggleSelection = (e) => {
        // do something with batch
        if (this.state.selected == false) this.setState({selected: true});
        else this.setState({selected : false});
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
        return (
            <div>
                <ModalBatch toggleModal={this.toggleModal} showModal={this.state.showModal}/>
                <div className={this.classNameCustom} onClick={this.toggleSelection} >
                    <div className="batchcardContents">
                        <button className="batchcardDelete badge badge-pill badge-danger "> <div className="deleteText"> X </div> </button>
                        <span className="batchcardTitle"> Batch 1 </span> 
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