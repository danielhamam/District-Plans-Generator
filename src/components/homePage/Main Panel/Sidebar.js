import {Navbar, Nav, NavItem, Button, Dropdown, DropdownButton, ButtonGroup} from 'react-bootstrap';
import React, {Component} from 'react';
import { ProSidebar, Menu, MenuItem, SubMenu, SidebarHeader, SidebarContent } from 'react-pro-sidebar';
// import 'bootstrap/dist/css/bootstrap.min.css';
import usaFlag from "./usaFlag.png"
import blackBackground from "./blackBackground.jpg"
import { FontAwesomeIcon } from 'react-fontawesome'

class Sidebar extends Component {
    constructor () {
        super();
        this.state = { 
            currentState : "Select a state",
            currentCollapsed : false,
            hideSidebarHeader : false,
            collapsedIconLeft : false,
            collapsedIconRight : false
        }
        // this.currentCollapsed = this.currentCollapsed.bind(this);
    }

    changeState(selection) {
        this.setState({currentState: selection})
    }

    toggleCollapse = () => {
        if (this.state.currentCollapsed == false) {
            {this.toggleHideHeader()};
            this.setState({currentCollapsed: true})
            document.getElementById("collapseButtonRight").style.visibility = "visible";
            document.getElementById("collapseButtonLeft").style.visibility = "hidden";
            document.getElementById("collapseButtonLeft").style.display = "none";
        }
        else {
            this.toggleHideHeader();
            this.setState({currentCollapsed : false})
            document.getElementById("collapseButtonRight").style.visibility = "hidden";
            document.getElementById("collapseButtonLeft").style.visibility = "visible";
            document.getElementById("collapseButtonLeft").style.display = "";
        }
    }

    toggleHideHeader = () => {
        if (this.state.hideSidebarHeader == false) {
            document.getElementById("sidebarHeader").style.visibility = "hidden";
            this.setState({hideSidebarHeader : true});
        }
        else {
            document.getElementById("sidebarHeader").style.visibility = "visible";
            this.setState({hideSidebarHeader : false})
        };
    }

    render() {
        return (
                <div id="mainSidebar" >
                    <ProSidebar image={blackBackground} collapsed={this.state.currentCollapsed} >
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
                                            <Dropdown.Item className="stateSelect" href="#/action-1">
                                                <div onClick={(e) => this.changeState(e.target.textContent)}> California </div> 
                                            </Dropdown.Item>
                                            <Dropdown.Item className="stateSelect" href="#/action-2">
                                                <div onClick={(e) => this.changeState(e.target.textContent)}> Georgia </div> 
                                            </Dropdown.Item>
                                            <Dropdown.Item className="stateSelect" href="#/action-3">
                                                <div onClick={(e) => this.changeState(e.target.textContent)}> New York </div> 
                                            </Dropdown.Item>
                                        </DropdownButton>
                                    </div>
                            </div>
                        </SidebarHeader>

                        <SidebarContent>
                        <Menu iconShape="square" >

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
                                    <MenuItem>Efficiency Gap: </MenuItem>
                                    <MenuItem>Competitive Districts: </MenuItem>
                                    <MenuItem>Majority non-white districts: </MenuItem>
                                    <MenuItem>County Splits: </MenuItem>
                                    <MenuItem>Compactness Rank: </MenuItem>
                                </SubMenu>

                        {/* -------------------------- */}
                            {/* -------------------------- */}
                                {/* YOUR BATCHES */}
                            {/* -------------------------- */}
                            {/* -------------------------- */}

                                <SubMenu id="yourBatchesWrapper" icon={<div> <i className="fa fa-briefcase" > </i> </div>} title={<b> Your Batches</b>} >
                                    {/* <MenuItem> */}
                                        <div class="card col-md-12 bg-light">
                                            <div class="card-header text-left bg-light text-dark row col-md-12">
                                                <i class="fa fa-times-circle text-left col-md-4"></i>
                                                <h5 class="card-title text-right col-md-5">Batch 1: </h5>
                                            </div>
                                            <div class="card-body bg-dark">
                                                <p class="text-white bg-dark text-left">
                                                <ul>
                                                    <li><b>Compactness: </b></li>
                                                    <li><b>Population Variation: </b></li>
                                                    <li><b>Number of Iterations: </b></li>
                                                    <li><b>Batch Name: </b></li>
                                                </ul>
                                                </p>
                                                </div>
                                            <div class="card-footer text-left bg-light text-dark">
                                                <p class="card-text text-left col-md-12">Analysis: lorem ipsum some other stuff i dont know</p>
                                                <p class="col-md-7"></p><button type="button" class="btn btn-secondary btn-sm col-md-5">Select</button>
                                            </div>
                                        </div>
                                    {/* </MenuItem> */}
                                    <MenuItem>Component 2</MenuItem>
                                </SubMenu>

                        {/* -------------------------- */}
                            {/* -------------------------- */}
                                {/* GENERATE BATCH PLAN */}
                            {/* -------------------------- */}
                            {/* -------------------------- */}

                                <SubMenu icon={<div> <i className="fa fa-bars" > </i> </div>} title={<b> Generate Batch Plan</b>} >
                                    <MenuItem>Component 1</MenuItem>
                                    <MenuItem>Component 2</MenuItem>
                                </SubMenu>

                        {/* -------------------------- */}
                            {/* -------------------------- */}
                                {/* USER INPUT */}
                            {/* -------------------------- */}
                            {/* -------------------------- */}

                                <SubMenu icon={<div> <i className="fa fa-edit" > </i> </div>} title={<b> User Input</b>} >
                                    <MenuItem>Component 1</MenuItem>
                                    <MenuItem>Component 2</MenuItem>
                                </SubMenu>

                        {/* -------------------------- */}
                            {/* -------------------------- */}
                                {/* DISPLAY GRAPH PANEL */}
                            {/* -------------------------- */}
                            {/* -------------------------- */}

                                <SubMenu icon={<div> <i className="fa fa-connectdevelop" > </i> </div>} title={<b> Display Graph Panel</b>} >
                                    <MenuItem>Component 1</MenuItem>
                                    <MenuItem>Component 2</MenuItem>
                                </SubMenu>
                            </Menu>
                        </SidebarContent>
                    </ProSidebar> 
                </div>
/* <!-- Sidebar --> */
        );
    }
}

export default Sidebar;