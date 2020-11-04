import React, {Component} from 'react';
import DistrictPlan from './DistrictPlan';

// This class represents the list of Jobs. This will handle "currently selected Job" and 
// generally manage the jobs. 

class YourDistrictingPlans extends Component {
    constructor () {
        super();
        this.state = {}
    }

    render() {
        return (
            // Passing in parent variables to keep track of selection (so no more than 1 can be selected at once)
            <div>
                <ul> 
                    <div> Selected Plan:</div> 
                        <br></br>
                        <br></br>
                    <div> {this.props.currentJobName} </div>
                    <DistrictPlan selectedPlanCheck={this.props.selectedPlanCheck} toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} />
                    <DistrictPlan selectedPlanCheck={this.props.selectedPlanCheck} toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} />
                    <DistrictPlan selectedPlanCheck={this.props.selectedPlanCheck} toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} />

                </ul>
            </div>
            
        );
    }
}

export default YourDistrictingPlans;