import React, {Component} from 'react';
import BatchCard from './BatchCard';

// This class represents the list of Batches. This will handle "currently selected Batch" and 
// generally manage the batches. 

class YourBatches extends Component {
    constructor () {
        super();
        this.state = {
            selectedCard : false,
            selectedCardName : ""
        }
        this.classNameCustom = "";
    }

    toggleSelectedCard = () => {
        if (this.state.selectedCard == false) this.setState({selectedCard : true});
        else this.setState({selectedCard : false});
    }

    // // maybe use this implementation?
    // changeSelectedCardName = (name) => {
    //     this.setState({selectedCardName : name});
    //     this.props.changeSelectedCardName(name);
    // }

    render() {
        return (
            // Passing in parent variables to keep track of selection (so no more than 1 can be selected at once)
            <div>
                <ul> 
                    <div> Selected Batch:</div> 
                        <br></br>
                        <br></br>
                    <div>Your Batches:</div>
                    <BatchCard batchName={"Batch 1"} changeSidebarBatch={this.props.changeSidebarBatch} selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                    <BatchCard batchName={"Batch 2"} changeSidebarBatch={this.props.changeSidebarBatch} selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                    <BatchCard batchName={"Batch 3"} changeSidebarBatch={this.props.changeSidebarBatch} selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                    <BatchCard batchName={"Batch 4"} changeSidebarBatch={this.props.changeSidebarBatch} selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                    <BatchCard batchName={"Batch 5"} changeSidebarBatch={this.props.changeSidebarBatch} selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                    <BatchCard batchName={"Batch 6"} changeSidebarBatch={this.props.changeSidebarBatch} selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                </ul>
            </div>
            
        );
    }
}

export default YourBatches;