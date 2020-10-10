import React, {Component} from 'react';
import BatchCard from './BatchCard';

// This class represents the list of Batches. This will handle "currently selected Batch" and 
// generally manage the batches. 

class YourBatches extends Component {
    constructor () {
        super();
        this.state = {}
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
                            return <BatchCard batchName={"Batch 1"} changeCurrentBatch={this.props.changeCurrentBatch} selectedCard={this.state.selectedCard} toggleSelectedCard={this.toggleSelectedCard}/>
                    })} */}

                    <BatchCard status={true} batchName={"Batch 1"} minorityAnalyzed={"African American"} populationLimit={"1.2%"} compactness={"56%"} numberPlans={"5011"} changeCurrentBatch={this.props.changeCurrentBatch} selectedBatchCheck={this.props.selectedBatchCheck} toggleSelectedBatchCheck={this.props.toggleSelectedBatchCheck} />
                    <BatchCard status={true} batchName={"Batch 2"} minorityAnalyzed={"Hawaiian"} populationLimit={"0.5%"} compactness={"81%"} numberPlans={"101"} changeCurrentBatch={this.props.changeCurrentBatch} selectedBatchCheck={this.props.selectedBatchCheck} toggleSelectedBatchCheck={this.props.toggleSelectedBatchCheck}/>
                    <BatchCard status={true} batchName={"3rd Batch"} minorityAnalyzed={"American Indian"} populationLimit={"1.6%"} compactness={"12%"} numberPlans={"411"} changeCurrentBatch={this.props.changeCurrentBatch} selectedBatchCheck={this.props.selectedBatchCheck} toggleSelectedBatchCheck={this.props.toggleSelectedBatchCheck} />
                    <BatchCard status={false} batchName={"Batch 4"} minorityAnalyzed={"Latino"} populationLimit={"0.3%"} compactness={"94%"} numberPlans={"19"} changeCurrentBatch={this.props.changeCurrentBatch} selectedBatchCheck={this.props.selectedBatchCheck} toggleSelectedBatchCheck={this.props.toggleSelectedBatchCheck}/>
                    <BatchCard status={false} batchName={"Low Comp. "} minorityAnalyzed={"Asian"} populationLimit={"0.8%"} compactness={"15%"} numberPlans={"941"} changeCurrentBatch={this.props.changeCurrentBatch} selectedBatchCheck={this.props.selectedBatchCheck} toggleSelectedBatchCheck={this.props.toggleSelectedBatchCheck}/>
                    <BatchCard status={false} batchName={"Mid Comp. "} minorityAnalyzed={"African American"} populationLimit={"1.3%"} compactness={"48%"} numberPlans={"192"} changeCurrentBatch={this.props.changeCurrentBatch} selectedBatchCheck={this.props.selectedBatchCheck} toggleSelectedBatchCheck={this.props.toggleSelectedBatchCheck}/>
                </ul>
            </div>
            
        );
    }
}

export default YourBatches;