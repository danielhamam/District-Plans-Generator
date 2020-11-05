import React, {Component} from 'react';
import RangeSlider from 'react-bootstrap-range-slider';
import {Form} from 'react-bootstrap';
import Select from 'react-select';
import makeAnimated from 'react-select/animated';

class InputsJob extends Component {
    constructor () {
        super();
        this.state = {
            jobName : "", // name of the job
            districtsAmount : 0, // # district 
            plansAmount : 0, // # district plans
            compactness : "", // compactness (string can be low, intermediate or high)
            populationDifference : 0, // population difference varies from 0-1.7%
            minorityFocus : ""
        }
    }

    // user changes job name
    changeJobName = (e) => {
        this.setState({jobName : e.target.value});
    }

    // user changes # of districts
    changeDistrictsAmount = (e) => {
        this.setState({districtsAmount : e.target.value});
    }

    // user changes # of district plans
    changePlanAmount = (e) => {
        this.setState({plansAmount : e.target.value});
    }

    // user changes preference of compactness (multi-select is turned off)
    changeCompactness = (e) => {
        this.setState({compactness : e.label});
    }

    // user changes % of population difference
    changePopulationDifference = (e) => {
        this.setState({populationDifference: e.target.value});
    }

    changeMinorityFocus = (e) => {
        this.setState({minorityFocus : e}); // e is an array of labels/values (label is the name). e also has length in it
    }

    handleGenerateJob = () => {
        
        // Convert districtsAmount, plansAmount and populationDifference to strings
        var string_districtsAmount = this.state.districtsAmount.toString();
        var string_plansAmount = this.state.plansAmount.toString();
        var string_populationDifference = this.state.populationDifference.toString();

        let userInputs = {
            jobName : this.state.jobName, // name of the job
            districtsAmount : string_districtsAmount, // # district 
            plansAmount :string_plansAmount, // # district plans
            compactness : this.state.compactness, // compactness (string can be low, intermediate or high)
            populationDifference : string_populationDifference, // population difference varies from 0-1.7%
            minorityFocus : this.state.minorityFocus
        }

        this.props.createJob(userInputs)
    }

    render() {

        const componentsAnimation = makeAnimated();
        const minorityOptions = [
            {
              label: "Ethnicity",
              options: [
                { label:"White", value: "white"},
                { label:"African American", value: "africanamerican"},
                { label:"Latino", value: "latino"},
                { label:"Asian", value: "asian"},
                { label:"American Indian", value: "americanindian"},
                { label:"Hawaiian", value: "hawaiian"},
                { label:"Other", value: "other"},
              ]},
          ];
          const compactnessOptions = [
            {
              label: "Measurement",
              options: [
                { label:"Low", value: "0"},
                { label:"Intermediate", value: "50"},
                { label:"High", value: "100"},
              ]},
          ];

        return (
            <Form> 
                <div className="form-group">
                {/* --------------------------------------- */}
                {/*             NUMBER OF DISTRICTS         */}
                {/* --------------------------------------- */}

                <label for="exampleInputEmail1"> Districts (#): </label>
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
                    <small className="form-text text-muted">Enter the amount of districts you'd like to construct for your selected state </small>

                    < br/>
                {/* --------------------------------------- */}
                {/*     NUMBER OF DISTRICT PLANS SLIDER  */}
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
                        <Select onChange={this.changeMinorityFocus} isSearchable={true} placeholder="Minority group(s)" components={componentsAnimation} className="basic-multi-select" options={minorityOptions} isMulti={true} />
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

                    <button type="submit" className="btn btn-primary" onClick={this.handleGenerateJob} >Generate</button>
            </Form>
        );
    }
}

export default InputsJob;