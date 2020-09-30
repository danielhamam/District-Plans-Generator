import Navbar from "../navbar/Navbar";
import MainPanel from "../homePage/MainPanel";
import Footer from "../footer/Footer";
import React, { Component } from "react";

export class HomeScreen extends Component {

  render() {
    return (
      <div id="home_screen">

          <Navbar /> 
          {/* This is the container showing the content the panel tells us to display */}
          <MainPanel />
          
          {/* <Footer /> */}
      </div>
    );
  }
}

export default HomeScreen;