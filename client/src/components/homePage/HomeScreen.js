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
          <MainPanel 
          jobCards={this.props.jobCards} currentState={this.props.currentState} changeSelectedFilters={this.props.changeSelectedFilters} changeCurrentState={this.props.changeCurrentState} 
          updateCurrentJob={this.props.updateCurrentJob} selectedPlanCheck={this.props.selectedPlanCheck} currentJob ={this.props.currentJob}
          toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} selectedJobCheck={this.props.selectedJobCheck} toggleSelectedCard={this.props.toggleSelectedCard} 
          enactedPlan = {this.props.enactedPlan}
          />
          
          {/* <Footer /> */}
      </div>
    );
  }
}

export default HomeScreen;