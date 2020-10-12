import React, {Component} from 'react';
import DeletePlanModal from './DeletePlanModal'

class DistrictPlan extends Component {
    constructor () {
        super();
        this.state = {
            selected : false,
            name: "District Plan",
            showDeleteModal : false
        }
        this.districtPlanClassStyle = "";
        this.goTop = "";
    }


    // selectedPlanCheck={this.props.selectedPlanCheck} toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck}
    
    toggleSelectPlan= (e) => {
        // do something with batch

        if (this.state.selected == false && this.props.selectedPlanCheck == false) {
            this.setState({selected: true});
            this.props.toggleSelectedPlanCheck();
            this.goTop="goTopPlan";
        }
        else if (this.state.selected == false && this.props.selectedPlanCheck == true) {
            this.setState({selected : false});
            this.goTop="";
        }
        else if (this.state.selected == true && this.props.selectedPlanCheck == true) {
            this.setState({selected: false});
            this.props.toggleSelectedPlanCheck();
            this.goTop="";
        }
        else {
            // is currently selected, but not selected card. do nothing. (impossible scenario)
        }
    }

    deletePlan = (e) => {
        e.stopPropagation();
    }

    toggleDeleteModal = (e) => {
        e.stopPropagation();
        if (this.state.showDeleteModal == false) this.setState({showDeleteModal : true});
        else this.setState({showDeleteModal : false});
    }

    render() {
        // Whenever we do setState, it rerenders
        if (this.state.selected == true) {
            this.districtPlanClassStyle = "districtPlan badge badge-pill badge-dark ";
        }
        else {
            this.districtPlanClassStyle = "districtPlan badge badge-pill badge-light ";
        }
        return (
                <div className={this.districtPlanClassStyle + this.goTop} >             
                    <div className="planContents" onClick={this.toggleSelectPlan}>
                        <button className="planDelete badge badge-pill badge-danger" onClick={this.toggleDeleteModal} > <div className="deleteText"> X </div> </button>
                        <span className="planTitle"> {this.state.name} </span> 
                        <button className="planView badge badge-pill badge-dark"> <div className="viewText" > Select </div> </button>
                    </div> 
                    <DeletePlanModal toggleDeleteModal={this.toggleDeleteModal} deletePlan={this.deletePlan} showDeleteModal={this.state.showDeleteModal} />
                    <br /> 
                    <br />
            </div>
        );
    }
}

export default DistrictPlan;