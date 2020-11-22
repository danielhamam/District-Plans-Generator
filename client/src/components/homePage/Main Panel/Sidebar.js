import {Dropdown, DropdownButton, ButtonGroup, Tabs, Tab} from 'react-bootstrap';
import React, {Component} from 'react';
import { ProSidebar, Menu, MenuItem, SubMenu, SidebarHeader } from 'react-pro-sidebar';
import usaFlag from "./usaFlag.png"
import blackBackground from "./blackBackground.jpg"
import YourJobs from './JobCards/YourJobs'
import YourDistrictingPlans from './DistrictingPlans/YourDistrictingPlans'
import InputsJob from './GenerateJob/InputsJob';
import BoxWhiskerModal from './GraphDisplay/BoxWhiskerModal'

class Sidebar extends Component {
    constructor () {
        super();
        this.state = { 
            currentState : "Select a state",
            statusCollapsed : false,
            hideSidebarHeader : false,
            collapsedIconLeft : false,
            collapsedIconRight : false,
            selectedFilters : null,
            modalOpen : false,
            graphOptions : {
                animationEnabled: true,
                theme: "light2",
                title:{
                    text: "VAP Filter vs. Indexed Districts" // Existing plan "overlaps"/compared alongside with these district plans.
                },
                legend:{
                    horizontalAlign: "right",
                    verticalAlign: "top",
                },
                axisY: {
                    title: "Voting Age Population (VAP) by Demographic Filter",
                },
                axisX: {
                    title: "Indexed Districts"
                },
                
                data: [{
                    type: "boxAndWhisker",
                    legendText: "Generated",
                    showInLegend: true,
                    color: "red",
                    upperBoxColor: "#A72A17",
                    lowerBoxColor: "#A3A3A3",
                    yValueFormatString: "###.0# ",
                    dataPoints: [
                        { label: "1", y: [61, 65, 63.5, 70, 68] },
                        { label: "2", y: [63, 68, 66.5, 76, 72] },
                        { label: "3", y: [65, 71, 69.5, 78, 75] },
                        { label: "4", y: [67, 73, 72, 80, 78] },
                        { label: "5", y: [69, 76, 75, 83, 80] },
                        { label: "6", y: [71, 78, 78,  85, 83] },
                        { label: "7", y: [74, 81, 81, 87, 86] },
                        
                    ]
                },
                {
                    type: "scatter",
                    legendText: "Enacted",
                    showInLegend: true,
                    markerSize: 8,
                    color: "#007BFF",
                    toolTipContent: "District Percentage: {y}",
                    dataPoints: [
                        { x: 0, y: 68},
                        { x: 1, y: 71},
                        { x: 2, y: 73},
                        { x: 3, y: 74},
                        { x: 4, y: 77},
                        { x: 5, y: 80},
                        { x: 6, y: 83},
                    ]
                }]
            },
        }
    }

    toggleCollapse = () => {
        if (this.state.statusCollapsed == false) {
            document.getElementById("sidebarHeader").style.visibility = "hidden";
            this.setState({hideSidebarHeader : true});
            this.setState({statusCollapsed: true})
            document.getElementById("collapseButtonRight").style.visibility = "visible";
            document.getElementById("collapseButtonLeft").style.visibility = "hidden";
            document.getElementById("collapseButtonLeft").style.display = "none";
        }
        else {
            document.getElementById("sidebarHeader").style.visibility = "visible";
            this.setState({hideSidebarHeader : false})
            this.setState({statusCollapsed : false})
            document.getElementById("collapseButtonRight").style.visibility = "hidden";
            document.getElementById("collapseButtonLeft").style.visibility = "visible";
            document.getElementById("collapseButtonLeft").style.display = "";
        }
    }

    handleBoxWhiskerModal = () => {
        if (this.state.modalOpen == false) {
            this.props.generateBoxWhiskerValues();
            this.setState({modalOpen : true});
        }
        else this.setState({modalOpen : false});
    }

    render() {

        if (this.state.selectedFilters != this.props.selectedFilters) this.setState({selectedFilters : this.props.selectedFilters});
        if (this.state.currentState != this.props.currentState) this.setState({currentState : this.props.currentState});

        return (
                <div id="mainSidebar" >
                    <ProSidebar image={blackBackground} collapsed={this.state.statusCollapsed} >
                        <SidebarHeader id="sidebarHeader"> 
                            <i id="collapseButtonLeft" className="fa fa-angle-double-left" onClick={this.toggleCollapse}> </i>
                            <i id="collapseButtonRight" className="fa fa-angle-double-right" onClick={this.toggleCollapse}> </i>
                            <div className="row col-md-12">  {/* Holds Flag, "Current State", and Selection of State */}
                                <br />
                                    <div className="col-md-4"> 
                                        <img id="usaFlag" src={usaFlag} alt="logo" style={{ width: '45px'}}/> 
                                    </div>
                                    <div className="col-md-8">
                                        <div id="currentState"> Current State: </div>
                                        <DropdownButton as={ButtonGroup} id="dropdownButton" title={this.state.currentState} size="sm" variant="secondary">
                                            <Dropdown.Item className="stateSelect" >
                                                <div title="GA" onClick={(e) => this.props.changeCurrentState(e.target.title, e.target.textContent)}>Georgia</div> 
                                            </Dropdown.Item>
                                            <Dropdown.Item className="stateSelect" key="PA">
                                                <div title="PA" onClick={(e) => this.props.changeCurrentState(e.target.title, e.target.textContent)}>Pennsylvania</div> 
                                            </Dropdown.Item>
                                            <Dropdown.Item className="stateSelect" key="MD">
                                                <div title="MD" onClick={(e) => this.props.changeCurrentState(e.target.title, e.target.textContent)}>Maryland</div> 
                                            </Dropdown.Item>
                                        </DropdownButton>
                                    </div>
                            </div>
                        </SidebarHeader>

                        <Menu id="menuofCategories" iconShape="square" >

                            {/* -------------------------- */}
                            {/* -------------------------- */}
                                {/* STATE DETAILS */}
                            {/* -------------------------- */}
                            {/* -------------------------- */}

                                <SubMenu icon={<div> <i className="fa fa-home" > </i> </div>} title={<b> State Details</b>}> 
                                    <MenuItem>Population: {this.props.totalPopulation} </MenuItem>
                                    <MenuItem>Number of Precincts: {this.props.numOfPrecincts} </MenuItem>
                                    <MenuItem>Number of Counties: {this.props.numOfCounties} </MenuItem>
                                </SubMenu>

                            {/* -------------------------- */}
                            {/* -------------------------- */}
                                {/* YOUR Jobs */}
                            {/* -------------------------- */}
                            {/* -------------------------- */}

                                <SubMenu icon={<div> <i className="fa fa-briefcase" > </i> </div>} title={<b> Your Jobs</b>} >
                                    
                                    <div id="yourJobsWrapper">
                                        <YourJobs 
                                        selectedJobCheck={this.props.selectedJobCheck} toggleSelectedCard={this.props.toggleSelectedCard} 
                                        updateCurrentJob={this.props.updateCurrentJob} jobCards = {this.props.jobCards} deleteJob={this.props.deleteJob}
                                        cancelJob = {this.props.cancelJob}
                                        />
                                    </div> 
                                </SubMenu>

                            {/* -------------------------- */}
                            {/* -------------------------- */}
                                {/* DISTRICT PLANS*/}
                            {/* -------------------------- */}
                            {/* -------------------------- */}

                                <SubMenu icon={<div> <i className="fa fa-bars" > </i> </div>} title={<b> District Plans</b>} >
                                    <YourDistrictingPlans currentJob ={this.props.currentJob} selectedPlanCheck={this.props.selectedPlanCheck} 
                                    toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} enactedPlan = {this.props.enactedPlan} deletePlan={this.props.deletePlan}
                                    />
                                </SubMenu>

                            {/* -------------------------- */}
                            {/* -------------------------- */}
                                {/* GENERATE Job PLAN */}
                            {/* -------------------------- */}
                            {/* -------------------------- */}

                                <SubMenu icon={<div> <i className="fa fa-edit" > </i> </div>} title={<b> Generate New Job </b>} >
                                    <InputsJob createJob={this.props.createJob}/>
                                </SubMenu>

                            {/* -------------------------- */}
                            {/* -------------------------- */}
                                {/* DISPLAY GRAPH PANEL */}
                            {/* -------------------------- */}
                            {/* -------------------------- */}
                            <div id="displayGraph" > 
                                <MenuItem  icon={<div > <i className="fa fa-connectdevelop" > </i> </div>} title={<b> Display Graph Panel &nbsp;   <i className="fa fa-expand"> </i> </b>} >
                                    <div onClick={this.handleBoxWhiskerModal}>
                                        <b> Display Graph Panel </b>
                                        <i id="displayGraph_icon" className="fa fa-external-link"> </i> 
                                    </div>
                                    <BoxWhiskerModal 
                                        graphOptions={this.state.graphOptions} 
                                        handleBoxWhiskerModal ={this.handleBoxWhiskerModal} 
                                        showModal={this.state.modalOpen} > 
                                    </BoxWhiskerModal >
                                </MenuItem>
                            </div>
                            </Menu>
                    </ProSidebar> 
                </div>
        );
    }
}

export default Sidebar;