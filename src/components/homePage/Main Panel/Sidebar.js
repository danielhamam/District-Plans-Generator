import {Navbar, Nav, NavItem, Button, Dropdown, DropdownButton} from 'react-bootstrap';
import React, {Component} from 'react';
import { ProSidebar, Menu, MenuItem, SubMenu, SidebarHeader } from 'react-pro-sidebar';
// import 'bootstrap/dist/css/bootstrap.min.css';
import usaFlag from "./usaFlag.png"

class Sidebar extends Component {

    render() {
        return (
                <div id="mainSidebar">
                    <ProSidebar>
                        <br />
                        <SidebarHeader> 
                            <div className="row col-md-12">  {/* Holds Flag, "Current State", and Selection of State */}
                                <br />
                                    <div className="col-md-4"> 
                                        <img id="usaFlag" src={usaFlag} alt="logo" style={{ width: '45px'}}/> 
                                    </div>
                                    
                                    <div className="col-md-8">
                                        <div id="currentState"> Current State: </div>
                                        <DropdownButton id="dropdown-basic-button" title="State" size="sm" variant="secondary">
                                        <Dropdown.Item href="#/action-1">California</Dropdown.Item>
                                        <Dropdown.Item href="#/action-2">Georgia</Dropdown.Item>
                                        <Dropdown.Item href="#/action-3">New York</Dropdown.Item>
                                        </DropdownButton>
                                    </div>
                            </div>
                        < br />
                        </SidebarHeader>
                        <Menu iconShape="square">
                            <SubMenu title={<b> <i className="fa fa-home"></i> View State Details</b>}> 
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
                            <SubMenu title={<b> <i class="fa fa-briefcase"></i> Your Batches</b>} >
                                <MenuItem>Component 1</MenuItem>
                                <MenuItem>Component 2</MenuItem>
                            </SubMenu>
                            <SubMenu title={<b> <i class="fa fa-bars"></i> Generate Batch Plan</b>} >
                                <MenuItem>Component 1</MenuItem>
                                <MenuItem>Component 2</MenuItem>
                            </SubMenu>
                            <SubMenu title={<b> <i class="fa fa-edit"></i> User Input</b>} >
                                <MenuItem>Component 1</MenuItem>
                                <MenuItem>Component 2</MenuItem>
                            </SubMenu>
                            <SubMenu title={<b> <i class="fa fa-connectdevelop"></i> Display Graph Panel</b>} >
                                <MenuItem>Component 1</MenuItem>
                                <MenuItem>Component 2</MenuItem>
                            </SubMenu>
                        </Menu>
                    </ProSidebar> 
                </div>
/* <!-- Sidebar --> */
        );
    }
}

export default Sidebar;