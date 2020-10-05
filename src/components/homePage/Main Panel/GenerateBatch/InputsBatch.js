import React, {Component} from 'react';
import RangeSlider from 'react-bootstrap-range-slider';
import {Form, Col, Row} from 'react-bootstrap';

class InputsBatch extends Component {
    constructor () {
        super();
        this.state = {
            plansAmount : 0,
            compactnessAmount : 0
            // plansChangeAmount : ""
            // slideStop={this.changeValue}

        }
    }

    // To change the amount in the slider
    changePlanAmount = (e) => {
        this.setState({plansAmount : e.target.value});
    }

    changeCompactnessAmount = (e) => {
        this.setState({compactnessAmount : e.target.value});
    }

    render() {
        return (
            <Form> 
                <div className="form-group">

                {/* --------------------------------------- */}
                {/*     NUMBER OF DISTRICT PLANS SLIDER  */}
                {/* --------------------------------------- */}

                    <label for="exampleInputEmail1"> Number of District Plans: </label>
                    <div className="row"> 
                        <div className="col-4">
                            <Form.Control size="sm" value={this.state.plansAmount} onChange={this.changePlanAmount}/>
                        </div>
                        <div className="col-8">
                            <div className="rangeSliderContainer"> 
                                <RangeSlider className="rangeSlider" disabled={false} onChange={this.changePlanAmount} step={1} min={0} max={10000} tooltip='auto' value={this.state.plansAmount} />
                            </div>
                        </div>
                    </div>
                    <small className="form-text text-muted">Enter the amount of district plans you would like to generate. The current limit is 10000. </small>

                < br/>
                {/* --------------------------------------- */}
                {/*     COMPACTNESS MEASURE SLIDER          */}
                {/* --------------------------------------- */}

                <label for="exampleInputEmail1"> Compactness: </label>
                    <div className="row"> 
                        <div className="col-4">
                            <Form.Control size="sm" value={this.state.compactnessAmount} onChange={this.changeCompactnessAmount}/>
                        </div>
                        <div className="col-8">
                            <div className="rangeSliderContainer"> 
                                <RangeSlider className="rangeSlider" disabled={false} onChange={this.changeCompactnessAmount} step={0.01} min={0} max={1} tooltip='auto' value={this.state.compactnessAmount} />
                            </div>
                        </div>
                    </div>
                    <small className="form-text text-muted"> Enter the compactness preference for your district plans. </small>
                </div>

                <div className="form-group">
                    <label >Batch Name:</label>
                    <div className="customBatchNameContainer"> <input className="input-normal form-control" maxlength={11} placeholder="Custom Batch Name"/> </div> 
                </div>
                <button type="submit" className="btn btn-primary">Generate</button>
            </Form>
        );
    }
}

export default InputsBatch;