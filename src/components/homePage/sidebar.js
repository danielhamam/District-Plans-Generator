import {Navbar, Nav, NavItem, Button} from 'react-bootstrap';
import React, {Component} from 'react';
import { ProSidebar, Menu, MenuItem, SubMenu, SidebarHeader } from 'react-pro-sidebar';

class sidebar extends Component {

    render() {
        return (
              <div>
                <ProSidebar>
                <SidebarHeader> Selected State: </SidebarHeader>
                <Menu iconShape="square">
                    <SubMenu title="View State Details"> 
                        <MenuItem>Component 1</MenuItem>
                        <MenuItem>Component 2</MenuItem>
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
                    <SubMenu title="Help" >
                        <MenuItem>Component 1</MenuItem>
                        <MenuItem>Component 2</MenuItem>
                    </SubMenu>
                    

                </Menu>
                </ProSidebar>
              </div>
        );
    }
}

export default sidebar;