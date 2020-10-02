import React, {Component} from 'react';

class BatchCard extends Component {
    // constructor () {
        
    // }

    handleChange() {
        // do something with batch
        
    }

    render() {
        return (
    
            <div className="batchCard badge badge-pill badge-light" onClick={this.handleChange}>
                <div className="batchcardContents">
                    <button className="batchcardDelete badge badge-pill badge-danger "> <div className="deleteText"> X </div> </button>
                    <span className="batchcardTitle"> Batch 1 </span> 
                    <button className="batchcardView badge badge-pill badge-dark"> <div className="viewText"> View </div> </button>
                </div> 
                <br /> 
                <br />
            </div>
        );
    }
}

export default BatchCard;