import React, {Component} from 'react';
import RangeSlider from 'react-bootstrap-range-slider';
import {Form} from 'react-bootstrap';
import Select from 'react-select';
import makeAnimated from 'react-select/animated';

class InputsJob extends Component {
    constructor () {
        super();
        this.state = {

            // Inputs
            jobName : '', 
            plansAmount : 0, 
            compactness : '',
            populationDifference : 0,
            minorityAnalyzed : '',

            // Errors
            jobNameError : null,
            compactnessError : null,
            minorityAnalyzedError : null
        }
    }

    changeJobName = (e) => this.setState({jobName : e.target.value});
    changePlanAmount = (e) => this.setState({plansAmount : e.target.value});
    changeCompactness = (e) => {
        this.setState({compactness : [{
            label : e.label,
            value : null
        }]
    })}
    changePopulationDifference = (e) => this.setState({populationDifference: e.target.value});
    changeMinorityAnalyzed = (e) => this.setState({minorityAnalyzed : e}); 

    handleGenerateJob = (e) => {
        e.preventDefault(); 
        let foundEmpty = 0;

        // Check if fields are entered:
        if (this.state.compactness == '') { this.setState({ compactnessError : "Required!"}); foundEmpty = 1 }
        if (this.state.minorityAnalyzed == '') { this.setState({ minorityAnalyzedError : "Required!"}); foundEmpty = 1 }
        if (this.state.jobName== '') { this.setState({ jobNameError : "Required!"}); foundEmpty = 1 }

        if (!this.state.compactness == '') this.setState({ compactnessError : null});
        if (!this.state.minorityAnalyzed == '') this.setState({ minorityAnalyzedError : null});
        if (!this.state.jobName== '') this.setState({ jobNameError : null});

        if (foundEmpty == 1) return

        var string_plansAmount = this.state.plansAmount.toString();
        var string_populationDifference = this.state.populationDifference.toString();
        let valuesMinorities = [];
        this.state.minorityAnalyzed.forEach(element => { valuesMinorities.push(element.value); })
        let userInputs = {
            jobName : this.state.jobName, 
            plansAmount :string_plansAmount, 
            compactness : this.state.compactness[0].label, 
            populationDifference : string_populationDifference,
            minorityAnalyzed : valuesMinorities
        }
        this.props.createJob(userInputs)
        // RESET ALL INPUTS
        this.setState({jobName : ''})
        this.setState({districtsAmount : 0})
        this.setState({plansAmount : 0})
        this.setState({compactness : ''})
        this.setState({populationDifference : 0})
        this.setState({minorityAnalyzed : ''})
    }

    render() {

        const componentsAnimation = makeAnimated();
        const minorityOptions = [
            {
              label: "Ethnicity",
              options: [
                { label:"White", value: "WHITE_AMERICAN"},
                { label:"African American", value: "AFRICAN_AMERICAN"},
                { label:"Latino", value: "LATINO_AMERICAN"},
                { label:"Asian", value: "ASIAN_AMERICAN"},
                { label:"American Indian", value: "AMERICAN_INDIAN"},
                { label:"Hawaiian", value: "HAWAIIAN_AMERICAN"},
                { label:"Other", value: "OTHER_AMERICAN"},
              ]},
          ];
          const compactnessOptions = [
            {
              label: "Measurement",
              options: [
                { label:"LOW", value: "0"},
                { label:"MEDIUM", value: "50"},
                { label:"HIGH", value: "100"},
              ]},
          ];

        return (
            <Form> 
                <div className="form-group">

                {/* --------------------------------------- */}
                {/*     NUMBER OF DISTRICT PLANS SLIDER     */}
                {/* --------------------------------------- */}

                    <label for="exampleInputEmail1"> Districting Plans (#): </label>
                    <div className="row"> 
                        <div className="col-4">
                            <Form.Control size="sm" value={this.state.plansAmount} onChange={this.changePlanAmount}/>
                        </div>
                        <div className="col-8">
                            <div className="rangeSliderContainer"> 
                                <RangeSlider className="rangeSlider" disabled={false} onChange={this.changePlanAmount} step={1} min={0} max={5000} tooltip='auto' value={this.state.plansAmount} />
                                <div className = "errorStyle"> {this.state.plansAmountError} </div>
                            </div>
                        </div>
                    </div>
                    <small className="form-text text-muted">Enter the amount of district plans you would like to generate. The current limit is 5000. </small>
                    < br/>

                {/* --------------------------------------- */}
                {/*     POPULATION DIFFERENCE SLIDER        */}
                {/* --------------------------------------- */}

                    <label for="exampleInputEmail1"> Population Difference Limit (%): </label>
                    <div className="row"> 
                        <div className="col-4">
                            <Form.Control size="sm" value={this.state.populationDifference} onChange={this.changePopulationDifference}/>
                        </div>
                        <div className="col-8">
                            <div className="rangeSliderContainer"> 
                                <RangeSlider className="rangeSlider" disabled={false} onChange={this.changePopulationDifference} step={0.1} min={0} max={1.7} tooltip='auto' value={this.state.populationDifference} />
                                <div className = "errorStyle"> {this.state.populationDifferenceError} </div>
                            </div>
                        </div>
                    </div>
                    <small className="form-text text-muted"> Enter the limit as measured by the difference between the most populous district and the least populous district. </small>
                    <br /> 

                {/* --------------------------------------- */}
                {/*     COMPACTNESS MEASURE SLIDER          */}
                {/* --------------------------------------- */}

                    <label for="exampleInputEmail1"> Compactness Preference: </label>
                    <div className="compactnessStyle">  
                        <Select isSearchable={true} onChange={this.changeCompactness} value={this.state.compactness} placeholder = "Compactness Measure" components={componentsAnimation} className="basic-multi-select" options={compactnessOptions} />
                        <div className = "errorStyle"> {this.state.compactnessError} </div>
                    </div>
                    <small className="form-text text-muted"> Enter the compactness preference for your district plans. </small>
                    </div>

                {/* --------------------------------------- */}
                {/*        MINORITIES TO BE ANALYZED        */}
                {/* --------------------------------------- */}

                    <label > Minority Focus Group(s): </label>
                    <div className="minorityFocusStyle">
                        <Select onChange={this.changeMinorityAnalyzed} value={this.state.minorityAnalyzed} isSearchable={true} placeholder="Minority group(s)" components={componentsAnimation} className="basic-multi-select" options={minorityOptions} isMulti={true} />
                        <div className = "errorStyle"> {this.state.minorityAnalyzedError} </div>
                    </div>
                    <small className="form-text text-muted"> Select the minority group(s) in the dropdown to particularly analyze. </small>
                    <br />

                {/* --------------------------------------- */}
                {/*                JOB NAME                 */}
                {/* --------------------------------------- */}

                    <div className="form-group">
                        <label >Job Name:</label>
                        <div className="customJobNameContainer"> <input className="input-normal form-control" maxlength={11} placeholder="Custom Job Name" value={this.state.jobName} onChange={this.changeJobName} /> </div> 
                        <div className = "errorStyle"> {this.state.jobNameError} </div>
                    </div>
                    <br /> 

                {/* --------------------------------------- */}
                {/*            GENERATE BUTTON              */}
                {/* --------------------------------------- */}

                    <button className="btn btn-primary" onClick={this.handleGenerateJob} >Generate</button>
            </Form>
        );
    }
}

export default InputsJob;