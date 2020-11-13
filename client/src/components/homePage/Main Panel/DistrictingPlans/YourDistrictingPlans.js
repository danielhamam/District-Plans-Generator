import React, {Component} from 'react';
import DistrictPlan from './DistrictPlan';

class YourDistrictingPlans extends Component {
    constructor () {
        super();
        this.state = {
            title: "No Job Selected",
        }
    }

    receivePlans = () => {

        if (this.props.currentJob != "") {
            return this.props.currentJob.districtPlans.map( (districtPlan) => 
                <DistrictPlan 
                // Attributes:
                currentJob={this.props.currentJob} plan={districtPlan} 
                type={districtPlan.type} 
                // Methods:
                selectedPlanCheck={this.props.selectedPlanCheck} toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck}
                deletePlan={this.props.deletePlan}
                /> )
        }
    }

    render() {

        if (this.props.currentJob == "" && this.state.title != "No Job Selected") this.setState({title : "No Job Selected"});
        else if (this.props.currentJob != "" && this.state.title != this.props.currentJob.jobName) this.setState({title : this.props.currentJob.jobName});

        return (
                <ul> 
                    <div> Selected Plan: </div> 
                        <br></br>
                        <br></br>
                    <div> {this.state.title}: </div>
                    < br />
                    {/* Enacted Plan */}
                    <DistrictPlan plan={this.props.enactedPlan} type={this.props.enactedPlan.type} currentJob={this.props.currentJob}
                    selectedPlanCheck={this.props.selectedPlanCheck} toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} />
                    {/* Other Plans: */}
                    {this.receivePlans()}

                </ul>
        );
    }
}

export default YourDistrictingPlans;