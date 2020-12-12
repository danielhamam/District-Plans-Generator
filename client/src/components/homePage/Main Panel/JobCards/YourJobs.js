import React, {Component} from 'react';
import JobCard from './JobCard';

class YourJobs extends Component {
    constructor () {
        super();
        this.state = {}
    }
    render() {

        if (this.props.currentState != "Select a state") {
            setInterval(this.props.updateJobStatus, 10000);
            console.log("Checking jobs")
        }

        return (
                <div >
                    <div> Selected Job:</div> 
                    <br></br>
                    <br></br>
                    <div>Your Jobs:</div>
                    < br />
                    {this.props.jobCards.map( (jobCard) => {
                            return <JobCard 
                            // Attributes
                            jobCard={jobCard} jobName={jobCard.jobName} deleteJob={this.props.deleteJob}
                            minorityAnalyzed={jobCard.minorityAnalyzed} populationDifference={jobCard.populationDifference} 
                            compactness={jobCard.compactness} plansAmount={jobCard.plansAmount} districts={jobCard.districts} 
                            status={jobCard.status} 
                            // Methods
                            updateCurrentJob={this.props.updateCurrentJob} selectedJobCheck={this.props.selectedJobCheck} 
                            toggleSelectedCard={this.props.toggleSelectedCard} cancelJob = {this.props.cancelJob}
                            />
                    })}
                </div>
        );
    }
}

export default YourJobs;