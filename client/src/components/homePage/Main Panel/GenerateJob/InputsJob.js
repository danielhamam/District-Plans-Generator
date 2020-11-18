import React, {Component} from 'react';
import RangeSlider from 'react-bootstrap-range-slider';
import {Form} from 'react-bootstrap';
import Select from 'react-select';
import makeAnimated from 'react-select/animated';

class InputsJob extends Component {
    constructor () {
        super();
        this.state = {
            jobName : "", 
            districtsAmount : 0, 
            plansAmount : 0, 
            compactness : "", 
            populationDifference : 0,
            minorityAnalyzed : ""
        }
    }

    changeJobName = (e) => this.setState({jobName : e.target.value});
    // changeDistrictsAmount = (e) => this.setState({districtsAmount : e.target.value});
    changePlanAmount = (e) => this.setState({plansAmount : e.target.value});
    changeCompactness = (e) => this.setState({compactness : e.label});
    changePopulationDifference = (e) => this.setState({populationDifference: e.target.value});
    changeMinorityAnalyzed = (e) => this.setState({minorityAnalyzed : e}); 

    handleGenerateJob = (e) => {
        e.preventDefault(); 

        // var string_districtsAmount = this.state.districtsAmount.toString();
        var string_plansAmount = this.state.plansAmount.toString();
        var string_populationDifference = this.state.populationDifference.toString();
        let valuesMinorities = [];
        this.state.minorityAnalyzed.forEach(element => { valuesMinorities.push(element.value); })
        let userInputs = {
            jobName : this.state.jobName, 
            // districtsAmount : string_districtsAmount, 
            plansAmount :string_plansAmount, 
            compactness : this.state.compactness, 
            populationDifference : string_populationDifference,
            minorityAnalyzed : valuesMinorities
        }
        this.props.createJob(userInputs)
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
                {/*             NUMBER OF DISTRICTS         */}
                {/* --------------------------------------- */}

                    {/* <label for="exampleInputEmail1"> Districts (#): </label>
                    <div className="row"> 
                        <div className="col-4">
                            <Form.Control size="sm" value={this.state.districtsAmount} onChange={this.changeDistrictsAmount}/>
                        </div>
                        <div className="col-8">
                            <div className="rangeSliderContainer"> 
                                <RangeSlider className="rangeSlider" disabled={false} onChange={this.changeDistrictsAmount} step={1} min={0} max={300} tooltip='auto' value={this.state.districtsAmount} />
                            </div>
                        </div>
                    </div>
                    <small className="form-text text-muted">Enter the amount of districts you'd like to construct for your selected state </small> */}

                    {/* < br/> */}
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
                        <Select isSearchable={true} onChange={this.changeCompactness} placeholder="Compactness Measure" components={componentsAnimation} className="basic-multi-select" options={compactnessOptions} />
                    </div>
                    <small className="form-text text-muted"> Enter the compactness preference for your district plans. </small>
                    </div>

                {/* --------------------------------------- */}
                {/*        MINORITIES TO BE ANALYZED        */}
                {/* --------------------------------------- */}

                    <label > Minority Focus Group(s): </label>
                    <div className="minorityFocusStyle">
                    <Select onChange={this.changeMinorityAnalyzed} isSearchable={true} placeholder="Minority group(s)" components={componentsAnimation} className="basic-multi-select" options={minorityOptions} isMulti={true} />
                    </div>
                    <small className="form-text text-muted"> Select the minority group(s) in the dropdown to particularly analyze. </small>
                    <br />

                {/* --------------------------------------- */}
                {/*                JOB NAME                 */}
                {/* --------------------------------------- */}

                    <div className="form-group">
                        <label >Job Name:</label>
                        <div className="customJobNameContainer"> <input className="input-normal form-control" maxlength={11} placeholder="Custom Job Name" onChange={this.changeJobName} /> </div> 
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