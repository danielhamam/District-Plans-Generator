import Navbar from "../navbar/Navbar";
import MainPanel from "./Main Panel/MainPanel";
import Footer from "../footer/Footer";
import React, { Component } from "react";

export class HomeScreen extends Component {

  render() {
    return (
      <div id="home_screen">

          <Navbar /> 
          {/* This is the container showing the content the panel tells us to display */}
          <MainPanel currentState={this.props.currentState} changeSelectedFilters={this.props.changeSelectedFilters} changeCurrentState={this.props.changeCurrentState} currentJobName={this.props.currentJobName} updateCurrentJobName={this.props.updateCurrentJobName} selectedPlanCheck={this.props.selectedPlanCheck} toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} selectedJobCheck={this.props.selectedJobCheck} toggleSelectedJobCheck={this.props.toggleSelectedJobCheck} />
          
          {/* <Footer /> */}
      </div>
    );
  }
}

export default HomeScreen;