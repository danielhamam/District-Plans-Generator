import {Dropdown, DropdownButton, ButtonGroup, Tabs, Tab} from 'react-bootstrap';
import React, {Component} from 'react';
import { ProSidebar, Menu, MenuItem, SubMenu, SidebarHeader } from 'react-pro-sidebar';
import usaFlag from "./usaFlag.png"
import blackBackground from "./blackBackground.jpg"
import YourBatches from './BatchCards/YourBatches'
import YourDistrictingPlans from './DistrictingPlans/YourDistrictingPlans'
import InputsBatch from './GenerateBatch/InputsBatch';
import ModalGraph from './GraphDisplay/ModalGraph'

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

            // Graph Variables
            modalOpen : false,
            graphOptions : {
                animationEnabled: true,
                theme: "light2",
                title:{
                    text: "VAP Filter vs. Indexed Districts" // Existing plan v.s probabilistic plan - R. Kelly's words. Existing plan should "overlap" or be compared alongside with these district plans.
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

    changeCurrentStatefromSidebar(selection) {
        this.setState({currentState: selection})
        this.props.changeCurrentStatefromSidebar(selection);
    }

    toggleCollapse = () => {
        if (this.state.statusCollapsed == false) {

            // First, Let's toggle hiding the header

            if (this.state.hideSidebarHeader == false) {
                document.getElementById("sidebarHeader").style.visibility = "hidden";
                this.setState({hideSidebarHeader : true});
            }
            else {
                document.getElementById("sidebarHeader").style.visibility = "visible";
                this.setState({hideSidebarHeader : false})
            };

            //  Let's toggle the visibility of the rest

            this.setState({statusCollapsed: true})
            document.getElementById("collapseButtonRight").style.visibility = "visible";
            document.getElementById("collapseButtonLeft").style.visibility = "hidden";
            document.getElementById("collapseButtonLeft").style.display = "none";
        }
        else {

            // First, Let's toggle hiding the header

            if (this.state.hideSidebarHeader == false) {
                document.getElementById("sidebarHeader").style.visibility = "hidden";
                this.setState({hideSidebarHeader : true});
            }
            else {
                document.getElementById("sidebarHeader").style.visibility = "visible";
                this.setState({hideSidebarHeader : false})
            };

            //  Let's toggle the visibility of the rest

            this.setState({statusCollapsed : false})
            document.getElementById("collapseButtonRight").style.visibility = "hidden";
            document.getElementById("collapseButtonLeft").style.visibility = "visible";
            document.getElementById("collapseButtonLeft").style.display = "";
        }
    }

    toggleModalGraph = () => {
        // console.log(this.state.selectedFilters);
        if (this.state.modalOpen == false) this.setState({modalOpen : true});
        else this.setState({modalOpen : false});
    }

    render() {

        if (this.state.selectedFilters != this.props.selectedFilters) this.setState({selectedFilters : this.props.selectedFilters});

        // If changed from map
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
                                            <Dropdown.Item className="stateSelect" key="California">
                                                <div onClick={(e) => this.changeCurrentStatefromSidebar(e.target.textContent)}>California</div> 
                                            </Dropdown.Item>
                                            <Dropdown.Item className="stateSelect" key="Georgia">
                                                <div onClick={(e) => this.changeCurrentStatefromSidebar(e.target.textContent)}>Georgia</div> 
                                            </Dropdown.Item>
                                            <Dropdown.Item className="stateSelect" key="NewYork">
                                                <div onClick={(e) => this.changeCurrentStatefromSidebar(e.target.textContent)}>New York</div> 
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
                                    <MenuItem>Population: </MenuItem>
                                    <MenuItem>Number of Precincts: </MenuItem>
                                    <MenuItem>Number of Counties: </MenuItem>
                                    <MenuItem>Number of Districts: </MenuItem>
                                    {/* <MenuItem>Efficiency Gap: </MenuItem> */}
                                    {/* <MenuItem>Competitive Districts: </MenuItem> */}
                                    <MenuItem>Majority-minority districts: </MenuItem>
                                    {/* <MenuItem>County Splits: </MenuItem> */}
                                    {/* <MenuItem>Compactness Rank: </MenuItem> */}
                                </SubMenu>

                            {/* -------------------------- */}
                            {/* -------------------------- */}
                                {/* YOUR BATCHES */}
                            {/* -------------------------- */}
                            {/* -------------------------- */}

                                <SubMenu id="yourBatchesWrapper" icon={<div> <i className="fa fa-briefcase" > </i> </div>} title={<b> Your Batches</b>} >
                                        <YourBatches selectedBatchCheck={this.props.selectedBatchCheck} toggleSelectedBatchCheck={this.props.toggleSelectedBatchCheck} updateCurrentBatchName={this.props.updateCurrentBatchName} />
                                </SubMenu>

                            {/* -------------------------- */}
                            {/* -------------------------- */}
                                {/* DISTRICT PLANS*/}
                            {/* -------------------------- */}
                            {/* -------------------------- */}

                                <SubMenu icon={<div> <i className="fa fa-bars" > </i> </div>} title={<b> District Plans</b>} >
                                    <YourDistrictingPlans selectedPlanCheck={this.props.selectedPlanCheck} toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} currentBatchName={this.props.currentBatchName}/>
                                </SubMenu>

                            {/* -------------------------- */}
                            {/* -------------------------- */}
                                {/* GENERATE BATCH PLAN */}
                            {/* -------------------------- */}
                            {/* -------------------------- */}

                                <SubMenu icon={<div> <i className="fa fa-edit" > </i> </div>} title={<b> Generate Batch Plan</b>} >
                                    {/* <MenuItem>Component 1</MenuItem>
                                    <MenuItem>Component 2</MenuItem> */}
                                    <InputsBatch />
                                </SubMenu>

                            {/* -------------------------- */}
                            {/* -------------------------- */}
                                {/* DISPLAY GRAPH PANEL */}
                            {/* -------------------------- */}
                            {/* -------------------------- */}
                            <div id="displayGraph" onClick={this.toggleModalGraph}> 
                                <MenuItem icon={<div > <i className="fa fa-connectdevelop" > </i> </div>} title={<b> Display Graph Panel &nbsp;   <i className="fa fa-expand"> </i> </b>} >
                                    <b> Display Graph Panel </b>
                                    <i id="displayGraph_icon" className="fa fa-external-link"> </i> 
                                    <ModalGraph graphOptions={this.state.graphOptions} toggleModal ={this.toggleModalGraph} showModal={this.state.modalOpen} > </ModalGraph>
                                </MenuItem>
                            </div>
                            </Menu>
                    </ProSidebar> 
                </div>
/* <!-- Sidebar --> */
        );
    }
}

export default Sidebar;