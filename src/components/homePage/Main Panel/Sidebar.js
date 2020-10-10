import {Navbar, Nav, NavItem, Button, Dropdown, DropdownButton, ButtonGroup, Tabs, Tab} from 'react-bootstrap';
import React, {Component} from 'react';
import { ProSidebar, Menu, MenuItem, SubMenu, SidebarHeader, SidebarContent } from 'react-pro-sidebar';
// import 'bootstrap/dist/css/bootstrap.min.css';
import usaFlag from "./usaFlag.png"
import blackBackground from "./blackBackground.jpg"
import { FontAwesomeIcon } from 'react-fontawesome'
// import BatchCard from './BatchCards/BatchCard';
import YourBatches from './BatchCards/YourBatches'
import YourDistrictingPlans from './DistrictingPlans/YourDistrictingPlans'
import InputsBatch from './GenerateBatch/InputsBatch';
import GraphDisplay from './GraphDisplay/GraphDisplay'

class Sidebar extends Component {
    constructor () {
        super();
        this.state = { 
            currentState : "Select a state",
            currentBatch : "No Batch Selected: ", // name of the currently selected batch
            statusCollapsed : false,
            hideSidebarHeader : false,
            collapsedIconLeft : false,
            collapsedIconRight : false,
            selectedFilters : null,
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

    changeCurrentBatch = (name) => {
        if (name == "") this.setState({currentBatch : "No Batch Selected: "});
        else this.setState({currentBatch : name + ":"});
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
                                    {/* <MenuItem>Number of Counties: </MenuItem> */}
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
                                        <YourBatches selectedBatchCheck={this.props.selectedBatchCheck} toggleSelectedBatchCheck={this.props.toggleSelectedBatchCheck} changeCurrentBatch={this.changeCurrentBatch} />
                                </SubMenu>

                            {/* -------------------------- */}
                            {/* -------------------------- */}
                                {/* DISTRICT PLANS*/}
                            {/* -------------------------- */}
                            {/* -------------------------- */}

                                <SubMenu icon={<div> <i className="fa fa-bars" > </i> </div>} title={<b> District Plans</b>} >
                                    <YourDistrictingPlans selectedPlanCheck={this.props.selectedPlanCheck} toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} currentBatch={this.state.currentBatch}/>
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

                                <SubMenu icon={<div> <i className="fa fa-connectdevelop" > </i> </div>} title={<b> Display Graph Panel</b>} >
                                        <GraphDisplay selectedFilters={this.state.selectedFilters}/>
                                </SubMenu>
                            </Menu>
                    </ProSidebar> 
                </div>
/* <!-- Sidebar --> */
        );
    }
}

export default Sidebar;