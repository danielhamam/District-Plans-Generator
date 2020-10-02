import {Navbar, Nav, NavItem, Button, Dropdown, DropdownButton, ButtonGroup} from 'react-bootstrap';
import React, {Component} from 'react';
import { ProSidebar, Menu, MenuItem, SubMenu, SidebarHeader } from 'react-pro-sidebar';


class BatchCard extends Component {
    // constructor () {
        
    // }

    render() {
        return (
            <div class="card bg-light">
                <div class="card-header text-left bg-light text-dark row col-sm-12">
                {/* <i class="fa fa-times-circle text-left"></i> */}
                    <button type="button" class="btn btn-danger col-sm-2">X</button>
                    <h5 class="card-title text-right col-sm-6">Batch 1 </h5>
                    <button type="button" class="btn btn-secondary col-sm-4">View</button>
                </div>
                {/* <img class="card-img-top" src="..." alt="Card image cap"></img> */}
                
            </div>
        );
    }
}

export default BatchCard;