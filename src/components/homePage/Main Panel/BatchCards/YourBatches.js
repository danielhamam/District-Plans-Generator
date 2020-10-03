import React, {Component} from 'react';
import BatchCard from './BatchCard';

// This class represents the list of Batches. This will handle "currently selected Batch" and 
// generally manage the batches. 

class YourBatches extends Component {
    constructor () {
        super();
        this.state = {
            selectedCard : false
        }
        this.classNameCustom = "";
    }

    toggleSelectedCard = () => {
        if (this.state.selectedCard == false) this.setState({selectedCard : true});
        else this.setState({selectedCard : false});
    }

    render() {
        return (
            // Passing in parent variables to keep track of selection (so no more than 1 can be selected at once)
            <div>
                <div >Selected Batch:</div> 
                <BatchCard selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                <br></br>
                <br></br>
                <div>Your Batches:</div>
                <BatchCard selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                <BatchCard selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                <BatchCard selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                <BatchCard selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                <BatchCard selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
            </div>
            
        );
    }
}

export default YourBatches;