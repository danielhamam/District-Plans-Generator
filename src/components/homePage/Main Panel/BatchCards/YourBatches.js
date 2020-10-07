import React, {Component} from 'react';
import BatchCard from './BatchCard';

// This class represents the list of Batches. This will handle "currently selected Batch" and 
// generally manage the batches. 

class YourBatches extends Component {
    constructor () {
        super();
        this.state = {
            selectedCard : false,
            selectedCardName : "",
            batchCards: []
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
                <ul> 
                    <div> Selected Batch:</div> 
                        <br></br>
                        <br></br>
                    <div>Your Batches:</div>
                    {/* {this.state.batchCards.map( (batchCard) => {
                            return <BatchCard batchName={"Batch 1"} changeSidebarBatch={this.props.changeSidebarBatch} selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                    })} */}

                    <BatchCard status={true} batchName={"Batch 1"} minorityAnalyzed={"African American"} populationLimit={"1.2%"} compactness={"56%"} numberPlans={"5011"} changeSidebarBatch={this.props.changeSidebarBatch} selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                    <BatchCard status={true} batchName={"Batch 2"} minorityAnalyzed={"Hawaiian"} populationLimit={"0.5%"} compactness={"81%"} numberPlans={"101"} changeSidebarBatch={this.props.changeSidebarBatch} selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                    <BatchCard status={true} batchName={"3rd Batch"} minorityAnalyzed={"American Indian"} populationLimit={"1.6%"} minorityAnalyzed={"African American"} compactness={"12%"} numberPlans={"411"} changeSidebarBatch={this.props.changeSidebarBatch} selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                    <BatchCard status={false} batchName={"Batch 4"} minorityAnalyzed={"Latino"} populationLimit={"0.3%"} compactness={"94%"} numberPlans={"19"} hangeSidebarBatch={this.props.changeSidebarBatch} selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                    <BatchCard status={false} batchName={"Low Comp. "} minorityAnalyzed={"Asian"} populationLimit={"0.8%"} compactness={"15%"} numberPlans={"941"} changeSidebarBatch={this.props.changeSidebarBatch} selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                    <BatchCard status={false} batchName={"Mid Comp. "} minorityAnalyzed={"African American"} populationLimit={"1.3%"} compactness={"48%"} numberPlans={"192"} changeSidebarBatch={this.props.changeSidebarBatch} selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                </ul>
            </div>
            
        );
    }
}

export default YourBatches;