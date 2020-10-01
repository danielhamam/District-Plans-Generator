import {Navbar, Nav, NavItem, Button} from 'react-bootstrap';
import React, {Component} from 'react';
import { ProSidebar, Menu, MenuItem, SubMenu, SidebarHeader } from 'react-pro-sidebar';
// import 'bootstrap/dist/css/bootstrap.min.css';
import usaFlag from "./usaFlag.png"

class Sidebar extends Component {

    render() {
        return (
                <div id="mainSidebar">
                    <ProSidebar sidebar-color="red">
                    <SidebarHeader> 
                        <b> Current State: </b>
                        <br />
                        <img src={usaFlag} alt="logo" style={{ width: '45px'}}/> 
                         </SidebarHeader>
                    <Menu iconShape="square">
                        <SubMenu title="View State Details"> 
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
                        <SubMenu title="Your Batches" >
                            <MenuItem>Component 1</MenuItem>
                            <MenuItem>Component 2</MenuItem>
                        </SubMenu>
                        <SubMenu title="Generate Batch Plan" >
                            <MenuItem>Component 1</MenuItem>
                            <MenuItem>Component 2</MenuItem>
                        </SubMenu>
                        <SubMenu title="User Input" >
                            <MenuItem>Component 1</MenuItem>
                            <MenuItem>Component 2</MenuItem>
                        </SubMenu>
                        <SubMenu title="Display Graph Panel" >
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