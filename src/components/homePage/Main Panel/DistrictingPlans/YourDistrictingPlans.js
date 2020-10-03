import React, {Component} from 'react';
import DistrictPlan from './DistrictPlan';

// This class represents the list of Batches. This will handle "currently selected Batch" and 
// generally manage the batches. 

class YourDistrictingPlans extends Component {
    constructor () {
        super();
        this.state = {
            selectedPlan : false,
            selectedCardName : ""
        }
        this.classNameCustom = "";
    }

    toggleSelectedPlan = () => {
        if (this.state.selectedPlan == false) this.setState({selectedPlan: true});
        else this.setState({selectedPlan : false});
    }

    render() {
        return (
            // Passing in parent variables to keep track of selection (so no more than 1 can be selected at once)
            <div>
                <ul> 
                    <div> Selected Plan:</div> 
                        <br></br>
                        <br></br>
                    <div>Your Plans:</div>
                    <DistrictPlan toggleSelectedPlan={this.toggleSelectedPlan} />
                    {/* <DistrictPlan /> */}
                    {/* <DistrictPlan /> */}
                    {/* <DistrictPlan /> */}

                </ul>
            </div>
            
        );
    }
}

export default YourDistrictingPlans;