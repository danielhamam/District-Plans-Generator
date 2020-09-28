import React, { Component } from "react";
import logo from '../../logo.png';

function Navbar() {
    // Code imported from https://getbootstrap.com/docs/4.5/components/navbar/ and Adjusted for Customization
    // <IconContext.Provider value = {{ size:"1em"}}>

    return (
        
        <nav className="navbar navbar-expand-lg navbar-light bg-dark">
            <div className="float-left">
                <img src={logo} alt="logo" style={{ width: '45px'}}/> 
                <a className="navbar-brand text-white text-uppercase font-weight-bold" href="#">Broncos</a>
                <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
            </div>

            <div className="collapse navbar-collapse" id="navbarSupportedContent">
                <ul className="navbar-nav m-auto"> 
                    <li className="nav-item active">
                            <a className="nav-link text-white text-uppercase ml-5 " href="#">Home <i className="fa fa-home"></i> </a>
                    </li>
                    <li className="nav-item">
                        <a className="nav-link text-white text-uppercase ml-5" href="#">Research <i className="fa fa-book"></i></a>
                    </li>
                    <li className="nav-item dropdown">
                        <a className="nav-link dropdown-toggle text-white text-uppercase ml-5" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Dropdown
                        </a>
                        <div className="dropdown-menu " aria-labelledby="navbarDropdown">
                            <a className="dropdown-item" href="#">Action</a>
                            <a className="dropdown-item" href="#">Another action</a>
                            <div className="dropdown-divider"></div>
                            <a className="dropdown-item" href="#">Something else here</a>
                        </div>
                    </li>
                    {/* <li className="nav-item">
                        <a className="nav-link disabled text-white" href="#" tabindex="-1" aria-disabled="true">Disabled</a>
                    </li> */}
                </ul>
                <form className="form-inline my-2 my-lg-0">
                <input className="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search"/>
                <button className="btn btn-outline-primary my-2 my-sm-0" type="submit">Search </button>
                </form>
            </div>
        </nav>
    )
}

export default Navbar;