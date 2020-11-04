import React, {Component} from 'react';
import DistrictPlan from './DistrictPlan';

// This class represents the list of Jobs. This will handle "currently selected Job" and 
// generally manage the jobs. 

class YourDistrictingPlans extends Component {
    constructor () {
        super();
        this.state = {
            title: "No Job Selected"
        }
    }

    render() {
        if (this.props.currentJob == "" && this.state.title != "No Job Selected") this.setState({title : "No Job Selected"}); // if there is no job, then title is "No Job Selected: "
        else if (this.props.currentJob != "" && this.state.title != this.props.currentJob.jobName) this.setState({title : this.props.currentJob.jobName});

        return (
            // Passing in parent variables to keep track of selection (so no more than 1 can be selected at once)
            <div>
                <ul> 
                    <div> Selected Plan: </div> 
                        <br></br>
                        <br></br>
                    <div> {this.state.title}: </div>

                    {/* {this.props.jobCards.map( (jobCard) => {
                            return <JobCard jobName={jobCard.jobName} minorityAnalyzed={jobCard.minorityAnalyzed} 
                            populationLimit={jobCard.populationLimit} compactness={jobCard.compactness}
                            numberPlans={jobCard.numberPlans} districts={jobCard.districts} status={jobCard.status}
                            updateCurrentJob={this.props.updateCurrentJob} selectedJobCheck={this.props.selectedJobCheck} 
                            toggleSelectedCard={this.props.toggleSelectedCard}/>
                    })} */}

                    <DistrictPlan selectedPlanCheck={this.props.selectedPlanCheck} toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} />
                    <DistrictPlan selectedPlanCheck={this.props.selectedPlanCheck} toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} />
                    <DistrictPlan selectedPlanCheck={this.props.selectedPlanCheck} toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} />

                </ul>
            </div>
            
        );
    }
}

export default YourDistrictingPlans;