import Navbar from "../navbar/Navbar";
import MainPanel from "./Main Panel/MainPanel";
import React, { Component } from "react";

export class HomeScreen extends Component {

  render() {
    return (
      <div id="home_screen">
          <Navbar /> 
          <MainPanel 

          // State-Related Content
          currentState={this.props.currentState} changeCurrentState={this.props.changeCurrentState}
          jobCards={this.props.jobCards} enactedPlan = {this.props.enactedPlan} totalPopulation={this.props.totalPopulation}
          numOfPrecincts={this.props.numOfPrecincts} numOfCounties={this.props.numOfCounties} numOfDistricts = {this.props.numOfDistricts}

          // Job-Related Content
          currentJob ={this.props.currentJob} updateCurrentJob={this.props.updateCurrentJob}
          selectedJobCheck={this.props.selectedJobCheck} toggleSelectedCard={this.props.toggleSelectedCard} 
          deleteJob={this.props.deleteJob} createJob = {this.props.createJob}
          cancelJob = {this.props.cancelJob} generateBoxWhiskerValues= {this.props.generateBoxWhiskerValues}

          // Plan-Related Content
          selectedPlanCheck={this.props.selectedPlanCheck} toggleSelectedPlanCheck={this.props.toggleSelectedPlanCheck} 
          deletePlan={this.props.deletePlan}

          // Map-Related Content
          districtsView = {this.props.districtsView} districtsContent = {this.props.districtsContent} selectedPlanCheck={this.props.selectedPlanCheck}
          precinctsView = {this.props.precinctsView} precinctsContent = {this.props.precinctsContent} changeViewFromZoom={this.props.changeViewFromZoom}
          changeSelectedFilters={this.props.changeSelectedFilters} selectedFilters={this.props.selectedFilters}

          // For Precinct Modal
          precinctName = {this.props.precinctName}
          selectedFeature = {this.props.selectedFeature} 
          featureObject = {this.props.featureObject}
          togglePrecinctModal = {this.props.togglePrecinctModal}

          />

      </div>
    );
  }
}

export default HomeScreen;