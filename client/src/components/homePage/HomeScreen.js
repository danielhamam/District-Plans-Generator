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
          enactedPlan = {this.props.enactedPlan} deleteJob={this.props.deleteJob} deletePlan={this.props.deletePlan} createJob = {this.props.createJob}
          cancelJob = {this.props.cancelJob} generateBoxWhiskerValues= {this.props.generateBoxWhiskerValues} 

          // Map-Related Content
          districtsView = {this.props.districtsView} districtsContent = {this.props.districtsContent} selectedPlanCheck={this.props.selectedPlanCheck}
          precinctsView = {this.props.precinctsView} precinctsContent = {this.props.precinctsContent} changeViewFromZoom={this.props.changeViewFromZoom}
          />
          
          {/* <Footer /> */}
      </div>
    );
  }
}

export default HomeScreen;