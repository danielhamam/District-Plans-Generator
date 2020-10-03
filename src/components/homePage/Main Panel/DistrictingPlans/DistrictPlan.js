import React, {Component} from 'react';

class DistrictPlan extends Component {
    constructor () {
        super();
        this.state = {
            selected : false,
            name: "District 1",
        }
        this.classNameCustom = "";
        this.goTop = "";
    }

    toggleSelectPlan= (e) => {
        // do something with batch

        if (this.state.selected == false && this.props.selectedPlan == false) {
            this.setState({selected: true});
            this.props.toggleSelectedPlan();
            this.goTop="goTop";
        }
        else if (this.state.selected == false && this.props.selectedPlan == true) {
            this.setState({selected : false});
            this.goTop="";
        }
        else if (this.state.selected == true && this.props.selectedPlan == true) {
            this.setState({selected: false});
            this.props.toggleSelectedPlan();
            this.goTop="";
        }
        else {
            // is currently selected, but not selected card. do nothing. (impossible scenario)
        }

    }

    render() {
        // Whenever we do setState, it rerenders
        if (this.state.selected == true) {
            this.classNameCustom = "districtPlan badge badge-pill badge-dark ";
        }
        else {
            this.classNameCustom = "districtPlan badge badge-pill badge-light ";
        }
        return (
                <div className={this.classNameCustom + this.goTop} onClick={this.toggleSelectPlan}>             
                    <div className="planContents">
                        <button className="planDelete badge badge-pill badge-danger" onClick={this.deleteBatch} > <div className="deleteText"> X </div> </button>
                        <span className="planTitle"> {this.state.name} </span> 
                        <button className="planView badge badge-pill badge-dark" onClick={this.toggleModal}> <div className="viewText" > Select </div> </button>
                    </div> 
            </div>
        );
    }
}

export default DistrictPlan;