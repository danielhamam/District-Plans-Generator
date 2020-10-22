import HomeScreen from "./components/homePage/HomeScreen";
import DeveloperScreen from "./components/developerscreen/Dev"
import React, { Component } from "react";
import { BrowserRouter, Route, Redirect } from "react-router-dom";
import Switch from "react-bootstrap/esm/Switch";

// App.js is the parent component
class App extends Component {
    state = {
      batchCards : [],
      selectedBatchCheck: false,
      selectedPlanCheck: false,
      currentBatchName : "No Batch Selected: ", // name of the currently selected batch
      // selectedBatchName : "", // take name of selected district
      // selectedPlanName : "", // take name of selected district
      // todoLists: testTodoListData.todoLists, // Portion of my code taken from CSE 316
    }

  createBatch = () => {
    
  }

  deleteBatch = (id) => { // string

  }

  deletePlan = (id) => { // num

  }

  getSelectedBatch = () => {
    // returns selected batch
    // .map and .filter use
    // call in your districting plans and do .districtPlans from batch's database for plans
  }
  
  toggleSelectedBatchCheck = () => {
    if (this.state.selectedBatchCheck == false) this.setState({selectedBatchCheck : true});
    else this.setState({selectedBatchCheck : false});
}

  toggleSelectedPlanCheck = () => {
    if (this.state.selectedPlanCheck == false) this.setState({selectedPlanCheck: true});
    else this.setState({selectedPlanCheck : false});
}

  updateCurrentBatchName = (name) => {
    if (name == "") this.setState({currentBatchName : "No Batch Selected: "});
    else this.setState({currentBatchName : name + ":"});
  }

  render() {
  return (
    <div>

    
        <BrowserRouter>
          <Switch>
            <Redirect exact from="/" to={{ pathname: "/home" }} />
            <Route path="/home">
            <HomeScreen currentBatchName ={this.state.currentBatchName} updateCurrentBatchName={this.updateCurrentBatchName} selectedPlanCheck={this.state.selectedPlanCheck} toggleSelectedPlanCheck={this.toggleSelectedPlanCheck} selectedBatchCheck={this.state.selectedBatchCheck} toggleSelectedBatchCheck={this.toggleSelectedBatchCheck}/>
            </Route>

            <Route path="/dev">
              <DeveloperScreen/>            
            </Route>
              
      
          </Switch>
        </BrowserRouter>
      </div>
    );
  }
}

export default App;
