import React, {Component} from 'react';
import JobCard from './JobCard';

// This class represents the list of Jobes. This will handle "currently selected Job" and 
// generally manage the Jobs. 

class YourJobs extends Component {
    constructor () {
        super();
        this.state = {}
    }

    render() {
        return (
            // Passing in parent variables to keep track of selection (so no more than 1 can be selected at once)
            <div>
                <ul> 
                    <div> Selected Job:</div> 
                        <br></br>
                        <br></br>
                    <div>Your Jobs:</div>
        
                    {this.props.jobCards.map( (jobCard) => {
                            return <JobCard jobCard={jobCard} jobName={jobCard.jobName} 
                            minorityAnalyzed={jobCard.minorityAnalyzed} populationLimit={jobCard.populationLimit} 
                            compactness={jobCard.compactness} numberPlans={jobCard.numberPlans} districts={jobCard.districts} 
                            status={jobCard.status} updateCurrentJob={this.props.updateCurrentJob} 
                            selectedJobCheck={this.props.selectedJobCheck} toggleSelectedCard={this.props.toggleSelectedCard} />
                    })}

                    {/* <JobCard status={true} JobName={"Job 1"} minorityAnalyzed={"African American"} populationLimit={"1.2%"} compactness={"56%"} numberPlans={"5011"} updateCurrentJobName={this.props.updateCurrentJobName} selectedJobCheck={this.props.selectedJobCheck} toggleSelectedJobCheck={this.props.toggleSelectedJobCheck} />
                    <JobCard status={true} JobName={"Job 2"} minorityAnalyzed={"Hawaiian"} populationLimit={"0.5%"} compactness={"81%"} numberPlans={"101"} updateCurrentJobName={this.props.updateCurrentJobName} selectedJobCheck={this.props.selectedJobCheck} toggleSelectedJobCheck={this.props.toggleSelectedJobCheck}/>
                    <JobCard status={true} JobName={"3rd Job"} minorityAnalyzed={"American Indian"} populationLimit={"1.6%"} compactness={"12%"} numberPlans={"411"} updateCurrentJobName={this.props.updateCurrentJobName} selectedJobCheck={this.props.selectedJobCheck} toggleSelectedJobCheck={this.props.toggleSelectedJobCheck} />
                    <JobCard status={false} JobName={"Job 4"} minorityAnalyzed={"Latino"} populationLimit={"0.3%"} compactness={"94%"} numberPlans={"19"} updateCurrentJobName={this.props.updateCurrentJobName} selectedJobCheck={this.props.selectedJobCheck} toggleSelectedJobCheck={this.props.toggleSelectedJobCheck}/>
                    <JobCard status={false} JobName={"Low Comp. "} minorityAnalyzed={"Asian"} populationLimit={"0.8%"} compactness={"15%"} numberPlans={"941"} updateCurrentJobName={this.props.updateCurrentJobName} selectedJobCheck={this.props.selectedJobCheck} toggleSelectedJobCheck={this.props.toggleSelectedJobCheck}/>
                    <JobCard status={false} JobName={"Mid Comp. "} minorityAnalyzed={"African American"} populationLimit={"1.3%"} compactness={"48%"} numberPlans={"192"} updateCurrentJobName={this.props.updateCurrentJobName} selectedJobCheck={this.props.selectedJobCheck} toggleSelectedJobCheck={this.props.toggleSelectedJobCheck}/> */}
                </ul>
            </div>
            
        );
    }
}

export default YourJobs;