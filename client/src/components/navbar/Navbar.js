import React, { Component } from "react";
import logo from '../../logo.png';

function Navbar() {

    return (
        
        <nav id="navbarWrapper" className="navbar navbar-expand-lg navbar-light ">
            <div className="float-left">
                <img src={logo} alt="logo" style={{ width: '45px'}}/> 
                <a className="navbar-brand text-white text-uppercase font-weight-bold" href="#">Broncos</a>
                <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
            </div>

            <div className="collapse navbar-collapse" id="navbarSupportedContent">
                <ul className="navbar-nav"> 
                    <li className="nav-item active">
                            <a className="nav-link text-white text-uppercase ml-5" href="#">Home <i className="fa fa-home"></i> </a>
                    </li>
                    <li className="nav-item">
                        {/* <a className="nav-link text-white text-uppercase ml-5" href="#">Research <i className="fa fa-book"></i></a> */}
                    </li>
                    {/* <li className="nav-item dropdown">
                        <a className="nav-link dropdown-toggle text-white text-uppercase ml-5" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Dropdown
                        </a>
                        <div className="dropdown-menu " aria-labelledby="navbarDropdown">
                            <a className="dropdown-item" href="#">Action</a>
                            <a className="dropdown-item" href="#">Another action</a>
                            <div className="dropdown-divider"></div>
                            <a className="dropdown-item" href="#">Something else here</a>
                        </div>
                    </li> */}
                </ul>
            </div>
        </nav>
    )
}

export default Navbar;